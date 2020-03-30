package com.fightcovid.signin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.appevents.UserDataStore.EMAIL
import com.facebook.login.LoginResult
import com.fightcovid.main.MainActivity
import com.fightcovid.util.TOKEN
import com.fightcovid.util.TinyDb
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.fightcorona.R
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_signin.*
import timber.log.Timber
import javax.inject.Inject

class SignInActivity : AppCompatActivity(), HasAndroidInjector {

    private val SIGNIN_RESULT_CODE = 253

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var tinyDb: TinyDb

    private lateinit var viewModel: SigninViewModel

    private lateinit var callbackManager: CallbackManager

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        checkIfUserIsSignedIn()
        setupFacebookButton()
        viewModel = ViewModelProvider(this, viewModelFactory).get(SigninViewModel::class.java)

        viewModel.isLoading.observe(this, Observer { result ->
            result?.let { isLoading ->
                if (isLoading) {
                    login_progress_spinner.show()
                    facebook_login_button.visibility = View.INVISIBLE
                    gmail_signin_button.visibility = View.INVISIBLE
                } else {
                    login_progress_spinner.hide()
                    facebook_login_button.visibility = View.VISIBLE
                    gmail_signin_button.visibility = View.VISIBLE
                }
            }
        })

        viewModel.loginSuccessful.observe(this, Observer { result ->
            result?.let { loginSuccessful ->
                if (loginSuccessful) {
                    startMainActivity()
                } else {
                    Timber.d("Error login in")
                }
            }
        })
    }

    private fun checkIfUserIsSignedIn() {
        if (firebaseAuth.currentUser == null) {
            showSigninButton()
        } else {
            saveToken()
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun showSigninButton() {
        gmail_signin_button.visibility = View.VISIBLE
        gmail_signin_button.setOnClickListener {
            startSigninFlow()
        }
    }

    private fun setupFacebookButton() {
        callbackManager = CallbackManager.Factory.create()
        facebook_login_button.setReadPermissions(listOf(EMAIL))
        facebook_login_button.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Timber.d("facebook:onSuccess:$loginResult")
                    firebaseAuthWithFacebook(loginResult.accessToken)
                }

                override fun onCancel() {
                    Timber.d("facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Timber.d("facebook:onError $error")
                }
            })
    }

    private fun startSigninFlow() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("113199709004-lnm748nm64fmmu7en2trbhavi9iuvqm6.apps.googleusercontent.com")
            .requestEmail()
            .build()
        val signinIntent = GoogleSignIn.getClient(this, gso)
        startActivityForResult(signinIntent.signInIntent, SIGNIN_RESULT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGNIN_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account!!)
                } catch (e: ApiException) {
                    Timber.e(e)
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    saveToken()
                    viewModel.getUser()
                } else {
                    Timber.d("signInWithCredential:failure ${task.exception}")
                }
            }
    }

    private fun firebaseAuthWithFacebook(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    saveToken()
                    viewModel.getUser()
                } else {
                    Timber.w("signInWithCredential:failure ${task.exception}")
                }
            }
    }

    private fun saveToken() {
        val task = firebaseAuth.currentUser?.getIdToken(false)
        try {
            val tokenResult = task?.getResult(ApiException::class.java)
            tokenResult?.token?.let {
                Timber.d("Stored token is $it")
                tinyDb.putString(TOKEN, it)
            }
        } catch (e: ApiException) {
            Timber.e(e)
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK
            return intent
        }
    }
}
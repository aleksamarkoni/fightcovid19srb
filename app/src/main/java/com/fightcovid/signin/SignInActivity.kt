package com.fightcovid.signin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fightcovid.main.MainActivity
import com.fightcovid.util.AccountError
import com.fightcovid.util.LoginSuccess
import com.fightcovid.util.TokenSaved
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.fightcorona.R
import com.google.firebase.auth.FirebaseAuth
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

class SignInActivity : AppCompatActivity(), HasAndroidInjector {

    private val SIGNIN_RESULT_CODE = 253

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private lateinit var viewModel: SigninViewModel

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SigninViewModel::class.java)

        viewModel.isLoading.observe(this, Observer { result ->
            result?.let { isLoading ->
                if (isLoading) {
                    login_progress_spinner.show()
                    gmail_signin_button.visibility = View.INVISIBLE
                } else {
                    login_progress_spinner.hide()
                    gmail_signin_button.visibility = View.VISIBLE
                }
            }
        })

        viewModel.loginFlow.observe(this, Observer { result ->
            result?.let {
                when (it) {
                    LoginSuccess -> {
                        startMainActivity()
                    }
                    TokenSaved -> {
                        viewModel.getUserBackend()
                    }
                    is AccountError -> {
                        showSigninButton()
                        Toast.makeText(
                            this,
                            it.error,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        })

        viewModel.userLoggedIn.observe(this, Observer { result ->
            result?.let { userLoggedIn ->
                if (userLoggedIn) {
                    startMainActivity()
                } else {
                    showSigninButton()
                }
            }
        })
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun showSigninButton() {
        login_progress_spinner.hide()
        gmail_signin_button.visibility = View.VISIBLE
        gmail_signin_button.setOnClickListener {
            startSigninFlow()
        }
    }

    private fun startSigninFlow() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("113199709004-lnm748nm64fmmu7en2trbhavi9iuvqm6.apps.googleusercontent.com")
            .requestEmail()
            .build()
        val signinIntent = GoogleSignIn.getClient(this, gso)
        startActivityForResult(signinIntent.signInIntent, SIGNIN_RESULT_CODE)
    }

    @InternalCoroutinesApi
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGNIN_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel.loginUserFirebase(data)
            }
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
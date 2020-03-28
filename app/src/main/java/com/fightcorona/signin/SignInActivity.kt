package com.fightcorona.signin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fightcorona.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.fightcorona.R
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

    private lateinit var firebaseAuth: FirebaseAuth

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        firebaseAuth = FirebaseAuth.getInstance()
        checkIfUserIsSignedIn()
    }

    private fun checkIfUserIsSignedIn() {
        if (firebaseAuth.currentUser == null) {
            showSigninButton()
        } else {
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
        signin_button.visibility = View.VISIBLE
        signin_button.setOnClickListener {
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
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("signInWithCredential:success")
                    val user = firebaseAuth.currentUser
                    startMainActivity()
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.d("signInWithCredential:failure ${task.exception}")
                }
            }
    }
}
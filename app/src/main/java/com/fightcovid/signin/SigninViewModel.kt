package com.fightcovid.signin

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightcovid.remote.repository.UserRepository
import com.fightcovid.util.AccountError
import com.fightcovid.util.AccountResult
import com.fightcovid.util.LoginSuccess
import com.fightcovid.util.UserLoggedIn
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class SigninViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _userLoggedIn = MutableLiveData<Boolean>()

    private val _loginFlow = MutableLiveData<AccountResult>()

    val loginFlow: LiveData<AccountResult>
        get() = _loginFlow

    val userLoggedIn: LiveData<Boolean>
        get() = _userLoggedIn

    init {
        checkForTokens()
    }

    fun getUserBackend() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = userRepository.getUser()
                if (result) {
                    _loginFlow.value = LoginSuccess
                } else {
                    userRepository.logout()
                    _loginFlow.value = AccountError("Error getting user from backend")
                }
            } catch (e: IOException) {
                _isLoading.value = false
            }
        }
    }

    private fun checkForTokens() {
        viewModelScope.launch {
            val result = userRepository.checkTokens() as UserLoggedIn
            _userLoggedIn.value = result.isLoggedIn
        }
    }

    @InternalCoroutinesApi
    fun loginUserFirebase(intent: Intent?) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = userRepository.loginUser(intent)
                result.collect {
                    _loginFlow.value = it
                }
            } catch (e: IOException) {
                _isLoading.value = false
            }
        }
    }
}
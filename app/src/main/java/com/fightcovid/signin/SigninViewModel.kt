package com.fightcovid.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightcovid.remote.repository.UserRepository
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class SigninViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _loginSuccessful = MutableLiveData<Boolean>()

    val loginSuccessful: LiveData<Boolean>
        get() = _loginSuccessful

    fun getUser() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _loginSuccessful.value = userRepository.getUser()
            } catch (e: IOException) {

            } finally {
                _isLoading.value = false
            }
        }
    }
}
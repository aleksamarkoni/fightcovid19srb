package com.fightcovid.main.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightcovid.remote.repository.UserRepository
import com.fightcovid.util.LogoutResults
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _logoutState = MutableLiveData<LogoutResults>()
    private val _isLoading = MutableLiveData<Boolean>()

    val logoutState: LiveData<LogoutResults>
        get() = _logoutState

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun logoutUser() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = userRepository.logout()
                result.collect {
                    _logoutState.value = it
                }
            } catch (e: IOException) {
                Timber.e(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
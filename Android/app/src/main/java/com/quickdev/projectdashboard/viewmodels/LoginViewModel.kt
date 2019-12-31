package com.quickdev.projectdashboard.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.quickdev.projectdashboard.data.UserHelper
import com.quickdev.projectdashboard.data.network.AuthService
import com.quickdev.projectdashboard.models.DTO.identity.LoginDTO
import kotlinx.coroutines.launch
import java.io.InterruptedIOException
import java.net.SocketTimeoutException

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val userHelper = UserHelper(context)

    val email = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")

    private val _loginResponse = MutableLiveData<Int>()
    val loginResponse: LiveData<Int>
        get() = _loginResponse

    fun loginUser() {
        viewModelScope.launch {
            val loginCall = AuthService.HTTP.loginUser(LoginDTO(email.value!!, password.value!!))

            try {
                val result = loginCall.await()
                userHelper.saveUser(result.authToken, result.picture)
                userHelper.setUserCredentials(email.value!!, password.value!!)
                _loginResponse.value = 200
            }
            catch (e: Exception) {
                _loginResponse.value = when (e) {
                    is retrofit2.HttpException -> e.code()
                    is InterruptedIOException -> 504
                    is SocketTimeoutException -> 504
                    else -> {
                        e.printStackTrace()
                        400
                    }
                }
            }

            _loginResponse.value = null
        }
    }

    class Factory(private val application: Application): ViewModelProvider.Factory {

        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java))
                return LoginViewModel(application) as T

            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

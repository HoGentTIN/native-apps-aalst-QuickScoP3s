package com.quickdev.projectdashboard.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.quickdev.projectdashboard.data.UserHelper
import com.quickdev.projectdashboard.data.network.AuthService
import com.quickdev.projectdashboard.models.DTO.LoginDTO
import kotlinx.coroutines.launch
import java.io.InterruptedIOException
import retrofit2.HttpException as HttpException1

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
                _loginResponse.value = 200
            }
            catch (e: retrofit2.HttpException) {
                _loginResponse.value = e.code()
            }
            catch (e: InterruptedIOException) {
                _loginResponse.value = 504
            }
            catch (e: Exception) {
                _loginResponse.value = 400
                e.printStackTrace()
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

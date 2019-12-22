package com.quickdev.projectdashboard.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.quickdev.projectdashboard.data.UserHelper
import com.quickdev.projectdashboard.data.network.AuthService
import com.quickdev.projectdashboard.models.DTO.RegisterDTO
import com.quickdev.projectdashboard.models.domain.Company
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val userHelper = UserHelper(context)

    private val _picture = MutableLiveData<String>()
    val picture: LiveData<String>
        get() = _picture

    val firstName = MutableLiveData<String>("")
    val lastName = MutableLiveData<String>("")
    val email = MutableLiveData<String>("")
    val phoneNr = MutableLiveData<String>("")

    val password = MutableLiveData<String>("")
    val passwordConfirm = MutableLiveData<String>("")

    private val _registerResponse = MutableLiveData<Int>()
    val registerResponse: LiveData<Int>
        get() = _registerResponse

    fun registerUser() {
        viewModelScope.launch {
            val company = Company(name = "", address = "")
            val registerCall = AuthService.HTTP.registerUser(RegisterDTO(picture.value!!, firstName.value!!, lastName.value!!, email.value!!, phoneNr.value!!, company, password.value!!, passwordConfirm.value!!))
            try {
                val result = registerCall.await()
                userHelper.saveUser(result.authToken, result.picture)
                _registerResponse.value = 200
            } catch (e: HttpException) {
                _registerResponse.value = e.code()
            } catch (e: Exception) {
                _registerResponse.value = 0
                e.printStackTrace()
            }
        }
    }

    class Factory(private val application: Application): ViewModelProvider.Factory {

        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterViewModel::class.java))
                return RegisterViewModel(application) as T

            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

package com.quickdev.projectdashboard.viewmodels

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.lifecycle.*
import com.quickdev.projectdashboard.data.UserHelper
import com.quickdev.projectdashboard.data.UserPictureHelper
import com.quickdev.projectdashboard.data.network.AuthService
import com.quickdev.projectdashboard.models.DTO.identity.RegisterDTO
import com.quickdev.projectdashboard.models.domain.Company
import kotlinx.coroutines.launch
import java.io.InterruptedIOException
import java.net.SocketTimeoutException

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val userHelper = UserHelper(context)

    private val _picture = MutableLiveData<String>("")
    val picture: LiveData<String>
        get() = _picture

    val canClearPicture: LiveData<Int> = Transformations.map(_picture) { x -> if ((x ?: "").isEmpty()) View.GONE else View.VISIBLE}

    val firstName = MutableLiveData<String>("")
    val lastName = MutableLiveData<String>("")
    val email = MutableLiveData<String>("")
    val phoneNr = MutableLiveData<String>("")

    val company = MutableLiveData<Company>(null)

    val password = MutableLiveData<String>("")
    val passwordConfirm = MutableLiveData<String>("")

    fun clearProfilePicture() {
        _picture.value = null
    }

    fun setProfilePicture(uri: Uri) {
        val stream = context.contentResolver.openInputStream(uri)
        if (stream != null)
            _picture.value = UserPictureHelper.encodePicture(stream)
    }

    private val _registerResponse = MutableLiveData<Int>()
    val registerResponse: LiveData<Int>
        get() = _registerResponse

    fun registerUser() {
        viewModelScope.launch {
            val companyId = company.value?.id
            val registerCall = AuthService.HTTP.registerUser(RegisterDTO(picture.value!!, firstName.value!!, lastName.value!!, email.value!!, phoneNr.value!!, companyId, password.value!!, passwordConfirm.value!!))
            try {
                val result = registerCall.await()
                userHelper.saveUser(result.authToken, result.picture)
                userHelper.setUserCredentials(email.value!!, password.value!!)
                _registerResponse.value = 200
            }
            catch (e: Exception) {
                _registerResponse.value = when (e) {
                    is retrofit2.HttpException -> e.code()
                    is InterruptedIOException -> 504
                    is SocketTimeoutException -> 504
                    else -> {
                        e.printStackTrace()
                        0
                    }
                }
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

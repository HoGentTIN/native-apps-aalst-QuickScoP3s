package com.quickdev.projectdashboard.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.quickdev.projectdashboard.data.UserHelper
import com.quickdev.projectdashboard.data.database.getDatabase
import com.quickdev.projectdashboard.models.domain.repositories.ProjectRepository
import com.quickdev.projectdashboard.models.domain.repositories.TeamRepository
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val projectRepository = ProjectRepository(database)
    private val teamRepository = TeamRepository(database)
    private val userHelper = UserHelper(application)

    private val _picture = MutableLiveData<String>()
    val picture: LiveData<String>
        get() = _picture

    private val _firstName = MutableLiveData<String>()
    val firstName: LiveData<String>
        get() = _firstName

    private val _lastName = MutableLiveData<String>()
    val lastName: LiveData<String>
        get() = _lastName

    private val _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email

    private val _projectCount = MutableLiveData<Int>()
    val projectCount: LiveData<Int>
        get() = _projectCount

    private val _teamsCount = MutableLiveData<Int>()
    val teamsCount: LiveData<Int>
        get() = _teamsCount

    init {
        viewModelScope.launch {
            _projectCount.value = projectRepository.getProjects().size
            _teamsCount.value = teamRepository.getTeams().size

            val user = userHelper.getSignedInUser()
            _picture.value = user?.picture
            _firstName.value = user?.firstName
            _lastName.value = user?.lastName
            _email.value = user?.email
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {

        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java))
                return ProfileViewModel(application) as T

            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

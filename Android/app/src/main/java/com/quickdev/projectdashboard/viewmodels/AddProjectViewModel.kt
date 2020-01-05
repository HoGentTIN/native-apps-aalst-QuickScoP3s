package com.quickdev.projectdashboard.viewmodels

import androidx.lifecycle.*
import com.quickdev.projectdashboard.App
import com.quickdev.projectdashboard.data.database.AppDatabase
import com.quickdev.projectdashboard.models.DTO.ProjectDTO
import com.quickdev.projectdashboard.models.domain.ContactInfo
import com.quickdev.projectdashboard.models.domain.Team
import com.quickdev.projectdashboard.models.domain.repositories.ProjectRepository
import com.quickdev.projectdashboard.models.domain.repositories.TeamRepository
import kotlinx.coroutines.launch

class AddProjectViewModel(database: AppDatabase) : ViewModel() {

    private val projectRepository = ProjectRepository(database)
    private val teamRepository = TeamRepository(database)

    val projectName = MutableLiveData<String>()
    val contactFirstName = MutableLiveData<String>()
    val contactLastName = MutableLiveData<String>()
    val contactEmail = MutableLiveData<String>()
    val contactPhoneNr = MutableLiveData<String>()

    private val _selectedTeam = MutableLiveData<Int>()

    private val _teams = MutableLiveData<List<Team>>()
    val teams: LiveData<List<Team>>
        get() = _teams

    init {
        viewModelScope.launch {
            _teams.value = teamRepository.getTeams()
        }
    }

    fun setSelectedTeamId(teamId: Int) {
        _selectedTeam.value = teamId
    }

    private val _createResponse = MutableLiveData<Int>()
    val createResponse: LiveData<Int>
        get() = _createResponse

    fun createProject() {
        val user = App.getUserHelper().getSignedInUser()!!
        val companyId = user.companyId!!

        val contact = ContactInfo(
            firstName = contactFirstName.value!!,
            lastName = contactLastName.value!!,
            email = contactEmail.value!!,
            phoneNumber = contactPhoneNr.value!!
        )

        viewModelScope.launch {
            _createResponse.value = null
            _createResponse.value = projectRepository.postProject(
                ProjectDTO(
                    name = projectName.value!!,
                    teamId = _selectedTeam.value!!,
                    ownerId = companyId,
                    contactPerson = contact
                )
            )
        }
    }

    class Factory(private val database: AppDatabase) : ViewModelProvider.Factory {

        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddProjectViewModel::class.java))
                return AddProjectViewModel(database) as T

            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

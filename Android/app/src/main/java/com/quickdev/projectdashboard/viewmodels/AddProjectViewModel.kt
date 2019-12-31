package com.quickdev.projectdashboard.viewmodels

import androidx.lifecycle.*
import com.quickdev.projectdashboard.models.domain.repositories.ProjectRepository
import kotlinx.coroutines.launch

class AddProjectViewModel(private val projectRepository: ProjectRepository): ViewModel() {

	val projectName = MutableLiveData<String>()
	val contactFirstName = MutableLiveData<String>()
	val contactLastName = MutableLiveData<String>()
	val contactEmail = MutableLiveData<String>()
	val contactPhoneNr = MutableLiveData<String>()

	private val _postResponse = MutableLiveData<Int>()
	val postResponse: LiveData<Int>
		get() = _postResponse

	fun createProject() {
		viewModelScope.launch {
			_postResponse.value = null
//			_postResponse.value = projectRepository.postProject(
//				ProjectDTO(
//					name = projectName.value!!,
//					teamId = teamId,
//					ownerId = ownerId,
//					contactPerson = contact
//				)
//			)
		}
	}

	class Factory(private val projectRepository: ProjectRepository) : ViewModelProvider.Factory {

		@Suppress("unchecked_cast")
		override fun <T : ViewModel?> create(modelClass: Class<T>): T {
			if (modelClass.isAssignableFrom(AddProjectViewModel::class.java))
				return AddProjectViewModel(projectRepository) as T

			throw IllegalArgumentException("Unknown ViewModel class")
		}
	}
}
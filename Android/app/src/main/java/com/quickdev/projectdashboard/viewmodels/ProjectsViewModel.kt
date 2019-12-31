package com.quickdev.projectdashboard.viewmodels

import android.view.View
import androidx.lifecycle.*
import com.quickdev.projectdashboard.models.domain.Project
import com.quickdev.projectdashboard.models.domain.repositories.ProjectRepository
import kotlinx.coroutines.launch

class ProjectsViewModel(private val projectRepository: ProjectRepository) : ViewModel() {

	private val _projects = MutableLiveData<List<Project>>()
	val projects: LiveData<List<Project>>
		get() = _projects

	val noContentVisible: LiveData<Int> =
		Transformations.map(_projects) { x -> if (x.isEmpty()) View.VISIBLE else View.GONE }

	private val _isLoading = MutableLiveData<Boolean>(null)
	val isLoading: LiveData<Boolean>
		get() = _isLoading

	init {
		refreshData()
	}

	fun refreshData() {
		viewModelScope.launch {
			_isLoading.value = true
			_projects.value = projectRepository.getProjects()
			_isLoading.value = false
			_isLoading.value = null
		}
	}

	class Factory(private val projectRepository: ProjectRepository) : ViewModelProvider.Factory {

		@Suppress("unchecked_cast")
		override fun <T : ViewModel?> create(modelClass: Class<T>): T {
			if (modelClass.isAssignableFrom(ProjectsViewModel::class.java))
				return ProjectsViewModel(projectRepository) as T

			throw IllegalArgumentException("Unknown ViewModel class")
		}
	}
}
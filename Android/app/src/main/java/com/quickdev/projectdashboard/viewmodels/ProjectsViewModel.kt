package com.quickdev.projectdashboard.viewmodels

import android.view.View
import androidx.lifecycle.*
import com.quickdev.projectdashboard.data.database.AppDatabase
import com.quickdev.projectdashboard.models.domain.Project
import com.quickdev.projectdashboard.models.domain.repositories.ProjectRepository
import com.quickdev.projectdashboard.models.domain.repositories.TeamRepository
import kotlinx.coroutines.launch

class ProjectsViewModel(database: AppDatabase) : ViewModel() {

	private val projectRepository = ProjectRepository(database)
	private val teamRepository = TeamRepository(database)

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
			teamRepository.getTeams() // Projects depend on this
			_projects.value = projectRepository.getProjects()
			_isLoading.value = false
			_isLoading.value = null
		}
	}

	class Factory(private val database: AppDatabase) : ViewModelProvider.Factory {

		@Suppress("unchecked_cast")
		override fun <T : ViewModel?> create(modelClass: Class<T>): T {
			if (modelClass.isAssignableFrom(ProjectsViewModel::class.java))
				return ProjectsViewModel(database) as T

			throw IllegalArgumentException("Unknown ViewModel class")
		}
	}
}
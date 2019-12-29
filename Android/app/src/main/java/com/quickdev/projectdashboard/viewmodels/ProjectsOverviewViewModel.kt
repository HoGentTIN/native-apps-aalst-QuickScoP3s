package com.quickdev.projectdashboard.viewmodels

import androidx.lifecycle.*
import com.quickdev.projectdashboard.models.domain.Project
import com.quickdev.projectdashboard.models.domain.repositories.ProjectRepository
import kotlinx.coroutines.launch

class ProjectsOverviewViewModel(private val projectRepository: ProjectRepository): ViewModel() {

    private val _projects = MutableLiveData<List<Project>>()
    val projects: LiveData<List<Project>>
        get() = _projects

    init {
        viewModelScope.launch {
            _projects.value = projectRepository.getProjects()
        }
    }

    class Factory(private val projectRepository: ProjectRepository): ViewModelProvider.Factory {

        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProjectsOverviewViewModel::class.java))
                return ProjectsOverviewViewModel(projectRepository) as T

            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
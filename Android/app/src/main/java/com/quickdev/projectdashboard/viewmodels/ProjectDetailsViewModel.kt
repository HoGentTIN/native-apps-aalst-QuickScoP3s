package com.quickdev.projectdashboard.viewmodels

import androidx.lifecycle.*
import com.quickdev.projectdashboard.data.database.AppDatabase
import com.quickdev.projectdashboard.models.domain.ProjectTask
import com.quickdev.projectdashboard.models.domain.repositories.ProjectRepository
import com.quickdev.projectdashboard.models.domain.repositories.TasksRepository
import kotlinx.coroutines.launch

class ProjectDetailsViewModel(private val projectId: Int, database: AppDatabase) : ViewModel() {

	private val projectRepository = ProjectRepository(database)
	private val tasksRepository = TasksRepository(database)

	var isFinshedTabSelected: Boolean = false

	private val _projectName = MutableLiveData<String>()
	val projectName: LiveData<String>
		get() = _projectName

	private val _teamName = MutableLiveData<String>()
	val teamName: LiveData<String>
		get() = _teamName

	private val _tasks = MutableLiveData<List<ProjectTask>>()
	val tasks: LiveData<List<ProjectTask>>
		get() = _tasks

	private val _isLoading = MutableLiveData<Boolean>(null)
	val isLoading: LiveData<Boolean>
		get() = _isLoading

	private val _taskFinishedResponse = MutableLiveData<Int>()
	val taskFinishedResponse: LiveData<Int>
		get() = _taskFinishedResponse

	init {
		refreshData()
	}

	fun setInitialNames(projectName: String, teamName: String) {
		_projectName.value = projectName
		_teamName.value = teamName
	}

	fun refreshData() {
		viewModelScope.launch {
			_isLoading.value = true

			val project = projectRepository.getById(projectId)
			_projectName.value = project.name
			_teamName.value = project.team!!.name

			_tasks.value = tasksRepository.getAllForProject(projectId, isFinshedTabSelected)

			_isLoading.value = false
			_isLoading.value = null
		}
	}

	fun setFinished(taskId: Int) {
		viewModelScope.launch {
			_taskFinishedResponse.value = null
			val response = tasksRepository.setTaskFinished(taskId)
			if (response == 200)
				_tasks.value = tasksRepository.getAllForProject(projectId, isFinshedTabSelected, true)

			_taskFinishedResponse.value = response
		}
	}

	class Factory(private val projectId: Int, private val database: AppDatabase) : ViewModelProvider.Factory {

		@Suppress("unchecked_cast")
		override fun <T : ViewModel?> create(modelClass: Class<T>): T {
			if (modelClass.isAssignableFrom(ProjectDetailsViewModel::class.java))
				return ProjectDetailsViewModel(projectId, database) as T

			throw IllegalArgumentException("Unknown ViewModel class")
		}
	}
}
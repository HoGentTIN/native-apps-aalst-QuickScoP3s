package com.quickdev.projectdashboard.viewmodels

import android.view.View
import androidx.lifecycle.*
import com.quickdev.projectdashboard.models.domain.Team
import com.quickdev.projectdashboard.models.domain.repositories.TeamRepository
import kotlinx.coroutines.launch

class TeamsViewModel(private val teamRepository: TeamRepository): ViewModel() {

    private val _teams = MutableLiveData<List<Team>>()
    val teams: LiveData<List<Team>>
        get() = _teams

    val noContentVisible: LiveData<Int> = Transformations.map(_teams) { x -> if (x.isEmpty()) View.VISIBLE else View.GONE }

    private val _isLoading = MutableLiveData<Boolean>(null)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            _isLoading.value = true
            _teams.value = teamRepository.getTeams()
            _isLoading.value = false
            _isLoading.value = null
        }
    }

    class Factory(private val teamRepository: TeamRepository): ViewModelProvider.Factory {

        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TeamsViewModel::class.java))
                return TeamsViewModel(teamRepository) as T

            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
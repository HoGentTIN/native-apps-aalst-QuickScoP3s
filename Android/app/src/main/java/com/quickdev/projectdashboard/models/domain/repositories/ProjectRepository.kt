package com.quickdev.projectdashboard.models.domain.repositories

import com.quickdev.projectdashboard.data.database.ProjectDao
import com.quickdev.projectdashboard.models.domain.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProjectRepository(private val projectDao: ProjectDao) {

    suspend fun getProjects(): List<Project> {
        return withContext(Dispatchers.IO) {
            projectDao.getProjects()
        }
    }

    suspend fun getById(id: Int): Project {
        return withContext(Dispatchers.IO) {
            projectDao.getById(id)
        }
    }
}
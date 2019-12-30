package com.quickdev.projectdashboard.models.domain.repositories

import com.quickdev.projectdashboard.data.database.ProjectDao
import com.quickdev.projectdashboard.data.network.ProjectService
import com.quickdev.projectdashboard.models.domain.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class ProjectRepository(private val projectDao: ProjectDao) {

    suspend fun getProjects(): List<Project> {
        return withContext(Dispatchers.IO) {
            val call = ProjectService.HTTP.getAll()
            try {
                val projects = call.await()
                projectDao.clear()
                projectDao.insertAll(*projects.map { x -> x.toModel() }.toTypedArray())
            }
            catch (e: Exception) { }

            projectDao.getProjects()
        }
    }

    suspend fun getById(id: Int): Project {
        return withContext(Dispatchers.IO) {
            val call = ProjectService.HTTP.getById(id)
            try {
                val project = call.await()
                projectDao.update(project.toModel())
            }
            catch (e: Exception) { }

            projectDao.getById(id)
        }
    }
}
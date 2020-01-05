package com.quickdev.projectdashboard.models.domain.repositories

import com.quickdev.projectdashboard.data.database.AppDatabase
import com.quickdev.projectdashboard.data.network.ProjectService
import com.quickdev.projectdashboard.models.DTO.ProjectDTO
import com.quickdev.projectdashboard.models.domain.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InterruptedIOException
import java.net.SocketTimeoutException

class ProjectRepository(database: AppDatabase) {

    private val projectDao = database.projectDao
    private val teamDao = database.teamDao

    suspend fun getProjects(): List<Project> {
        return withContext(Dispatchers.IO) {
            val call = ProjectService.HTTP.getAll()
            try {
                val projects = call.await()
                projectDao.clear()
                projectDao.insertAll(*projects.map { x -> x.toModel() }.toTypedArray())
            }
            catch (e: Exception) {
                e.printStackTrace()
            }

            val projects = projectDao.getAll()
            projects.forEach { project -> // Fill all objects
                project.team = teamDao.getById(project.teamId)
            }

            projects
        }
    }

    suspend fun getById(id: Int): Project {
        return withContext(Dispatchers.IO) {
            val call = ProjectService.HTTP.getById(id)
            try {
                val project = call.await()
                projectDao.update(project.toModel())
            }
            catch (e: Exception) {
                e.printStackTrace()
            }

            val project = projectDao.getById(id)
            project.team = teamDao.getById(project.teamId)

            project
        }
    }

    suspend fun postProject(project: ProjectDTO): Int {
        return withContext(Dispatchers.IO) {
            var response: Int

            val call = ProjectService.HTTP.post(project)
            try {
            	val result = call.await()
                projectDao.insert(result.toModel())
                response = 200
            }
            catch (e: Exception) {
                response = when (e) {
                    is retrofit2.HttpException -> e.code()
                    is InterruptedIOException -> 504
                    is SocketTimeoutException -> 504
                    else -> {
                        e.printStackTrace()
                        400
                    }
                }
            }

            response
        }
    }
}
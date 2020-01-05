package com.quickdev.projectdashboard.models.domain.repositories

import com.quickdev.projectdashboard.data.database.AppDatabase
import com.quickdev.projectdashboard.data.network.ProjectService
import com.quickdev.projectdashboard.data.network.TasksService
import com.quickdev.projectdashboard.models.domain.ProjectTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InterruptedIOException
import java.net.SocketTimeoutException

class TasksRepository(database: AppDatabase) {

    private val tasksDao = database.tasksDao
    private val usersDao = database.userDao

    suspend fun getAllForProject(projectId: Int, isFinished: Boolean, localOnly: Boolean = false): List<ProjectTask> {
        return withContext(Dispatchers.IO) {
            if (!localOnly) {
                val call = ProjectService.HTTP.getTasksForProject(projectId)
                try {
                    val tasks = call.await()
                    tasksDao.insertAll(*tasks.toTypedArray())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            val tasks = tasksDao.getAllForProject(projectId, isFinished)
            tasks.forEach { task ->
                if (task.assigneeId != null)
                    task.assignee = usersDao.getById(task.assigneeId!!)
            }

            tasks
        }
    }

    suspend fun getById(taskId: Int): ProjectTask {
        return withContext(Dispatchers.IO) {
            val call = TasksService.HTTP.getById(taskId)
            try {
                val task = call.await()
                tasksDao.update(task)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val task = tasksDao.getById(taskId)
            if (task.assigneeId != null)
                task.assignee = usersDao.getById(task.assigneeId!!)

            task
        }
    }

    suspend fun setTaskFinished(taskId: Int): Int {
        return withContext(Dispatchers.IO) {
            var response: Int

            val task = tasksDao.getById(taskId)
            task.isFinished = true

            val call = TasksService.HTTP.put(taskId, task)
            try {
                call.await()
                tasksDao.update(task)
                response = 200
            } catch (e: Exception) {
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

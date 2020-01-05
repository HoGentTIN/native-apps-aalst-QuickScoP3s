package com.quickdev.projectdashboard.data.network

import com.quickdev.projectdashboard.models.domain.ProjectTask
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface TasksApiService {

    @GET("$BASE_URL/{id}")
    fun getById(@Path("id") id: Int): Deferred<ProjectTask>

    @POST(BASE_URL)
    fun post(@Body task: ProjectTask): Deferred<ProjectTask>


    @PUT("$BASE_URL/{id}")
    fun put(@Path("id") id: Int, @Body task: ProjectTask): Deferred<Unit>


    @DELETE("$BASE_URL/{id}")
    fun delete(@Path("id") id: Int): Deferred<ProjectTask>

    companion object {
        private const val BASE_URL: String = "tasks"
    }
}

object TasksService {
    val HTTP: TasksApiService by lazy { BaseService.RETROFIT.create(TasksApiService::class.java) }
}

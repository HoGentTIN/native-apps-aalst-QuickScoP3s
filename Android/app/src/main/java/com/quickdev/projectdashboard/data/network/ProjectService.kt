package com.quickdev.projectdashboard.data.network

import com.quickdev.projectdashboard.models.DTO.CompanyDTO
import com.quickdev.projectdashboard.models.DTO.ProjectDTO
import com.quickdev.projectdashboard.models.domain.Company
import com.quickdev.projectdashboard.models.domain.Project
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface ProjectApiService {

    @GET(BASE_URL)
    fun getAll(): Deferred<List<ProjectDTO>>

    @GET("$BASE_URL/{id}")
    fun getById(@Path("id") id: Int): Deferred<ProjectDTO>

    @POST(BASE_URL)
    fun post(@Body dto: ProjectDTO): Deferred<ProjectDTO>


    @PUT("$BASE_URL/{id}")
    fun put(@Path("id") id: Int, @Body dto: ProjectDTO): Deferred<Unit>


    @DELETE("$BASE_URL/{id}")
    fun put(@Path("id") id: Int): Deferred<ProjectDTO>

    companion object {
        private const val BASE_URL: String = "projects"
    }
}

object ProjectService {
    val HTTP: ProjectApiService by lazy { BaseService.RETROFIT.create(ProjectApiService::class.java) }
}

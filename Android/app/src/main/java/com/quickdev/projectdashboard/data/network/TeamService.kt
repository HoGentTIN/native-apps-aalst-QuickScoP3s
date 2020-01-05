package com.quickdev.projectdashboard.data.network

import com.quickdev.projectdashboard.models.domain.Team
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface TeamApiService {

    @GET(BASE_URL)
    fun getAll(): Deferred<List<Team>>

    @GET("$BASE_URL/{id}")
    fun getById(@Path("id") id: Int): Deferred<Team>

    @POST(BASE_URL)
    fun post(@Body dto: Team): Deferred<Team>

    @PUT("$BASE_URL/{id}")
    fun put(@Path("id") id: Int, @Body dto: Team): Deferred<Unit>

    @DELETE("$BASE_URL/{id}")
    fun delete(@Path("id") id: Int): Deferred<Team>

    companion object {
        private const val BASE_URL: String = "teams"
    }
}

object TeamService {
    val HTTP: TeamApiService by lazy { BaseService.RETROFIT.create(TeamApiService::class.java) }
}

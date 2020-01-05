package com.quickdev.projectdashboard.data.network

import com.quickdev.projectdashboard.models.DTO.CompanyDTO
import com.quickdev.projectdashboard.models.domain.Company
import com.quickdev.projectdashboard.models.domain.User
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface CompanyApiService {

    @GET(BASE_URL)
    fun getAll(): Deferred<List<Company>>

    @GET("$BASE_URL/{id}")
    fun getById(@Path("id") id: Int): Deferred<Company>

    @GET("$BASE_URL/{id}/users")
    fun getUsersFromCompany(@Path("id") id: Int): Deferred<List<User>>

    @POST(BASE_URL)
    fun post(@Body dto: CompanyDTO): Deferred<Company>


    @PUT("$BASE_URL/{id}")
    fun put(@Path("id") id: Int, @Body dto: CompanyDTO): Deferred<Unit>


    @DELETE("$BASE_URL/{id}")
    fun delete(@Path("id") id: Int): Deferred<Company>

    companion object {
        private const val BASE_URL: String = "companies"
    }
}

object CompanyService {
    val HTTP: CompanyApiService by lazy { BaseService.RETROFIT.create(CompanyApiService::class.java) }
}

package com.quickdev.projectdashboard.data.network

import com.quickdev.projectdashboard.models.DTO.CompanyDTO
import com.quickdev.projectdashboard.models.domain.Company
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface CompanyApiService {

    @GET("companies")
    fun getAll(): Deferred<List<Company>>

    @GET("companies/{id}")
    fun getById(@Path("id") id: Int): Deferred<Company>

    @POST("companies")
    fun post(@Body dto: CompanyDTO): Deferred<Company>


    @PUT("companies/{id}")
    fun put(@Path("id") id: Int, @Body dto: CompanyDTO): Deferred<Unit>


    @DELETE("companies/{id}")
    fun put(@Path("id") id: Int): Deferred<Company>
}

object CompanyService {
    val HTTP: CompanyApiService by lazy { BaseService.RETROFIT.create(CompanyApiService::class.java) }
}

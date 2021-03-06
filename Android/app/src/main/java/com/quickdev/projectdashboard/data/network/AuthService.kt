package com.quickdev.projectdashboard.data.network

import com.quickdev.projectdashboard.models.DTO.identity.AuthDTO
import com.quickdev.projectdashboard.models.DTO.identity.LoginDTO
import com.quickdev.projectdashboard.models.DTO.identity.RegisterDTO
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    fun loginUser(@Body dto: LoginDTO): Deferred<AuthDTO>

    @POST("auth/login")
    fun loginRefresh(@Body dto: LoginDTO): Call<AuthDTO>

    @POST("auth/register")
    fun registerUser(@Body dto: RegisterDTO): Deferred<AuthDTO>
}

object AuthService {
    val HTTP: AuthApiService by lazy { BaseService.RETROFIT.create(AuthApiService::class.java) }
}

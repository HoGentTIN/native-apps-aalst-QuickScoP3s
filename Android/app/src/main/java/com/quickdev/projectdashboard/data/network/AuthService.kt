package com.quickdev.projectdashboard.data.network

import com.quickdev.projectdashboard.models.DTO.LoginDTO
import com.quickdev.projectdashboard.models.DTO.RegisterDTO
import com.quickdev.projectdashboard.models.DTO.UserDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    fun loginUser(@Body dto: LoginDTO): Deferred<UserDTO>

    @POST("auth/register")
    fun registerUser(@Body dto: RegisterDTO): Deferred<UserDTO>

    // TODO Implementeer andere Auth methoden
}

object AuthService {
    val HTTP: AuthApiService by lazy { BaseService.RETROFIT.create(AuthApiService::class.java) }
}

package com.quickdev.projectdashboard.models.DTO.identity

import com.squareup.moshi.Json

data class AuthDTO(

    @Json(name = "token")
    val authToken: String,

    // Base64 Encoded image
    val picture: String?
)

package com.quickdev.projectdashboard.models.DTO

import com.squareup.moshi.Json

data class UserDTO(

    @Json(name = "token")
    val authToken: String,

    // Base64 Encoded image
    val picture: String?
)

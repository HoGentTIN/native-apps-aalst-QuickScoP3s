package com.quickdev.projectdashboard.models.DTO.identity

data class RegisterDTO(
    val picture: String?,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val companyId: Int?,
    val password: String,
    val passwordConfirm: String
)

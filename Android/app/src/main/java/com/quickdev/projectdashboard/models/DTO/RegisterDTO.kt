package com.quickdev.projectdashboard.models.DTO

import com.quickdev.projectdashboard.models.domain.Company

data class RegisterDTO(
    var picture: String?,
    var firstName: String,
    var lastName: String,
    var email: String,
    var phoneNr: String,
    var company: Company,
    var password: String,
    var confirmPassword: String
)

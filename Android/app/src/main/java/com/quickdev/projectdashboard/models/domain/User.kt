package com.quickdev.projectdashboard.models.domain

data class User(
    val id: Int = 0,
    var afbeelding: String?,
    var firstName: String,
    var lastName: String,
    var email: String,
    var phoneNr: String,
    val company: Company
)

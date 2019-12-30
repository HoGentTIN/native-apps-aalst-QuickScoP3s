package com.quickdev.projectdashboard.models.DTO

data class CompanyDTO(
    val id: Int = 0,
    val name: String,
    val streetNo: String,
    val streetName: String,
    val zipCode: String,
    val city: String,
    val state: String = "",
    val country: String
)
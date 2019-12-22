package com.quickdev.projectdashboard.models.domain

import java.util.*

data class Project(
    val id: Int = 0,
    // TODO Add team & tasks
    val lastEdit: Date,
    val owner: Company,
    val contact: User
)

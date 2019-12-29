package com.quickdev.projectdashboard.models.domain

import androidx.room.Entity
import androidx.room.Index
import java.util.*

@Entity(
    tableName = "projects",
    indices = [
        Index(value = ["id"], unique = true)
    ]
)
data class Project(
    val id: Int = 0,
    val name: String,
    val lastEdit: Date,
    val team: String,
    val tasks: String,
    val owner: Company,
    val contact: User
)

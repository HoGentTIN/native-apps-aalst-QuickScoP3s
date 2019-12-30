package com.quickdev.projectdashboard.models.domain

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import java.time.LocalDate
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
    val lastEdit: LocalDate,

    val team: String,
    val tasks: String,

    val ownerId: Int,

    @Embedded
    val contact: ContactInfo
)

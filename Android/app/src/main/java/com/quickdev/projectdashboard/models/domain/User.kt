package com.quickdev.projectdashboard.models.domain

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [
        Index(value = ["id"], unique = true)
    ]
)
data class User(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    var picture: String?,
    var firstName: String,
    var lastName: String,
    var email: String,
    var phoneNr: String,
    val companyId: Int?
)

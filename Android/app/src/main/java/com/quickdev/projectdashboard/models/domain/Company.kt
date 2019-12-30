package com.quickdev.projectdashboard.models.domain

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "companies",
    indices = [
        Index(value = ["id"], unique = true)
    ]
)
data class Company(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val name: String,
    val address: String
)

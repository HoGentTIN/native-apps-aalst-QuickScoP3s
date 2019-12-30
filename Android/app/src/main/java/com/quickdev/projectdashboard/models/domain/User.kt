package com.quickdev.projectdashboard.models.domain

import androidx.room.Entity
import androidx.room.Index

@Entity(
	tableName = "users",
	indices = [
		Index(value = ["id"], unique = true)
	]
)
data class User(
    val id: Int = 0,
    var afbeelding: String?,
    var firstName: String,
    var lastName: String,
    var email: String,
    var phoneNr: String,
	val companyId: Int?
)

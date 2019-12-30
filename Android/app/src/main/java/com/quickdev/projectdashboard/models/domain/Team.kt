package com.quickdev.projectdashboard.models.domain

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index

@Entity(
	tableName = "teams",
	indices = [
		Index(value = ["id"], unique = true)
	]
)
data class Team(
	val id: Int = 0,
	val name: String,
	val leadId: Int,
	val memberIds: List<Int>,

	@Ignore
	var lead: User? = null,

	@Ignore
	var members: List<User> = listOf()
)
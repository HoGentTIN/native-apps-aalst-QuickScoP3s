package com.quickdev.projectdashboard.models.domain

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
	tableName = "tasks",
	indices = [
		Index(value = ["id"], unique = true)
	]
)
data class ProjectTask(
	@PrimaryKey(autoGenerate = false)
	val id: Int = 0,
	val projectId: Int,
	var description: String,
	var assigneeId: Int?
) {
	@Ignore
	var assignee: User? = null
}
package com.quickdev.projectdashboard.models.domain

import androidx.room.*
import com.quickdev.projectdashboard.util.converters.IntListConverter

@Entity(
	tableName = "teams",
	indices = [
		Index(value = ["id"], unique = true)
	]
)
@TypeConverters(value = [IntListConverter::class])
data class Team(
	@PrimaryKey(autoGenerate = false)
	val id: Int = 0,
	val name: String,
	val leadId: Int,
	val memberIds: List<Int>
) {
	@Ignore
	var lead: User? = null

	@Ignore
	var members: List<User> = listOf()
}
package com.quickdev.projectdashboard.models.domain

import androidx.room.*
import com.quickdev.projectdashboard.util.converters.DateConverter
import java.time.LocalDate

@Entity(
    tableName = "projects",
    indices = [
        Index(value = ["id"], unique = true)
    ]
)
@TypeConverters(value = [DateConverter::class])
data class Project(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val name: String,
    val lastEdit: LocalDate,
    val teamId: Int,
    val tasks: String,
    val ownerId: Int,

    @Embedded
    val contact: ContactInfo
) {
    @Ignore
    var owner: Company? = null

    @Ignore
    var team: Team? = null
}

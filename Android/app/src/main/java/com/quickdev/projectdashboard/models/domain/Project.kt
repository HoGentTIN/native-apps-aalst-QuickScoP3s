package com.quickdev.projectdashboard.models.domain

import androidx.room.*
import com.quickdev.projectdashboard.util.converters.DateConverter
import com.quickdev.projectdashboard.util.converters.IntListConverter
import java.time.LocalDate

@Entity(
    tableName = "projects",
    indices = [
        Index(value = ["id"], unique = true)
    ]
)
@TypeConverters(value = [DateConverter::class, IntListConverter::class])
data class Project(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val name: String,
    val lastEdit: LocalDate,
    val teamId: Int,
    val taskIds: List<Int>,
    val ownerId: Int,

    @Embedded
    val contact: ContactInfo
) {
    /** Variable to be filled in by the repository, not saved in the Room */
    @Ignore
    var owner: Company? = null

    /** Variable to be filled in by the repository, not saved in the Room */
    @Ignore
    var team: Team? = null

    /** Variable to be filled in by the repository, not saved in the Room */
    @Ignore
    var tasks: List<ProjectTask> = listOf()
}

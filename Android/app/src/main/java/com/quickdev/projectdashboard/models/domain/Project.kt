package com.quickdev.projectdashboard.models.domain

import androidx.room.*
import com.quickdev.projectdashboard.util.converters.DateTimeConverter
import java.time.LocalDateTime

@Entity(
    tableName = "projects",
    indices = [
        Index(value = ["id"], unique = true)
    ]
)
@TypeConverters(value = [DateTimeConverter::class])
data class Project(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val name: String,
    val lastEdit: LocalDateTime,
    val teamId: Int,
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

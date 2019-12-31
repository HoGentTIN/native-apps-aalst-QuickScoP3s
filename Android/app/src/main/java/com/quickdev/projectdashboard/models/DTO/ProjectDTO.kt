package com.quickdev.projectdashboard.models.DTO

import com.quickdev.projectdashboard.models.domain.ContactInfo
import com.quickdev.projectdashboard.models.domain.Project
import com.quickdev.projectdashboard.models.domain.ProjectTask
import java.time.LocalDate

data class ProjectDTO(
    val id: Int = 0,
    val name: String,
    val teamId: Int,
    val ownerId: Int,
    val tasks: List<ProjectTask> = listOf(),
    val lastEdit: LocalDate = LocalDate.now(),
    val contactPerson: ContactInfo
): ModelDTO<Project> {

    override fun toModel(): Project {
        return Project(
            id = id,
            name = name,
            teamId = teamId,
            taskIds = tasks.map { x -> x.id },
            ownerId = ownerId,
            lastEdit = lastEdit,
            contact = contactPerson
        )
    }

}
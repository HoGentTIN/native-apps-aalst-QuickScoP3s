package com.quickdev.projectdashboard.models.DTO

import com.quickdev.projectdashboard.models.domain.ContactInfo
import com.quickdev.projectdashboard.models.domain.Project
import java.time.LocalDate

data class ProjectDTO(
    val id: Int = 0,
    val name: String,
    val teamId: Int,
    val ownerId: Int,
    val lastEdit: LocalDate,
    val contactPerson: ContactInfo
): ModelDTO<Project> {

    override fun toModel(): Project {
        return Project(
            id = id,
            name = name,
            team = "", // TODO fix this
            tasks = "",
            ownerId = ownerId,
            lastEdit = lastEdit,
            contact = contactPerson
        )
    }

}
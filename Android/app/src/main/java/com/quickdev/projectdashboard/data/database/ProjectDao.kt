package com.quickdev.projectdashboard.data.database

import androidx.room.Dao
import androidx.room.Query
import com.quickdev.projectdashboard.models.domain.Project

@Dao
interface ProjectDao {

    @Query("SELECT * FROM projects ORDER BY lastEdit DESC")
    fun getProjects(): List<Project>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun getById(id: Int): Project
}
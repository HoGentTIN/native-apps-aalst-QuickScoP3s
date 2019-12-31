package com.quickdev.projectdashboard.data.database

import androidx.room.*
import com.quickdev.projectdashboard.models.domain.ProjectTask

@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ProjectTask)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg items: ProjectTask)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(item: ProjectTask)

    @Query("SELECT * FROM tasks WHERE projectId = :projectId")
    fun getAllForProject(projectId: Int): List<ProjectTask>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun getById(id: Int): ProjectTask

    @Query("DELETE FROM projects")
    fun clear()
}
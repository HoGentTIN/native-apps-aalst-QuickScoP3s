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

    @Query("SELECT * FROM tasks WHERE projectId = :projectId AND isFinished = :isFinished")
    fun getAllForProject(projectId: Int, isFinished: Boolean): List<ProjectTask>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getById(id: Int): ProjectTask

    @Query("DELETE FROM tasks")
    fun clear()
}

package com.quickdev.projectdashboard.data.database

import androidx.room.*
import com.quickdev.projectdashboard.models.domain.Project

@Dao
interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Project)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg items: Project)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(item: Project)

    @Query("SELECT * FROM projects ORDER BY lastEdit DESC")
    fun getAll(): List<Project>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun getById(id: Int): Project

    @Query("DELETE FROM projects")
    fun clear()
}
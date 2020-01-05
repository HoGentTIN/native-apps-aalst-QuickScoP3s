package com.quickdev.projectdashboard.data.database

import androidx.room.*
import com.quickdev.projectdashboard.models.domain.Company

@Dao
interface CompanyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Company)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg items: Company)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(item: Company)

    @Query("SELECT * FROM companies ORDER BY name")
    fun getAll(): List<Company>

    @Query("SELECT * FROM companies WHERE id = :id")
    fun getById(id: Int): Company

    @Query("DELETE FROM companies")
    fun clear()
}

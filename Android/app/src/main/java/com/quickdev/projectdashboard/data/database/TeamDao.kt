package com.quickdev.projectdashboard.data.database

import androidx.room.*
import com.quickdev.projectdashboard.models.domain.Team

@Dao
interface TeamDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(item: Team)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertAll(vararg items: Team)

	@Update(onConflict = OnConflictStrategy.REPLACE)
	fun update(item: Team)

	@Query("SELECT * FROM teams ORDER BY name")
	fun getAll(): List<Team>

	@Query("SELECT * FROM teams WHERE id = :id")
	fun getById(id: Int): Team

	@Query("DELETE FROM teams")
	fun clear()

}
package com.quickdev.projectdashboard.data.database

import androidx.room.*
import com.quickdev.projectdashboard.models.domain.User

@Dao
interface UserDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(item: User)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertAll(vararg items: User)

	@Update(onConflict = OnConflictStrategy.REPLACE)
	fun update(item: User)

	@Query("SELECT * FROM users ORDER BY lastName")
	fun getProjects(): List<User>

	@Query("SELECT * FROM users WHERE id = :id")
	fun getById(id: Int): User

	@Query("DELETE FROM users")
	fun clear()

}
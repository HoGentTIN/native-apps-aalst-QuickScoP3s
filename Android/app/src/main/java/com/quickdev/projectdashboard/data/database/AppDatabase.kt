package com.quickdev.projectdashboard.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.quickdev.projectdashboard.models.domain.*

@Database(
    entities = [Company::class, Project::class, ProjectTask::class, Team::class, User::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val companyDao: CompanyDao
    abstract val projectDao: ProjectDao
    abstract val tasksDao: TasksDao
    abstract val teamDao: TeamDao
    abstract val userDao: UserDao
}

@Volatile
private lateinit var INSTANCE: AppDatabase

fun getDatabase(context: Context): AppDatabase {
    synchronized(AppDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_database")
                .fallbackToDestructiveMigration()
                .build()
        }

        return INSTANCE
    }
}
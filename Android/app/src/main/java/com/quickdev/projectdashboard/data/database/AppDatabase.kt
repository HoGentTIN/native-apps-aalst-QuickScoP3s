package com.quickdev.projectdashboard.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.quickdev.projectdashboard.models.domain.Company
import com.quickdev.projectdashboard.models.domain.Project

@Database(
    entities = [Project::class, Company::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val projectDao: ProjectDao
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
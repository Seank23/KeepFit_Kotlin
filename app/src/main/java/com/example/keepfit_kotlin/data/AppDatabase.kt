package com.example.keepfit_kotlin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Goal::class, Log::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun goalDao(): GoalDao
    abstract fun logDao(): LogDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            val tempInstance = instance
            if (tempInstance != null) return tempInstance

            synchronized(this) {

                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                instance = newInstance
                return newInstance
            }
        }
    }
}
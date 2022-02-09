package com.example.keepfit_kotlin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Goal::class], version = 1, exportSchema = false)
abstract class GoalDatabase: RoomDatabase() {

    abstract fun goalDao(): GoalDao

    companion object {
        @Volatile
        private var instance: GoalDatabase? = null

        fun getDatabase(context: Context): GoalDatabase {

            val tempInstance = instance
            if (tempInstance != null) return tempInstance

            synchronized(this) {

                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    GoalDatabase::class.java,
                    "goal_database"
                ).build()
                instance = newInstance
                return newInstance
            }
        }
    }
}
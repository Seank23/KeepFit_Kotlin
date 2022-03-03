package com.example.keepfit_kotlin.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LogDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLog(log: Log)

    @Update
    suspend fun updateLog(log: Log)

    @Delete
    suspend fun deleteLog(log: Log)

    @Query("SELECT * FROM log_data ORDER BY time DESC")
    fun getLogs(): LiveData<List<Log>>

    @Query("SELECT * FROM log_data WHERE date = :date ORDER BY time DESC")
    fun getLogsByDate(date: String): LiveData<List<Log>>
}
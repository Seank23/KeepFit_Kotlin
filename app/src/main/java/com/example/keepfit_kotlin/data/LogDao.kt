package com.example.keepfit_kotlin.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface LogDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLog(log: Log)

    @Delete
    suspend fun deleteLog(log: Log)

    @Query("SELECT * FROM log_data ORDER BY id ASC")
    fun getLogs(): LiveData<List<Log>>
}
package com.example.keepfit_kotlin.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GoalDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addGoal(goal: Goal)

    @Query("SELECT * FROM goal_data ORDER BY id ASC")
    fun getGoals(): LiveData<List<Goal>>
}
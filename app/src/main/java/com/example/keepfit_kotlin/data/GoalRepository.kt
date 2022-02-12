package com.example.keepfit_kotlin.data

import androidx.lifecycle.LiveData

class GoalRepository(private val goalDao: GoalDao) {

    val getGoals: LiveData<List<Goal>> = goalDao.getGoals()

    suspend fun addGoal(goal: Goal) {
        goalDao.addGoal(goal)
    }

    suspend fun updateGoal(goal: Goal) {
        goalDao.updateGoal(goal)
    }
}
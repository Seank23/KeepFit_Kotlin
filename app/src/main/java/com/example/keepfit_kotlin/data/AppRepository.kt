package com.example.keepfit_kotlin.data

import androidx.lifecycle.LiveData

class AppRepository(private val goalDao: GoalDao, private val logDao: LogDao) {

    val getGoals: LiveData<List<Goal>> = goalDao.getGoals()

    suspend fun addGoal(goal: Goal) {
        goalDao.addGoal(goal)
    }

    suspend fun updateGoal(goal: Goal) {
        goalDao.updateGoal(goal)
    }

    suspend fun deleteGoal(goal: Goal) {
        goalDao.deleteGoal(goal)
    }

    val getLogs: LiveData<List<Log>> = logDao.getLogs()

    fun getLogsByDate(date: String): LiveData<List<Log>> = logDao.getLogsByDate(date)

    suspend fun addLog(log: Log) {
        logDao.addLog(log)
    }

    suspend fun deleteLog(log: Log) {
        logDao.deleteLog(log)
    }
}
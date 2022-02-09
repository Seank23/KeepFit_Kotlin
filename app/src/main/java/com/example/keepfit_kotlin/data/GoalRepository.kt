package com.example.keepfit_kotlin.data

class GoalRepository private constructor(private val goalDao: GoalDao) {

    fun addGoal(goal: Goal) {
        goalDao.addGoal(goal)
    }

    fun getGoals() = goalDao.getGoals()

    companion object {
        @Volatile private var instance: GoalRepository? = null

        fun getInstance(goalDao: GoalDao) =
            instance ?: synchronized(this) {
                instance ?: GoalRepository(goalDao).also { instance = it }
            }
    }
}
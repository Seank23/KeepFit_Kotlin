package com.example.keepfit_kotlin.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class GoalDao {

    private val goalList = mutableListOf<Goal>()
    private val goals = MutableLiveData<List<Goal>>()

    init {
        goals.value = goalList
    }

    fun addGoal(goal: Goal) {
        goalList.add(goal)
        goals.value = goalList
    }

    fun getGoals() = goals as LiveData<List<Goal>>
}
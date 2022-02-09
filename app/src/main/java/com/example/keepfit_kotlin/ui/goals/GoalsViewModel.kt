package com.example.keepfit_kotlin.ui.goals

import androidx.lifecycle.ViewModel
import com.example.keepfit_kotlin.data.Goal
import com.example.keepfit_kotlin.data.GoalRepository

class GoalsViewModel(private val goalRepository: GoalRepository) : ViewModel() {

    fun getGoals() = goalRepository.getGoals()

    fun addGoal(goal: Goal) = goalRepository.addGoal(goal)
}
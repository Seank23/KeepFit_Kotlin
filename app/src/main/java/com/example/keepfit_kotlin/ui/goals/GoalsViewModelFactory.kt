package com.example.keepfit_kotlin.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.keepfit_kotlin.data.GoalRepository

class GoalsViewModelFactory(private val goalRepository: GoalRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GoalsViewModel(goalRepository) as T
    }
}
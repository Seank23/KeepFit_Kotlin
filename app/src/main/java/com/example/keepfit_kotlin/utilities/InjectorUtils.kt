package com.example.keepfit_kotlin.utilities

import com.example.keepfit_kotlin.data.Database
import com.example.keepfit_kotlin.data.GoalRepository
import com.example.keepfit_kotlin.ui.goals.GoalsViewModelFactory

object InjectorUtils {

    fun provideGoalsViewModelFactory(): GoalsViewModelFactory {
        return GoalsViewModelFactory(GoalRepository.getInstance(Database.getInstance().goalDao))
    }
}
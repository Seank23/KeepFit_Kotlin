package com.example.keepfit_kotlin.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.keepfit_kotlin.SharedData

class HomeViewModel(application: Application, sharedData: SharedData) : AndroidViewModel(application) {

    private val mSharedData = sharedData
    private var numSteps = 0

    fun addSteps(steps: Int) {
        numSteps += steps
    }

    fun getSteps() = numSteps

    fun getActiveGoalName(): String {
        val goal = mSharedData.activeGoal
        return goal?.name ?: "None"
    }

    fun getActiveGoalSteps(): Int {
        val goal = mSharedData.activeGoal
        return goal?.steps ?: 0
    }

    fun getGoalProgress(): Float {
        return numSteps.toFloat() / mSharedData.activeGoal!!.steps
    }
}
package com.example.keepfit_kotlin.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.keepfit_kotlin.Utils
import com.example.keepfit_kotlin.data.Goal
import com.example.keepfit_kotlin.data.HistoryActivity
import com.example.keepfit_kotlin.data.Log

class EditHistoryViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var editedHistoryActivity: HistoryActivity
    lateinit var selectedGoal: Goal

    fun createLog(steps: Int, time: String) {

        editedHistoryActivity.logs.add(Log(0, Utils.getTimestamp(editedHistoryActivity.date), time, steps, selectedGoal.name, selectedGoal.steps))
        editedHistoryActivity.logs.sortByDescending { it.time }
        editedHistoryActivity.totalSteps += steps
        editedHistoryActivity.goalProgress = editedHistoryActivity.totalSteps.toFloat() / editedHistoryActivity.goalSteps
    }

    fun deleteLog(logIndex: Int) {

        val log = editedHistoryActivity.logs[logIndex]
        editedHistoryActivity.logs.removeAt(logIndex)
        editedHistoryActivity.totalSteps -= log.steps
        editedHistoryActivity.goalProgress = editedHistoryActivity.totalSteps.toFloat() / editedHistoryActivity.goalSteps
    }

    fun setGoal(goalName: String, goalSteps: Int) {
        selectedGoal = Goal(0, goalName, goalSteps, false)
        editedHistoryActivity.goalName = selectedGoal.name
        editedHistoryActivity.goalSteps = selectedGoal.steps
        editedHistoryActivity.goalProgress = editedHistoryActivity.totalSteps.toFloat() / editedHistoryActivity.goalSteps
    }
}
package com.example.keepfit_kotlin.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.keepfit_kotlin.data.AppRepository
import com.example.keepfit_kotlin.data.Goal
import com.example.keepfit_kotlin.data.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(application: Application, appRepository: AppRepository) : AndroidViewModel(application) {

    private val repository = appRepository
    val getTodaysLogs: LiveData<List<Log>> = repository.getLogsByDate(SimpleDateFormat("ddMMyyyy").format(Date()))
    val getGoals: LiveData<List<Goal>> = repository.getGoals


    private fun addLog(log: Log) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLog(log)
        }
    }

    fun deleteLog(log: Log) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteLog(log)
        }
    }

    fun addSteps(steps: Int) {
        val log = Log(0, SimpleDateFormat("ddMMyyyy").format(Date()), SimpleDateFormat("HH:mm").format(Date()), steps)
        addLog(log)
    }

    fun getSteps(): Int {
        var steps = 0
        if(getTodaysLogs.value != null) {
            for (log in getTodaysLogs.value!!) {
                steps += log.steps
            }
        }
        return steps
    }

    private fun getActiveGoal(): Goal? {

        if(getGoals.value != null) {
            if (getGoals.value!!.isNotEmpty())
                return getGoals.value!!.find { goal -> goal.isActive }!!
        }
        return null
    }

    fun getActiveGoalName(): String {
        val goal = getActiveGoal()
        return goal?.name ?: "None"
    }

    fun getActiveGoalSteps(): Int {
        val goal = getActiveGoal()
        return goal?.steps ?: 0
    }

    fun getGoalProgress(): Float {
        return if(getActiveGoal() != null)
            getSteps().toFloat() / getActiveGoal()!!.steps
        else
            0f
    }
}
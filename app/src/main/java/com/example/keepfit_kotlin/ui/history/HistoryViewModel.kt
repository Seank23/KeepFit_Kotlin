package com.example.keepfit_kotlin.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.keepfit_kotlin.Utils.observeForeverOnce
import com.example.keepfit_kotlin.data.AppRepository
import com.example.keepfit_kotlin.data.Goal
import com.example.keepfit_kotlin.data.Log
import com.example.keepfit_kotlin.data.HistoryActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HistoryViewModel(application: Application, appRepository: AppRepository) : AndroidViewModel(application) {

    private val repository = appRepository
    private val getGoals: LiveData<List<Goal>> = repository.getGoals
    var currentHistoryActivity: HistoryActivity = HistoryActivity("", 0, "", 0, 0F, emptyList())

    fun getHistoryByDate(date: String, onRetrieve: (HistoryActivity?) -> Unit) {

        lateinit var activityLogs: List<Log>

        viewModelScope.launch {
            repository.getLogsByDate(date).observeForeverOnce {
                activityLogs = it

                if (activityLogs.isNotEmpty()) {

                    var totalSteps = 0
                    for (log: Log in activityLogs)
                        totalSteps += log.steps

                    val historyActivity = HistoryActivity(
                        date,
                        totalSteps,
                        activityLogs[0].goalName,
                        activityLogs[0].goalSteps,
                        totalSteps.toFloat() / activityLogs[0].goalSteps,
                        activityLogs
                    )
                    currentHistoryActivity = historyActivity
                    onRetrieve(historyActivity)
                } else {
                    onRetrieve(null)
                }
            }
        }
    }

    fun getGoalNames(): List<String> {

        var goalNames: MutableList<String> = mutableListOf()
        if(getGoals.value != null) {
            for(goal: Goal in getGoals.value!!)
                goalNames.add(goal.name)
        }
        return goalNames
    }

    fun getGoalSteps(): List<Int> {

        var goalSteps: MutableList<Int> = mutableListOf()
        if(getGoals.value != null) {
            for(goal: Goal in getGoals.value!!)
                goalSteps.add(goal.steps)
        }
        return goalSteps
    }

    fun editHistory(date: String, additionalSteps: Int, goalIndex: Int) {

        val currentLogs = currentHistoryActivity.logs
        val goalNames = getGoalNames()
        val goalSteps = getGoalSteps()

        for(log: Log in currentLogs) {
            log.goalName = goalNames[goalIndex]
            log.goalSteps = goalSteps[goalIndex]
            viewModelScope.launch(Dispatchers.IO) {
                repository.updateLog(log)
            }
        }

        if(additionalSteps > 0) {
            val newLog = Log(0, date, "23:59", additionalSteps, goalNames[goalIndex], goalSteps[goalIndex])
            viewModelScope.launch(Dispatchers.IO) {
                repository.addLog(newLog)
            }
        }
        currentHistoryActivity.date = date
        currentHistoryActivity.totalSteps += additionalSteps
        currentHistoryActivity.goalName = goalNames[goalIndex]
        currentHistoryActivity.goalSteps = goalSteps[goalIndex]
        currentHistoryActivity.goalProgress = currentHistoryActivity.totalSteps.toFloat() / goalSteps[goalIndex]
        currentHistoryActivity.logs = currentLogs
    }
}
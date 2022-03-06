package com.example.keepfit_kotlin.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.keepfit_kotlin.Utils.getDate
import com.example.keepfit_kotlin.Utils.getTimestamp
import com.example.keepfit_kotlin.Utils.observeOnceNoLC
import com.example.keepfit_kotlin.data.AppRepository
import com.example.keepfit_kotlin.data.Goal
import com.example.keepfit_kotlin.data.Log
import com.example.keepfit_kotlin.data.HistoryActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HistoryViewModel(application: Application, appRepository: AppRepository) : AndroidViewModel(application) {

    private val repository = appRepository
    private val getGoals: LiveData<List<Goal>> = repository.getGoals
    var currentHistoryActivity: HistoryActivity = HistoryActivity(SimpleDateFormat("ddMMyyyy").format(Date()), 0, "None", 0, 0F, mutableListOf())

    fun getHistoryByDate(date: String, onRetrieve: (HistoryActivity) -> Unit) {

            repository.getLogsByDate(getTimestamp(date)).observeOnceNoLC {
                val activityLogs = it

                if (activityLogs.isNotEmpty()) {

                    var totalSteps = 0
                    for (log: Log in activityLogs)
                        totalSteps += log.steps


                    currentHistoryActivity = HistoryActivity(
                                                date,
                                                totalSteps,
                                                activityLogs[0].goalName,
                                                activityLogs[0].goalSteps,
                                                totalSteps.toFloat() / activityLogs[0].goalSteps,
                                                activityLogs.toMutableList()
                                            )
                } else
                    currentHistoryActivity = HistoryActivity(date, 0, "None", 0, 0F, mutableListOf())

                onRetrieve(currentHistoryActivity)
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

    fun saveHistory(editedHistoryActivity: HistoryActivity) {

        val logIds = mutableListOf<Int>()
        for(log: Log in editedHistoryActivity.logs)
            logIds.add(log.id)

        val removedLogs = mutableListOf<Log>()
        for(log: Log in currentHistoryActivity.logs) {
            if(!logIds.contains(log.id))
                removedLogs.add(log)
        }

        if(editedHistoryActivity.logs.isEmpty()) {
            // Add placeholder log to store goal when no logs are present
            viewModelScope.launch(Dispatchers.IO) {
                repository.addLog(Log(0, getTimestamp(editedHistoryActivity.date), "", 0, editedHistoryActivity.goalName, editedHistoryActivity.goalSteps))
            }
        } else {
            for (log: Log in editedHistoryActivity.logs) {

                log.goalName = editedHistoryActivity.goalName
                log.goalSteps = editedHistoryActivity.goalSteps
                if (log.id == 0) {
                    viewModelScope.launch(Dispatchers.IO) {
                        repository.addLog(log)
                    }
                } else {
                    viewModelScope.launch(Dispatchers.IO) {
                        repository.updateLog(log)
                    }
                }
            }
        }

        for(log: Log in removedLogs) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.deleteLog(log)
            }
        }
        currentHistoryActivity = editedHistoryActivity
    }

    fun getGraphData(startDate: String, endDate: String, onRetrieve: (List<HistoryActivity>) -> Unit) {

        repository.getLogsByDateRange(getTimestamp(startDate), getTimestamp(endDate)).observeOnceNoLC {

            val activityLogs = it
            val dates = getDatesBetween(getTimestamp(startDate), getTimestamp(endDate))
            val historyData = List(dates.size) { d -> HistoryActivity(getDate(dates[d]), 0, "", 0, 0F, mutableListOf()) }

            for(log: Log in activityLogs) {
                val history = historyData[dates.indexOf(log.date)]
                history.totalSteps += log.steps
                history.goalName = log.goalName
                history.goalSteps = log.goalSteps
                history.logs.add(log)
            }

            for(history: HistoryActivity in historyData) {
                if(history.goalSteps > 0)
                    history.goalProgress = history.totalSteps.toFloat() / history.goalSteps
            }

            onRetrieve(historyData)
        }
    }

    private fun getDatesBetween(startDate: Long, endDate: Long): List<Long> {
        val dates = mutableListOf<Long>()
        var date = startDate
        while(date <= endDate) {
            dates.add(date)
            date += 86400000
        }
        return dates
    }
}
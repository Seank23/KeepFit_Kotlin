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
    var currentHistoryActivity: HistoryActivity = HistoryActivity(SimpleDateFormat("ddMMyyyy").format(Date()), 0, "None", 0, 0F, mutableListOf())

    fun getHistoryByDate(date: String, onRetrieve: (HistoryActivity) -> Unit) {

        lateinit var activityLogs: List<Log>

        viewModelScope.launch {
            repository.getLogsByDate(date).observeForeverOnce {
                activityLogs = it

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

    fun editHistory(editedHistoryActivity: HistoryActivity) {

        val logIds = mutableListOf<Int>()
        for(log: Log in editedHistoryActivity.logs)
            logIds.add(log.id)

        val removedLogs = mutableListOf<Log>()
        for(log: Log in currentHistoryActivity.logs) {
            if(!logIds.contains(log.id))
                removedLogs.add(log)
        }

        for(log: Log in editedHistoryActivity.logs) {

            log.goalName = editedHistoryActivity.goalName
            log.goalSteps = editedHistoryActivity.goalSteps
            if(log.id == 0) {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.addLog(log)
                }
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.updateLog(log)
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
}
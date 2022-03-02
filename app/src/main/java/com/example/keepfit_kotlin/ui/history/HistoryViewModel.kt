package com.example.keepfit_kotlin.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.keepfit_kotlin.Utils.observeForeverOnce
import com.example.keepfit_kotlin.data.AppRepository
import com.example.keepfit_kotlin.data.Log
import com.example.keepfit_kotlin.data.HistoryActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HistoryViewModel(application: Application, appRepository: AppRepository) : AndroidViewModel(application) {

    private val repository = appRepository

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
                    onRetrieve(historyActivity)
                } else {
                    onRetrieve(null)
                }
            }
        }
    }
}
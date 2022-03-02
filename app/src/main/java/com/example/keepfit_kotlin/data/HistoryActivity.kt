package com.example.keepfit_kotlin.data

data class HistoryActivity(var date: String,
                           var totalSteps: Int,
                           var goalName: String,
                           var goalSteps: Int,
                           var goalProgress: Float,
                           var logs: List<Log>)
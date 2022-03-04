package com.example.keepfit_kotlin.data

data class HistoryActivity(var date: String,
                           var totalSteps: Int,
                           var goalName: String,
                           var goalSteps: Int,
                           var goalProgress: Float,
                           var logs: MutableList<Log>) : Cloneable
{
    public override fun clone(): Any {
        return super.clone()
    }
}
package com.example.keepfit_kotlin.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "log_data")
data class Log (

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var date: String,
    var time: String,
    var steps: Int,
    var goalName: String,
    var goalSteps: Int
)
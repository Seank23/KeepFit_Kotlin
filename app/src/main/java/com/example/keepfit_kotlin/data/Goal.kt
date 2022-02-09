package com.example.keepfit_kotlin.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goal_data")
data class Goal (

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String,
    var steps: Int,
    var isActive: Boolean
)
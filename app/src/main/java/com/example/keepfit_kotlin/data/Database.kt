package com.example.keepfit_kotlin.data

class Database private constructor() {

    var goalDao = GoalDao()
        private set

    companion object {
        @Volatile private var instance: Database? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: Database().also { instance = it }
            }
    }
}
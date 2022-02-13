package com.example.keepfit_kotlin.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private var numSteps = 0

    fun addSteps(steps: Int) {
        numSteps += steps
    }

    fun getSteps() = numSteps

}
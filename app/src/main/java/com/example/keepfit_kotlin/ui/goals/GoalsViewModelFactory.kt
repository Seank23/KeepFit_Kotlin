package com.example.keepfit_kotlin.ui.goals

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.keepfit_kotlin.SharedData

class GoalsViewModelFactory(application: Application, sharedData: SharedData): ViewModelProvider.Factory {

    private val mApplication = application
    private val mSharedData = sharedData

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GoalsViewModel(mApplication, mSharedData) as T
    }
}
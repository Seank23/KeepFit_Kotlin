package com.example.keepfit_kotlin.ui.goals

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.keepfit_kotlin.data.AppRepository

class GoalsViewModelFactory(application: Application, repository: AppRepository): ViewModelProvider.Factory {

    private val mApplication = application
    private val mRepository = repository

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GoalsViewModel(mApplication, mRepository) as T
    }
}
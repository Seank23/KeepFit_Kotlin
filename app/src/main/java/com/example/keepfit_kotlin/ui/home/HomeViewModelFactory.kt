package com.example.keepfit_kotlin.ui.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.keepfit_kotlin.SharedData

class HomeViewModelFactory(application: Application, sharedData: SharedData): ViewModelProvider.Factory {

    private val mApplication = application
    private val mSharedData = sharedData

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(mApplication, mSharedData) as T
    }
}
package com.example.keepfit_kotlin.ui.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.keepfit_kotlin.data.AppRepository

class HomeViewModelFactory(application: Application, repository: AppRepository): ViewModelProvider.Factory {

    private val mApplication = application
    private val mRepository = repository

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(mApplication, mRepository) as T
    }
}
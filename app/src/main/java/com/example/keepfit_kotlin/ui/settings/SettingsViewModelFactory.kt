package com.example.keepfit_kotlin.ui.settings

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.keepfit_kotlin.data.AppRepository

class SettingsViewModelFactory(application: Application, repository: AppRepository): ViewModelProvider.Factory {

    private val mApplication = application
    private val mRepository = repository

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(mApplication, mRepository) as T
    }
}
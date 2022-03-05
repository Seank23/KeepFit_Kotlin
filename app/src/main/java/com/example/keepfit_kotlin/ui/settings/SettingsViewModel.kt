package com.example.keepfit_kotlin.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepfit_kotlin.data.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application, appRepository: AppRepository) : AndroidViewModel(application) {

    val repository = appRepository

    fun deleteAllHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllLogs()
        }
    }
}
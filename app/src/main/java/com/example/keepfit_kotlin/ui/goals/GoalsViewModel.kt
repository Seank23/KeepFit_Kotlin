package com.example.keepfit_kotlin.ui.goals

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.keepfit_kotlin.data.Goal
import com.example.keepfit_kotlin.data.GoalDatabase
import com.example.keepfit_kotlin.data.GoalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IndexOutOfBoundsException

class GoalsViewModel(application: Application) : AndroidViewModel(application) {

    val getGoals: LiveData<List<Goal>>
    private val repository: GoalRepository

    init {
        val goalDao = GoalDatabase.getDatabase(application).goalDao()
        repository = GoalRepository(goalDao)
        getGoals = repository.getGoals
    }

    fun getGoalCount() = if(getGoals.value != null) getGoals.value?.size else 0

    fun addGoal(goal: Goal) {

        viewModelScope.launch(Dispatchers.IO) {
            repository.addGoal(goal)
        }
    }

    fun updateGoal(goal: Goal) {

        viewModelScope.launch(Dispatchers.IO) {
            repository.updateGoal(goal)
        }
    }

    fun setActive(name: String) {

    }
}

class LinearLayoutManagerWrapper(context: Context): LinearLayoutManager(context) {

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch(e: IndexOutOfBoundsException) {
            return
        }
    }
}
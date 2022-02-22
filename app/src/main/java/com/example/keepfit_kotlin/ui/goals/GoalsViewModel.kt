package com.example.keepfit_kotlin.ui.goals

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.keepfit_kotlin.data.Goal
import com.example.keepfit_kotlin.data.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IndexOutOfBoundsException

class GoalsViewModel(application: Application, appRepository: AppRepository) : AndroidViewModel(application) {

    val getGoals: LiveData<List<Goal>>
    private val repository = appRepository

    private var activeGoal: Goal
    private var prevActiveGoal: Goal

    init {
        getGoals = repository.getGoals
        activeGoal = Goal(-1, "", 0, false)
        prevActiveGoal = Goal(-1, "", 0, false)
    }

    fun getGoalCount() = if(getGoals.value != null) getGoals.value?.size else 0

    fun addGoal(goal: Goal) {

        if(getGoalCount() == 0) {
            goal.isActive = true
            activeGoal = goal
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.addGoal(goal)
        }
    }

    fun updateGoal(goal: Goal) {

        viewModelScope.launch(Dispatchers.IO) {
            repository.updateGoal(goal)
        }
    }

    fun deleteGoal(goal: Goal) {

        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteGoal(goal)
        }
    }

    fun setActive(goal: Goal) {

        var goalActive: Goal = goal
        goalActive.isActive = true
        updateGoal(goalActive)

        if(getGoalCount()!! > 1) {

            var goalInactive: Goal = activeGoal
            goalInactive.isActive = false
            updateGoal(goalInactive)
            prevActiveGoal = goalInactive
        }
        activeGoal = goal
    }

    fun checkGoalNameExists(name: String): Boolean {

        val goal = getGoals.value!!.find { goal -> goal.name == name }
        return goal != null
    }

    fun getActiveGoal(): Goal {

        if(getGoals.value != null) {
            if (activeGoal.id == -1 && getGoals.value!!.isNotEmpty())
                activeGoal = getGoals.value!!.find { goal -> goal.isActive }!!
        }
        return activeGoal
    }

    fun getPrevActiveGoal() = prevActiveGoal
}
package com.example.keepfit_kotlin.ui.goals

import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.data.Goal
import com.example.keepfit_kotlin.utilities.InjectorUtils

class GoalsFragment : Fragment(R.layout.fragment_goals) {

    override fun onStart() {

        super.onStart()
        initializeViewModel()
    }

    private fun initializeViewModel() {

        // Setup GoalViewModel
        val factory = InjectorUtils.provideGoalsViewModelFactory()
        val viewModel = ViewModelProvider(this, factory).get(GoalsViewModel::class.java)

        // Setup recycler and adapter
        val adapter = GoalsAdapter(viewModel.getGoals().value!!)
        val recycler = view?.findViewById<RecyclerView>(R.id.rvGoals)
        recycler?.adapter = adapter
        recycler?.layoutManager = LinearLayoutManager(this.context)

        view?.findViewById<Button>(R.id.btnAddGoal)?.setOnClickListener {
            val goal = Goal(view?.findViewById<EditText>(R.id.txtGoal)?.text.toString(), 0, false)
            viewModel.addGoal(goal)
            adapter.notifyItemInserted(viewModel.getGoalCount()!! - 1)
        }
    }
}
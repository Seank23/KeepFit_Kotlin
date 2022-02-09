package com.example.keepfit_kotlin.ui.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.data.Goal

class GoalsFragment : Fragment(R.layout.fragment_goals) {

    private lateinit var viewModel: GoalsViewModel

    override fun onStart() {
        super.onStart()
        initializeViewModel()
    }

    private fun initializeViewModel() {

        // Setup GoalViewModel
        viewModel = ViewModelProvider(this)[GoalsViewModel::class.java]

        // Setup recycler and adapter
        val adapter = GoalsAdapter()
        val recycler = view?.findViewById<RecyclerView>(R.id.rvGoals)
        recycler?.adapter = adapter
        recycler?.layoutManager = LinearLayoutManager(this.context)

        viewModel.getGoals.observe(viewLifecycleOwner) { goals -> adapter.setData(goals) }

        view?.findViewById<Button>(R.id.btnAddGoal)?.setOnClickListener {

            val txtGoal = view?.findViewById<EditText>(R.id.txtGoal)
            viewModel.addGoal(Goal(0, txtGoal?.text.toString(), 0, false))
            adapter.notifyItemInserted(viewModel.getGoalCount()!! - 1)
            var empty: String = ""
            txtGoal?.setText(empty)
        }
    }
}
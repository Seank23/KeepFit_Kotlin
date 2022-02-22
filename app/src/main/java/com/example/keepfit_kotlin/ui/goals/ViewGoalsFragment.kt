package com.example.keepfit_kotlin.ui.goals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.keepfit_kotlin.LinearLayoutManagerWrapper
import com.example.keepfit_kotlin.R
import kotlinx.android.synthetic.main.fragment_view_goals.*

class ViewGoalsFragment(goalsAdapter: GoalsAdapter) : Fragment() {

    private lateinit var p: GoalsFragment
    private val adapter: GoalsAdapter = goalsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_goals, container, false)

        p = parentFragment as GoalsFragment

        return view
    }

    override fun onStart() {
        super.onStart()

        fbtnAddGoal.setOnClickListener {
            p.onNavAddGoal()
        }

        // Setup recycler and adapter
        rvGoals.adapter = adapter
        rvGoals.layoutManager = LinearLayoutManagerWrapper(this.requireContext())
        p.observeGoalData(viewLifecycleOwner)
    }
}
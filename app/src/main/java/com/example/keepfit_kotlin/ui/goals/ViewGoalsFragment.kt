package com.example.keepfit_kotlin.ui.goals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.keepfit_kotlin.R
import kotlinx.android.synthetic.main.fragment_view_goals.*
import kotlinx.android.synthetic.main.fragment_view_goals.view.*

class ViewGoalsFragment(goalsAdapter: GoalsAdapter) : Fragment() {

    private val viewModel by activityViewModels<GoalsViewModel>()
    private lateinit var p: GoalsFragment
    private var adapter: GoalsAdapter = goalsAdapter

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
        rvGoals?.adapter = adapter
        rvGoals?.layoutManager = LinearLayoutManagerWrapper(this.requireContext())

        viewModel.getGoals.observe(viewLifecycleOwner) { goals -> adapter.setData(goals) }
    }
}
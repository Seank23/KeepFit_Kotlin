package com.example.keepfit_kotlin.ui.goals

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.data.Goal
import kotlinx.android.synthetic.main.fragment_goals.*


class GoalsFragment : Fragment(R.layout.fragment_goals) {

    private val viewModel by viewModels<GoalsViewModel>()
    private val fragments = arrayOfNulls<Fragment>(2)
    private lateinit var goalsAdapter: GoalsAdapter

    override fun onStart() {

        super.onStart()

        goalsAdapter = GoalsAdapter()

        fragments[0] = ViewGoalsFragment(goalsAdapter)
        fragments[1] = AddGoalFragment()

        setCurrentFragment(fragments[0]!!, R.id.flGoals)
    }

    fun onGoalAdded(goal: Goal) {
        viewModel.addGoal(goal)
        setCurrentFragment(fragments[0]!!, R.id.flGoals)
        Toast.makeText(requireContext(), "${goal.name} added successfully!", Toast.LENGTH_SHORT).show()
    }

    fun onNavBack() {
        setCurrentFragment(fragments[0]!!, R.id.flGoals)
    }

    fun onNavAddGoal() {
        setCurrentFragment(fragments[1]!!, R.id.flGoals)
    }

    private fun setCurrentFragment(fragment: Fragment, frameLayout: Int) =
        childFragmentManager.beginTransaction().apply {
            replace(frameLayout, fragment)
            commit()
        }
}
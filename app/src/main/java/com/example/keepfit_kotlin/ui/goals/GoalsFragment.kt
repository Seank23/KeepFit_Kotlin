package com.example.keepfit_kotlin.ui.goals

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.data.Goal

class GoalsFragment : Fragment(R.layout.fragment_goals) {

    private val viewModel by activityViewModels<GoalsViewModel>()
    private val fragments = arrayOfNulls<Fragment>(3)
    private lateinit var goalsAdapter: GoalsAdapter

    override fun onStart() {

        super.onStart()

        goalsAdapter = GoalsAdapter(this)

        fragments[0] = ViewGoalsFragment(goalsAdapter)
        fragments[1] = AddGoalFragment()
        fragments[2] = EditGoalFragment()

        setCurrentFragment(fragments[0]!!, R.id.flGoals)
    }

    fun onGoalAdded(goal: Goal) {
        viewModel.addGoal(goal)
        setCurrentFragment(fragments[0]!!, R.id.flGoals)
        Toast.makeText(requireContext(), "${goal.name} added successfully!", Toast.LENGTH_SHORT).show()
    }

    fun onGoalUpdated(goal: Goal) {
        viewModel.updateGoal(goal)
        setCurrentFragment(fragments[0]!!, R.id.flGoals)
        Toast.makeText(requireContext(), "Goal updated", Toast.LENGTH_SHORT).show()
    }

    fun onGoalDeleted(goal: Goal) {
        viewModel.deleteGoal(goal)
        setCurrentFragment(fragments[0]!!, R.id.flGoals)
        Toast.makeText(requireContext(), "${goal.name} removed", Toast.LENGTH_SHORT).show()
    }

    fun onSetActive(goal: Goal) {
        viewModel.setActive(goal)
        Toast.makeText(requireContext(), "Active goal: ${viewModel.getActiveGoal().name}", Toast.LENGTH_SHORT).show()
    }

    fun onNavBack() {
        setCurrentFragment(fragments[0]!!, R.id.flGoals)
    }

    fun onNavAddGoal() {
        setCurrentFragment(fragments[1]!!, R.id.flGoals)
    }

    fun onNavEditGoal(goal: Goal) {
        (fragments[2] as EditGoalFragment).currentGoal = goal
        setCurrentFragment(fragments[2]!!, R.id.flGoals)
    }

    fun getActiveGoal() = viewModel.getActiveGoal()

    fun getPrevActiveGoal() = viewModel.getPrevActiveGoal()

    fun getGoalCount() = viewModel.getGoalCount()

    fun checkGoalNameExists(name: String) = viewModel.checkGoalNameExists(name)

    private fun setCurrentFragment(fragment: Fragment, frameLayout: Int) =
        childFragmentManager.beginTransaction().apply {
            replace(frameLayout, fragment)
            commit()
        }
}
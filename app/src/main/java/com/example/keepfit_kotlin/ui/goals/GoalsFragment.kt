package com.example.keepfit_kotlin.ui.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.SharedData
import com.example.keepfit_kotlin.data.Goal
import com.example.keepfit_kotlin.ui.MainActivity

class GoalsFragment(sharedData: SharedData) : Fragment(R.layout.fragment_goals) {

    private val viewModel by activityViewModels<GoalsViewModel>{ GoalsViewModelFactory(requireActivity().application, sharedData) }
    private val fragments = arrayOfNulls<Fragment>(3)
    private lateinit var goalsAdapter: GoalsAdapter
    private var hasInit: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel.getGoals.observe(viewLifecycleOwner) {
            if(!hasInit) {
                viewModel.initSharedData()
                (activity as MainActivity).onNavHome()
                hasInit = true
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

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
        Toast.makeText(requireContext(), "Active goal: ${viewModel.getActiveGoal()?.name}", Toast.LENGTH_SHORT).show()
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

    fun checkGoalNameExists(name: String) = viewModel.checkGoalNameExists(name)

    fun observeGoalData(lifecycleOwner: LifecycleOwner) {
        viewModel.getGoals.observe(lifecycleOwner) { goals -> goalsAdapter.setData(goals) }
    }

    private fun setCurrentFragment(fragment: Fragment, frameLayout: Int) =
        childFragmentManager.beginTransaction().apply {
            replace(frameLayout, fragment)
            commit()
        }
}
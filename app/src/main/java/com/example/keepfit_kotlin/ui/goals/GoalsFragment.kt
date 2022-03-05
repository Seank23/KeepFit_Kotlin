package com.example.keepfit_kotlin.ui.goals

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.keepfit_kotlin.ui.settings.Prefs
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.Utils.toBool
import com.example.keepfit_kotlin.data.AppRepository
import com.example.keepfit_kotlin.data.Goal

class GoalsFragment(repository: AppRepository) : Fragment(R.layout.fragment_goals) {

    private val viewModel by activityViewModels<GoalsViewModel>{ GoalsViewModelFactory(requireActivity().application, repository) }
    private val fragments = arrayOfNulls<Fragment>(3)
    private lateinit var goalsAdapter: GoalsAdapter

    override fun onStart() {

        super.onStart()

        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)!!
        prefs.registerOnSharedPreferenceChangeListener { sharedPreferences, _ ->
            if(context != null)
                goalsAdapter.setGoalEditing(Prefs.getPrefs(sharedPreferences, getString(R.string.enable_goal_editing)).toBool)
        }

        goalsAdapter = GoalsAdapter(this)

        fragments[0] = ViewGoalsFragment(goalsAdapter)
        fragments[1] = AddGoalFragment()
        fragments[2] = EditGoalFragment()

        setCurrentFragment(fragments[0]!!, R.id.flGoals)
    }

    override fun onResume() {
        super.onResume()
        goalsAdapter.setGoalEditing(Prefs.getPrefs(activity?.getPreferences(Context.MODE_PRIVATE)!!, getString(R.string.enable_goal_editing)).toBool)
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

    fun deleteGoal(goal: Goal) {

        val dialog = AlertDialog.Builder(requireContext())
        dialog.setPositiveButton("Yes"){ _, _ -> onGoalDeleted(goal) }
        dialog.setNegativeButton("No"){ _, _ -> }
        dialog.setTitle("Delete ${goal.name}?")
        dialog.setMessage("Are you sure you want to delete the goal: ${goal.name}?")
        dialog.create().show()
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

    private fun onGoalDeleted(goal: Goal) {
        viewModel.deleteGoal(goal)
        setCurrentFragment(fragments[0]!!, R.id.flGoals)
        Toast.makeText(requireContext(), "${goal.name} removed", Toast.LENGTH_SHORT).show()
    }

    private fun setCurrentFragment(fragment: Fragment, frameLayout: Int) =
        childFragmentManager.beginTransaction().apply {
            replace(frameLayout, fragment)
            commit()
        }
}
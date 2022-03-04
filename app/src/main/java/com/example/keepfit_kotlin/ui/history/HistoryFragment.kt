package com.example.keepfit_kotlin.ui.history

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.data.AppRepository
import com.example.keepfit_kotlin.data.HistoryActivity
import com.example.keepfit_kotlin.ui.goals.AddGoalFragment
import com.example.keepfit_kotlin.ui.goals.EditGoalFragment
import com.example.keepfit_kotlin.ui.goals.GoalsAdapter
import com.example.keepfit_kotlin.ui.goals.ViewGoalsFragment
import com.example.keepfit_kotlin.ui.home.HomeViewModel
import com.example.keepfit_kotlin.ui.home.HomeViewModelFactory

class HistoryFragment(repository: AppRepository) : Fragment(R.layout.fragment_history) {

    private val viewModel by activityViewModels<HistoryViewModel>{ HistoryViewModelFactory(requireActivity().application, repository) }
    private val fragments = arrayOfNulls<Fragment>(2)

    override fun onStart() {

        super.onStart()

        fragments[0] = ViewHistoryFragment()
        fragments[1] = EditHistoryFragment()

        setCurrentFragment(fragments[0]!!, R.id.flHistory)
    }

    fun getHistoryByDate(date: String, onRetrieve: (HistoryActivity?) -> Unit) = viewModel.getHistoryByDate(date, onRetrieve)

    fun getCurrentHistoryActivity(): HistoryActivity? = viewModel.currentHistoryActivity

    fun getGoalNames(): List<String> = viewModel.getGoalNames()

    fun getGoalSteps(): List<Int> = viewModel.getGoalSteps()

    fun editHistory(date: String, additionalSteps: Int, goalIndex: Int) = viewModel.editHistory(date, additionalSteps, goalIndex)

    fun onNavEditHistory() {
        setCurrentFragment(fragments[1]!!, R.id.flHistory)
    }

    fun onNavBack() {
        setCurrentFragment(fragments[0]!!, R.id.flHistory)
    }

    private fun setCurrentFragment(fragment: Fragment, frameLayout: Int) =
        childFragmentManager.beginTransaction().apply {
            replace(frameLayout, fragment)
            commit()
        }
}
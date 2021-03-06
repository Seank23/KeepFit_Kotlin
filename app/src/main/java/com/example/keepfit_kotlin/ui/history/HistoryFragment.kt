package com.example.keepfit_kotlin.ui.history

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.data.AppRepository
import com.example.keepfit_kotlin.data.HistoryActivity
import com.example.keepfit_kotlin.ui.home.LogsAdapter
import java.util.*

class HistoryFragment(repository: AppRepository) : Fragment(R.layout.fragment_history) {

    private val viewModel by activityViewModels<HistoryViewModel>{ HistoryViewModelFactory(requireActivity().application, repository) }
    private val fragments = arrayOfNulls<Fragment>(2)
    private lateinit var logsAdapter: LogsAdapter

    override fun onStart() {

        super.onStart()

        logsAdapter = LogsAdapter(this)

        fragments[0] = ViewHistoryFragment(logsAdapter)
        fragments[1] = EditHistoryFragment(logsAdapter)

        setCurrentFragment(fragments[0]!!, R.id.flHistory)
    }

    fun getHistoryByDate(date: String, onRetrieve: (HistoryActivity) -> Unit) = viewModel.getHistoryByDate(date, onRetrieve)

    fun getCurrentHistoryActivity(): HistoryActivity = viewModel.currentHistoryActivity

    fun getGoalNames(): List<String> = viewModel.getGoalNames()

    fun getGoalSteps(): List<Int> = viewModel.getGoalSteps()

    fun saveHistory(editedHistoryActivity: HistoryActivity) = viewModel.saveHistory(editedHistoryActivity)

    fun onNavEditHistory() {
        setCurrentFragment(fragments[1]!!, R.id.flHistory)
    }

    fun onNavBack(date: String) {
        (fragments[0]!! as ViewHistoryFragment).dateToShow = date
        setCurrentFragment(fragments[0]!!, R.id.flHistory)
    }

    fun onDeleteLog(logIndex: Int) {
        (fragments[1]!! as EditHistoryFragment).deleteLog(logIndex)
    }

    fun getGraphData(startDate: String, endDate: String, onRetrieve: (List<HistoryActivity>) -> Unit) = viewModel.getGraphData(startDate, endDate, onRetrieve)

    private fun setCurrentFragment(fragment: Fragment, frameLayout: Int) =
        childFragmentManager.beginTransaction().apply {
            replace(frameLayout, fragment)
            commit()
        }
}
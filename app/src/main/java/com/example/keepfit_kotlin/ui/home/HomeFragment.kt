package com.example.keepfit_kotlin.ui.home

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.keepfit_kotlin.LinearLayoutManagerWrapper
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.Utils.safeInt
import com.example.keepfit_kotlin.data.AppRepository
import com.example.keepfit_kotlin.data.Log
import com.example.keepfit_kotlin.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment(repository: AppRepository) : Fragment(R.layout.fragment_home) {

    private val viewModel by activityViewModels<HomeViewModel>{ HomeViewModelFactory(requireActivity().application, repository) }
    private lateinit var logsAdapter: LogsAdapter

    private var hasInit: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Initialise app data from database
        viewModel.getGoals.observe(viewLifecycleOwner) {
            if(!hasInit) {
                lblCurGoalName.text = viewModel.getActiveGoalName()
                lblCurGoalSteps.text = "${viewModel.getActiveGoalSteps()} steps"
                viewModel.getTodaysLogs.observe(viewLifecycleOwner) {
                    if(!hasInit) {
                        setProgressTracker(viewModel.getSteps(), viewModel.getGoalProgress())
                        hasInit = true
                    }
                }
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onStart() {

        super.onStart()

        logsAdapter = LogsAdapter(this)
        rvLogs.adapter = logsAdapter
        rvLogs.layoutManager = LinearLayoutManagerWrapper(this.requireContext())

        viewModel.getTodaysLogs.observe(viewLifecycleOwner) { logs ->
            logsAdapter.setData(logs)
            setProgressTracker(viewModel.getSteps(), viewModel.getGoalProgress())
        }

        btnActiveGoal.setOnClickListener {
            (activity as MainActivity).onNavGoals()
        }

        btnViewHistory.setOnClickListener {
            (activity as MainActivity).onNavHistory()
        }

        fbtnAddSteps.setOnClickListener {
            viewModel.addSteps(safeInt(txtStepsInput.text.toString(), 0))
            txtStepsInput.setText("")
            //Toast.makeText(this.context, "Steps added successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {

        super.onResume()
        lblCurGoalName.text = viewModel.getActiveGoalName()
        lblCurGoalSteps.text = "${viewModel.getActiveGoalSteps()} steps"
        setProgressTracker(viewModel.getSteps(), viewModel.getGoalProgress())
    }

    private fun setProgressTracker(steps: Int, progress: Float) {

        lblSteps.text = "$steps steps"
        lblPercent.text = "${kotlin.math.min((progress * 100).toInt(), 100)}%"
        ObjectAnimator.ofInt(pbCircularTracker, "progress", ((100 * progress) * 0.6667).toInt()).setDuration(500).start()
        ObjectAnimator.ofInt(pbStepsBar, "progress", (progress * 100).toInt()).setDuration(500).start()
    }

    fun onDeleteLog(log: Log) {

        val dialog = AlertDialog.Builder(requireContext())
        dialog.setPositiveButton("Yes"){ _, _ -> viewModel.deleteLog(log) }
        dialog.setNegativeButton("No"){ _, _ -> }
        dialog.setTitle("Delete logged activity?")
        dialog.setMessage("Are you sure you want to delete this logged activity: ${log.steps} steps?")
        dialog.create().show()
    }
}
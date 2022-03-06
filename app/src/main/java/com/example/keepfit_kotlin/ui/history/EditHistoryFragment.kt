package com.example.keepfit_kotlin.ui.history

import android.animation.ObjectAnimator
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.example.keepfit_kotlin.LinearLayoutManagerWrapper
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.Utils.getFormattedDate
import com.example.keepfit_kotlin.Utils.selected
import com.example.keepfit_kotlin.data.AppRepository
import com.example.keepfit_kotlin.data.HistoryActivity
import com.example.keepfit_kotlin.ui.home.LogsAdapter
import kotlinx.android.synthetic.main.fragment_edit_history.*
import java.text.SimpleDateFormat
import java.util.*

class EditHistoryFragment(logsAdapter: LogsAdapter) : Fragment() {

    private val viewModel by activityViewModels<EditHistoryViewModel>()
    private lateinit var p: HistoryFragment
    private val adapter = logsAdapter
    private var timeStr = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        p = parentFragment as HistoryFragment
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_history, container, false)
    }

    override fun onStart() {
        super.onStart()

        rvLogs.adapter = adapter
        rvLogs.layoutManager = LinearLayoutManagerWrapper(this.requireContext())

        ibtnBack.setOnClickListener {
            p.onNavBack(viewModel.editedHistoryActivity.date)
        }

        fbtnSave.setOnClickListener {
            p.saveHistory(viewModel.editedHistoryActivity)
            p.onNavBack(viewModel.editedHistoryActivity.date)
        }

        fbtnHistoryAddSteps.setOnClickListener {
            viewModel.createLog(txtHistoryStepsInput.text.toString().toInt(), timeStr)
            txtHistoryStepsInput.setText("")
            updateHistoryUI()
        }

        tbtnLogTime.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            val timePicker = TimePickerDialog(requireContext(), { _, hour, minute ->
                timeStr = "$hour:$minute"
                tbtnLogTime.text = timeStr
            }, hour, minute, true)
            timePicker.show()
        }

        spGoal.selected {
            viewModel.setGoal(p.getGoalNames()[it], p.getGoalSteps()[it])
            updateHistoryUI()
        }

    }

    override fun onResume() {
        super.onResume()

        val ch = p.getCurrentHistoryActivity()
        viewModel.editedHistoryActivity = HistoryActivity(ch.date, ch.totalSteps, ch.goalName, ch.goalSteps, ch.goalProgress, ch.logs.toMutableList())

        lblDate.text = getFormattedDate(viewModel.editedHistoryActivity.date)
        timeStr = SimpleDateFormat("HH:mm").format(Date())
        tbtnLogTime.text = timeStr
        updateHistoryUI()

        val allGoalNames = p.getGoalNames()
        ArrayAdapter(requireContext(), R.layout.spinner_item, allGoalNames)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spGoal.adapter = adapter

                if(allGoalNames.contains(viewModel.editedHistoryActivity.goalName))
                    spGoal.setSelection(adapter.getPosition(viewModel.editedHistoryActivity.goalName))
                else
                    spGoal.setSelection(0)
            }
    }

    fun deleteLog(logIndex: Int) {
        viewModel.deleteLog(logIndex)
        updateHistoryUI()
    }

    private fun updateHistoryUI() {

        val percentProgress = (viewModel.editedHistoryActivity.goalProgress * 100).toInt()
        lblSteps.text = "${viewModel.editedHistoryActivity.totalSteps} ($percentProgress%)"
        ObjectAnimator.ofInt(pbTrackerStepsBar, "progress", percentProgress).setDuration(500).start()
        adapter.setData(viewModel.editedHistoryActivity.logs)
    }
}
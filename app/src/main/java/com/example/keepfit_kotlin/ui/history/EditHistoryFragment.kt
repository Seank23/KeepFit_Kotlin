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
import com.example.keepfit_kotlin.LinearLayoutManagerWrapper
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.Utils.getFormattedDate
import com.example.keepfit_kotlin.Utils.getTimestamp
import com.example.keepfit_kotlin.Utils.selected
import com.example.keepfit_kotlin.data.HistoryActivity
import com.example.keepfit_kotlin.data.Log
import com.example.keepfit_kotlin.ui.home.LogsAdapter
import kotlinx.android.synthetic.main.fragment_edit_history.*
import java.text.SimpleDateFormat
import java.util.*

class EditHistoryFragment(logsAdapter: LogsAdapter) : Fragment() {

    private lateinit var p: HistoryFragment
    private val adapter = logsAdapter

    private lateinit var editedHistoryActivity: HistoryActivity
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
            p.onNavBack(editedHistoryActivity.date)
        }

        fbtnSave.setOnClickListener {
            saveHistory()
        }

        fbtnHistoryAddSteps.setOnClickListener {
            createLog(txtHistoryStepsInput.text.toString().toInt(), timeStr)
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
            editedHistoryActivity.goalName = p.getGoalNames()[it]
            editedHistoryActivity.goalSteps = p.getGoalSteps()[it]
            editedHistoryActivity.goalProgress = editedHistoryActivity.totalSteps.toFloat() / editedHistoryActivity.goalSteps
            updateHistoryUI()
        }

    }

    override fun onResume() {
        super.onResume()

        val ch = p.getCurrentHistoryActivity()
        editedHistoryActivity = HistoryActivity(ch.date, ch.totalSteps, ch.goalName, ch.goalSteps, ch.goalProgress, ch.logs.toMutableList())
        lblDate.text = getFormattedDate(editedHistoryActivity.date)
        timeStr = SimpleDateFormat("HH:mm").format(Date())
        tbtnLogTime.text = timeStr
        updateHistoryUI()

        val allGoalNames = p.getGoalNames()
        ArrayAdapter(requireContext(), R.layout.spinner_item, allGoalNames)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spGoal.adapter = adapter

                if(allGoalNames.contains(editedHistoryActivity.goalName))
                    spGoal.setSelection(adapter.getPosition(editedHistoryActivity.goalName))
                else
                    spGoal.setSelection(0)
            }
    }

    fun deleteLog(logIndex: Int) {

        val log = editedHistoryActivity.logs[logIndex]
        editedHistoryActivity.logs.removeAt(logIndex)
        editedHistoryActivity.totalSteps -= log.steps
        editedHistoryActivity.goalProgress = editedHistoryActivity.totalSteps.toFloat() / editedHistoryActivity.goalSteps
        updateHistoryUI()
    }

    private fun updateHistoryUI() {

        val percentProgress = (editedHistoryActivity.goalProgress * 100).toInt()
        lblSteps.text = "${editedHistoryActivity.totalSteps} ($percentProgress%)"
        ObjectAnimator.ofInt(pbTrackerStepsBar, "progress", percentProgress).setDuration(500).start()
        adapter.setData(editedHistoryActivity.logs)
    }

    private fun createLog(steps: Int, time: String) {

        editedHistoryActivity.logs.add(Log(0, getTimestamp(editedHistoryActivity.date), time, steps, p.getGoalNames()[spGoal.selectedItemId.toInt()], p.getGoalSteps()[spGoal.selectedItemId.toInt()]))
        editedHistoryActivity.logs.sortByDescending { it.time }
        editedHistoryActivity.totalSteps += steps
        editedHistoryActivity.goalProgress = editedHistoryActivity.totalSteps.toFloat() / editedHistoryActivity.goalSteps
    }

    private fun saveHistory() {
        p.editHistory(editedHistoryActivity)
        p.onNavBack(editedHistoryActivity.date)
    }
}
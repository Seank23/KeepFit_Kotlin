package com.example.keepfit_kotlin.ui.history

import android.animation.ObjectAnimator
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.Utils.MONTHS
import com.example.keepfit_kotlin.Utils.getDateString
import com.example.keepfit_kotlin.data.HistoryActivity
import kotlinx.android.synthetic.main.fragment_view_history.*
import java.text.SimpleDateFormat
import java.util.*

class ViewHistoryFragment : Fragment() {

    private lateinit var p: HistoryFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        p = parentFragment as HistoryFragment
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_history, container, false)
    }

    override fun onStart() {
        super.onStart()

        btnDatePicker.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, year, month, day ->
                p.getHistoryByDate(getDateString(year, month, day)) {
                    btnDatePicker.text = "$day ${MONTHS[month]} $year"
                    setDailyTracker(it)
                }
            }, year, month, day)

            datePicker.datePicker.maxDate = System.currentTimeMillis()
            datePicker.show()
        }

        btnEditDay.setOnClickListener {
            p.onNavEditHistory()
        }
    }

    override fun onResume() {
        super.onResume()

        p.getHistoryByDate(SimpleDateFormat("ddMMyyyy").format(Date())) {
            btnDatePicker.text = "${SimpleDateFormat("dd").format(Date())} ${MONTHS[SimpleDateFormat("MM").format(Date()).toInt() - 1]} ${SimpleDateFormat("yyyy").format(Date())}"
            setDailyTracker(it)
        }
    }

    private fun setDailyTracker(dayHistory: HistoryActivity?) {

        if(dayHistory != null) {
            val percentProgress = (dayHistory.goalProgress * 100).toInt()
            lblTrackerGoalName.text = dayHistory.goalName
            lblTrackerGoalSteps.text = "${dayHistory.goalSteps} steps"
            lblTrackerSteps.text = "${dayHistory.totalSteps} ($percentProgress%)"
            ObjectAnimator.ofInt(pbTrackerStepsBar, "progress", percentProgress).setDuration(500).start()
        } else {
            lblTrackerGoalName.text = "None"
            lblTrackerGoalSteps.text = "0 steps"
            lblTrackerSteps.text = "0"
            pbTrackerStepsBar.progress = 0
        }
    }
}
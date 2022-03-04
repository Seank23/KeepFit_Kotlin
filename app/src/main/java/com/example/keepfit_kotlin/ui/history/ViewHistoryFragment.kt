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
import com.example.keepfit_kotlin.Utils.getFormattedDate
import com.example.keepfit_kotlin.data.HistoryActivity
import kotlinx.android.synthetic.main.fragment_view_history.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

class ViewHistoryFragment : Fragment() {

    private lateinit var p: HistoryFragment
    var dateToShow: String = SimpleDateFormat("ddMMyyyy").format(Date(System.currentTimeMillis() - 86400000))

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

            datePicker.datePicker.maxDate = System.currentTimeMillis() - 86400000
            datePicker.show()
        }

        btnEditDay.setOnClickListener {
            p.onNavEditHistory()
        }
    }

    override fun onResume() {
        super.onResume()

        p.getHistoryByDate(dateToShow) {
            btnDatePicker.text = getFormattedDate(it.date)
            setDailyTracker(it)
        }
    }

    private fun setDailyTracker(dayHistory: HistoryActivity) {

        val percentProgress = (dayHistory.goalProgress * 100).toInt()
        lblTrackerGoalName.text = dayHistory.goalName
        lblTrackerGoalSteps.text = "${dayHistory.goalSteps} steps"
        lblTrackerSteps.text = "${dayHistory.totalSteps} ($percentProgress%)"
        ObjectAnimator.ofInt(pbTrackerStepsBar, "progress", percentProgress).setDuration(500).start()
    }
}
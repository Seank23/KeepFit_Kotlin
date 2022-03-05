package com.example.keepfit_kotlin.ui.history

import android.animation.ObjectAnimator
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.keepfit_kotlin.ui.settings.Prefs
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.Utils.getDateString
import com.example.keepfit_kotlin.Utils.getFormattedDate
import com.example.keepfit_kotlin.Utils.toPx
import com.example.keepfit_kotlin.data.HistoryActivity
import com.example.keepfit_kotlin.data.Log
import com.example.keepfit_kotlin.ui.home.LogsAdapter
import kotlinx.android.synthetic.main.fragment_view_history.*
import java.text.SimpleDateFormat
import java.util.*

class ViewHistoryFragment(logsAdapter: LogsAdapter) : Fragment() {

    private lateinit var p: HistoryFragment
    private val adapter = logsAdapter
    var dateToShow: String = SimpleDateFormat("ddMMyyyy").format(Date(System.currentTimeMillis() - 86400000))
    private lateinit var logsList: List<Log>
    private var showLogs = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        p = parentFragment as HistoryFragment
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_history, container, false)
    }

    override fun onStart() {
        super.onStart()

        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)!!
        prefs.registerOnSharedPreferenceChangeListener { sharedPreferences, _ ->
            onPrefsChange(sharedPreferences)
        }

        btnDatePicker.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, year, month, day ->
                p.getHistoryByDate(getDateString(year, month, day)) {
                    dateToShow = getDateString(year, month, day)
                    btnDatePicker.text = getFormattedDate(dateToShow)
                    setDailyTracker(it)
                }
            }, year, month, day)

            datePicker.datePicker.maxDate = System.currentTimeMillis() - 86400000
            datePicker.show()
        }

        btnEditDay.setOnClickListener {
            p.onNavEditHistory()
        }

        btnViewActivity.setOnClickListener {

            if(!showLogs) {
                adapter.readOnly = true
                val activityDialog = ViewHistoryDialogFragment(adapter, logsList, getFormattedDate(dateToShow))
                childFragmentManager.beginTransaction().apply {
                    replace(R.id.flDialog, activityDialog)
                    commit()
                }
                flDialog.translationZ = 10f
                flDialog.visibility = View.VISIBLE
                btnViewActivity.text = "View Stats"
                showLogs = true
            } else {
                adapter.readOnly = false
                flDialog.translationZ = -10f
                flDialog.visibility = View.INVISIBLE
                btnViewActivity.text = "View Logs"
                showLogs = false
            }

        }
    }

    override fun onResume() {
        super.onResume()

        onPrefsChange(activity?.getPreferences(Context.MODE_PRIVATE)!!)

        p.getHistoryByDate(dateToShow) {
            btnDatePicker.text = getFormattedDate(it.date)
            setDailyTracker(it)
        }
    }

    private fun setDailyTracker(dayHistory: HistoryActivity) {

        logsList = dayHistory.logs
        val percentProgress = (dayHistory.goalProgress * 100).toInt()
        lblTrackerGoalName.text = dayHistory.goalName
        lblTrackerGoalSteps.text = "${dayHistory.goalSteps} steps"
        lblTrackerSteps.text = "${dayHistory.totalSteps} ($percentProgress%)"
        ObjectAnimator.ofInt(pbTrackerStepsBar, "progress", percentProgress).setDuration(500).start()
    }

    private fun onPrefsChange(prefs: SharedPreferences) {

        if (context != null) {
            if (Prefs.getPrefs(prefs, getString(R.string.enable_history_editing)) == 1) {
                btnEditDay.visibility = View.VISIBLE
                (btnViewActivity.layoutParams as FrameLayout.LayoutParams).width =
                    170.toPx(requireContext())
                btnViewActivity.requestLayout()
            } else {
                btnEditDay.visibility = View.INVISIBLE
                (btnViewActivity.layoutParams as FrameLayout.LayoutParams).width =
                    FrameLayout.LayoutParams.MATCH_PARENT
                btnViewActivity.requestLayout()
            }
        }
    }
}
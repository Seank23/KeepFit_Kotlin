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
import com.example.keepfit_kotlin.Utils.getFormattedDateNoYear
import com.example.keepfit_kotlin.Utils.toPx
import com.example.keepfit_kotlin.data.HistoryActivity
import com.example.keepfit_kotlin.data.Log
import com.example.keepfit_kotlin.ui.home.LogsAdapter
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.fragment_view_history.*
import java.text.SimpleDateFormat
import java.util.*

class ViewHistoryFragment(logsAdapter: LogsAdapter) : Fragment() {

    private lateinit var p: HistoryFragment
    private lateinit var logsDialog: ViewHistoryDialogFragment
    private val adapter = logsAdapter
    var dateToShow: String = SimpleDateFormat("ddMMyyyy").format(Date(System.currentTimeMillis() - 86400000)) // Yesterday
    private lateinit var logsList: List<Log>
    private var showLogs = false
    private var graphStartDate = SimpleDateFormat("ddMMyyyy").format(Date(System.currentTimeMillis() - 604800000)) // Yesterday - 1 week
    private var graphEndDate = SimpleDateFormat("ddMMyyyy").format(Date(System.currentTimeMillis() - 86400000)) // Yesterday
    private val graphDatesList = mutableListOf<String>()


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

        setupGraph()

        btnDatePicker.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, year, month, day ->
                dateToShow = getDateString(year, month, day)
                updateTrackerDate(dateToShow)
            }, year, month, day)

            datePicker.datePicker.maxDate = System.currentTimeMillis() - 86400000
            datePicker.show()
        }

        logsDialog = ViewHistoryDialogFragment(adapter)
        childFragmentManager.beginTransaction().apply {
            replace(R.id.flDialog, logsDialog)
            commit()
        }
        flDialog.translationZ = -10f
        flDialog.visibility = View.INVISIBLE

        btnEditDay.setOnClickListener {
            p.onNavEditHistory()
        }

        btnViewActivity.setOnClickListener {

            if(!showLogs) {
                adapter.readOnly = true
                logsDialog.setData(getFormattedDate(dateToShow), logsList)
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
        updateTrackerDate(dateToShow)
        setHistoryGraphData()
    }

    private fun updateTrackerDate(date: String) {
        p.getHistoryByDate(date) {
            btnDatePicker.text = getFormattedDate(it.date)
            setDailyTrackerUI(it)
            if(showLogs) {
                logsDialog.setData(getFormattedDate(dateToShow), logsList)
            }
        }
    }

    private fun setDailyTrackerUI(dayHistory: HistoryActivity) {

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

    private fun setupGraph() {

        chartHistory.animateXY(1000, 1000)
        chartHistory.axisLeft.setDrawGridLines(false)
        chartHistory.axisLeft.textColor = resources.getColor(R.color.main_color_1)
        chartHistory.axisRight.setDrawGridLines(false)
        chartHistory.axisRight.setDrawLabels(false)
        chartHistory.xAxis.setDrawAxisLine(false)
        chartHistory.xAxis.setDrawGridLines(false)
        chartHistory.xAxis.textColor = resources.getColor(R.color.main_color_1)
        chartHistory.description.isEnabled = false
        chartHistory.setScaleEnabled(false)
        chartHistory.setOnChartValueSelectedListener(object: OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if(e != null) {
                    dateToShow = graphDatesList[e.x.toInt()]
                    updateTrackerDate(dateToShow)
                }
            }
            override fun onNothingSelected() {}
        })
    }

    private fun setHistoryGraphData() {

        p.getGraphData(graphStartDate, graphEndDate) {
            val datesList = mutableListOf<String>()
            val goalVals = mutableListOf<BarEntry>()
            val stepsVals = mutableListOf<BarEntry>()

            for((index, data: HistoryActivity) in it.withIndex()) {
                graphDatesList.add(data.date)
                datesList.add(getFormattedDateNoYear(data.date))
                goalVals.add(BarEntry(index.toFloat(), data.goalSteps.toFloat()))
                stepsVals.add(BarEntry(index.toFloat(), data.totalSteps.toFloat()))
            }

            val goalDataset = BarDataSet(goalVals, "Goal")
            goalDataset.color = resources.getColor(R.color.main_color_3)
            goalDataset.setDrawValues(false)
            goalDataset.highLightColor = resources.getColor(R.color.white)
            val stepsDataset = BarDataSet(stepsVals, "Total Steps")
            stepsDataset.color = resources.getColor(R.color.main_color_1)
            stepsDataset.valueTextColor = resources.getColor(R.color.main_color_1)
            goalDataset.highLightColor = resources.getColor(R.color.white)
            val barData = BarData(goalDataset, stepsDataset)

            chartHistory.xAxis.valueFormatter = IndexAxisValueFormatter(datesList)
            chartHistory.data = barData

        }
    }
}
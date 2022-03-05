package com.example.keepfit_kotlin.ui.history

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.keepfit_kotlin.LinearLayoutManagerWrapper
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.data.Log
import com.example.keepfit_kotlin.ui.home.LogsAdapter
import kotlinx.android.synthetic.main.fragment_edit_history.*
import kotlinx.android.synthetic.main.fragment_edit_history.rvLogs
import kotlinx.android.synthetic.main.fragment_view_history_dialog.*

class ViewHistoryDialogFragment(logsAdapter: LogsAdapter, logsList: List<Log>, date: String): DialogFragment() {

    private val adapter = logsAdapter
    private val logs = logsList
    private val date = date

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_history_dialog, container, false)
    }

    override fun onStart() {
        super.onStart()

        lblActivityDate.text = date
        rvLogs.adapter = adapter
        rvLogs.layoutManager = LinearLayoutManagerWrapper(this.requireContext())
        adapter.setData(logs)
    }
}
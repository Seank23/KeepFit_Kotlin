package com.example.keepfit_kotlin.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.data.Log
import com.example.keepfit_kotlin.ui.history.HistoryFragment
import kotlinx.android.synthetic.main.item_log.view.*

class LogsAdapter(parentFragment: Fragment) : RecyclerView.Adapter<LogsAdapter.LogViewHolder>() {

    private var logList = emptyList<Log>()
    private val p = parentFragment
    var readOnly = false

    inner class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {

        return LogViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_log, parent, false))
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.itemView.apply {

            if(logList[position].time == "") {
                lblLogSteps.text = "No steps recorded"
                lblLogTime.visibility = View.INVISIBLE
                ibtnDelete.visibility = View.INVISIBLE
            } else {
                lblLogTime.text = logList[position].time
                lblLogSteps.text = "${logList[position].steps} steps"
                lblLogTime.visibility = View.VISIBLE
                ibtnDelete.visibility = View.VISIBLE
            }

            if(readOnly) {
                ibtnDelete.visibility = View.INVISIBLE
            } else {
                ibtnDelete.setOnClickListener {
                    if(p.javaClass.typeName == "com.example.keepfit_kotlin.ui.home.HomeFragment")
                        (p as HomeFragment).onDeleteLog(logList[position])
                    else if(p.javaClass.typeName == "com.example.keepfit_kotlin.ui.history.HistoryFragment")
                        (p as HistoryFragment).onDeleteLog(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return logList.size
    }

    fun setData(logs: List<Log>) {
        val logListMutable = logs.toMutableList()
        if(logListMutable.size > 1) {
            for(log: Log  in logListMutable) {
                if(log.time == "")
                    logListMutable.remove(log) // Remove placeholder log
            }
        }
        logList = logListMutable.ifEmpty { listOf(Log(0, 0L, "", 0, "", 0)) }
        notifyDataSetChanged()
    }

}
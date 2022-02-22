package com.example.keepfit_kotlin.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.data.Log
import kotlinx.android.synthetic.main.item_log.view.*

class LogsAdapter(parentFragment: HomeFragment) : RecyclerView.Adapter<LogsAdapter.LogViewHolder>() {

    private var logList = emptyList<Log>()
    private val p = parentFragment

    inner class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_log, parent, false)
        return LogViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.itemView.apply {
            lblLogTime.text = logList[position].time
            lblLogSteps.text = "${logList[position].steps} steps"

            ibtnDelete.setOnClickListener {
                p.onDeleteLog(logList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return logList.size
    }

    fun setData(logs: List<Log>) {
        logList = logs
        notifyDataSetChanged()
    }

}
package com.example.keepfit_kotlin.ui.goals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.data.Goal
import com.example.keepfit_kotlin.databinding.ItemGoalBinding

class GoalsAdapter(var goals: List<Goal>) : RecyclerView.Adapter<GoalsAdapter.GoalViewHolder>() {

    inner class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_goal, parent, false)
        return GoalViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.lblTitle).text = goals[position].name
            findViewById<CheckBox>(R.id.chbActive).isChecked = goals[position].isActive
        }
    }

    override fun getItemCount(): Int {
        return goals.size
    }
}
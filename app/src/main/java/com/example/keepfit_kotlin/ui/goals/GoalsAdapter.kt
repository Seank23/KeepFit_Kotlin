package com.example.keepfit_kotlin.ui.goals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.data.Goal

class GoalsAdapter : RecyclerView.Adapter<GoalsAdapter.GoalViewHolder>() {

    private var goalsList = emptyList<Goal>()

    inner class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_goal, parent, false)
        return GoalViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.lblGoalName).text = goalsList[position].name
            findViewById<TextView>(R.id.lblGoalSteps).text = "Steps: " + goalsList[position].steps.toString()
            //findViewById<CheckBox>(R.id.chbActive).isChecked = goalsList[position].isActive
        }
    }

    override fun getItemCount(): Int {
        return goalsList.size
    }

    fun setData(goals: List<Goal>) {
        goalsList = goals
        notifyDataSetChanged()
    }
}
package com.example.keepfit_kotlin.ui.goals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.data.Goal
import kotlinx.android.synthetic.main.item_goal.view.*

class GoalsAdapter(parentFragment: GoalsFragment) : RecyclerView.Adapter<GoalsAdapter.GoalViewHolder>() {

    private var goalsList = emptyList<Goal>()
    private val p = parentFragment

    inner class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_goal, parent, false)
        return GoalViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.itemView.apply {
            lblGoalName.text = goalsList[position].name
            lblGoalSteps.text = "Steps: " + goalsList[position].steps.toString()

            btnGoalEdit.setOnClickListener {
                p.onNavEditGoal(goalsList[position])
            }
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
package com.example.keepfit_kotlin.ui.goals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.data.Goal
import kotlinx.android.synthetic.main.item_goal.view.*

class GoalsAdapter(parentFragment: GoalsFragment) : RecyclerView.Adapter<GoalsAdapter.GoalViewHolder>() {

    private var allowGoalEditing = true
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

            if(goalsList[position].name == p.getActiveGoal().name) {

                btnGoal.setBackgroundColor(ContextCompat.getColor(context, R.color.main_color_1))
                lblGoalName.setTextColor(ContextCompat.getColor(context, R.color.white))
                lblGoalSteps.setTextColor(ContextCompat.getColor(context, R.color.white))
                btnGoalEdit.setBackgroundColor(ContextCompat.getColor(context, R.color.main_color_1))
                btnGoalEdit.setTextColor(ContextCompat.getColor(context, R.color.white))
                btnGoalEdit.text = ""
                imgActiveIcon.translationZ = 10F
                imgDeleteIcon.translationZ = 0F
            } else if(goalsList[position] == p.getPrevActiveGoal()) {

                btnGoal.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                lblGoalName.setTextColor(ContextCompat.getColor(context, R.color.main_color_1))
                lblGoalSteps.setTextColor(ContextCompat.getColor(context, R.color.main_color_2))
                btnGoalEdit.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                btnGoalEdit.setTextColor(ContextCompat.getColor(context, R.color.main_color_1))
                imgActiveIcon.translationZ = 0F
                btnGoalEdit.text = "Edit"
            }

            if(!goalsList[position].isActive) {
                if (allowGoalEditing) {
                    imgDeleteIcon.translationZ = 0F
                    btnGoalEdit.text = "Edit"
                    btnGoalEdit.setOnClickListener {
                        p.onNavEditGoal(goalsList[position])
                    }
                } else {
                    imgDeleteIcon.translationZ = 10F
                    btnGoalEdit.text = ""
                    btnGoalEdit.setOnClickListener {
                        p.deleteGoal(goalsList[position])
                    }
                }
            }

            btnGoal.setOnClickListener {
                if(!goalsList[position].isActive)
                    p.onSetActive(goalsList[position])
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

    fun setGoalEditing(enabled: Boolean) {
        allowGoalEditing = enabled
        notifyDataSetChanged()
    }
}
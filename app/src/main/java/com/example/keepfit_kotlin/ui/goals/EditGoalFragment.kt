package com.example.keepfit_kotlin.ui.goals

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.Utils.safeInt
import com.example.keepfit_kotlin.data.Goal
import kotlinx.android.synthetic.main.fragment_edit_goal.*

class EditGoalFragment : Fragment() {

    private lateinit var p: GoalsFragment
    var currentGoal: Goal = Goal(0, "", 0, false)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_goal, container, false)

        p = parentFragment as GoalsFragment

        return view
    }

    override fun onStart() {
        super.onStart()

        fbtnSave.setOnClickListener {
            updateGoal()
        }

        ibtnBack.setOnClickListener {
            p.onNavBack()
        }

        fbtnDelete.setOnClickListener {
            p.deleteGoal(currentGoal)
        }
    }

    override fun onResume() {
        super.onResume()

        txtEditGoalName.setText(currentGoal.name)
        txtEditTargetSteps.setText(currentGoal.steps.toString())
    }

    private fun updateGoal() {

        val goalName = txtEditGoalName.text.toString()
        val targetSteps = safeInt(txtEditTargetSteps.text.toString(), 0)

        if(checkInput(goalName, targetSteps)) {
            if(p.checkGoalNameExists(goalName) && goalName != currentGoal.name) {
                Toast.makeText(requireContext(), "Goal could not be updated, a goal with this name already exists", Toast.LENGTH_SHORT).show()
            } else {
                val updatedGoal = Goal(currentGoal.id, goalName, targetSteps, false)
                p.onGoalUpdated(updatedGoal)
            }
        } else {
            Toast.makeText(requireContext(), "Goal could not be edited, please try again...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkInput(goalName: String, targetSteps: Int): Boolean {
        return !(TextUtils.isEmpty(goalName) || targetSteps <= 0)
    }
}

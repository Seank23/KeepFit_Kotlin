package com.example.keepfit_kotlin.ui.goals

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.data.Goal
import kotlinx.android.synthetic.main.fragment_add_goal.*
import kotlinx.android.synthetic.main.fragment_add_goal.view.*

class AddGoalFragment : Fragment() {

    private lateinit var p: GoalsFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_goal, container, false)

        p = parentFragment as GoalsFragment

        return view
    }

    override fun onStart() {
        super.onStart()

        btnAdd.setOnClickListener {
            sendToDB()
        }

        ibtnBack.setOnClickListener {
            p.onNavBack()
        }
    }

    private fun sendToDB() {

        val goalName = txtGoalName.text.toString()
        val targetSteps = safeInt(txtTargetSteps.text.toString(), 0)

        if(checkInput(goalName, targetSteps)) {
            val newGoal = Goal(0, goalName, targetSteps, false)
            p.onGoalAdded(newGoal)
        } else {
            Toast.makeText(requireContext(), "Goal could not be created, please try again...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkInput(goalName: String, targetSteps: Int): Boolean {
        return !(TextUtils.isEmpty(goalName) || targetSteps <= 0)
    }

    private fun safeInt(text: String, fallback: Int): Int {
        return text.toIntOrNull() ?: fallback
    }
}

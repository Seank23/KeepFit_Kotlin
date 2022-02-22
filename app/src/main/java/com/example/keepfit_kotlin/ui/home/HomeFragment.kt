package com.example.keepfit_kotlin.ui.home

import android.animation.ObjectAnimator
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.SharedData
import com.example.keepfit_kotlin.Utils.safeInt
import com.example.keepfit_kotlin.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Math.min

class HomeFragment(sharedData: SharedData) : Fragment(R.layout.fragment_home) {

    private val viewModel by activityViewModels<HomeViewModel>{ HomeViewModelFactory(requireActivity().application, sharedData) }

    override fun onStart() {

        super.onStart()

        btnViewGoals.setOnClickListener {
            (activity as MainActivity).onNavGoals()
        }

        btnViewHistory.setOnClickListener {
            (activity as MainActivity).onNavHistory()
        }

        fbtnAddSteps.setOnClickListener {

            viewModel.addSteps(safeInt(txtStepsInput.text.toString(), 0))
            setProgressTracker(viewModel.getSteps(), viewModel.getGoalProgress())
            txtStepsInput.setText("")
            //Toast.makeText(this.context, "Steps added successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {

        super.onResume()
        lblCurGoalName.text = viewModel.getActiveGoalName()
        lblCurGoalSteps.text = "${viewModel.getActiveGoalSteps()} steps"
        setProgressTracker(viewModel.getSteps(), viewModel.getGoalProgress())
    }

    private fun setProgressTracker(steps: Int, progress: Float) {

        lblSteps.text = "$steps steps"
        lblPercent.text = "${kotlin.math.min((progress * 100).toInt(), 100)}%"
        ObjectAnimator.ofInt(pbCircularTracker, "progress", ((100 * progress) * 0.6667).toInt()).setDuration(500).start()
        ObjectAnimator.ofInt(pbStepsBar, "progress", (progress * 100).toInt()).setDuration(500).start()
    }
}
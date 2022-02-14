package com.example.keepfit_kotlin.ui.home

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.SharedData
import com.example.keepfit_kotlin.Utils.safeInt
import com.example.keepfit_kotlin.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_home.*

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
            lblSteps.text = "${viewModel.getSteps()} steps"
            txtStepsInput.setText("")
            Toast.makeText(this.context, "Steps added successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {

        super.onResume()

        lblSteps.text = "${viewModel.getSteps()} steps"
        lblCurGoalName.text = viewModel.getActiveGoalName()
        lblCurGoalSteps.text = "${viewModel.getActiveGoalSteps()} steps"
    }
}
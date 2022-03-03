package com.example.keepfit_kotlin.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.Utils.getFormattedDate
import kotlinx.android.synthetic.main.fragment_edit_history.*

class EditHistoryFragment : Fragment() {

    private lateinit var p: HistoryFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        p = parentFragment as HistoryFragment
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_history, container, false)
    }

    override fun onStart() {
        super.onStart()

        ibtnBack.setOnClickListener {
            p.onNavBack()
        }

        fbtnSave.setOnClickListener {
            saveHistory()
        }
    }

    override fun onResume() {
        super.onResume()

        val currentHistory = p.getCurrentHistoryActivity()
        lblDate.text = getFormattedDate(currentHistory.date)
        txtSteps.setText(currentHistory.totalSteps.toString())

        val allGoalNames = p.getGoalNames()
        ArrayAdapter(requireContext(), R.layout.spinner_item, allGoalNames)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spGoal.adapter = adapter
                if(allGoalNames.contains(currentHistory.goalName))
                    spGoal.setSelection(adapter.getPosition(currentHistory.goalName))
                else
                    spGoal.setSelection(0)
            }
    }

    private fun saveHistory() {
        val additionalSteps = txtSteps.text.toString().toInt() - p.getCurrentHistoryActivity().totalSteps
        p.editHistory(p.getCurrentHistoryActivity().date, additionalSteps, spGoal.selectedItemId.toInt())
        p.onNavBack()
    }
}
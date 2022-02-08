package com.example.keepfit_kotlin

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment(R.layout.fragment_settings), AdapterView.OnItemSelectedListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        // Themes dropdown setup
        val themeSpinner = view?.findViewById<Spinner>(R.id.spTheme)

        ArrayAdapter.createFromResource(view.context, R.array.themes, R.layout.spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                themeSpinner.adapter = adapter
            }

        themeSpinner.onItemSelectedListener = this

        // Switch setup
        val swGoalEditing = view?.findViewById<Switch>(R.id.swGoalEditing)
        swGoalEditing.setOnClickListener { onClickSwitch(R.id.swGoalEditing, swGoalEditing.isChecked) }
        val swHistoryEditing = view?.findViewById<Switch>(R.id.swHistoryEditing)
        swHistoryEditing.setOnClickListener { onClickSwitch(R.id.swHistoryEditing, swHistoryEditing.isChecked) }

        val btnRemoveHistory = view?.findViewById<Button>(R.id.btnRemoveHistory)
        btnRemoveHistory.setOnClickListener {
            Toast.makeText(this.context, "Are you sure you want to remove all history?", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClickSwitch(id: Int, value: Boolean) {

        if(value) {
            when(id) {
                R.id.swGoalEditing -> Toast.makeText(this.context, "Goal editing enabled", Toast.LENGTH_SHORT).show()
                R.id.swHistoryEditing -> Toast.makeText(this.context, "History editing enabled", Toast.LENGTH_SHORT).show()
            }
        } else {
            when(id) {
                R.id.swGoalEditing -> Toast.makeText(this.context, "Goal editing disabled", Toast.LENGTH_SHORT).show()
                R.id.swHistoryEditing -> Toast.makeText(this.context, "History editing disabled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {

        Toast.makeText(this.context, parent?.getItemAtPosition(pos).toString() + " enabled", Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {

    }
}

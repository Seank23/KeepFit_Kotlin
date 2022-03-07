package com.example.keepfit_kotlin.ui.settings

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.Utils.selected
import com.example.keepfit_kotlin.Utils.toBool
import com.example.keepfit_kotlin.data.AppRepository
import com.example.keepfit_kotlin.ui.goals.GoalsViewModel
import com.example.keepfit_kotlin.ui.goals.GoalsViewModelFactory
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment(repository: AppRepository) : Fragment(R.layout.fragment_settings), AdapterView.OnItemSelectedListener {

    private val viewModel by activityViewModels<SettingsViewModel>{ SettingsViewModelFactory(requireActivity().application, repository) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        // Themes dropdown setup
        ArrayAdapter.createFromResource(view.context, R.array.themes, R.layout.spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spTheme.adapter = adapter
            }

        spTheme.selected {
            when(it) {
                0 -> Prefs.setPrefs(activity?.getPreferences(Context.MODE_PRIVATE)!!, getString(R.string.theme_mode), 0)
                1 -> Prefs.setPrefs(activity?.getPreferences(Context.MODE_PRIVATE)!!, getString(R.string.theme_mode), 1)
            }
        }
        spTheme.setSelection(Prefs.getPrefs(activity?.getPreferences(Context.MODE_PRIVATE)!!, getString(R.string.theme_mode)))

        // Theme selection unfinished
        spTheme.visibility = View.INVISIBLE

        // Switch setup
        swGoalEditing.setOnClickListener { onClickSwitch(R.id.swGoalEditing, swGoalEditing.isChecked) }
        swHistoryEditing.setOnClickListener { onClickSwitch(R.id.swHistoryEditing, swHistoryEditing.isChecked) }
        swGoalEditing.isChecked = Prefs.getPrefs(activity?.getPreferences(Context.MODE_PRIVATE)!!, getString(R.string.enable_goal_editing)).toBool
        swHistoryEditing.isChecked = Prefs.getPrefs(activity?.getPreferences(Context.MODE_PRIVATE)!!, getString(R.string.enable_history_editing)).toBool

        btnRemoveHistory.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
            dialog.setPositiveButton("Yes"){ _, _ -> viewModel.deleteAllHistory() }
            dialog.setNegativeButton("No"){ _, _ -> }
            dialog.setTitle("Delete All History?")
            dialog.setMessage("Are you sure you want to delete all recorded activity?\n\nWARNING: This cannot be undone")
            dialog.create().show()
        }
    }

    private fun onClickSwitch(id: Int, value: Boolean) {

        if(value) {
            when(id) {
                R.id.swGoalEditing -> {
                    Prefs.setPrefs(activity?.getPreferences(Context.MODE_PRIVATE)!!, getString(R.string.enable_goal_editing), 1)
                    Toast.makeText(this.context, "Goal editing enabled", Toast.LENGTH_SHORT).show()
                }
                R.id.swHistoryEditing -> {
                    Prefs.setPrefs(activity?.getPreferences(Context.MODE_PRIVATE)!!, getString(R.string.enable_history_editing), 1)
                    Toast.makeText(this.context, "History editing enabled", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            when(id) {
                R.id.swGoalEditing -> {
                    Prefs.setPrefs(activity?.getPreferences(Context.MODE_PRIVATE)!!, getString(R.string.enable_goal_editing), 0)
                    Toast.makeText(this.context, "Goal editing disabled", Toast.LENGTH_SHORT).show()
                }
                R.id.swHistoryEditing -> {
                    Prefs.setPrefs(activity?.getPreferences(Context.MODE_PRIVATE)!!, getString(R.string.enable_history_editing), 0)
                    Toast.makeText(this.context, "History editing disabled", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {}

    override fun onNothingSelected(parent: AdapterView<*>) {}
}

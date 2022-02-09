package com.example.keepfit_kotlin.ui.goals

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.data.Goal
import com.example.keepfit_kotlin.utilities.InjectorUtils

class GoalsFragment : Fragment(R.layout.fragment_goals) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeViewModel()
    }

    override fun onStart() {
        super.onStart()

        var goalList = mutableListOf(
            Goal("My Goal 1", 2000, true),
            Goal("My Goal 2", 5000, false),
            Goal("My Goal 3", 10000, false)
        )

        val adapter = GoalsAdapter(goalList)
        val recycler = view?.findViewById<RecyclerView>(R.id.rvGoals)
        recycler?.adapter = adapter
        recycler?.layoutManager = LinearLayoutManager(this.context)

        view?.findViewById<Button>(R.id.btnAddGoal)?.setOnClickListener {
            val goal = Goal(view?.findViewById<EditText>(R.id.txtGoal)?.text.toString(), 0, false)
            goalList.add(goal)
            adapter.notifyItemInserted(goalList.size - 1)
        }
    }

    private fun initializeViewModel() {

        val factory = InjectorUtils.provideGoalsViewModelFactory()
        val viewModel = ViewModelProviders.of(this, factory).get(GoalsViewModel::class.java)

        viewModel.getGoals().observe(this, Observer { goals ->
            val stringBuilder = StringBuilder()
            goals.forEach{ goal ->
                stringBuilder.append("$goal\n\n")
            }
            //view?.findViewById<TextView>(R.id.textView_quotes).text = stringBuilder.toString()
        })

        /*findViewById<Button>(R.id.button_add_quote).setOnClickListener {
            val goal = Goal(findViewById<TextView>(R.id.editText_quote).text.toString(),
                findViewById<TextView>(R.id.editText_author).text.toString())
            viewModel.addGoal(goal)
            findViewById<TextView>(R.id.editText_quote).text = ""
            findViewById<TextView>(R.id.editText_author).text = ""
        }*/
    }
}
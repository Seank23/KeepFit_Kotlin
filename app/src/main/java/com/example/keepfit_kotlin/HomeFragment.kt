package com.example.keepfit_kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class HomeFragment : Fragment(R.layout.fragment_home) {

    var numSteps = 0

    override fun onStart() {

        super.onStart()

        val btnAddSteps = view?.findViewById<Button>(R.id.btnAddSteps)

        btnAddSteps?.setOnClickListener {

            Toast.makeText(this.context, "Steps added successfully!", Toast.LENGTH_SHORT).show()
            val stepsInput = view?.findViewById<EditText>(R.id.txtStepsInput)
            numSteps += stepsInput?.text.toString().toInt()
            view?.findViewById<TextView>(R.id.lblSteps)?.text = "$numSteps steps"
            stepsInput?.setText("")
        }
    }

    override fun onResume() {

        super.onResume()

        view?.findViewById<TextView>(R.id.lblSteps)?.text = "$numSteps steps"
    }
}
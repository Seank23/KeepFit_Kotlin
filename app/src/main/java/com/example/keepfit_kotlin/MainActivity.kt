package com.example.keepfit_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val homeFragment = HomeFragment()
        val goalsFragment = GoalsFragment()
        val historyFragment = HistoryFragment()

        setCurrentFragment(homeFragment)

        val navView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.navHome -> setCurrentFragment(homeFragment)
                R.id.navGoals -> setCurrentFragment(goalsFragment)
                R.id.navHistory -> setCurrentFragment(historyFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
}
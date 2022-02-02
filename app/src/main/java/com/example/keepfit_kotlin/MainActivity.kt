package com.example.keepfit_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val settingsButton = findViewById<ImageView>(R.id.settingsButton)

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

        settingsButton.setOnClickListener {
            if(drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                drawerLayout.closeDrawer(Gravity.RIGHT)
            } else {
                drawerLayout.openDrawer(Gravity.RIGHT)
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
}
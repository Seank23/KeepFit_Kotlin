package com.example.keepfit_kotlin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.ui.goals.GoalsFragment
import com.example.keepfit_kotlin.ui.history.HistoryFragment
import com.example.keepfit_kotlin.ui.home.HomeFragment
import com.example.keepfit_kotlin.ui.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var settingsDrawer : DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Remove top bar
        supportActionBar?.hide()

        // Settings drawer setup
        settingsDrawer = findViewById(R.id.dlSettings)
        settingsDrawer.addDrawerListener(MyDrawerListener())
        settingsDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        // Fragments setup
        val homeFragment = HomeFragment()
        val goalsFragment = GoalsFragment()
        val historyFragment = HistoryFragment()
        val settingsFragment = SettingsFragment()

        setCurrentFragment(homeFragment, R.id.flFragment)
        setCurrentFragment(settingsFragment, R.id.flFragmentSettings)

        // Navigation bar setup
        val navView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.navHome -> setCurrentFragment(homeFragment, R.id.flFragment)
                R.id.navGoals -> setCurrentFragment(goalsFragment, R.id.flFragment)
                R.id.navHistory -> setCurrentFragment(historyFragment, R.id.flFragment)
            }
            true
        }
        navView.selectedItemId = R.id.navHome

        // Settings button event handler
        findViewById<ImageView>(R.id.btnSettings).setOnClickListener {
            if(settingsDrawer.isDrawerOpen(Gravity.RIGHT)) {
                settingsDrawer.closeDrawer(Gravity.RIGHT)
            } else {
                settingsDrawer.openDrawer(Gravity.RIGHT)
                settingsDrawer.translationZ = 1f
                settingsDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
        }

        findViewById<ImageView>(R.id.btnCloseDrawer).setOnClickListener {
            settingsDrawer.closeDrawer(Gravity.RIGHT)
        }
    }

    private fun setCurrentFragment(fragment: Fragment, frameLayout: Int) =
        supportFragmentManager.beginTransaction().apply {
            replace(frameLayout, fragment)
            commit()
        }

    inner class MyDrawerListener : DrawerLayout.SimpleDrawerListener() {

        override fun onDrawerClosed(drawerView: View) {
            settingsDrawer.translationZ = -1f
            settingsDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }
}
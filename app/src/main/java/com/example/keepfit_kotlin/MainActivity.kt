package com.example.keepfit_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
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
        setCurrentFragment(homeFragment)

        // Navigation bar setup
        val navView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.navHome -> setCurrentFragment(homeFragment)
                R.id.navGoals -> setCurrentFragment(goalsFragment)
                R.id.navHistory -> setCurrentFragment(historyFragment)
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

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }

    inner class MyDrawerListener : DrawerLayout.SimpleDrawerListener() {

        override fun onDrawerClosed(drawerView: View) {
            settingsDrawer.translationZ = -1f
            settingsDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }
}
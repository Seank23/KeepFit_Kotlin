package com.example.keepfit_kotlin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.keepfit_kotlin.R
import com.example.keepfit_kotlin.SharedData
import com.example.keepfit_kotlin.ui.goals.GoalsFragment
import com.example.keepfit_kotlin.ui.goals.GoalsViewModel
import com.example.keepfit_kotlin.ui.history.HistoryFragment
import com.example.keepfit_kotlin.ui.home.HomeFragment
import com.example.keepfit_kotlin.ui.home.HomeViewModel
import com.example.keepfit_kotlin.ui.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var settingsDrawer : DrawerLayout
    private val fragments = arrayOfNulls<Fragment>(4)
    private lateinit var sharedData: SharedData

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
        sharedData = SharedData()
        fragments[0] = HomeFragment(sharedData)
        fragments[1] = GoalsFragment(sharedData)
        fragments[2] = HistoryFragment()
        fragments[3] = SettingsFragment()

        setCurrentFragment(fragments[1]!!, R.id.flFragment)
        setCurrentFragment(fragments[3]!!, R.id.flFragmentSettings)

        // Navigation bar setup
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.navHome -> setCurrentFragment(fragments[0]!!, R.id.flFragment)
                R.id.navGoals -> setCurrentFragment(fragments[1]!!, R.id.flFragment)
                R.id.navHistory -> setCurrentFragment(fragments[2]!!, R.id.flFragment)
            }
            true
        }
        bottomNavigationView.selectedItemId = R.id.navGoals

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

    fun onNavHome() {
        setCurrentFragment(fragments[0]!!, R.id.flFragment)
        bottomNavigationView.selectedItemId = R.id.navHome
    }

    fun onNavGoals() {
        setCurrentFragment(fragments[1]!!, R.id.flFragment)
        bottomNavigationView.selectedItemId = R.id.navGoals
    }

    fun onNavHistory() {
        setCurrentFragment(fragments[2]!!, R.id.flFragment)
        bottomNavigationView.selectedItemId = R.id.navHistory
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
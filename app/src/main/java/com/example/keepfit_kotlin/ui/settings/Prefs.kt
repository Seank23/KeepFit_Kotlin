package com.example.keepfit_kotlin.ui.settings

import android.content.SharedPreferences

class Prefs {

    companion object {

        fun setPrefs(prefsFile: SharedPreferences, prefStr: String, value: Int) {
            with (prefsFile.edit()) {
                putInt(prefStr, value)
                apply()
            }
        }

        fun getPrefs(prefsFile: SharedPreferences, prefStr: String): Int {
            return prefsFile.getInt(prefStr, 0)
        }
    }
}
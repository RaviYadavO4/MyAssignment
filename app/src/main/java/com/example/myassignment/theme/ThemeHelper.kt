package com.example.myassignment.theme

import androidx.appcompat.app.AppCompatDelegate
import com.example.myassignment.persistence.Prefs

class ThemeHelper(private val prefs: Prefs) {

    fun applySavedTheme() {
        setDarkMode(prefs.getIsDarkMode())
    }

    fun setDarkMode(enabled: Boolean) {
        prefs.setIsDarkMode(enabled)
        AppCompatDelegate.setDefaultNightMode(
            if (enabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun isDarkMode(): Boolean = prefs.getIsDarkMode()
}

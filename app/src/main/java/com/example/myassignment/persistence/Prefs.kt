package com.example.myassignment.persistence

import android.content.SharedPreferences
import androidx.core.content.edit


class Prefs(private val prefs: SharedPreferences) {

    companion object {

        private const val PREF_KEY_IS_LOGIN = "PREF_KEY_IS_LOGIN"
        private const val PREF_KEY_DARK_MODE = "PREF_KEY_DARK_MODE"
        private const val PREF_KEY_NOTIFICATIONS_ENABLED = "PREF_KEY_NOTIFICATIONS_ENABLED"

    }

    fun setIsLogin(data: Boolean) {
        prefs.edit { putBoolean(PREF_KEY_IS_LOGIN, data) }
    }

    fun getIsLogin(): Boolean =
        prefs.getBoolean(PREF_KEY_IS_LOGIN, false)

    fun setIsDarkMode(data: Boolean) {
        prefs.edit { putBoolean(PREF_KEY_DARK_MODE, data) }
    }

    fun getIsDarkMode(): Boolean =
        prefs.getBoolean(PREF_KEY_DARK_MODE, false)

    fun setNotificationsEnabled(data: Boolean) {
        prefs.edit { putBoolean(PREF_KEY_NOTIFICATIONS_ENABLED, data) }
    }

    fun getIsNotificationsEnabled(): Boolean =
        prefs.getBoolean(PREF_KEY_NOTIFICATIONS_ENABLED, true)

    fun clear() {
        prefs.edit().clear().apply()
    }
}
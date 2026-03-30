package com.example.ui_rgr.utils

import android.content.Context
import com.example.ui_rgr.data.model.User

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("app", Context.MODE_PRIVATE)
    private val darkModeKey = "dark_mode"

    fun saveUser(user: User) {
        prefs.edit()
            .putString("token", user.token)
            .putString("email", user.email)
            .putString("name", user.name)
            .apply()
    }

    fun getToken() = prefs.getString("token", "") ?: ""
    fun getEmail() = prefs.getString("email", "") ?: ""
    fun getName() = prefs.getString("name", "") ?: ""

    fun isDarkMode() = prefs.getBoolean(darkModeKey, false)

    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean(darkModeKey, enabled).apply()
    }
    
    fun clear() {
        prefs.edit()
            .remove("token")
            .remove("email")
            .remove("name")
            .apply()
    }
}
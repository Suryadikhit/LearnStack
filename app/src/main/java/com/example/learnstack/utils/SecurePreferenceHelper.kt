package com.example.learnstack.utils

import android.content.Context
import android.util.Log

class SharedPreferenceHelper private constructor(context: Context) {

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val PREF_USER_USERNAME = "user_username"
        private const val PREF_IS_LOGGED_IN = "is_logged_in"
        private const val PREF_FIRST_LAUNCH = "first_launch"
        private const val PREF_DARK_MODE = "dark_mode"

        @Volatile
        private var instance: SharedPreferenceHelper? = null

        fun getInstance(context: Context): SharedPreferenceHelper {
            return instance ?: synchronized(this) {
                instance ?: SharedPreferenceHelper(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    // User login-related methods
    fun storeUsername(username: String) {
        preferences.edit().putString(PREF_USER_USERNAME, username).apply()
        setLoggedInStatus(true)
    }

    fun clearLoginInfo() {
        preferences.edit().clear().apply()
    }

    fun isUserLoggedIn(): Boolean =
        preferences.getBoolean(PREF_IS_LOGGED_IN, false)

    private fun setLoggedInStatus(isLoggedIn: Boolean) {
        preferences.edit().putBoolean(PREF_IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun getStoredUsername(): String =
        preferences.getString(PREF_USER_USERNAME, "Guest") ?: "Guest"

    // First launch-related methods
    fun isFirstLaunch(): Boolean =
        preferences.getBoolean(PREF_FIRST_LAUNCH, true)

    fun setFirstLaunch(isFirstLaunch: Boolean) {
        preferences.edit().putBoolean(PREF_FIRST_LAUNCH, isFirstLaunch).apply()
    }

    // Fetch the 'updatedAt' timestamp for a specific roadmap
    fun getUpdatedAtForRoadmap(roadmapName: String): String? {
        val timestamp = preferences.getString("$roadmapName-updatedAt", null)
        Log.d(
            "SharedPreferenceHelper",
            "Fetching updatedAt for roadmap: $roadmapName, Value: $timestamp"
        )
        return timestamp
    }

    // Set the 'updatedAt' timestamp for a specific roadmap
    fun setUpdatedAtForRoadmap(roadmapName: String, updatedAt: String) {
        preferences.edit().putString("$roadmapName-updatedAt", updatedAt).apply()
        Log.d(
            "SharedPreferenceHelper",
            "Setting updatedAt for roadmap: $roadmapName, Value: $updatedAt"
        )
    }


    fun setDarkMode(isDarkMode: Boolean) {
        preferences.edit().putBoolean(PREF_DARK_MODE, isDarkMode).apply()
    }

    fun isDarkMode(): Boolean {
        return preferences.getBoolean(PREF_DARK_MODE, false)
    }
}
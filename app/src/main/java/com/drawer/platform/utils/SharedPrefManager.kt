package com.drawer.platform.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager private constructor(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        @Volatile private var INSTANCE: SharedPrefManager? = null
        fun getInstance(context: Context): SharedPrefManager {
            return INSTANCE ?: synchronized(this) {
                SharedPrefManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    fun saveUserSession(userId: Long, mode: String, name: String, email: String) {
        prefs.edit()
            .putBoolean(Constants.KEY_IS_LOGGED_IN, true)
            .putLong(Constants.KEY_USER_ID, userId)
            .putString(Constants.KEY_USER_MODE, mode)
            .putString(Constants.KEY_USER_NAME, name)
            .putString(Constants.KEY_USER_EMAIL, email)
            .apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(Constants.KEY_IS_LOGGED_IN, false)
    fun getUserId(): Long = prefs.getLong(Constants.KEY_USER_ID, -1L)
    fun getUserMode(): String = prefs.getString(Constants.KEY_USER_MODE, "") ?: ""
    fun getUserName(): String = prefs.getString(Constants.KEY_USER_NAME, "") ?: ""
    fun getUserEmail(): String = prefs.getString(Constants.KEY_USER_EMAIL, "") ?: ""

    fun clearSession() = prefs.edit().clear().apply()

    fun setThemeMode(isDarkMode: Boolean) {
        prefs.edit().putBoolean("key_theme_mode", isDarkMode).apply()
    }

    fun isDarkMode(): Boolean = prefs.getBoolean("key_theme_mode", true)

    fun setOnboardingFinished(finished: Boolean) {
        prefs.edit().putBoolean(Constants.KEY_ONBOARDING_FINISHED, finished).apply()
    }

    fun isOnboardingFinished(): Boolean = prefs.getBoolean(Constants.KEY_ONBOARDING_FINISHED, false)
}

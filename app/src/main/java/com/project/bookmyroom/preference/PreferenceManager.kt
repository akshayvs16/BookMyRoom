package com.project.bookmyroom.preference
import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveCredentials(username: String, email: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, username)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_PASSWORD, password)
        editor.apply()
    }

    fun getUsername(): String? {
        return sharedPreferences.getString(KEY_USERNAME, null)
    }

    fun getEmail(): String? {
        return sharedPreferences.getString(KEY_EMAIL, null)
    }

    fun getPassword(): String? {
        return sharedPreferences.getString(KEY_PASSWORD, null)
    }

    fun clearCredentials() {
        val editor = sharedPreferences.edit()
        editor.remove(KEY_USERNAME)
        editor.remove(KEY_EMAIL)
        editor.remove(KEY_PASSWORD)
        editor.apply()
    }

    companion object {
        private const val PREF_NAME = "MyPrefs"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"
    }

    fun areCredentialsSaved(): Boolean {
        return sharedPreferences.contains(KEY_USERNAME) && sharedPreferences.contains(KEY_PASSWORD)
    }


}

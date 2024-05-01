package com.project.bookmyroom.preference
import android.content.Context
import android.content.SharedPreferences
import com.project.bookmyroom.model.data.User

class PreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveCredentials(rememberMe: String) {
        val editor = sharedPreferences.edit()
        editor.putString(REMEMBER_ME, rememberMe)

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
        editor.remove(REMEMBER_ME)
        editor.apply()
    }
    fun clearUser() {
        val editor = sharedPreferences.edit()
        editor.remove(KEY_ID)
        editor.remove(KEY_EMAIL)
        editor.remove(KEY_PASSWORD)
        editor.remove(KEY_USERNAME)
        editor.remove(KEY_PHONE)
        editor.remove(KEY_USER_ID)
        editor.remove(KEY_CREATED_AT)
        editor.remove(KEY_UPDATED_AT)
        editor.remove(KEY_V)
      //  editor.remove(REMEMBER_ME)
        editor.apply()
    }

    companion object {
        private const val PREF_NAME = "MyPrefs"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"

        private const val KEY_ID = "id"
        private const val KEY_PHONE = "phone"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_CREATED_AT = "created_at"
        private const val KEY_UPDATED_AT = "updated_at"
        private const val KEY_V = "__v"
        private const val REMEMBER_ME = "remember_me"
    }

    fun areCredentialsSaved(): Boolean {
        return sharedPreferences.contains(KEY_USERNAME) && sharedPreferences.contains(KEY_PASSWORD)
    }


    fun saveUser(user: User) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_ID, user._id)
        editor.putString(KEY_EMAIL, user.email)
        editor.putString(KEY_PASSWORD, user.password)
        editor.putString(KEY_USERNAME, user.firstName)
        editor.putString(KEY_PHONE, user.phone)
        editor.putString(KEY_USER_ID, user.userId)
        editor.putString(KEY_CREATED_AT, user.createdAt)
        editor.putString(KEY_UPDATED_AT, user.updatedAt)
        editor.putInt(KEY_V, user.__v)
        editor.apply()
    }

    fun getUser(): User? {
        val id = sharedPreferences.getString(KEY_ID, null)
        val email = sharedPreferences.getString(KEY_EMAIL, null)
        val password = sharedPreferences.getString(KEY_PASSWORD, null)
        val firstName = sharedPreferences.getString(KEY_USERNAME, null)
        val phone = sharedPreferences.getString(KEY_PHONE, null)
        val userId = sharedPreferences.getString(KEY_USER_ID, null)
        val createdAt = sharedPreferences.getString(KEY_CREATED_AT, null)
        val updatedAt = sharedPreferences.getString(KEY_UPDATED_AT, null)
        val v = sharedPreferences.getInt(KEY_V, 0)

        return if (id != null && email != null && password != null && firstName != null &&
            phone != null && userId != null && createdAt != null && updatedAt != null
        ) {
            User(id, email, password, firstName, phone, userId, createdAt, updatedAt, v)
        } else {
            null
        }
    }

}

package com.project.bookmyroom.view.fragments.data

import com.project.bookmyroom.view.fragments.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
           val user=" $username"
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), user)
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}
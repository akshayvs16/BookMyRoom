package com.project.bookmyroom.view.fragments.data

import android.util.Log
import com.project.bookmyroom.model.data.LoginRequest
import com.project.bookmyroom.model.data.LoginResponse
import com.project.bookmyroom.network.RetrofitClient
import com.project.bookmyroom.view.fragments.data.model.LoggedInUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    private lateinit var user: LoggedInUser
    val TAG = "LoginDataSource"

    fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            val loginRequest = LoginRequest(email = username, password = password)

            loginUser(loginRequest) { loggedInUser ->
                user = loggedInUser // Set the user property after successful login
            }
            // Check if user is not null and return Result.Success with user
            user.let {
                Log.d(TAG, "id: ${user.userId}")
                Log.d(TAG, "Name: ${user.displayName}")
                Result.Success(it)
            } ?: Result.Error(IOException("User not available"))
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }

    private fun loginUser(loginRequest: LoginRequest, callback: (LoggedInUser) -> Unit) {
        val apiService = RetrofitClient.instance
        val call = apiService.loginUser(loginRequest)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        // Handle successful login response
                        val message = loginResponse.message
                        val user = loginResponse.user!!

                        if (message == "Authentication Success") {


                            val loggedInUser =
                                LoggedInUser(userId = user.userId, displayName = user.firstName)
                            callback(loggedInUser)
                        } else {
                            // Handle unsuccessful authentication
                            callback(LoggedInUser("", ""))
                            Log.d(TAG, "onResponse: $message")
                        }
                    } else {
                        callback(LoggedInUser("", ""))
                        Log.e(TAG, "Login response body is empty")
                    }
                } else {
                    callback(LoggedInUser("", ""))
                    Log.e(TAG, "Unsuccessful login response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Handle network or API call failure
                callback(LoggedInUser("", ""))
                Log.e(TAG, "Network error: ${t.message}")
            }
        })
    }


}

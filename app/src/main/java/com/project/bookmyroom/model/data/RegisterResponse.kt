package com.project.bookmyroom.model.data

data class RegisterResponse(
    val message: String,
    val newUser: newUser? = null // Define the User model if needed
)
data class LoginResponse(
    val message: String,
    val user: User? = null
)
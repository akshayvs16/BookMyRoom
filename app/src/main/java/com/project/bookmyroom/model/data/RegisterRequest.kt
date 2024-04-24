package com.project.bookmyroom.model.data

data class RegisterRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val phone: String
)
data class LoginRequest(
    val email: String,
    val password: String
)
data class DistrictRequestforPlaces(val districtId: String)
data class DistrictRequest(val id: String)

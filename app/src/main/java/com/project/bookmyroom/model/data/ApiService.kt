package com.project.bookmyroom.model.data


import retrofit2.Call
import retrofit2.http.*
import retrofit2.Response

interface ApiService {

    @POST("user/register")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("user/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>
}


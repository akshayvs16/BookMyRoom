package com.project.bookmyroom.model.data

import com.google.gson.annotations.SerializedName

data class newUser(
    @SerializedName("_id")
    val id: String,
    val email: String,
    val password: String,
    @SerializedName("firstName")
    val firstName: String,
    val phone: String,
    val userId: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)

data class User(
    val _id: String,
    val email: String,
    val firstName: String,
    val phone: String,
    val userId: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)

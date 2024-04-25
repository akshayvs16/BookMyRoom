package com.project.bookmyroom.model.data

import java.io.Serializable


data class Hotel(
    val _id: String,
    val name: String,
    val description: String,
    val address: String,
    val contactNumber: String,
    val price: String,
    val districtId: String,
    val placeId: String,
    val image: String,
    val id: String
) : Serializable

data class HotelResponse(
    val message: String,
    val data: List<Hotel>
)

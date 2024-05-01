package com.project.bookmyroom.model.data

import java.io.Serializable


data class Hotel(
    val engagedRooms: String?,
    val _id: String,
    val name: String,
    val description: String,
    val address: String,
    val contactNumber: String,
    val price: String,
    val districtId: String,
    val placeId: String,
    val image: String,
    val id: String,
    val availableRooms: String?,
    val totalRooms:String?
) : Serializable

data class HotelResponse(
    val message: String,
    val data: List<Hotel>
)

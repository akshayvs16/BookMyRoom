package com.project.bookmyroom.model.data

import java.io.Serializable

data class BookingDetailsData (
    val hotelId: String,
    val hotelName: String,
    val roomType: String,
    val checkInDate: String,
    val checkOutDate: String,
    val rooms: Int,
    val persons: Int
    ) : Serializable

package com.project.bookmyroom.model.data

data class BookingItem(
    val _id: String,
    val checkIn: String,
    val checkOut: String,
    val hotelId: String,
    val hotelName: String,
    val noOfPeople: String,
    val noOfRooms: String,
    val price: String,
    val type: String,
    val userId: String,
    val id: String,
    val createdAt: String,
    val updatedAt: String,
)


data class MyBookingUserRequest(val id: String)

data class BookedRoomsResponse(val message: String, val data: List<BookingItem>)


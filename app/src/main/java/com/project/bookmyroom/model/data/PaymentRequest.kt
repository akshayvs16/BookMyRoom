package com.project.bookmyroom.model.data

data class PaymentRequest(
    val checkIn: String,
    val checkOut: String,
    val noOfRooms: String,
    val noOfPeople: String,
    val price: String,
    val userId: String,
    val hotelId: String,
    val type: String
)

data class PaymentResponse(
    val message: String,
    val newRoom: NewRoom
)

data class NewRoom(
    val _id: String,
    val checkIn: String,
    val checkOut: String,
    val noOfRooms: String,
    val noOfPeople: String,
    val price: String,
    val userId: String,
    val hotelId: String,
    val type: String,
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)


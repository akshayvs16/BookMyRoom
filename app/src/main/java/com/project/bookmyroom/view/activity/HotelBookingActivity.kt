package com.project.bookmyroom.view.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.project.bookmyroom.R.*
import com.project.bookmyroom.viewmodel.RecentsData

class HotelBookingActivity : AppCompatActivity() {
    private lateinit var hotelData: RecentsData
    private lateinit var hotelImage: ImageView
    private lateinit var hotel_name: TextView
    private lateinit var textViewAddress: TextView
    private lateinit var textViewContact: TextView
    private lateinit var editTextCheckInDate: EditText
    private lateinit var editTextCheckOutDate: EditText
    private lateinit var editTextRooms: EditText
    private lateinit var editTextPersons: EditText
    private lateinit var buttonBookNow: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_hotel_booking)

        hotelData = intent.getSerializableExtra("PLACE_DATA") as RecentsData


        hotelImage = findViewById(id.hotel_image_place)
        hotel_name = findViewById(id.hotel_name)
        textViewAddress = findViewById(id.textViewAddress)
        textViewContact = findViewById(id.textViewContact)
        editTextCheckInDate = findViewById(id.editTextCheckInDate)
        editTextCheckOutDate = findViewById(id.editTextCheckOutDate)
        editTextRooms = findViewById(id.editTextRooms)
        editTextPersons = findViewById(id.editTextPersons)
        buttonBookNow = findViewById(id.buttonBookNow)

        // Set address and contact number
        populateData()

        buttonBookNow.setOnClickListener {
            // Handle the "Book Now" button click event
            val checkInDate = editTextCheckInDate.text.toString()
            val checkOutDate = editTextCheckOutDate.text.toString()
            val rooms = editTextRooms.text.toString().toInt()
            val persons = editTextPersons.text.toString().toInt()



            // Perform booking or other actions here
            // For example:
            // bookHotel(checkInDate, checkOutDate, rooms, persons)
        }
    }

    private fun populateData() {
        // Load and set place image
        // Example: placeImage.setImageResource(R.drawable.place_image)

        // Set description
        hotel_name.text= hotelData?.placeName
        Glide.with(this)
            .load(hotelData?.imageUrl)
            .into(hotelImage)
        textViewAddress.text=hotelData?.address
        textViewContact.text=hotelData?.contactNumber
        // Load and set hotels data
        // Example: hotelsList.addAll(getHotelsNearby())
        // hotelsAdapter.notifyDataSetChanged()
    }
}
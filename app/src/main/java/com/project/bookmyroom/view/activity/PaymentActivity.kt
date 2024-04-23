package com.project.bookmyroom.view.activity

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import com.project.bookmyroom.R
import com.project.bookmyroom.model.data.BookingDetailsData
import com.project.bookmyroom.preference.PreferenceManager

class PaymentActivity : AppCompatActivity() {
    private lateinit var preferenceManager: PreferenceManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        preferenceManager = PreferenceManager(this)

        val bookingDetailsCard = findViewById<MaterialCardView>(R.id.bookingDetailsCard)
        val textViewCheckInDate = findViewById<TextView>(R.id.textViewCheckInDate)
        val textViewCheckOutDate = findViewById<TextView>(R.id.textViewCheckOutDate)
        val textViewRooms = findViewById<TextView>(R.id.textViewRooms)
        val textViewPersons = findViewById<TextView>(R.id.textViewPersons)
        val textViewHotelName = findViewById<TextView>(R.id.textViewHotelName)
        val textViewTotalAmount = findViewById<TextView>(R.id.textViewTotalAmount)
        val username = findViewById<TextView>(R.id.textViewUserName)
        val email = findViewById<TextView>(R.id.textViewEmail)
        val phone = findViewById<TextView>(R.id.textViewPhone)


        username.text=preferenceManager.getUsername()
        email.text=preferenceManager.getEmail()
        phone.text="98958676582"

        // Retrieve booking details from intent
        val bookingDetails = intent.getSerializableExtra("BOOKING_DETAILS") as BookingDetailsData

        // Populate booking details
        textViewCheckInDate.text = "Check-In Date: ${bookingDetails.checkInDate}"
        textViewCheckOutDate.text = "Check-Out Date: ${bookingDetails.checkOutDate}"
        textViewRooms.text = "Rooms: ${bookingDetails.rooms}"
        textViewPersons.text = "Persons: ${bookingDetails.persons}"
        textViewHotelName.text = "Hotel Name: ${bookingDetails.hotelName}"

        // Calculate total amount (replace this with your calculation logic)
        val totalAmount = calculateTotalAmount(bookingDetails.rooms.toInt(), bookingDetails.persons.toInt())
        textViewTotalAmount.text = "Total Amount: $totalAmount"
    }

    private fun calculateTotalAmount(rooms: Int, persons: Int): Double {
        // Replace this with your actual calculation logic based on room rates, etc.
        return rooms * persons * 1000.0
    }
}
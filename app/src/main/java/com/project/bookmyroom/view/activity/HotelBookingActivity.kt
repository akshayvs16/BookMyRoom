package com.project.bookmyroom.view.activity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isGone
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.project.bookmyroom.R.*
import com.project.bookmyroom.model.data.BookingDetailsData
import com.project.bookmyroom.model.data.Hotel
import com.project.bookmyroom.viewmodel.RecentsData
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HotelBookingActivity : AppCompatActivity() {
    private lateinit var btnBack: ImageButton
    private lateinit var hotelData: Hotel
    private lateinit var hotelImage: ImageView
    private lateinit var hotel_name: TextView
    private lateinit var textViewAddress: TextView
    private lateinit var textViewContact: TextView
    private lateinit var textViewLocation: TextView
    private lateinit var engaged_rooms: TextView
    private lateinit var available_rooms: TextView
    private lateinit var total_rooms: TextView
    private lateinit var editTextCheckInDate: TextInputEditText
    private lateinit var editTextCheckOutDate: TextInputEditText
    private lateinit var editTextRooms: TextInputEditText
    private lateinit var editTextRoomType: AppCompatSpinner
    private lateinit var editTextPersons: TextInputEditText
    private lateinit var buttonBookNow: Button
    private lateinit var payment_enter: Button
    private lateinit var booking_layout: MaterialCardView
    private lateinit var progress_circular: CircularProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_hotel_booking)

        hotelData = intent.getSerializableExtra("PLACE_DATA") as Hotel

        booking_layout=findViewById(id.booking_layout)
        hotelImage = findViewById(id.hotel_image_place)
        btnBack = findViewById(id.btnBack)
        hotel_name = findViewById(id.hotel_name)
        textViewAddress = findViewById(id.textViewAddress)
        textViewContact = findViewById(id.textViewContact)
        textViewLocation=findViewById((id.textViewLocation))
        progress_circular=findViewById(id.progressLoading_circular)

        editTextCheckInDate = findViewById(id.editTextCheckInDate)
        editTextCheckOutDate = findViewById(id.editTextCheckOutDate)
        editTextRooms = findViewById(id.editTextRooms)
        editTextRoomType = findViewById(id.editTextRoomType)
        editTextPersons = findViewById(id.editTextPersons)
        buttonBookNow = findViewById(id.buttonBookNow)
        payment_enter = findViewById(id.payment_enter)
        total_rooms = findViewById(id.total_rooms)
        engaged_rooms = findViewById(id.engaged_rooms)
        available_rooms = findViewById(id.available_rooms)

        // Set address and contact number
        populateData()

        btnBack.setOnClickListener {
            finish() // Finish the current activity and go back to the previous one
        }

        buttonBookNow.setOnClickListener {
            // Handle the "Book Now" button click event
            if (hotelData.availableRooms=="0"){
                Toast.makeText(this, "Rooms not available here now", Toast.LENGTH_SHORT).show()

            }else{
                booking_layout.visibility = View.VISIBLE
                buttonBookNow.visibility = View.GONE
            }

            /*val checkInDate = editTextCheckInDate.text.toString()
            val checkOutDate = editTextCheckOutDate.text.toString()
            val rooms = editTextRooms.text.toString().toInt()
            val persons = editTextPersons.text.toString().toInt()*/
        }
        payment_enter.setOnClickListener {

            val checkInDate = editTextCheckInDate.text.toString()
            val checkOutDate = editTextCheckOutDate.text.toString()
            val roomsStr = editTextRooms.text.toString()
            val personsStr = editTextPersons.text.toString()
            val hotelName = hotel_name.text.toString()
            val roomType = editTextRoomType.selectedItem.toString()
            val hotelId = hotelData.id
            val hotelRoomPrice = hotelData.price.toInt()
            val totalDays=calculateTotalDays(checkInDate,checkOutDate).toInt()

            if (validateFields(checkInDate, checkOutDate, roomsStr, personsStr, hotelName, roomType,)) {
                val rooms = roomsStr.toInt()
                val persons = personsStr.toInt()
                val bookingDetails = BookingDetailsData(hotelId,hotelName,roomType,checkInDate, checkOutDate, rooms,persons, hotelRoomPrice,totalDays)

                val intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra("BOOKING_DETAILS", bookingDetails)
                startActivity(intent)
            }

        }


            editTextCheckInDate.setOnClickListener {
                progress_circular.show()
                showDatePickerDialog(editTextCheckInDate)
            }

            editTextCheckOutDate.setOnClickListener {
                progress_circular.show()

                showDatePickerDialog(editTextCheckOutDate)
            }

        textViewLocation.setOnClickListener {
            loadLocation(this,hotelData.name,hotelData.address)
        }
            // Perform booking or other actions here
            // For example:
            // bookHotel(checkInDate, checkOutDate, rooms, persons)

    }


    private fun validateFields(
        checkInDate: String,
        checkOutDate: String,
        roomsStr: String,
        personsStr: String,
        hotelName: String,
        roomType: String
    ): Boolean {
        if (checkInDate.isEmpty() || checkOutDate.isEmpty() || roomsStr.isEmpty() || personsStr.isEmpty() || hotelName.isEmpty() || roomType.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }
        // Additional validation checks if needed
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val checkIn = dateFormat.parse(checkInDate)
        val checkOut = dateFormat.parse(checkOutDate)

        if (checkOut.before(checkIn)) { // Check if check-out date is before check-in date
            Toast.makeText(this, "Check-out date must be after or equal to check-in date", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(Date(selectedYear - 1900, selectedMonth, selectedDay))
                editText.setText(formattedDate)
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
        progress_circular.visibility=View.GONE

    }

    private fun populateData() {
        // Load and set place image
        // Example: placeImage.setImageResource(R.drawable.place_image)

        // Set description
        hotel_name.text = hotelData.name
        Glide.with(this)
            .load(hotelData.image)
            .into(hotelImage)
        textViewAddress.text = hotelData.address
        textViewContact.text = hotelData.contactNumber

        total_rooms.text = "Total Rooms :${ hotelData.totalRooms }"
        available_rooms.text = "Available :${hotelData.availableRooms}"
        engaged_rooms.text ="Engaged :${ hotelData.engagedRooms}"
        // Load and set hotels data
        // Example: hotelsList.addAll(getHotelsNearby())
        // hotelsAdapter.notifyDataSetChanged()
    }

    private fun calculateTotalDays(checkInDate: String, checkOutDate: String): Long {
        if (checkInDate.isEmpty() || checkOutDate.isEmpty()) {
            return 0 // or handle the case where dates are empty in your logic
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try {
            val checkIn = dateFormat.parse(checkInDate)
            val checkOut = dateFormat.parse(checkOutDate)

            // Calculate the difference in milliseconds and convert to days
            val differenceInMillis = checkOut.time - checkIn.time
            return differenceInMillis / (1000 * 60 * 60 * 24)
        } catch (e: ParseException) {
            e.printStackTrace()
            return 0 // or handle the parsing error as needed
        }
    }

    fun loadLocation(ctx: Context, name: String?, address: String?) {
        try {
            val geoUri = "http://maps.google.com/maps?q=$name,$address"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))

            ctx.startActivity(intent)
        }
        catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}
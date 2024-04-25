package com.project.bookmyroom.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.project.bookmyroom.R
import com.project.bookmyroom.model.data.BookingDetailsData
import com.project.bookmyroom.model.data.PaymentRequest
import com.project.bookmyroom.model.data.PaymentResponse
import com.project.bookmyroom.model.data.User
import com.project.bookmyroom.network.RetrofitClient
import com.project.bookmyroom.preference.PreferenceManager
import com.project.bookmyroom.view.CommonDataArea.Companion.userEmail
import com.project.bookmyroom.view.CommonDataArea.Companion.userName
import com.project.bookmyroom.view.CommonDataArea.Companion.userPhone
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentActivity : AppCompatActivity() {
    private lateinit var totalAmount: String
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var btnBack: ImageButton
    private lateinit var payment_button: MaterialButton
    private lateinit var user: User




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
        user = preferenceManager.getUser()!!
        val bookingDetailsCard = findViewById<MaterialCardView>(R.id.bookingDetailsCard)
        val textViewCheckInDate = findViewById<TextView>(R.id.textViewCheckInDate)
        val textViewCheckOutDate = findViewById<TextView>(R.id.textViewCheckOutDate)
        val textViewRooms = findViewById<TextView>(R.id.textViewRooms)
        val textViewRoomsType = findViewById<TextView>(R.id.textViewRoomsType)
        val textViewPersons = findViewById<TextView>(R.id.textViewPersons)
        val textViewHotelName = findViewById<TextView>(R.id.textViewHotelName)
        val textViewTotalAmount = findViewById<TextView>(R.id.textViewTotalAmount)
        val username = findViewById<TextView>(R.id.textViewUserName)
        val email = findViewById<TextView>(R.id.textViewEmail)
        val phone = findViewById<TextView>(R.id.textViewPhone)
        btnBack = findViewById(R.id.btnBack)
        payment_button = findViewById(R.id.payment_button)


        username.text="Name:  ${user?.firstName}"
        email.text="Email:  ${ user?.email}"
        phone.text= "Contact Number:  ${ user?.phone}"

        // Retrieve booking details from intent
        val bookingDetails = intent.getSerializableExtra("BOOKING_DETAILS") as BookingDetailsData

        // Populate booking details
        textViewCheckInDate.text = "Check-In Date: ${bookingDetails.checkInDate}"
        textViewCheckOutDate.text = "Check-Out Date: ${bookingDetails.checkOutDate}"
        textViewRooms.text = "Rooms: ${bookingDetails.rooms}"
        textViewRoomsType.text = "Room Type: ${bookingDetails.roomType}"
        textViewPersons.text = "Persons: ${bookingDetails.persons}"
        textViewHotelName.text = "Hotel Name: ${bookingDetails.hotelName}"

        // Calculate total amount (replace this with your calculation logic)
        totalAmount = calculateTotalAmount(bookingDetails.rooms, bookingDetails.persons).toString()
        textViewTotalAmount.text = "Total Amount: $totalAmount"
        payment_button.text="Pay $totalAmount"

        btnBack.setOnClickListener {
           // finishActivity(1)
            finishAndRemoveTask() // Finish the current activity and go back to the previous one
        }

        payment_button.setOnClickListener {
            confirmPayment(bookingDetails)
        }

    }

    private fun calculateTotalAmount(rooms: Int, persons: Int): Double {
        // Replace this with your actual calculation logic based on room rates, etc.
        return rooms * persons * 1000.0
    }


    private fun confirmPayment(bookingDetails: BookingDetailsData) {
        showSuccessDialog()

        val paymentRequest = PaymentRequest(
            checkIn = bookingDetails.checkInDate,
            checkOut = bookingDetails.checkOutDate,
            noOfRooms = bookingDetails.rooms.toString(),
            noOfPeople = bookingDetails.persons.toString(),
            price = totalAmount,
            userId = user!!.userId, // Replace with actual user ID
            hotelId = bookingDetails.hotelId, // Replace with actual hotel ID
            type = bookingDetails.roomType
        )

        val apiService = RetrofitClient.instance
        apiService.saveUserPayment(paymentRequest).enqueue(object : Callback<PaymentResponse> {
            override fun onResponse(call: Call<PaymentResponse>, response: Response<PaymentResponse>) {
                if (response.isSuccessful) {
                    val paymentResponse = response.body()
                    if (paymentResponse != null) {
                        // Handle successful response
                       // Toast.makeText(this@PaymentActivity, paymentResponse.message, Toast.LENGTH_SHORT).show()
                        // Optionally, you can navigate to another activity or perform other actions here
                    } else {
                        showToast("Error Can't make Payment right now")

                    }
                } else {
                    // Handle unsuccessful response
                    showToast("Failed to save payment details")
                }
            }

            override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {
                // Handle API call failure
                showToast("Error: ${t.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this@PaymentActivity, message, Toast.LENGTH_SHORT).show()
    }
    private fun showSuccessDialog() {
        val dialogView = layoutInflater.inflate(R.layout.layout_dialog_success, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.show()

        val imageSuccess = dialogView.findViewById<ImageView>(R.id.imageSuccess)
        val progressLoading = dialogView.findViewById<ProgressBar>(R.id.progressLoading)
        val btnClose = dialogView.findViewById<Button>(R.id.btnClose)

        // Initially show progressLoading
        progressLoading.visibility = View.VISIBLE
        imageSuccess.visibility = View.GONE
        btnClose.visibility = View.GONE

        // Delayed switch to imageSuccess and btnClose after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            progressLoading.visibility = View.GONE
            imageSuccess.visibility = View.VISIBLE
            btnClose.visibility = View.VISIBLE
        }, 2000)

        btnClose.setOnClickListener {
            dialog.dismiss()
            goToMainActivity()         }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish() // Finish the current activity
    }
}



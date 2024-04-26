package com.project.bookmyroom.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.project.bookmyroom.databinding.ActivityPaymentBinding
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
import java.util.Calendar

class PaymentActivity : AppCompatActivity() {
    private lateinit var bookingDetails: BookingDetailsData
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var totalAmount: String
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var user: User




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        preferenceManager = PreferenceManager(this)
        user = preferenceManager.getUser()!!

        // Initialize views using view binding
        with(binding) {
             bookingDetails = intent.getSerializableExtra("BOOKING_DETAILS") as BookingDetailsData

            if (user != null) {
                // Populate user information
                textViewUserName.text = "Name: ${user?.firstName}"
                textViewEmail.text = "Email: ${user?.email}"
                textViewPhone.text = "Contact Number: ${user?.phone}"
            }else{
                Log.d("USerNull", "onCreate: User Null")
            }
            // Populate booking details
            textViewCheckInDate.text = "Check-In Date: ${bookingDetails.checkInDate}"
            textViewCheckOutDate.text = "Check-Out Date: ${bookingDetails.checkOutDate}"
            textViewRooms.text = "Rooms: ${bookingDetails.rooms}"
            textViewRoomsType.text = "Room Type: ${bookingDetails.roomType}"
            textViewPersons.text = "Persons: ${bookingDetails.persons}"
            textViewHotelName.text = "Hotel Name: ${bookingDetails.hotelName}"

            // Calculate and set total amount
            totalAmount = calculateTotalAmount(bookingDetails.rooms, bookingDetails.price,bookingDetails.totalDays).toString()
            textViewPrice.text = "Total Amount: ${totalAmount}"

            paymentButton.text = "Pay $totalAmount"
        }

        // Set click listeners using view binding
        binding.btnBack.setOnClickListener {
            finishAndRemoveTask()
        }
        binding.proceedToPayement.setOnClickListener {

            binding.bookingDetailsCard.visibility=View.GONE
            binding.userDetailsCard.visibility=View.GONE
            binding.infoCard.visibility=View.GONE

            binding.cardVisible.visibility=View.VISIBLE

            binding.relativeBooking.visibility=View.VISIBLE
            binding.etCardNumber.visibility=View.VISIBLE
        }

        binding.paymentButton.setOnClickListener {
            if (validateCardDetails()) {

                confirmPayment(bookingDetails)
            }
        }
    }


    private fun calculateTotalAmount(rooms: Int, hotelRoomPrice: Int,totalDays:Int): Int {
        // Replace this with your actual calculation logic based on room rates, etc.
        return rooms * hotelRoomPrice * totalDays
    }

    private fun validateCardDetails(): Boolean {
        val cardName= binding.etNameOnCard.text.toString().trim()
        val cardNumber = binding.etCardNumber.text.toString().trim()
        val expiry = binding.etExpiry.text.toString().trim()
        val cvv = binding.etCvv.text.toString().trim()

        if (cardName.isEmpty()) {
            showToast("Please Enter Card Name")
            return false
        }

        // Check if card number is empty or not in a valid format (e.g., 16 digits for Visa/MasterCard)
        if (cardNumber.isEmpty() || !isValidCardNumber(cardNumber)) {
            showToast("Please enter a valid 16 digit card number")
            return false
        }

        // Check if expiry date is empty or not in a valid format (e.g., MM/YY)
        if (expiry.isEmpty() || !isValidExpiryDate(expiry)) {
            showToast("Please enter a valid expiry date with Month and Year (MM/YY)")
            return false
        }

        // Check if CVV is empty or not in a valid format (e.g., 3 or 4 digits)
        if (cvv.isEmpty() || !isValidCVV(cvv)) {
            showToast("Please enter a valid 3 digit CVV")
            return false
        }

        // Additional validations if needed

        return true
    }

    private fun isValidCardNumber(cardNumber: String): Boolean {
        // Add your card number validation logic here
        // For example, check if the card number has 16 digits for Visa/MasterCard
        return cardNumber.length == 16
    }

    private fun isValidExpiryDate(expiry: String): Boolean {
        // Add your expiry date validation logic here
        // For example, check if the format is MM/YY and if MM is between 01 and 12
        val parts = expiry.split("/")
        if (parts.size != 2) return false
        val (month, year) = parts
        val currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100 // Get last two digits of the current year
        val validYear = year.toIntOrNull() ?: return false
        val validMonth = month.toIntOrNull() ?: return false
        return validYear >= currentYear && validMonth in 1..12
    }

    private fun isValidCVV(cvv: String): Boolean {
        // Add your CVV validation logic here
        // For example, check if CVV has 3 or 4 digits
        return cvv.length in 3..4 && cvv.toIntOrNull() != null
    }



    private fun confirmPayment(bookingDetails: BookingDetailsData) {
        showSuccessDialog()

        val paymentRequest = PaymentRequest(
            hotelName = bookingDetails.hotelName,
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
        val paymentSuccessfull = dialogView.findViewById<TextView>(R.id.paymentSuccessfull)
        val btnClose = dialogView.findViewById<Button>(R.id.btnClose)

        // Initially show progressLoading
        progressLoading.visibility = View.VISIBLE
        imageSuccess.visibility = View.GONE
        btnClose.visibility = View.GONE
        paymentSuccessfull.visibility = View.GONE

        // Delayed switch to imageSuccess and btnClose after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            progressLoading.visibility = View.GONE
            imageSuccess.visibility = View.VISIBLE
            paymentSuccessfull.visibility = View.VISIBLE
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



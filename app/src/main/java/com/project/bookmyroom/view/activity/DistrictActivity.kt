package com.project.bookmyroom.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.project.bookmyroom.R
import com.project.bookmyroom.databinding.ActivityDistrictBinding
import com.project.bookmyroom.view.components.interfaces.LocationChangeListener
import com.project.bookmyroom.view.fragments.HomeFragment

class DistrictActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDistrictBinding
    private var locationChangeListener: LocationChangeListener? = null

    private val allDistricts = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDistrictBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val cardViews = listOf(
            binding.cardTrivandrum,
            binding.cardKollam,
            binding.cardPathanamthitta,
            binding.cardAlappuzha,
            binding.cardKottayam,
            binding.cardIdukki,
            binding.cardErnakulam,
            binding.cardThrissur,
            binding.cardPalakkad,
            binding.cardMalappuram,
            binding.cardKozhikode,
            binding.cardWayanad,
            binding.cardKannur,
            binding.cardKasaragod
        )
        binding.currentLocation.text=MainActivity.defaultLocation
        cardViews.forEachIndexed { index, cardView ->
            cardView.setOnClickListener {
                val districtName = getDistrictName(index)
                updateLocation(districtName)
                Log.d("updateLocation", "districtName: $districtName")
            }
        }
        binding.btnBack.setOnClickListener {
            finish() // Finish the current activity and go back to the previous one
        }

    }


    private fun updateLocation(newLocation: String) {
        binding.currentLocation.text = newLocation
        MainActivity.defaultLocation = newLocation
        locationChangeListener?.onLocationChanged(newLocation)
        goToMainActivity()

    }

        private fun getDistrictName(index: Int): String {
            return when (index) {
                0 -> "Thiruvananthapuram"
                1 -> "Kollam"
                2 -> "Pathanamthitta"
                3 -> "Alappuzha"
                4 -> "Kottayam"
                5 -> "Idukki"
                6 -> "Ernakulam"
                7 -> "Thrissur"
                8 -> "Palakkad"
                9 -> "Malappuram"
                10 -> "Kozhikode"
                11 -> "Wayanad"
                12 -> "Kannur"
                13 -> "Kasaragod"
                else -> "Unknown"
            }
        }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish() // Finish the current activity
    }

    }

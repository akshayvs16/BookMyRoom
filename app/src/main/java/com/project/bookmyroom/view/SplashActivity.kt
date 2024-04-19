package com.project.bookmyroom.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.project.bookmyroom.R
import com.project.bookmyroom.preference.PreferenceManager

class SplashActivity : AppCompatActivity() {
    private lateinit var preferenceManager: PreferenceManager // D

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        preferenceManager = PreferenceManager(this)
        supportActionBar?.hide()

        // Example of navigating further after a delay (2 seconds)
        Thread {
            Thread.sleep(2000)
            navigateToNextScreen()
        }.start()
    }

    private fun navigateToNextScreen() {
        if (preferenceManager.areCredentialsSaved()) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, CarouselScreenActivity::class.java))
        }
        finish()
    }
}
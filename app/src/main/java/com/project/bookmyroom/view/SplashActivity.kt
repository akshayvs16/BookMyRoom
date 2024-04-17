package com.project.bookmyroom.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.project.bookmyroom.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        // Example of navigating further after a delay (2 seconds)
        Thread {
            Thread.sleep(2000)
            startActivity(Intent(this, CarouselScreenActivity::class.java))
            finish()
        }.start()
    }
}
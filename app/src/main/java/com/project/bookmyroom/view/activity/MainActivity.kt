package com.project.bookmyroom.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.bookmyroom.R
import com.project.bookmyroom.view.components.ExitDialog
import com.project.bookmyroom.view.fragments.HomeFragment
import com.project.bookmyroom.view.fragments.MyBookingFragment
import com.project.bookmyroom.view.fragments.ProfileFragment
import com.project.bookmyroom.view.fragments.SearchFragment

class MainActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    private lateinit var exitDialog: ExitDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        exitDialog = ExitDialog(this)
        //replaceFragment(R.id.fragmentContainer,HomeFragment())


        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(HomeFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_search -> {
                    loadFragment(SearchFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_booking -> {
                    loadFragment(MyBookingFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    loadFragment(ProfileFragment())
                    return@setOnItemSelectedListener true
                }
                else -> return@setOnItemSelectedListener false
            }
        }

        // Load the default fragment
        loadFragment(HomeFragment())
    }


    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
    override fun onBackPressed() {
        exitDialog.showDoubleBackPressExitToast(this)
    }
}
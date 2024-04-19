package com.project.bookmyroom.view.components

import android.R
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast

import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

class ExitDialog(private val context: Context) {
    private var alertDialog: AlertDialog? = null
    private val DOUBLE_PRESS_TIME_THRESHOLD: Long = 2000
    private var lastBackPressTime: Long = 0
    private var doubleBackToExitPressedOnce = false


    fun showExitDialog(onExitConfirmed: () -> Unit) {
        alertDialog = AlertDialog.Builder(context)
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ ->
                onExitConfirmed.invoke()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
                alertDialog = null
            }
            .show()
    }

    fun showDoubleBackPressExitToast(activity: Activity) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressTime < DOUBLE_PRESS_TIME_THRESHOLD) {
            activity.finish()
        } else {
            doubleBackToExitPressedOnce = true
            lastBackPressTime = currentTime
            Toast.makeText(activity, "Press back again to exit", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({
                doubleBackToExitPressedOnce = false
            }, DOUBLE_PRESS_TIME_THRESHOLD)
        }
    }

    fun dismissDialog() {
        alertDialog?.dismiss()
        alertDialog = null
    }
}

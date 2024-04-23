package com.project.bookmyroom.view.components
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.project.bookmyroom.R

class ProgressDialogHandler(private val context: Context) {
    private var progressDialog: AlertDialog? = null

    fun showProgressDialog(message: String) {
        dismissProgressDialog() // Dismiss any existing dialog

        val dialogView = LayoutInflater.from(context).inflate(R.layout.custom_loading_dialog, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(true)

        progressDialog = builder.create()
        progressDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        progressDialog?.show()
    }

    fun dismissProgressDialog() {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
        progressDialog = null
    }
}

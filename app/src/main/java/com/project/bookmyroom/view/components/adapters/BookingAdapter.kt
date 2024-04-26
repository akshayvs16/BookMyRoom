package com.project.bookmyroom.view.components.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.bookmyroom.R
import com.project.bookmyroom.viewmodel.BookingItem

class BookingAdapter(private val bookingList: List<BookingItem>) :
    RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // UI elements in the item layout
        private val hotelNameTextView = itemView.findViewById<TextView>(R.id.fragment_booking_time_tv)
        private val hotelImageView = itemView.findViewById<ImageView>(R.id.fragment_booking_hotel_iv)
        private val dateTextView = itemView.findViewById<TextView>(R.id.fragment_booking_hotel_tv)
        private val locationImageView = itemView.findViewById<ImageView>(R.id.fragment_booking_location_iv)
        private val locationTextView = itemView.findViewById<TextView>(R.id.fragment_booking_location_tv)
        private val priceTextView = itemView.findViewById<TextView>(R.id.fragment_booking_price)
      //  private val bookButton = itemView.findViewById<Button>(R.id.fragment_booking_book_btn)

        fun bind(bookingItem: BookingItem) {
            // Bind data to UI elements
            hotelNameTextView.text = "Hotel Name : ${bookingItem.hotelName}"
            dateTextView.text = ("Check In : ${bookingItem.checkIn}")
            locationTextView.text =("Check Date : ${bookingItem.checkOut}")
            priceTextView.text =("Price : ${bookingItem.price.toString()}")

            // Set onClickListener for booking button if needed
         /*   bookButton.setOnClickListener {
                // Handle booking button click action
            }*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_booking_items, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val currentItem = bookingList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }
}

package com.project.bookmyroom.view.components.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.bookmyroom.R
import com.project.bookmyroom.viewmodel.RecentsData

class PopularHotelsAdapter(private var hotelList: List<RecentsData>) :
    RecyclerView.Adapter<PopularHotelsAdapter.HotelViewHolder>() {

    inner class HotelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Bind UI elements here
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.near_hotels_row_item, parent, false)
        return HotelViewHolder(view)
    }

    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {
        val currentItem = hotelList[position]
        // Bind data to UI elements in the ViewHolder
    }

    override fun getItemCount(): Int {
        return hotelList.size
    }

    fun submitList(list: List<RecentsData>) {
        hotelList = list
        notifyDataSetChanged()
    }
}

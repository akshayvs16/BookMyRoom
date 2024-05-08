package com.project.bookmyroom.view.components.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.bookmyroom.R
import com.project.bookmyroom.model.data.Hotel
import com.project.bookmyroom.model.data.getDistrictNameById
import com.project.bookmyroom.view.activity.HotelBookingActivity
import com.project.bookmyroom.view.components.adapters.RecentsAdapter.RecentsViewHolder

class RecentsAdapter(var context: Context, var recentsDataList: MutableList<Hotel>) :
    RecyclerView.Adapter<RecentsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentsViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.recents_hotels_row_item, parent, false)
        return RecentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecentsViewHolder, position: Int) {
        val currentItem = recentsDataList[position]


        holder.placeName.text = currentItem.name
        holder.countryName.text = getDistrictNameById(currentItem.districtId.toInt())
        holder.price.text = "From ₹ " + currentItem.price

        // Load image using Glide
        Glide.with(context)
            .load(currentItem.image) // Assuming imageUrl is a valid URL string
            /* .placeholder(R.drawable.placeholder_image) // Placeholder image while loading
                .error(R.drawable.error_image) // Error image if loading fails*/
            .into(holder.placeImage)

        holder.itemView.setOnClickListener {
            val i = Intent(context, HotelBookingActivity::class.java)
            i.putExtra("PLACE_DATA", currentItem)
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return recentsDataList.size
    }

    fun setData(newData: List<Hotel>?) {
        recentsDataList.clear()
        recentsDataList.addAll(newData!!)
        notifyDataSetChanged()
    }

    class RecentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var placeImage: ImageView = itemView.findViewById(R.id.place_image2)
        var placeName: TextView = itemView.findViewById(R.id.place_name)
        var countryName: TextView = itemView.findViewById(R.id.country_name)
        var price: TextView = itemView.findViewById(R.id.price)
    }
}

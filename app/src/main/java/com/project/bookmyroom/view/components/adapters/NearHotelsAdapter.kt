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
import com.project.bookmyroom.model.data.getDistrictNameById
import com.project.bookmyroom.view.activity.DetailsActivity
import com.project.bookmyroom.viewmodel.NearPlacesData

class NearHotelsAdapter(
    private val context: Context,
    private val dataList: List<NearPlacesData>
) : RecyclerView.Adapter<NearHotelsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.near_hotels_row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val placeNameTextView: TextView = itemView.findViewById(R.id.place_name)
        private val countryNameTextView: TextView = itemView.findViewById(R.id.country_name)
        private val priceTextView: TextView = itemView.findViewById(R.id.price)
        private val imageView: ImageView = itemView.findViewById(R.id.place_image)
      //  private val descriptionTextView: TextView = itemView.findViewById(R.id.description_text)

        fun bind(item: NearPlacesData) {
            placeNameTextView.text = item.name
            countryNameTextView.text = getDistrictNameById(item.districtId.toInt())
            priceTextView.text = item.description
         //   descriptionTextView.text = item.description

            // Load image using Glide with placeholder and error handling
            Glide.with(context)
                .load(item.image)
                .placeholder(R.drawable.dot_active)
                .error(R.drawable.ic_info_square_border)
                .into(imageView)

            itemView.setOnClickListener(View.OnClickListener {
                val i = Intent(context, DetailsActivity::class.java)
                i.putExtra("PLACE_DATA", item)
                context.startActivity(i)
            })
        }
    }


}

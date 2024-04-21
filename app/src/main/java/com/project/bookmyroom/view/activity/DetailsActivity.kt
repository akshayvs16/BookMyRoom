package com.project.bookmyroom.view.activity

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.bookmyroom.R
import com.project.bookmyroom.view.components.adapters.RecentsAdapter
import com.project.bookmyroom.viewmodel.RecentsData
import com.project.bookmyroom.viewmodel.TopPlacesData
import java.io.IOException

class DetailsActivity : AppCompatActivity() {

    private lateinit var placeData: TopPlacesData
    private lateinit var placeHeading: TextView
    private lateinit var placeImage: ImageView
    private lateinit var descriptionText: TextView
    private lateinit var hotelsRecyclerView: RecyclerView
    private lateinit var hotelsAdapter: RecentsAdapter
    private val hotelsList: MutableList<RecentsData> = mutableListOf()
    private lateinit var trendingData: List<RecentsData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        trendingData = getTrendingData()

         placeData = intent.getSerializableExtra("PLACE_DATA") as TopPlacesData
        Log.d("DetailsActivity", "onCreate: ${placeData?.placeName}")

        placeImage = findViewById(R.id.place_image)
        placeHeading = findViewById(R.id.place_heading)
        descriptionText = findViewById(R.id.description_text)
        hotelsRecyclerView = findViewById(R.id.hotels_recycler)
        trendingData = getTrendingData()



        // Set up RecyclerView
        hotelsAdapter  = RecentsAdapter(this, hotelsList)
        hotelsRecyclerView.layoutManager = LinearLayoutManager(this)
        hotelsRecyclerView.adapter = hotelsAdapter
        setRecentRecycler(trendingData)

        // Populate data
        populateData()
    }


    private fun setRecentRecycler(dataList: List<RecentsData>) {
        hotelsList.clear()
        hotelsList.addAll(dataList)

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        hotelsRecyclerView.layoutManager = layoutManager
        hotelsAdapter = RecentsAdapter(this, hotelsList)
        hotelsRecyclerView.adapter = hotelsAdapter
        hotelsRecyclerView.invalidate()
    }

    private fun populateData() {
        // Load and set place image
        // Example: placeImage.setImageResource(R.drawable.place_image)

        // Set description
        placeHeading.text= placeData?.placeName
        Glide.with(this)
            .load(placeData?.imageUrl)
            .into(placeImage)
        descriptionText.text=placeData?.description
        // Load and set hotels data
        // Example: hotelsList.addAll(getHotelsNearby())
        // hotelsAdapter.notifyDataSetChanged()
    }

    private fun getHotelsNearby(): List<TopPlacesData> {
        // Implement logic to load hotels near the place
        return listOf() // Placeholder return value
    }
    private fun getTrendingData(): List<RecentsData> {
        // Load and parse trending_hotels.json from assets folder
        return loadJsonData("trendingHotel.json")
    }
    private fun loadJsonData(fileName: String): List<RecentsData> {
        val jsonString = try {
            val inputStream = this.assets.open(fileName)
            inputStream.bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            Log.e("HomeFragment", "Error reading JSON file: ${e.message}")
            e.printStackTrace()
            ""
        }

        val type = object : TypeToken<List<RecentsData>>() {}.type
        return Gson().fromJson(jsonString, type) ?: emptyList()
    }


}

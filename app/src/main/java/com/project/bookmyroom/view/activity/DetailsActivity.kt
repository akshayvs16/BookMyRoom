package com.project.bookmyroom.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.bookmyroom.R
import com.project.bookmyroom.model.data.DistrictRequest
import com.project.bookmyroom.model.data.Hotel
import com.project.bookmyroom.model.data.HotelResponse
import com.project.bookmyroom.network.RetrofitClient
import com.project.bookmyroom.view.components.adapters.RecentsAdapter
import com.project.bookmyroom.viewmodel.NearPlacesData
import com.project.bookmyroom.viewmodel.RecentsData
import com.project.bookmyroom.viewmodel.TopPlacesData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class DetailsActivity : AppCompatActivity() {

    private lateinit var progress_circular: CircularProgressIndicator
    private lateinit var btnBack: ImageButton
    private lateinit var nearstBusStop: TextView
    private lateinit var nearstRailway: TextView
    private lateinit var placeData: NearPlacesData
    private lateinit var placeHeading: TextView
    private lateinit var placeImage: ImageView
    private lateinit var place_location: MaterialCardView
    private lateinit var descriptionText: TextView
    private lateinit var hotelsRecyclerView: RecyclerView
    private lateinit var hotelsAdapter: RecentsAdapter
    private val hotelsList: MutableList<Hotel> = mutableListOf()
    private lateinit var trendingData: List<RecentsData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        trendingData = getTrendingData()

         placeData = intent.getSerializableExtra("PLACE_DATA") as NearPlacesData
        Log.d("DetailsActivity", "onCreate: ${placeData?.placeName}")
        btnBack = findViewById(R.id.btnBack)

        progress_circular=findViewById(R.id.progress_circular_View)
        progress_circular.visibility=View.GONE
        placeImage = findViewById(R.id.place_image)
        placeHeading = findViewById(R.id.place_heading)
        descriptionText = findViewById(R.id.description_text)
        hotelsRecyclerView = findViewById(R.id.hotels_recycler)
        nearstBusStop = findViewById(R.id.nearst_busStop)
        nearstRailway = findViewById(R.id.nearst_railway)
        place_location = findViewById(R.id.place_location)
        trendingData = getTrendingData()



        // Set up RecyclerView
        hotelsAdapter  = RecentsAdapter(this, hotelsList)
        hotelsRecyclerView.layoutManager = LinearLayoutManager(this)
        hotelsRecyclerView.adapter = hotelsAdapter
        // Populate data
        populateData()
        btnBack.setOnClickListener {
            finish() // Finish the current activity and go back to the previous one
        }

        place_location.setOnClickListener {
            loadLocation(this, placeData?.name, placeData?.address)
        }

    }


    private fun setRecentRecycler(dataList: List<Hotel>) {
        hotelsList.clear()
        hotelsList.addAll(dataList)

        val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        hotelsRecyclerView.layoutManager = layoutManager
        hotelsAdapter = RecentsAdapter(this, hotelsList)
        hotelsRecyclerView.adapter = hotelsAdapter
        hotelsRecyclerView.invalidate()
        progress_circular.visibility= View.GONE

    }

    private fun populateData() {
        // Load and set place image
        // Example: placeImage.setImageResource(R.drawable.place_image)

        // Set description
        placeHeading.text= placeData?.name
        Glide.with(this)
            .load(placeData?.image)
            .into(placeImage)
        descriptionText.text=placeData?.description
        nearstRailway.text=placeData?.nearByRailStops
        nearstBusStop.text=placeData?.nearByBusStops
        val placeId=placeData.id
        fetchHotelsByDistrict(placeId)

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

    private fun fetchHotelsByDistrict(districtId: String) {
        progress_circular.visibility= View.VISIBLE
        progress_circular.progress

        val apiService = RetrofitClient.instance
        val districtRequest = DistrictRequest(districtId)

        apiService.getHotelsByPlaces(districtRequest).enqueue(object : Callback<HotelResponse> {
            override fun onResponse(call: Call<HotelResponse>, response: Response<HotelResponse>) {
                if (response.isSuccessful) {
                    val hotelResponse = response.body()
                    hotelResponse?.let {
                        setRecentRecycler(parseHotelApiResponse(it))
                    }
                } else {
                    Log.e("HomeFragment", "API call failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<HotelResponse>, t: Throwable) {
                Log.e("HomeFragment", "Error fetching data: ${t.message}")
            }
        })
    }

    private fun parseHotelApiResponse(response: HotelResponse): List<Hotel> {
        val dataList = mutableListOf<Hotel>()
        val apiData = response.data
        for (item in apiData) {
            val nearPlace = Hotel(
                item.engagedRooms,
                item._id,
                item.name,
                item.description,
                item.address,
                item.contactNumber,
                item.price,
                item.districtId,
                item.placeId,
                item.image,
                item.id,
                item.availableRooms,
                item.totalRooms

            )
            dataList.add(nearPlace)
        }
        return dataList
    }

    fun loadLocation(ctx: Context, name: String?, address: String?) {
        try {
            val geoUri = "http://maps.google.com/maps?q=$name,$address"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))

            ctx.startActivity(intent)
        }
        catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}

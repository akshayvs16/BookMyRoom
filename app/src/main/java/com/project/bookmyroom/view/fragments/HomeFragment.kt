package com.project.bookmyroom.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.bookmyroom.R
import com.project.bookmyroom.model.data.DistrictRequest
import com.project.bookmyroom.model.data.DistrictRequestforPlaces
import com.project.bookmyroom.model.data.Hotel
import com.project.bookmyroom.model.data.HotelResponse
import com.project.bookmyroom.model.data.PlacesResponse
import com.project.bookmyroom.network.RetrofitClient
import com.project.bookmyroom.view.activity.MainActivity
import com.project.bookmyroom.view.components.adapters.NearPlacesAdapter
import com.project.bookmyroom.view.components.adapters.RecentsAdapter
import com.project.bookmyroom.view.replaceFragment
import com.project.bookmyroom.viewmodel.NearPlacesData
import com.project.bookmyroom.viewmodel.RecentsData
import com.project.bookmyroom.viewmodel.TopPlacesData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var recentRecycler: RecyclerView
    private lateinit var progress_circular: CircularProgressIndicator
    private lateinit var progress_circular2: CircularProgressIndicator
    private lateinit var nearPlaceRecycler: RecyclerView
    private var recentAdapter: RecentsAdapter? = null
    private val recentDataList: MutableList<Hotel> = ArrayList()
    private val nearHotelsDataList: MutableList<NearPlacesData> = ArrayList()
    private var nearPlacesAdapter: NearPlacesAdapter? = null



    private lateinit var recommendedData: List<RecentsData>
    private lateinit var popularData: List<RecentsData>
    private lateinit var trendingData: List<RecentsData>

    private lateinit var currentLocation:TextView
    private lateinit var currentLocation_layout: LinearLayout



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        recommendedData = getRecommendedData()
        popularData = getPopularData()
        trendingData = getTrendingData()

        progress_circular=view.findViewById(R.id.progress_circular_View)
        progress_circular2=view.findViewById(R.id.progress_circular_View_1)

        progress_circular.visibility=View.GONE
        progress_circular2.visibility=View.GONE

        // Initialize RecyclerView
        recentRecycler = view.findViewById(R.id.recent_recycler)
        nearPlaceRecycler = view.findViewById(R.id.near_places_recycler)
        currentLocation = view.findViewById(R.id.currentLocation)
        currentLocation_layout = view.findViewById(R.id.currentLocation_layout)

        currentLocation.text = MainActivity.defaultLocation

       // loadDataBasedOnLocation(currentLocation.toString())

        val districtId = getDistrictId(currentLocation.text.toString())

        fetchPlacesByDistrict(districtId)
        fetchHotelsByDistrict(districtId)

        //setRecentRecycler(trendingData) // Default: Recommended data
        //setNearPlacesRecycler(getNearPlacesData()) // Default: Recommended data


        
        currentLocation_layout.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(R.id.fragmentContainer, SearchFragment())
        }
        // Setup category chips click listeners

        return view
    }
    private fun getDistrictId(districtName: String): String {
        val districts = listOf(
            Pair("Thiruvananthapuram", 0),
            Pair("Kollam", 1),
            Pair("Pathanamthitta", 2),
            Pair("Alappuzha", 3),
            Pair("Kottayam", 4),
            Pair("Idukki", 5),
            Pair("Ernakulam", 6),
            Pair("Thrissur", 7),
            Pair("Palakkad", 8),
            Pair("Malappuram", 9),
            Pair("Kozhikode", 10),
            Pair("Wayanad", 11),
            Pair("Kannur", 12),
            Pair("Kasaragod", 13)
        )
        return (districts.firstOrNull { it.first == districtName }?.second ?: -1).toString()
    }

    private fun fetchPlacesByDistrict(districtId: String) {
        progress_circular.visibility=View.VISIBLE
        progress_circular.progress

        val apiService = RetrofitClient.instance
        val districtRequest = DistrictRequestforPlaces(districtId)

        apiService.getPlacesByDistrict(districtRequest).enqueue(object : Callback<PlacesResponse> {
            override fun onResponse(call: Call<PlacesResponse>, response: Response<PlacesResponse>) {
                if (response.isSuccessful) {
                    val hotelResponse = response.body()
                    hotelResponse?.let {
                        setNearPlacesRecycler(parseApiResponse(it))
                    }
                } else {
                    Log.e("HomeFragment", "API call failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PlacesResponse>, t: Throwable) {
                Log.e("HomeFragment", "Error fetching data: ${t.message}")
            }
        })
    }

    private fun fetchHotelsByDistrict(districtId: String) {
        progress_circular2.visibility=View.VISIBLE

        val apiService = RetrofitClient.instance
        val districtRequest = DistrictRequest(districtId)

        apiService.getHotelsByDistrict(districtRequest).enqueue(object : Callback<HotelResponse> {
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

   /* override fun onStart() {
        super.onStart()
        fetchPlacesByDistrict(districtId = getDistrictId(currentLocation.text.toString()))
        fetchHotelsByDistrict(districtId = getDistrictId(currentLocation.text.toString()))

    }*/

    private fun parseApiResponse(response: PlacesResponse): List<NearPlacesData> {
        val dataList = mutableListOf<NearPlacesData>()
        val apiData = response.data
        for (item in apiData) {
            val nearPlace = NearPlacesData(
                item.name,
                item.address,
                item.description,
                item.districtId,
                item.nearByBusStops,
                item.nearByRailStops,
                item.lat,
                item.image,
                item.id

            )
            dataList.add(nearPlace)
        }
        return dataList
    }

    private fun parseHotelApiResponse(response: HotelResponse): List<Hotel> {
        val dataList = mutableListOf<Hotel>()
        val apiData = response.data
        for (item in apiData) {
            val nearPlace = Hotel(
                item._id,
                item.name,
                item.description,
                item.address,
                item.contactNumber,
                item.price,
                item.districtId,
                item.placeId,
                item.image,
                item.id

            )
            dataList.add(nearPlace)
            Log.d("HOME", "parseHotelApiResponse: ${nearPlace.id}")

        }
        return dataList
    }
    private fun setRecentRecycler(dataList: List<Hotel>) {
        if (!isAdded || context == null) {
            return
        }
        recentDataList.clear()
        recentDataList.addAll(dataList)

        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recentRecycler.layoutManager = layoutManager
        recentAdapter = RecentsAdapter(requireContext(), recentDataList)
        recentRecycler.adapter = recentAdapter
        recentRecycler.invalidate()
        progress_circular2.visibility=View.GONE

    }

    private fun setNearPlacesRecycler(dataList: List<NearPlacesData>) {
        if (!isAdded || context == null) {
            return
        }
        nearHotelsDataList.clear()
        nearHotelsDataList.addAll(dataList)

        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        nearPlaceRecycler.layoutManager = layoutManager
        nearPlacesAdapter = NearPlacesAdapter(requireContext(), nearHotelsDataList)
        nearPlaceRecycler.adapter = nearPlacesAdapter
        nearPlaceRecycler.invalidate()
        progress_circular.visibility=View.GONE

    }

    private fun getRecommendedData(): List<RecentsData> {
        // Load and parse recommended_hotels.json from assets folder
        return loadJsonData("hotel.json")
    }

    private fun getPopularData(): List<RecentsData> {
        // Load and parse popular_hotels.json from assets folder
        return loadJsonData("popularHotel.json")
    }

    private fun getTrendingData(): List<RecentsData> {
        // Load and parse trending_hotels.json from assets folder
        return loadJsonData("trendingHotel.json")
    }

    private fun getNearPlacesData(): List<TopPlacesData> {
        // Load and parse trending_hotels.json from assets folder
        return loadNearByPlacesData("nearByPlaces.json")
    }

    private fun loadJsonData(fileName: String): List<RecentsData> {
        val jsonString = try {
            val inputStream = requireContext().assets.open(fileName)
            inputStream.bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            Log.e("HomeFragment", "Error reading JSON file: ${e.message}")
            e.printStackTrace()
            ""
        }

        val type = object : TypeToken<List<RecentsData>>() {}.type
        return Gson().fromJson(jsonString, type) ?: emptyList()
    }

    private fun loadNearByPlacesData(fileName: String): List<TopPlacesData> {
        val jsonString = try {
            val inputStream = requireContext().assets.open(fileName)
            inputStream.bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            Log.e("HomeFragment", "Error reading JSON file: ${e.message}")
            e.printStackTrace()
            ""
        }

        val type = object : TypeToken<List<TopPlacesData>>() {}.type
        return Gson().fromJson(jsonString, type) ?: emptyList()

    }
/*
    private fun loadDataBasedOnLocation(location: String) {
        // Check the location and load data accordingly
        when (location) {
            "Trivandrum" -> {
                // Load recommended data by default
                setRecentRecycler(getRecommendedData())
            }
            else -> {
                // Load popular data for other locations
                setRecentRecycler(getPopularData())
            }
        }
    }*/

}

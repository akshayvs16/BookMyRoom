package com.project.bookmyroom.view.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.bookmyroom.R
import com.project.bookmyroom.model.data.ApiService
import com.project.bookmyroom.model.data.PlacesResponse
import com.project.bookmyroom.model.data.SearchRequest
import com.project.bookmyroom.network.RetrofitClient
import com.project.bookmyroom.view.activity.MainActivity
import com.project.bookmyroom.view.components.adapters.NearPlacesAdapter
import com.project.bookmyroom.view.components.adapters.PopularHotelsAdapter
import com.project.bookmyroom.view.components.interfaces.LocationChangeListener
import com.project.bookmyroom.viewmodel.NearPlacesData
import com.project.bookmyroom.viewmodel.RecentsData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class SearchFragment : Fragment(), TextWatcher {

    private var locationChangeListener: LocationChangeListener? = null
    private lateinit var editTextSearch: AutoCompleteTextView
    private lateinit var buttonSearch: Button
    private val allDistricts = mutableListOf<String>()
    private lateinit var currentLocation: TextView

    private val nearHotelsDataList: MutableList<NearPlacesData> = ArrayList()
    private lateinit var nearPlaceRecycler: RecyclerView
    private var nearPlacesAdapter: NearPlacesAdapter? = null

    private lateinit var progress_circular: CircularProgressIndicator

    private lateinit var NearData_notFound: MaterialTextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        progress_circular=view.findViewById(R.id.progress_circular_View)
        NearData_notFound=view.findViewById(R.id.placeData_notFound)



        nearPlaceRecycler = view.findViewById(R.id.recycler_view_popular_hotels)

        editTextSearch = view.findViewById(R.id.edit_text_search)
        buttonSearch = view.findViewById(R.id.button_search)
        currentLocation=view.findViewById(R.id.currentLocation)
        //loadDistricts()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, allDistricts)
        editTextSearch.setAdapter(adapter)
        currentLocation.setText(MainActivity.defaultLocation)

        editTextSearch.addTextChangedListener(this)
        progress_circular.visibility=View.GONE

      /*  view.findViewById<MaterialCardView>(R.id.card_trivandrum)?.setOnClickListener {
            updateLocation("Thiruvananthapuram")
        }

        // Set similar click listeners for Ernakulam and Kozhikode cards
        view.findViewById<MaterialCardView>(R.id.card_ernakulam)?.setOnClickListener {
            updateLocation("Ernakulam")
        }

        view.findViewById<MaterialCardView>(R.id.card_kozhikode)?.setOnClickListener {
            updateLocation("Kozhikode")
        }*/
        // Setup click listener for the Search button
        buttonSearch.setOnClickListener {
            performSearch(editTextSearch.text.toString())
            //updateLocation(editTextSearch.text.toString())

        }

        // Setup popular hotels RecyclerView

        // Initialize adapter but don't set it to RecyclerView yet

        return view
    }


    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // No action needed here, but required by TextWatcher interface
        nearPlaceRecycler.visibility=View.VISIBLE

        progress_circular.visibility=View.VISIBLE


    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // No action needed here, but required by TextWatcher interface
    }

    override fun afterTextChanged(s: Editable?) {
        val query = s.toString().trim()
        if (query.isNotEmpty()) {
            performSearch(query)
        }else{
            nearHotelsDataList.clear()
            nearPlaceRecycler.visibility=View.GONE
            NearData_notFound.visibility=View.VISIBLE
            progress_circular.visibility=View.GONE

        }
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LocationChangeListener) {
            locationChangeListener = context
        } else {
            throw RuntimeException("$context must implement LocationChangeListener")
        }
    }


    private fun updateLocation(newLocation: String) {
        currentLocation.text = newLocation
        locationChangeListener?.onLocationChanged(newLocation)

    }


    private fun performSearch(query: String) {
        progress_circular.visibility = View.VISIBLE
        if (query.isNotEmpty()) {
            val apiService = RetrofitClient.instance
            val request = SearchRequest(query)

            apiService.searchPlaces(request).enqueue(object : Callback<PlacesResponse> {
                override fun onResponse(
                    call: Call<PlacesResponse>,
                    response: Response<PlacesResponse>
                ) {
                    if (response.isSuccessful) {
                        val placesResponse = response.body()
                        placesResponse?.let {
                            setNearPlacesRecycler(parseApiResponse(it))
                        }
                    } else {
                        Log.e("SearchFragment", "API call failed: ${response.code()}")
                        progress_circular.visibility=View.GONE
                        NearData_notFound.visibility=View.VISIBLE
                        nearPlaceRecycler.visibility=View.GONE
                    }
                }

                override fun onFailure(call: Call<PlacesResponse>, t: Throwable) {
                    Log.e("SearchFragment", "Error fetching data: ${t.message}")
                    progress_circular.visibility=View.GONE
                    NearData_notFound.visibility=View.VISIBLE
                    nearPlaceRecycler.visibility=View.GONE

                }
            })
        }else {
            progress_circular.visibility=View.GONE
            NearData_notFound.visibility=View.VISIBLE}
    }

    private fun parseApiResponse(response: PlacesResponse): List<NearPlacesData> {
        val dataList = mutableListOf<NearPlacesData>()
        val apiData = response.data
        if (apiData.isEmpty()){
            NearData_notFound.visibility=View.VISIBLE

        }else {
            NearData_notFound.visibility = View.GONE

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
        }
        return dataList
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


    private fun loadDistricts() {
        // Load districts data from your source and populate allDistricts list
        // For demonstration, assume allDistricts is already populated
        allDistricts.addAll(listOf(
            "Thiruvananthapuram",
            "Kollam",
            "Pathanamthitta",
            "Alappuzha",
            "Kottayam",
            "Idukki",
            "Ernakulam",
            "Thrissur",
            "Palakkad",
            "Malappuram",
            "Kozhikode",
            "Wayanad",
            "Kannur",
            "Kasaragod"
        ))
// Add your district names here
    }


}


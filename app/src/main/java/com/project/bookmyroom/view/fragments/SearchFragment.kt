package com.project.bookmyroom.view.fragments

import android.content.Context
import android.os.Bundle
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.bookmyroom.R
import com.project.bookmyroom.view.activity.MainActivity
import com.project.bookmyroom.view.components.adapters.PopularHotelsAdapter
import com.project.bookmyroom.view.components.interfaces.LocationChangeListener
import com.project.bookmyroom.viewmodel.RecentsData
import java.io.IOException

class SearchFragment : Fragment() {

    private var locationChangeListener: LocationChangeListener? = null
    private lateinit var recyclerViewPopularHotels: RecyclerView
    private lateinit var editTextSearch: AutoCompleteTextView
    private lateinit var buttonSearch: Button
    private lateinit var popularHotelsAdapter: PopularHotelsAdapter
    private val allDistricts = mutableListOf<String>()
    private lateinit var currentLocation: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)


        recyclerViewPopularHotels = view.findViewById(R.id.recycler_view_popular_hotels)
        editTextSearch = view.findViewById(R.id.edit_text_search)
        buttonSearch = view.findViewById(R.id.button_search)
        currentLocation=view.findViewById(R.id.currentLocation)
        loadDistricts()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, allDistricts)
        editTextSearch.setAdapter(adapter)
        currentLocation.setText(MainActivity.defaultLocation)
        // Setup click listener for the Search button
        buttonSearch.setOnClickListener {
            performSearch(editTextSearch.text.toString())
            updateLocation(editTextSearch.text.toString())

        }

        // Setup popular hotels RecyclerView
        recyclerViewPopularHotels.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // Initialize adapter but don't set it to RecyclerView yet
        popularHotelsAdapter = PopularHotelsAdapter(emptyList())
        recyclerViewPopularHotels.adapter = popularHotelsAdapter

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadDistricts() // Load districts data when the view is created
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


    private fun getPopularHotels(): List<RecentsData> {
        // Return a list of popular hotels
        return loadJsonData("trending.json")
       /* return listOf(
            RecentsData("Hotel 1", "Description 1", R.drawable.ic_hotel),
            RecentsData("Hotel 2", "Description 2", R.drawable.ic_hotel),
            // Add more hotel items as needed
        )*/
    }

    private fun performSearch(query: String) {
        // Implement your search logic here
        // For demonstration, let's just filter the popular hotels based on the query
        val filteredHotels = getPopularHotels().filter { hotel ->
            hotel.placeName.contains(query, ignoreCase = true) || hotel.countryName.contains(query, ignoreCase = true)
        }

        // Update the RecyclerView with the filtered results
        popularHotelsAdapter.submitList(filteredHotels)

        // Show the RecyclerView if there are search results
        if (filteredHotels.isNotEmpty()) {
            recyclerViewPopularHotels.visibility = View.VISIBLE
        } else {
            recyclerViewPopularHotels.visibility = View.GONE
        }
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

    private fun loadDistricts() {
        // Load districts data from your source and populate allDistricts list
        // For demonstration, assume allDistricts is already populated
        allDistricts.addAll(listOf(
            "Trivandrum",
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


package com.project.bookmyroom.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.bookmyroom.R

class SearchFragment : Fragment() {

    private lateinit var listViewSearchResults: ListView
    private lateinit var recyclerViewPopularHotels: RecyclerView
    private lateinit var editTextSearch: EditText
    private lateinit var buttonSearch: Button
    private lateinit var popularHotelsAdapter: PopularHotelsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        // Initialize views
        listViewSearchResults = view.findViewById(R.id.list_view_search_results)
        recyclerViewPopularHotels = view.findViewById(R.id.recycler_view_popular_hotels)
        editTextSearch = view.findViewById(R.id.edit_text_search)
        buttonSearch = view.findViewById(R.id.button_search)

        // Setup click listener for the Search button
        buttonSearch.setOnClickListener {
            performSearch(editTextSearch.text.toString())

        }

        // Setup popular hotels RecyclerView
        recyclerViewPopularHotels.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // Initialize adapter but don't set it to RecyclerView yet
        popularHotelsAdapter = PopularHotelsAdapter(emptyList())
        recyclerViewPopularHotels.adapter = popularHotelsAdapter

        return view
    }

    private fun getPopularHotels(): List<HotelItem> {
        // Return a list of popular hotels
        // You can fetch this data from your database or use hardcoded data for demonstration
        return listOf(
            HotelItem("Hotel 1", "Description 1", R.drawable.ic_hotel),
            HotelItem("Hotel 2", "Description 2", R.drawable.ic_hotel),
            // Add more hotel items as needed
        )
    }

    private fun performSearch(query: String) {
        // Implement your search logic here
        // For demonstration, let's just filter the popular hotels based on the query
        val filteredHotels = getPopularHotels().filter { hotel ->
            hotel.name.contains(query, ignoreCase = true) || hotel.description.contains(query, ignoreCase = true)
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

    // Inner class for the adapter
    private inner class PopularHotelsAdapter(private var hotelList: List<HotelItem>) :
        RecyclerView.Adapter<PopularHotelsAdapter.HotelViewHolder>() {

        inner class HotelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            // Bind UI elements here
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_hotel, parent, false)
            return HotelViewHolder(view)
        }

        override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {
            val currentItem = hotelList[position]
            // Bind data to UI elements in the ViewHolder
        }

        override fun getItemCount(): Int {
            return hotelList.size
        }

        fun submitList(list: List<HotelItem>) {
            hotelList = list
            notifyDataSetChanged()
        }
    }

    data class HotelItem(val name: String, val description: String, val imageResId: Int)
}


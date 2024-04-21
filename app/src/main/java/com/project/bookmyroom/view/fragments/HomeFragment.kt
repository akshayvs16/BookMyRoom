package com.project.bookmyroom.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.bookmyroom.R
import com.project.bookmyroom.view.components.adapters.RecentsAdapter
import com.project.bookmyroom.view.components.adapters.TopPlacesAdapter
import com.project.bookmyroom.viewmodel.RecentsData
import com.project.bookmyroom.viewmodel.TopPlacesData
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
    private lateinit var nearPlaceRecycler: RecyclerView
    private var recentAdapter: RecentsAdapter? = null
    private val recentDataList: MutableList<RecentsData> = ArrayList()
    private val nearHotelsDataList: MutableList<TopPlacesData> = ArrayList()
    private var nearPlacesAdapter: TopPlacesAdapter? = null


    private lateinit var recommendedData: List<RecentsData>
    private lateinit var popularData: List<RecentsData>
    private lateinit var trendingData: List<RecentsData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recommendedData = getRecommendedData()
        popularData = getPopularData()
        trendingData = getTrendingData()

        // Initialize RecyclerView
        recentRecycler = view.findViewById(R.id.recent_recycler)
        nearPlaceRecycler = view.findViewById(R.id.near_places_recycler)
        setRecentRecycler(recommendedData) // Default: Recommended data
        setNearPlacesRecycler(getNearPlacesData()) // Default: Recommended data

        // Setup category chips click listeners
        setupCategoryChips()

        return view
    }

    private fun setupCategoryChips() {
       /* val recommendedChip = view?.findViewById<TextView>(R.id.chipRecommended)
        val popularChip = view?.findViewById<TextView>(R.id.chipPopular)
        val trendingChip = view?.findViewById<TextView>(R.id.chipTrending)

        recommendedChip?.setOnClickListener {

            setRecentRecycler(getRecommendedData())
        }

        popularChip?.setOnClickListener {
            setRecentRecycler_2(getPopularData())
        }

        trendingChip?.setOnClickListener {
            setRecentRecycler_3(getTrendingData())
        }*/
    }

    private fun setRecentRecycler(dataList: List<RecentsData>) {
        recentDataList.clear()
        recentDataList.addAll(dataList)

        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recentRecycler.layoutManager = layoutManager
        recentAdapter = RecentsAdapter(requireContext(), recentDataList)
        recentRecycler.adapter = recentAdapter
        recentRecycler.invalidate()
    }
    private fun setRecentRecycler_2(dataList: List<RecentsData>) {
        recentDataList.clear()
        recentDataList.addAll(dataList)

        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recentRecycler.layoutManager = layoutManager
        recentAdapter = RecentsAdapter(requireContext(), recentDataList)
        recentRecycler.adapter = recentAdapter
        recentRecycler.invalidate()
    }
    private fun setRecentRecycler_3(dataList: List<RecentsData>) {
        recentDataList.clear()
        recentDataList.addAll(dataList)

        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recentRecycler.layoutManager = layoutManager
        recentAdapter = RecentsAdapter(requireContext(), recentDataList)
        recentRecycler.adapter = recentAdapter
        recentRecycler.invalidate()
    }

    private fun setNearPlacesRecycler(dataList: List<TopPlacesData>) {
        nearHotelsDataList.clear()
        nearHotelsDataList.addAll(dataList)

        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        nearPlaceRecycler.layoutManager = layoutManager
        nearPlacesAdapter = TopPlacesAdapter(requireContext(), nearHotelsDataList)
        nearPlaceRecycler.adapter = nearPlacesAdapter
        nearPlaceRecycler.invalidate()
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
}

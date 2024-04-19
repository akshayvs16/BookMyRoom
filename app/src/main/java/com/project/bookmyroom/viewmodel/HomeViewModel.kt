package com.project.bookmyroom.viewmodel

import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.bookmyroom.viewmodel.RecentsData
import com.project.bookmyroom.viewmodel.TopPlacesData
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel : ViewModel() {
    val recommendedData = MutableLiveData<List<RecentsData>>()
    val popularData = MutableLiveData<List<RecentsData>>()
    val trendingData = MutableLiveData<List<RecentsData>>()
    val nearPlacesData = MutableLiveData<List<TopPlacesData>>()

    fun fetchData(context: Context) {
        recommendedData.value = loadJsonData(context, "hotel.json")
        popularData.value = loadJsonData(context, "popularHotel.json")
        trendingData.value = loadJsonData(context, "trendingHotel.json")
        nearPlacesData.value = loadNearByPlacesData(context, "nearByPlaces.json")
    }

    fun fetchRecommendedData(context: Context) {
        viewModelScope.launch {
            recommendedData.value = loadJsonData(context, "recommended.json")
        }
    }

    fun fetchPopularData(context: Context) {
        viewModelScope.launch {
            popularData.value = loadJsonData(context, "popular.json")
        }
    }

    fun fetchTrendingData(context: Context) {
        viewModelScope.launch {
            trendingData.value = loadJsonData(context, "trending.json")
        }
    }

    private fun loadJsonData(context: Context, fileName: String): List<RecentsData> {
        val jsonString = try {
            val inputStream = context.assets.open(fileName)
            inputStream.bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            Log.e("HomeFragment", "Error reading JSON file: ${e.message}")
            e.printStackTrace()
            ""
        }

        val type = object : TypeToken<List<RecentsData>>() {}.type
        return Gson().fromJson(jsonString, type) ?: emptyList()
    }

    private fun loadNearByPlacesData(context: Context,fileName: String): List<TopPlacesData> {
        val jsonString = try {
            val inputStream = context.assets.open(fileName)
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

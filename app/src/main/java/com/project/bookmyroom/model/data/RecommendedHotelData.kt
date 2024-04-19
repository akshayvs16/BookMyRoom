package com.project.bookmyroom.model.data

import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import com.project.bookmyroom.viewmodel.RecentsData
import java.io.IOException
import kotlin.math.log

class RecommendedHotelData {


    fun getRecommededHotelData(context: Context): List<RecentsData> {
        try {
            val inputStream = context.assets.open("hotel.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<RecentsData>>() {}.type
            return Gson().fromJson(jsonString, type) ?: emptyList()
        } catch (e: IOException) {
            Log.e("RecommendedHotelData", "Error reading JSON file: ${e.localizedMessage}")
            e.printStackTrace()
        } catch (e: JsonSyntaxException) {
            Log.e("RecommendedHotelData", "Error parsing JSON: ${e.localizedMessage}")
            e.printStackTrace()
        }
        return emptyList()
    }


}
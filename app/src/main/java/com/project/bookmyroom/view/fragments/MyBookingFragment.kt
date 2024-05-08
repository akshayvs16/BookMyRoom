package com.project.bookmyroom.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textview.MaterialTextView
import com.project.bookmyroom.R
import com.project.bookmyroom.network.RetrofitClient
import com.project.bookmyroom.preference.PreferenceManager
import com.project.bookmyroom.view.components.adapters.BookingAdapter
import com.project.bookmyroom.model.data.BookedRoomsResponse
import com.project.bookmyroom.model.data.BookingItem
import com.project.bookmyroom.model.data.MyBookingUserRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyBookingFragment : Fragment() {

    private lateinit var preferenceManager: PreferenceManager
    private lateinit var booking_progressLoading_circular: CircularProgressIndicator

    private lateinit var myBookingRecycler: RecyclerView
    private lateinit var noDataText: MaterialTextView

    private val bookedDataList: MutableList<BookingItem> = ArrayList()
    private var bookingAdapter: BookingAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_my_booking, container, false)

        val user=preferenceManager.getUser()
        booking_progressLoading_circular=view.findViewById(R.id.booking_progressLoading_circular)
        myBookingRecycler = view.findViewById(R.id.bookingRecyclerview)
        noDataText = view.findViewById(R.id.myBookingDataEmpty)
        if (user != null) {
            fetchMyBookingsByUserId(user.userId)
        }else{
            Log.d("USerNull", "onCreate: User Null")
        }

        return view
    }


    private fun fetchMyBookingsByUserId(userId: String) {
        booking_progressLoading_circular.visibility=View.VISIBLE

        val apiService = RetrofitClient.instance
        val userIdRequest = MyBookingUserRequest(userId)

        apiService.getBookedRooms(userIdRequest).enqueue(object : Callback<BookedRoomsResponse> {
            override fun onResponse(call: Call<BookedRoomsResponse>, response: Response<BookedRoomsResponse>) {
                if (response.isSuccessful) {
                    val hotelResponse = response.body()
                    hotelResponse?.let {
                        setMyBookingRecycler(parseRoomApiResponse(it))
                    }
                } else {
                    Log.e("HomeFragment", "API call failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<BookedRoomsResponse>, t: Throwable) {
                Log.e("HomeFragment", "Error fetching data: ${t.message}")
            }
        })
    }

    private fun parseRoomApiResponse(response: BookedRoomsResponse): List<BookingItem> {
        val dataList = mutableListOf<BookingItem>()
        val apiData = response.data
        if (apiData.isEmpty()){
            noDataText.visibility=View.VISIBLE
            booking_progressLoading_circular.visibility=View.GONE
        }
        else {
            noDataText.visibility = View.GONE
            booking_progressLoading_circular.visibility=View.GONE
            for (item in apiData) {
                val bookedItem = BookingItem(
                    item._id,
                    item.checkIn,
                    item.checkOut,
                    item.hotelId,
                    item.hotelName,
                    item.noOfPeople,
                    item.noOfRooms,
                    item.price,
                    item.type,
                    item.userId,
                    item.id,
                    item.createdAt,
                    item.updatedAt
                )
                dataList.add(bookedItem)
                Log.d("HOME", "parseHotelApiResponse: ${bookedItem.hotelName}")

            }
        }
            return dataList

    }

    private fun setMyBookingRecycler(dataList: List<BookingItem>) {
        if (!isAdded || context == null) {
            return
        }
        bookedDataList.clear()
        bookedDataList.addAll(dataList)

        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        myBookingRecycler.layoutManager = layoutManager
        bookingAdapter = BookingAdapter(bookedDataList)
        myBookingRecycler.adapter = bookingAdapter
        myBookingRecycler.invalidate()
        booking_progressLoading_circular.visibility=View.GONE

    }

}
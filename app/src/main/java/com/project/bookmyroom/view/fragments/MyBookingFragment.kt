package com.project.bookmyroom.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.bookmyroom.R
import com.project.bookmyroom.model.data.Hotel
import com.project.bookmyroom.model.data.HotelResponse
import com.project.bookmyroom.network.RetrofitClient
import com.project.bookmyroom.preference.PreferenceManager
import com.project.bookmyroom.view.components.adapters.BookingAdapter
import com.project.bookmyroom.view.components.adapters.NearPlacesAdapter
import com.project.bookmyroom.viewmodel.BookedRoomsResponse
import com.project.bookmyroom.viewmodel.BookingItem
import com.project.bookmyroom.viewmodel.MyBookingUserRequest
import com.project.bookmyroom.viewmodel.NearPlacesData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyBookingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyBookingFragment : Fragment() {

    private lateinit var preferenceManager: PreferenceManager


    private lateinit var myBookingRecycler: RecyclerView

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
        myBookingRecycler = view.findViewById(R.id.bookingRecyclerview)

        fetchMyBookingsByUserId(user!!.userId)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




    }



    private fun fetchMyBookingsByUserId(userId: String) {
        /*progress_circular.visibility=View.VISIBLE
        progress_circular.progress*/

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
       /* progress_circular.visibility=View.GONE
        NearData_notFound.visibility=View.GONE*/

    }

}
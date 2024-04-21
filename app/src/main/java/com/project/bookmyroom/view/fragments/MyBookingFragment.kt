package com.project.bookmyroom.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.bookmyroom.R
import com.project.bookmyroom.view.components.adapters.BookingAdapter
import com.project.bookmyroom.viewmodel.BookingItem

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_my_booking, container, false)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookingList = listOf(
            BookingItem("Hotel Leela", "2024-04-25", "Trivandrum", "1200"),
            BookingItem("Hotel RAviz", "2024-04-27", "Trivandrum", "1500"),
            // Add more booking items as needed
        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.bookingRecyclerview)
        val adapter = BookingAdapter(bookingList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }


}
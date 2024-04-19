package com.project.bookmyroom.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.bookmyroom.R
import com.project.bookmyroom.view.components.adapters.RecentsAdapter
import com.project.bookmyroom.viewmodel.RecentsData

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
    private var recentsAdapter: RecentsAdapter? = null
    val recentsDataList: MutableList<RecentsData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hotelData()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recentRecycler = view.findViewById(R.id.recent_recycler)
        setRecentRecycler(recentsDataList)

        return view
    }



    private fun setRecentRecycler(recentsDataList: List<RecentsData>) {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recentRecycler?.setLayoutManager(layoutManager)
        recentsAdapter = RecentsAdapter(requireContext(), recentsDataList)
        recentRecycler?.setAdapter(recentsAdapter)
    }

    private fun hotelData(){
        recentsDataList.add(RecentsData("AM Lake", "India", "From $200", R.drawable.img_carousel_1))
        recentsDataList.add(
            RecentsData(
                "Nilgiri Hills",
                "India",
                "From $300",
                R.drawable.img_carousel_2
            )
        )
        recentsDataList.add(RecentsData("AM Lake", "India", "From $200", R.drawable.img_carousel_3))
        recentsDataList.add(
            RecentsData(
                "Nilgiri Hills",
                "India",
                "From $300",
                R.drawable.img_carousel_3
            )
        )
        recentsDataList.add(RecentsData("AM Lake", "India", "From $200", R.drawable.img_carousel_1))
        recentsDataList.add(
            RecentsData(
                "Nilgiri Hills",
                "India",
                "From $300",
                R.drawable.img_carousel_2
            )
        )

    }
}
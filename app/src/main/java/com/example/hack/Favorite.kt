package com.example.hack

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A simple [Fragment] subclass.
 * Use the [Favorite.newInstance] factory method to
 * create an instance of this fragment.
 */
class Favorite : Fragment() {
    private lateinit var recyclerView : RecyclerView
    private lateinit var mAdapter : RecyclerView.Adapter<*>
    private lateinit var layoutManager : RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val sharedprefs = requireActivity().getSharedPreferences("favorites", MODE_PRIVATE)
        val set = sharedprefs.getStringSet("Favorite", MyFav.list.toSet())?.toList()
        val rootView = inflater.inflate(R.layout.fragment_favorite, container, false)
        recyclerView = rootView.findViewById<RecyclerView>(R.id.my_recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.adapter = set?.let { MyAdapter(it) }

        return rootView
    }

}
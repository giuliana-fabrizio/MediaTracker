package com.example.mediatracker

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView

class ListeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var page = arguments?.getString("media")
        if ((activity as AppCompatActivity).supportActionBar?.title.toString() == "Liste")
            (activity as AppCompatActivity).supportActionBar?.title = page.toString()

        Log.i("", (activity as AppCompatActivity).supportActionBar?.title.toString())

        /**
         * traitement avec la BDD
         * */
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_liste, container, false)
    }
}
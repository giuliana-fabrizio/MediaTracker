package com.example.mediatracker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import android.widget.Button

class AccueilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_accueil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var btn_onglet_1 = view.findViewById<Button>(R.id.id_btn_onglet_1)
        var btn_onglet_2 = view.findViewById<Button>(R.id.id_btn_onglet_2)
        var btn_onglet_3 = view.findViewById<Button>(R.id.id_btn_onglet_3)

        btn_onglet_1.setOnClickListener {
            Log.i("", "tata")
        }
    }
}
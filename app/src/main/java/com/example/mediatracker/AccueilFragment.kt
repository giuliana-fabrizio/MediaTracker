package com.example.mediatracker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import android.widget.Button
import androidx.core.os.bundleOf

class AccueilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_accueil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var btnOnglet1 = view.findViewById<Button>(R.id.id_btn_onglet_1)
        var btnOnglet2 = view.findViewById<Button>(R.id.id_btn_onglet_2)
        var btnOnglet3 = view.findViewById<Button>(R.id.id_btn_onglet_3)

        btnOnglet1.setOnClickListener {
            view.findNavController().navigate(
                R.id.id_action_accueil_liste,
                bundleOf(
                    "quelle_page" to "animes"
                )
            )
        }

        btnOnglet2.setOnClickListener {
            view.findNavController().navigate(
                R.id.id_action_accueil_liste,
                bundleOf(
                    "quelle_page" to "films"
                )
            )
        }

        btnOnglet3.setOnClickListener {
            view.findNavController().navigate(
                R.id.id_action_accueil_liste,
                bundleOf(
                    "quelle_page" to "series"
                )
            )
        }
    }
}
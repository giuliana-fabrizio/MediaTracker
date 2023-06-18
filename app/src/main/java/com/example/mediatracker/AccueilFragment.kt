package com.example.mediatracker

import android.os.Bundle
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

        val btnOnglets = listOf(
            Pair(R.id.id_btn_onglet_1, "animes"),
            Pair(R.id.id_btn_onglet_2, "films"),
            Pair(R.id.id_btn_onglet_3, "series")
        )

        for (btnOnglet in btnOnglets) {
            view.findViewById<Button>(btnOnglet.first).setOnClickListener {
                view.findNavController().navigate(
                    R.id.id_action_accueil_liste,
                    bundleOf("quelle_page" to btnOnglet.second)
                )
            }
        }
    }
}

package com.example.mediatracker

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.mediatracker.MainActivity.Companion.db
import com.google.android.material.tabs.TabLayout

class AccueilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_accueil, container, false)

        var viewAccueil = listOf(
            Triple(R.id.id_viewPager_1, R.id.id_indicator_1, R.string.onglet_1),
            Triple(R.id.id_viewPager_2, R.id.id_indicator_2, R.string.onglet_1),
            Triple(R.id.id_viewPager_3, R.id.id_indicator_3, R.string.onglet_1)
        )

        for (view in viewAccueil) {
            val viewPager: ViewPager = rootView.findViewById(view.first)
            val indicator: TabLayout = rootView.findViewById(view.second)

            Log.i("tata", getString(view.third))
            try {
                val medias = db.mediaDao().getAll(getString(view.third))
                val imageUrls = medias.map { res -> res.image }

                val adapter = AccueilAdapter(this.context, imageUrls)
                viewPager.adapter = adapter
                indicator.setupWithViewPager(viewPager)
            } catch (e : java.lang.Exception) {
                Log.i("erreur", e.message.toString())
            }
        }
        return rootView
    }
}

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_accueil, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val btnOnglets = listOf(
//            Pair(R.id.id_btn_onglet_1, getString(R.string.onglet_1)),
//            Pair(R.id.id_btn_onglet_2, getString(R.string.onglet_2)),
//            Pair(R.id.id_btn_onglet_3, getString(R.string.onglet_3))
//        )
//
//        for (btnOnglet in btnOnglets) {
//            view.findViewById<Button>(btnOnglet.first).setOnClickListener {
//                view.findNavController().navigate(
//                    R.id.id_action_accueil_liste,
//                    bundleOf("media" to btnOnglet.second)
//                )
//            }
//        }
//    }
// }

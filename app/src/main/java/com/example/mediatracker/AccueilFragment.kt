package com.example.mediatracker

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class AccueilFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_accueil, container, false)

        val viewPager: ViewPager = rootView.findViewById(R.id.viewPager)
        val indicator: TabLayout = rootView.findViewById(R.id.indicator)

        val imageUrls = listOf(
            "https://th.bing.com/th/id/OIP.eTzI95wbSwa2eRRkD1GNGAHaHa?pid=ImgDet&rs=1",
            "https://www.journaldugeek.com/content/uploads/2023/04/minecraft-legends-tests.jpg",
            "https://th.bing.com/th/id/OIP.eTzI95wbSwa2eRRkD1GNGAHaHa?pid=ImgDet&rs=1"
        )

        val adapter = AccueilAdapter(this.context, imageUrls)
        viewPager.adapter = adapter
        indicator.setupWithViewPager(viewPager)

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

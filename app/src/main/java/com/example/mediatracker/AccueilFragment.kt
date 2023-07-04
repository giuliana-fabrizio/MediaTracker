package com.example.mediatracker

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.mediatracker.MainActivity.Companion.db
import com.google.android.material.tabs.TabLayout

class AccueilFragment : Fragment() {

    data class RessourcesCarrousel(
        var id_view_pager: Int = 0,
        var id_indicator: Int = 0,
        var str_categorie: String = "",
        var str_lien_img: String = ""
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_accueil, container, false)

        var viewAccueil = listOf(
            RessourcesCarrousel(
                R.id.id_viewPager_1,
                R.id.id_indicator_1,
                getString(R.string.onglet_1),
                getString(R.string.lien_img_onglet_1)
            ),
            RessourcesCarrousel(
                R.id.id_viewPager_2,
                R.id.id_indicator_2,
                getString(R.string.onglet_2),
                getString(R.string.lien_img_onglet_2)
            ),
            RessourcesCarrousel(
                R.id.id_viewPager_3,
                R.id.id_indicator_3,
                getString(R.string.onglet_3),
                getString(R.string.lien_img_onglet_3)
            )
        )

        for (view in viewAccueil) {
            val viewPager: ViewPager = rootView.findViewById(view.id_view_pager)
            val indicator: TabLayout = rootView.findViewById(view.id_indicator)

            val allMedias = db.mediaDao().getAll(view.str_categorie)
            var medias = listOf(
                Pair(view.str_categorie, view.str_lien_img)
            )
            medias = medias.plus(allMedias.map { res ->
                Pair(res.nom, res.image)
            }.filter { res -> res.second.length !== 0 })

            val adapter = AccueilAdapter(medias, this)
            viewPager.adapter = adapter
            indicator.setupWithViewPager(viewPager)
        }
        return rootView
    }
}
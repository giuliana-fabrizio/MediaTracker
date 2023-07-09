package com.example.mediatracker

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.example.mediatracker.MainActivity.Companion.db
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccueilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_accueil, container, false)

        val ressources = listOf(
            Pair("id_viewPager_", "id"),
            Pair("id_indicator_", "id"),
            Pair("onglet_", "string"),
            Pair("lien_img_onglet_", "string")
        )

        for (i in 1..3) {

            val params = (0..3).map { index ->
                resources.getIdentifier(
                    ressources[index].first + "$i",
                    ressources[index].second,
                    "com.example.mediatracker"
                )
            }

            val viewPager: ViewPager = rootView.findViewById(params[0])
            val indicator: TabLayout = rootView.findViewById(params[1])

            lifecycleScope.launch {
                val allMedias =
                    withContext(Dispatchers.IO) { db.mediaDao().getAll(getString(params[2])) }
                var medias = listOf(
                    Pair(getString(params[2]), getString(params[3]))
                )
                medias = medias.plus(allMedias.map { res ->
                    Pair(res.nom, res.image)
                }.filter { res -> res.second.length !== 0 })
                val adapter = AccueilAdapter(medias, this@AccueilFragment)
                viewPager.adapter = adapter
                indicator.setupWithViewPager(viewPager)
            }
        }
        return rootView
    }
}
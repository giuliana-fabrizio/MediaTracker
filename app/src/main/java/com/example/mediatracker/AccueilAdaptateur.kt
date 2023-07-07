package com.example.mediatracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso

class AccueilAdapter(
    private val medias: List<Pair<String, String>>,
    private val fragment: AccueilFragment
) : PagerAdapter() {

    override fun getCount(): Int {
        return medias.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as View
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(container.context)
        val itemView = layoutInflater.inflate(R.layout.item_accueil, container, false)
        val media = medias[position]
        val imageUrl = media.second

        if (imageUrl.isNotEmpty()) {
            val imageView: ImageView = itemView.findViewById(R.id.imageView)
            Picasso.get().load(imageUrl).into(imageView)
            container.addView(itemView)

            val params = listOf(
                Pair(R.id.id_action_accueil_liste, "media"),
                Pair(R.id.id_action_accueil_detail, "id_media")
            )

            imageView.setOnClickListener {
                Navigation.findNavController(fragment.view!!).navigate(
                    if (position == 0) params[0].first else params[1].first,
                    bundleOf((if (position == 0) params[0].second else params[1].second) to media.first)
                )
            }
        }

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}

package com.example.mediatracker

import android.content.Context
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

        val layoutInflater =
            fragment.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val media = medias[position]
        val itemView: View = layoutInflater.inflate(R.layout.item_accueil, container, false)
        val imageUrl = media.second

        if (imageUrl.length > 0) {
            val imageView: ImageView = itemView.findViewById(R.id.imageView)
            Picasso.get().load(imageUrl).into(imageView)
            container.addView(itemView)

            if (position == 0) {
                imageView.setOnClickListener {
                    Navigation.findNavController(fragment.view!!).navigate(
                        R.id.id_action_accueil_liste,
                        bundleOf( "media" to media.first)
                    )
                }
            } else {
                imageView.setOnClickListener {
                    Navigation.findNavController(fragment.view!!).navigate(
                        R.id.id_action_accueil_detail,
                        bundleOf(
                            "id_media" to media.first.toString()
                        )
                    )
                }
            }
        }
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}

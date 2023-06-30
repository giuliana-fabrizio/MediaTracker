package com.example.mediatracker

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.mediatracker.bdd.Media
import com.squareup.picasso.Picasso

class ListeAdaptateur(
    private val medias: List<Media>,
    private val fragment: Fragment
) : RecyclerView.Adapter<ListeAdaptateur.ItemListeViewHolder>() {

    class ItemListeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.id_img_media_detail)
        val textView: TextView = view.findViewById(R.id.id_textview_media_detail)
        val btn_site_web: Button = view.findViewById(R.id.id_btn_site_web_detail)
        val btn_page_detail: Button = view.findViewById(R.id.id_btn_page_detail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListeViewHolder {
        var layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_liste, parent, false)

        return ItemListeViewHolder(layout)
    }

    override fun getItemCount() = medias.size

    override fun onBindViewHolder(holder: ItemListeViewHolder, position: Int) {

        var media = medias[position]

        if (media.image.length === 0) holder.image.visibility = View.INVISIBLE
        else Picasso.get().load(media.image).into(holder.image)

        holder.textView.text = media.nom

        holder.btn_page_detail.setOnClickListener {
            Navigation.findNavController(fragment.view!!)
                .navigate(
                    R.id.id_action_liste_detail,
                    bundleOf("id_media" to media.id_media.toString())
                )
        }

        holder.btn_site_web.setOnClickListener {
            val url = media.lien
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            fragment.startActivity(intent)
        }

        if (media.lien.length === 0) holder.btn_site_web.visibility = View.INVISIBLE

        holder.btn_site_web.setOnClickListener {
            val url = media.lien
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            fragment.startActivity(intent)
        }
    }
}

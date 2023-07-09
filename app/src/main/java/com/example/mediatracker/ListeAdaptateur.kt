package com.example.mediatracker

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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

    inner class ItemListeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val trait: View = view.findViewById(R.id.id_trait_liste)
        val image: ImageView = view.findViewById(R.id.id_img_media_detail)
        val textView: TextView = view.findViewById(R.id.id_textview_media_detail)
        val btn_site_web: Button = view.findViewById(R.id.id_btn_site_web_detail)
        val btn_page_detail: Button = view.findViewById(R.id.id_btn_page_detail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val layout = layoutInflater.inflate(R.layout.item_liste, parent, false)
        return ItemListeViewHolder(layout)
    }

    override fun getItemCount(): Int = medias.size

    override fun onBindViewHolder(holder: ItemListeViewHolder, position: Int) {
        val media = medias[position]

        if (position == 0) {
            holder.trait.visibility = View.INVISIBLE
        }

        if (media.image.isNotEmpty()) {
            holder.image.visibility = View.VISIBLE
            Picasso.get().load(media.image).into(holder.image)
        } else {
            holder.image.visibility = View.INVISIBLE
        }

        holder.textView.text = media.nom

        holder.btn_page_detail.setOnClickListener {
            val bundle = bundleOf("id_media" to media.nom)
            Navigation.findNavController(fragment.requireView())
                .navigate(R.id.id_action_liste_detail, bundle)
        }

        holder.btn_site_web.setOnClickListener {
            try {
                val url = media.lien
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                fragment.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(
                    fragment.requireContext(),
                    fragment.getString(R.string.erreur_redirection_web),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
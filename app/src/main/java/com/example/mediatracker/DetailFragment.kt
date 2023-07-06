package com.example.mediatracker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.example.mediatracker.bdd.Media
import com.squareup.picasso.Picasso

class DetailFragment : Fragment() {
    private lateinit var media: Media

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        val args = arguments?.getString("id_media")
        media = MainActivity.db.mediaDao().getOne(args.toString())

        val imageView = view.findViewById<ImageView>(R.id.id_detail_img)
        val photoEditText = view.findViewById<EditText>(R.id.id_detail_photo)
        val nomEditText = view.findViewById<EditText>(R.id.id_detail_nom)
        val descriptionEditText = view.findViewById<EditText>(R.id.id_detail_description)
        val lienButton = view.findViewById<Button>(R.id.id_detail_btn_lien)
        val spinner = view.findViewById<Spinner>(R.id.id_detail_spinner)
        val saison = view.findViewById<EditText>(R.id.id_detail_saison)
        val episode = view.findViewById<EditText>(R.id.id_detail_episode)
        val suppButton = view.findViewById<Button>(R.id.id_detail_btn_supp)
        val modifierButton = view.findViewById<Button>(R.id.id_detail_btn_modifier)

        if (media.image.length !== 0) Picasso.get().load(media.image).into(imageView)
        nomEditText.hint = media.nom
        descriptionEditText.hint = media.description

        lienButton.setOnClickListener {
            if (media.lien.length !== 0) {
                val url = media.lien
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } else
                Toast.makeText(
                    context,
                    "Pas de lien renseigné 🥸",
                    Toast.LENGTH_SHORT
                ).show()
        }

        suppButton.setOnClickListener {
            MainActivity.db.mediaDao().deleteByName(media.nom)
            Navigation.findNavController(view)
                .navigate(
                    R.id.id_action_detail_liste,
                    bundleOf("media" to media.media_categorie)
                )
            Toast.makeText(
                context,
                "Media supprimé ✅",
                Toast.LENGTH_SHORT
            ).show()
        }

        return view
    }
}
package com.example.mediatracker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.example.mediatracker.bdd.MediaDetail
import com.squareup.picasso.Picasso

class DetailFragment : Fragment() {
    private lateinit var detail: MediaDetail
    private lateinit var imageView: ImageView
    private lateinit var photoEditText: EditText
    private lateinit var nomEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var lienButton: Button
    private lateinit var lienEditText: EditText
    private lateinit var spinner: Spinner
    private lateinit var saison: EditText
    private lateinit var episode: EditText
    private lateinit var suppButton: Button
    private lateinit var modifierButton: Button

    private lateinit var selectedText: String
    private var statuts = MainActivity.db.statutDao().getAllStatut()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        val args = arguments?.getString("id_media")
        detail = MainActivity.db.mediaDao().getOne(args.toString())

        imageView = view.findViewById(R.id.id_detail_img)
        photoEditText = view.findViewById(R.id.id_detail_photo)
        nomEditText = view.findViewById(R.id.id_detail_nom)
        descriptionEditText = view.findViewById(R.id.id_detail_description)
        lienButton = view.findViewById(R.id.id_detail_btn_lien)
        lienEditText = view.findViewById(R.id.id_detail_lien)
        spinner = view.findViewById(R.id.id_detail_spinner)
        saison = view.findViewById(R.id.id_detail_saison)
        episode = view.findViewById(R.id.id_detail_episode)
        suppButton = view.findViewById(R.id.id_detail_btn_supp)
        modifierButton = view.findViewById(R.id.id_detail_btn_modifier)

        if (detail.media_categorie == getString(R.string.onglet_2)) {
            val constraintLayout: ConstraintLayout =
                view.findViewById(R.id.id_constraint_layout_saison_episode)
            constraintLayout.visibility = View.GONE
        }

        editable(false)
        init()
        listener(view)

        return view
    }

    private fun init() {
        with(detail) {
            if (image.isNotEmpty()) {
                Picasso.get().load(image).into(imageView)
            }
            photoEditText.setText(image)
            nomEditText.setText(nom)
            descriptionEditText.setText(description)
            lienButton.visibility = View.VISIBLE
            lienEditText.visibility = View.INVISIBLE
            statuts = statuts.sortedByDescending { statut -> statut.contains(media_statut) }

            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statuts)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            saison.setText(num_saison.toString())
            episode.setText(num_episode.toString())
            suppButton.text = getString(R.string.btn_3)
            modifierButton.text = getString(R.string.btn_4)
        }
    }

    private fun editable(bool: Boolean) {
        val views =
            arrayOf(photoEditText, nomEditText, descriptionEditText, spinner, saison, episode)

        for (view in views) {
            view.isEnabled = bool
        }
    }

    private fun listener(view: View) {
        lienButton.setOnClickListener {
            if (detail.lien.isNotEmpty()) {
                val url = detail.lien
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } else
                Toast.makeText(
                    requireContext(),
                    getString(R.string.erreur_redirection_web),
                    Toast.LENGTH_SHORT
                ).show()
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedText = statuts[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        suppButton.setOnClickListener {
            if (!modifierButton.text.contains(getString(R.string.btn_4))) {
                init()
                editable(false)
            } else {
                MainActivity.db.mediaDao().deleteByName(detail.nom)
                val bundle = bundleOf("media" to detail.media_categorie)
                Navigation.findNavController(view).navigate(R.id.id_action_detail_liste, bundle)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.succes_suppression),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        modifierButton.setOnClickListener {
            val isModifierText = modifierButton.text.contains(getString(R.string.btn_4))
            editable(isModifierText)

            if (isModifierText) {
                modifierButton.text = getString(R.string.btn_5)
                suppButton.text = getString(R.string.btn_6)
                lienButton.visibility = View.INVISIBLE
                lienEditText.visibility = View.VISIBLE
                lienEditText.setText(detail.lien)
            } else {
                MainActivity.db.mediaDao().updateByName(
                    nom = nomEditText.text.toString(),
                    description = descriptionEditText.text.toString(),
                    image = photoEditText.text.toString(),
                    lien = lienEditText.text.toString(),
                    media_statut = selectedText,
                    num_saison = saison.text.toString().toInt(),
                    num_episode = episode.text.toString().toInt(),
                    ancienNom = detail.nom
                )
                detail = MainActivity.db.mediaDao().getOne(nomEditText.text.toString())
                init()
            }
        }
    }
}
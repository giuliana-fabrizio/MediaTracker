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
import androidx.navigation.Navigation
import com.example.mediatracker.bdd.MediaDetail
import com.squareup.picasso.Picasso

class DetailFragment : Fragment() {
    private var detail: MediaDetail? = null

    private lateinit var nomEditText: EditText

    private lateinit var descriptionEditText: EditText
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var labelLienTextView: TextView
    private lateinit var lienEditText: EditText
    private lateinit var lienButton: Button
    private lateinit var imageView: ImageView
    private lateinit var imageEditText: EditText
    private lateinit var spinner: Spinner
    private lateinit var dateSelectionneeTextView: TextView
    private lateinit var saison: EditText
    private lateinit var episode: EditText

    private lateinit var btnDate: Button
    private lateinit var calendarView: CalendarView

    private lateinit var constraintLayoutBtns: ConstraintLayout
    private lateinit var suppButton: Button
    private lateinit var modifierButton: Button

    private lateinit var selectedText: String
    private lateinit var statuts: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        val args = arguments?.getString("id_media")
        if (args != null) detail = MainActivity.db.mediaDao().getOne(args)
        else return null

        initValues(view)
        setEditable(false)
        initView()
        listener()

        return view
    }

    private fun initValues(view: View) {
        nomEditText = view.findViewById(R.id.id_ajout_nom)

        constraintLayout = view.findViewById(R.id.id_constraint_layout)
        descriptionEditText = view.findViewById(R.id.id_ajout_description)

        labelLienTextView = view.findViewById(R.id.id_label_lien)
        lienEditText = view.findViewById(R.id.id_ajout_lien)
        lienButton = view.findViewById(R.id.id_detail_btn_lien)

        imageView = view.findViewById(R.id.id_detail_img)
        imageEditText = view.findViewById(R.id.id_ajout_image)

        spinner = view.findViewById(R.id.id_liste_statut)

        dateSelectionneeTextView = view.findViewById(R.id.id_date_selectionnee)

        if (detail?.media_categorie == getString(R.string.onglet_2)) {
            val constraintLayout: ConstraintLayout =
                view.findViewById(R.id.id_constraint_layout_saison_episode)
            constraintLayout.visibility = View.GONE
        }
        saison = view.findViewById(R.id.id_ajout_saison)
        episode = view.findViewById(R.id.id_ajout_episode)

        btnDate = view.findViewById(R.id.id_btn_ajout_date)
        calendarView = view.findViewById(R.id.id_ajout_date)

        constraintLayoutBtns = view.findViewById(R.id.id_constraint_layout_btns)
        suppButton = view.findViewById(R.id.id_detail_btn_1)
        modifierButton = view.findViewById(R.id.id_detail_btn_2)
    }

    private fun setEditable(bool: Boolean) {
        val views = arrayOf(
            nomEditText,
            descriptionEditText,
            imageEditText,
            spinner,
            saison,
            episode
        )
        for (view in views) view.isEnabled = bool
    }

    private fun initView() {
        detail?.let { mediaDetail ->
            if (mediaDetail.image.isNotEmpty()) {
                Picasso.get().load(mediaDetail.image).into(imageView)
            }
            nomEditText.setText(mediaDetail.nom)
            descriptionEditText.setText(mediaDetail.description)
            lienEditText.setText(mediaDetail.lien)
            imageEditText.setText(mediaDetail.image)

            statuts = MainActivity.db.statutDao().getAllStatut().sortedByDescending { statut ->
                statut.contains(mediaDetail.media_statut)
            }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                statuts
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            saison.setText(mediaDetail.num_saison.toString())
            episode.setText(mediaDetail.num_episode.toString())
        }
        setVisibility(View.INVISIBLE)

        suppButton.text = getString(R.string.btn_3)
        modifierButton.text = getString(R.string.btn_4)
    }

    private fun setVisibility(visibility: Int) {
        labelLienTextView.visibility = visibility
        lienEditText.visibility = visibility
        lienButton.visibility =
            if (lienEditText.visibility == View.INVISIBLE) View.VISIBLE else View.INVISIBLE
        imageEditText.visibility = visibility
        imageView.visibility = lienButton.visibility
        calendarView.visibility =
            if (visibility == View.GONE) View.VISIBLE else View.GONE
        btnDate.visibility =
            if (
                imageEditText.visibility == View.VISIBLE ||
                calendarView.visibility == View.VISIBLE
            )
                View.VISIBLE else View.GONE
        constraintLayout.visibility =
            if (calendarView.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        constraintLayoutBtns.visibility = constraintLayout.visibility
    }

    private fun listener() {
        lienButton.setOnClickListener {
            detail?.let { mediaDetail ->
                try {
                    val url = mediaDetail.lien
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                } catch (e: Exception) {
                    toastInfo(getString(R.string.erreur_redirection_web))

                }
            }
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

        btnDate.setOnClickListener {
            setVisibility(if (calendarView.visibility == View.GONE) View.GONE else View.VISIBLE)
        }

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val formattedDate =
                "Date de sortie : " + String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            dateSelectionneeTextView.text = formattedDate
            setVisibility(View.VISIBLE)
        }

        suppButton.setOnClickListener {
            if (!modifierButton.text.contains(getString(R.string.btn_4))) {
                initView()
                setEditable(false)
            } else {
                detail?.let { mediaDetail ->
                    MainActivity.db.mediaDao().deleteByName(mediaDetail.nom)
                }
                Navigation.findNavController(requireView()).popBackStack()
                toastInfo(getString(R.string.succes_suppression))
            }
        }

        modifierButton.setOnClickListener {
            val isModifierText = modifierButton.text.contains(getString(R.string.btn_4))
            setEditable(isModifierText)

            if (isModifierText) {
                modifierButton.text = getString(R.string.btn_5)
                suppButton.text = getString(R.string.btn_6)
                setVisibility(View.VISIBLE)
            } else {
                val num_saison = saison.text.toString().toIntOrNull()
                val num_episode = episode.text.toString().toIntOrNull()
                var message = ""

                if (num_saison != null && num_episode != null) {
                    try {
                        detail?.let { mediaDetail ->
                            MainActivity.db.mediaDao().updateByName(
                                nom = nomEditText.text.toString(),
                                description = descriptionEditText.text.toString(),
                                image = imageEditText.text.toString(),
                                lien = lienEditText.text.toString(),
                                media_statut = selectedText,
                                num_saison = num_saison,
                                num_episode = num_episode,
                                ancienNom = mediaDetail.nom
                            )
                        }
                        detail = MainActivity.db.mediaDao().getOne(nomEditText.text.toString())
                        message = getString(R.string.succes_operation)
                    } catch (e: Exception) {
                        message = getString(R.string.erreur_operation)
                    }
                } else message = getString(R.string.erreur_nombre)

                initView()
                toastInfo(message)
            }
        }
    }

    private fun toastInfo(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
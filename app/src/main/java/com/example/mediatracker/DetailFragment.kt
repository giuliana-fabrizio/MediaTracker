package com.example.mediatracker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.mediatracker.bdd.MediaDetail
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailFragment : Fragment() {
    private var detail: MediaDetail? = null

    private lateinit var nomEditText: EditText

    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var labelDescription: TextView
    private lateinit var descriptionEditText: EditText
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
    private var dateSortie = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        val args = arguments?.getString("id_media")
        if (args != null) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) { detail = MainActivity.db.mediaDao().getOne(args) }
                initValues(view)
                setEditable(false)
                initView()
                listener()
            }
        } else return null

        return view
    }

    private fun initValues(view: View) {
        nomEditText = view.findViewById(R.id.id_ajout_nom)

        constraintLayout = view.findViewById(R.id.id_constraint_layout)
        labelDescription = view.findViewById(R.id.id_label_description)
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
            labelDescription.setText(getString(R.string.label_modal_description))
            descriptionEditText.setText(mediaDetail.description)
            lienEditText.setText(mediaDetail.lien)
            imageEditText.setText(mediaDetail.image)

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    statuts =
                        MainActivity.db.statutDao().getAllStatut().sortedByDescending { statut ->
                            statut.contains(mediaDetail.media_statut)
                        }
                }
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    statuts
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }

            dateSelectionneeTextView.setText(getString(R.string.date_sortie) + " " + mediaDetail.date_sortie)

            saison.setText(mediaDetail.num_saison.toString())
            episode.setText(mediaDetail.num_episode.toString())
        }
        setVisibility(View.INVISIBLE)

        suppButton.setText(getString(R.string.btn_3))
        modifierButton.setText(getString(R.string.btn_4))
    }

    private fun setVisibility(visibility: Int) {
        descriptionEditText.visibility =
            if (visibility == View.INVISIBLE) View.GONE else View.VISIBLE
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
        labelDescription.setOnClickListener {
            val alertDialogBuilder =
                androidx.appcompat.app.AlertDialog.Builder(requireContext())
            val inflater = LayoutInflater.from(requireContext())
            val modalDescription = inflater.inflate(R.layout.modal_description, null)
            alertDialogBuilder.setView(modalDescription)

            alertDialogBuilder.setTitle(R.string.label_description)

            val textView: TextView =
                modalDescription.findViewById(R.id.id_textview_description_detail)
            detail?.let { mediaDetail ->
                textView.setText(mediaDetail.description)
            }

            alertDialogBuilder.setNegativeButton("Fermer") { dialog, _ ->
                dialog.dismiss()
            }

            val modal = alertDialogBuilder.create()
            modal.show()
        }

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
            dateSortie =
                String.format(
                    "%02d/%02d/%04d",
                    dayOfMonth,
                    month + 1,
                    year
                )
            dateSelectionneeTextView.setText(getString(R.string.date_sortie) + " " + dateSortie)
            setVisibility(View.VISIBLE)
        }

        suppButton.setOnClickListener {
            if (!modifierButton.text.contains(getString(R.string.btn_4))) {
                initView()
                setEditable(false)
            } else {
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        detail?.let { mediaDetail ->
                            MainActivity.db.mediaDao().deleteByName(mediaDetail.nom)
                        }
                    }
                }
                Navigation.findNavController(requireView()).popBackStack()
                toastInfo(getString(R.string.succes_suppression))
            }
        }

        modifierButton.setOnClickListener {
            val isModifierText = modifierButton.text.contains(getString(R.string.btn_4))
            setEditable(isModifierText)

            if (isModifierText) {
                labelDescription.setText(getString(R.string.label_description))
                modifierButton.setText(getString(R.string.btn_5))
                suppButton.setText(getString(R.string.btn_6))
                setVisibility(View.VISIBLE)
            } else {
                val nom = nomEditText.text.toString()
                val num_saison = saison.text.toString().toIntOrNull()
                val num_episode = episode.text.toString().toIntOrNull()
                var message = ""

                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        if (nom.isBlank()) message = getString(R.string.erreur_nom)
                        else if (num_saison == null || num_episode == null)
                            message = getString(R.string.erreur_nombre)
                        else {
                            try {
                                detail?.let { mediaDetail ->
                                    MainActivity.db.mediaDao().updateByName(
                                        nom = nom,
                                        description = descriptionEditText.text.toString(),
                                        image = imageEditText.text.toString(),
                                        lien = lienEditText.text.toString(),
                                        media_statut = selectedText,
                                        num_saison = num_saison,
                                        num_episode = num_episode,
                                        date_sortie = dateSortie,
                                        ancienNom = mediaDetail.nom
                                    )
                                }
                                detail =
                                    MainActivity.db.mediaDao().getOne(nomEditText.text.toString())
                                message = getString(R.string.succes_operation)
                            } catch (e: Exception) {
                                Log.i("tata", e.message.toString())
                                message = getString(R.string.erreur_operation)
                            }
                        }
                    }
                    initView()
                    toastInfo(message)
                }
            }
        }
    }

    private fun toastInfo(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
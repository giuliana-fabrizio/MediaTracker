package com.example.mediatracker

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mediatracker.bdd.Media
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("DEPRECATION")
class ListeFragment : Fragment() {

    private lateinit var medias: List<Media>
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_liste, container, false)

        val page = arguments?.getString("media")
        if ((activity as AppCompatActivity).supportActionBar?.title.toString() == getString(R.string.label_vide))
            (activity as AppCompatActivity).supportActionBar?.title = page.toString()

        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            medias = withContext(Dispatchers.IO) {
                MainActivity.db.mediaDao()
                    .getAll((activity as AppCompatActivity).supportActionBar?.title.toString())
            }

            recyclerView = view.findViewById(R.id.id_recyclerview_liste)
            recyclerView.adapter = ListeAdaptateur(medias, this@ListeFragment)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_liste, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.id_liste_action_1 -> modalAjouter()
            R.id.id_liste_action_2 -> rechercherParNom(item)
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun modalAjouter() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val modalAjout = inflater.inflate(R.layout.fragment_detail, null)
        alertDialogBuilder.setView(modalAjout)

        val constraintLayout: ConstraintLayout = modalAjout.findViewById(R.id.id_constraint_layout)
        val nomEditText: EditText = modalAjout.findViewById(R.id.id_ajout_nom)
        val descriptionEditText: EditText = modalAjout.findViewById(R.id.id_ajout_description)

        val lienEditText: EditText = modalAjout.findViewById(R.id.id_ajout_lien)
        val lienButton: Button = modalAjout.findViewById(R.id.id_detail_btn_lien)

        val imageEditText: EditText = modalAjout.findViewById(R.id.id_ajout_image)
        val imageView: ImageView = modalAjout.findViewById(R.id.id_detail_img)

        val spinnerStatut: Spinner = modalAjout.findViewById(R.id.id_liste_statut)

        val constraintLayoutSaisonEpisode: ConstraintLayout =
            modalAjout.findViewById(R.id.id_constraint_layout_saison_episode)
        val saisonEditText: EditText = modalAjout.findViewById(R.id.id_ajout_saison)
        val episodeEditText: EditText = modalAjout.findViewById(R.id.id_ajout_episode)

        val dateSelectionneeTextView: TextView = modalAjout.findViewById(R.id.id_date_selectionnee)
        val btnDate: Button = modalAjout.findViewById(R.id.id_btn_ajout_date)
        val calendarView: CalendarView = modalAjout.findViewById(R.id.id_ajout_date)

        val constraintLayoutBtns: ConstraintLayout =
            modalAjout.findViewById(R.id.id_constraint_layout_btns)

        var selectedText = ""
        var dateSortie = ""

        constraintLayoutSaisonEpisode.visibility =
            if ((activity as AppCompatActivity).supportActionBar?.title.toString() == getString(R.string.onglet_2)) View.GONE else View.VISIBLE

        dateSelectionneeTextView.setText(getString(R.string.date_sortie))
        btnDate.setText(getString(R.string.date_sortie))

        var statuts = listOf("")
        lifecycleScope.launch {
            statuts = withContext(Dispatchers.IO) {
                MainActivity.db.statutDao().getAllStatut()
            }
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statuts)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerStatut.adapter = adapter
        }

        lienButton.visibility = View.GONE
        imageView.visibility = View.INVISIBLE
        calendarView.visibility = View.GONE
        constraintLayoutBtns.visibility = View.GONE

        spinnerStatut.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
            calendarView.visibility =
                if (calendarView.visibility == View.GONE) View.VISIBLE else View.GONE
            constraintLayout.visibility =
                if (calendarView.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            dateSortie = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            dateSelectionneeTextView.setText(getString(R.string.date_sortie) + " " + dateSortie)
            calendarView.visibility = View.GONE
            constraintLayout.visibility = View.VISIBLE
        }

        alertDialogBuilder.setPositiveButton(getString(R.string.btn_5)) { dialog, _ ->
            val nom = nomEditText.text.toString()
            val saison = saisonEditText.text.toString().toIntOrNull()
            val episode = episodeEditText.text.toString().toIntOrNull()

            if (nom.isBlank()) {
                Toast.makeText(requireContext(), getString(R.string.erreur_nom), Toast.LENGTH_SHORT)
                    .show()
                return@setPositiveButton
            }

            if (saison == null || episode == null) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.erreur_nombre),
                    Toast.LENGTH_SHORT
                ).show()
                return@setPositiveButton
            }

            val media = Media(
                nom = nom,
                description = descriptionEditText.text.toString(),
                image = imageEditText.text.toString(),
                lien = lienEditText.text.toString(),
                media_categorie = (activity as AppCompatActivity).supportActionBar?.title.toString(),
                media_statut = selectedText,
                num_saison = saison,
                num_episode = episode,
                date_sortie = dateSortie
            )

            lifecycleScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        MainActivity.db.mediaDao().apply {
                            insert(media)
                            medias =
                                getAll((activity as AppCompatActivity).supportActionBar?.title.toString())
                        }
                    }
                    recyclerView.adapter = ListeAdaptateur(medias, this@ListeFragment)

                    Toast.makeText(
                        requireContext(),
                        getString(R.string.succes_operation),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.erreur_operation),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        alertDialogBuilder.setNegativeButton(getString(R.string.btn_6)) { dialog, _ ->
            dialog.dismiss()
        }

        val modal = alertDialogBuilder.create()
        modal.show()
    }

    private fun rechercherParNom(item: MenuItem) {
        val searchView = item.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { performSearch(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { performSearch(it) }
                return true
            }
        })
    }

    private fun performSearch(query: String) {
        val filteredMedia = medias.filter { media ->
            media.nom.contains(query, true)
        }
        recyclerView.adapter = ListeAdaptateur(filteredMedia, this)
    }
}

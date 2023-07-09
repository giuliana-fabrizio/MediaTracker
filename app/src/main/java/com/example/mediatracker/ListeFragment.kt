package com.example.mediatracker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mediatracker.bdd.Media
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout

@Suppress("DEPRECATION")
class ListeFragment : Fragment() {

    private var fragment = this
    private lateinit var medias: List<Media>
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_liste, container, false)

        val page = arguments?.getString("media")
        if ((activity as AppCompatActivity).supportActionBar?.title.toString() == getString(R.string.label_vide))
            (activity as AppCompatActivity).supportActionBar?.title = page.toString()

        setHasOptionsMenu(true)

        medias = MainActivity.db.mediaDao()
            .getAll((activity as AppCompatActivity).supportActionBar?.title.toString())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.id_recyclerview_liste)
        recyclerView.adapter = ListeAdaptateur(medias, this)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_liste, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.id_liste_action_1 -> modal_ajouter()
            R.id.id_liste_action_2 -> rechercherParNom(item)
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("MissingInflatedId")
    private fun modal_ajouter() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val modalAjout = inflater.inflate(R.layout.fragment_detail, null)
        alertDialogBuilder.setView(modalAjout)

        val constraintLayout: ConstraintLayout =
            modalAjout.findViewById(R.id.id_constraint_layout)
        val nomEditText: EditText = modalAjout.findViewById(R.id.id_ajout_nom)
        val descriptionEditText: EditText = modalAjout.findViewById(R.id.id_ajout_description)
        val imageEditText: EditText = modalAjout.findViewById(R.id.id_ajout_image)
        val lienEditText: EditText = modalAjout.findViewById(R.id.id_ajout_lien)
        val spinnerStatut: Spinner = modalAjout.findViewById(R.id.id_liste_statut)
        val saisonEditText: EditText = modalAjout.findViewById(R.id.id_ajout_saison)
        val episodeEditText: EditText = modalAjout.findViewById(R.id.id_ajout_episode)
        val btnDate: Button = modalAjout.findViewById(R.id.id_btn_ajout_date)
        val dateSelectionneeTextView: TextView = modalAjout.findViewById(R.id.id_date_selectionnee)
        val calendarView: CalendarView = modalAjout.findViewById(R.id.id_ajout_date)

        var selectedText = ""
        val statuts = MainActivity.db.statutDao().getAllStatut()

        if ((activity as AppCompatActivity).supportActionBar?.title.toString() == getString(R.string.onglet_2)) {
            val constraintLayoutSaisonEpisode: ConstraintLayout =
                modalAjout.findViewById(R.id.id_constraint_layout_saison_episode)
            constraintLayoutSaisonEpisode.visibility = View.GONE
        }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statuts)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatut.adapter = adapter
        calendarView.visibility = View.GONE

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
            val formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            dateSelectionneeTextView.text = formattedDate
            calendarView.visibility = View.GONE
            constraintLayout.visibility = View.VISIBLE
        }

        alertDialogBuilder.setPositiveButton(getString(R.string.btn_5)) { dialog, _ ->
            val media = Media(
                nom = nomEditText.text.toString(),
                description = descriptionEditText.text.toString(),
                image = imageEditText.text.toString(),
                lien = lienEditText.text.toString(),
                media_categorie = (activity as AppCompatActivity).supportActionBar?.title.toString(),
                media_statut = selectedText,
                num_saison = saisonEditText.text.toString().toInt(),
                num_episode = episodeEditText.text.toString().toInt()
            )

            try {
                MainActivity.db.mediaDao().apply {
                    insert(media)
                    medias =
                        getAll((activity as AppCompatActivity).supportActionBar?.title.toString())
                }
                recyclerView.adapter = ListeAdaptateur(medias, fragment)

                Toast.makeText(
                    fragment.requireContext(),
                    getString(R.string.succes_operation),
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Toast.makeText(
                    fragment.requireContext(),
                    getString(R.string.erreur_operation),
                    Toast.LENGTH_SHORT
                ).show()
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
                if (query != null) {
                    val filteredMedia = medias.filter { media ->
                        media.nom.equals(query, true)
                    }
                    recyclerView.adapter = ListeAdaptateur(filteredMedia, fragment)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    val filteredMedia = medias.filter { media ->
                        media.nom.contains(newText, true)
                    }
                    recyclerView.adapter = ListeAdaptateur(filteredMedia, fragment)
                }
                return true
            }
        })
    }
}
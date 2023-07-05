package com.example.mediatracker

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mediatracker.bdd.Media
import com.example.mediatracker.bdd.MediaDao
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout

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
        Log.i("tata", medias.toString())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.id_recyclerview_liste)
        recyclerView.adapter = ListeAdaptateur(medias, this)
    }

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

    private fun modal_ajouter() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val modalAjout = inflater.inflate(R.layout.modal_ajout, null)
        alertDialogBuilder.setView(modalAjout)

        val constraintLayout: ConstraintLayout =
            modalAjout.findViewById(R.id.id_ConstraintLayout_modal)
        val nomEditText: EditText = modalAjout.findViewById(R.id.id_ajout_nom)
        val descriptionEditText: EditText = modalAjout.findViewById(R.id.id_ajout_description)
        val imageEditText: EditText = modalAjout.findViewById(R.id.id_ajout_image)
        val lienEditText: EditText = modalAjout.findViewById(R.id.id_ajout_lien)
        val spinnerStatut: Spinner = modalAjout.findViewById(R.id.id_liste_statut)
        var selectedText = ""
        val saisonEditText: EditText = modalAjout.findViewById(R.id.id_ajout_saison)
        val episodeEditText: EditText = modalAjout.findViewById(R.id.id_ajout_episode)
        val btnDate: Button = modalAjout.findViewById(R.id.id_btn_ajout_date)
        val dateSelectionneeTextView: TextView = modalAjout.findViewById(R.id.id_date_selectionnee)
        val calendarView: CalendarView = modalAjout.findViewById(R.id.id_ajout_date)

        val statuts = MainActivity.db.statutDao().getAllStatut()
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
            dateSelectionneeTextView.text = "$formattedDate"
            calendarView.visibility = View.GONE
            constraintLayout.visibility = View.VISIBLE
        }

        alertDialogBuilder.setPositiveButton("Valider ✅") { dialog, _ ->
//            dialog.dismiss()
            val media = Media(
                nom = nomEditText.text.toString(),
                description = descriptionEditText.text.toString(),
                image = imageEditText.text.toString(),
                lien = lienEditText.text.toString(),
                media_categorie = (activity as AppCompatActivity).supportActionBar?.title.toString(),
                media_statut = selectedText
            )

            MainActivity.db.mediaDao().apply {
                insert(media)
                medias = getAll((activity as AppCompatActivity).supportActionBar?.title.toString())
            }
            recyclerView.adapter = ListeAdaptateur(medias, fragment)

            Toast.makeText(
                fragment.requireContext(),
                "Ajout réussi ! 🧸",
                Toast.LENGTH_SHORT
            ).show()
        }

        alertDialogBuilder.setNegativeButton("Annuler ❌") { dialog, _ ->
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
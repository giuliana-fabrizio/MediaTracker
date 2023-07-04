package com.example.mediatracker

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mediatracker.bdd.Media
import com.example.mediatracker.bdd.MediaDao
import androidx.appcompat.widget.SearchView

class ListeFragment : Fragment() {

    private var fragment = this
    private lateinit var medias: List<Media>
    private lateinit var mediaDao: MediaDao
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

        mediaDao = MainActivity.db.mediaDao()
        medias = mediaDao.getAll((activity as AppCompatActivity).supportActionBar?.title.toString())

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

        val dateSelectionneeTextView: TextView = modalAjout.findViewById(R.id.id_date_selectionnee)
        val btnDate: Button = modalAjout.findViewById(R.id.id_btn_ajout_date)
        val calendarView: CalendarView = modalAjout.findViewById(R.id.id_ajout_date)
        val spinnerStatut: Spinner = modalAjout.findViewById(R.id.spinnerStatut)

        calendarView.visibility = View.GONE
        btnDate.setOnClickListener {
            calendarView.visibility = if (calendarView.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            dateSelectionneeTextView.text = "Date sélectionnée : $formattedDate"
            calendarView.visibility = View.GONE // Ajout de cette ligne pour masquer le calendrier après la sélection
        }

        val statuts = arrayOf("Option 1", "Option 2", "Option 3")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statuts)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerStatut.adapter = adapter

        alertDialogBuilder.setPositiveButton("Valider ✅") { dialog, _ ->
            dialog.dismiss()
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
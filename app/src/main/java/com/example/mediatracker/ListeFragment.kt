package com.example.mediatracker

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.CalendarView
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

        val alert_dialog = AlertDialog.Builder(requireContext())

        val inflater = LayoutInflater.from(requireContext())
        val modal_ajout = inflater.inflate(R.layout.modal_ajout, null)
        alert_dialog.setView(modal_ajout)

        val btn_date: Button = modal_ajout.findViewById(R.id.id_btn_ajout_date)
        btn_date.setOnClickListener {
            Log.i("tata", "tata")
            val calendarView: CalendarView = modal_ajout.findViewById(R.id.id_ajout_date)
            Log.i("tata", calendarView.visibility.toString())
            calendarView.visibility = View.VISIBLE
            Log.i("tata", calendarView.visibility.toString())
        }

        alert_dialog.setPositiveButton("Valider ✅") { dialog, which ->
            dialog.dismiss()
        }

        alert_dialog.setNegativeButton("Annuler ❌") { dialog, which ->
            dialog.dismiss()
        }

        val modal = alert_dialog.create()
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
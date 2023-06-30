package com.example.mediatracker

import android.os.Bundle
import android.util.Log
import android.view.*
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
            R.id.id_liste_action_1 -> Log.i("tata", "Ajouter")
            R.id.id_liste_action_2 -> {
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
        return super.onOptionsItemSelected(item)
    }
}
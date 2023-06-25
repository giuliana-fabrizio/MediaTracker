package com.example.mediatracker

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mediatracker.bdd.Media
import com.example.mediatracker.bdd.MediaDao

class ListeFragment : Fragment() {

    private lateinit var medias: List<Media>
    private lateinit var mediaDao: MediaDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var page = arguments?.getString("media")
        if ((activity as AppCompatActivity).supportActionBar?.title.toString() == getString(R.string.label_vide))
            (activity as AppCompatActivity).supportActionBar?.title = page.toString()

        mediaDao = MainActivity.db.mediaDao()

        medias = mediaDao.getAll((activity as AppCompatActivity).supportActionBar?.title.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_liste, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var textView: TextView = view.findViewById(R.id.todo_test)
        textView.text = medias.toString()
        Log.i("database successfully", medias.toString())
    }
}
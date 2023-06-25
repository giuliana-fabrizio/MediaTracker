package com.example.mediatracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.room.Room
import com.example.mediatracker.bdd.AppDatabase
import com.example.mediatracker.bdd.Categorie
import com.example.mediatracker.bdd.Media
import com.example.mediatracker.bdd.Statut
import com.example.mediatracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController

    companion object {
        lateinit var db: AppDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout
        navController = this.findNavController(R.id.navHostFragment)
        NavigationUI.setupWithNavController(binding.menuView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "media_tracker_bdd"
        ).allowMainThreadQueries().build()

// ================================================================================================= insert

        db.categorieDao().deleteTable()
        db.mediaDao().deleteTable()
        db.statutDao().deleteTable()

        var categories: List<Categorie> = listOf(
            Categorie(1, getString(R.string.onglet_1)),
            Categorie(2, getString(R.string.onglet_2)),
            Categorie(3, getString(R.string.onglet_3))
        )
        for (categorie: Categorie in categories)
            db.categorieDao().insert(categorie)

        Log.i("BDD", db.categorieDao().getAll().toString())

        var statuts: List<Statut> = listOf(
            Statut(1, getString(R.string.statut_1)),
            Statut(2, getString(R.string.statut_2)),
            Statut(3, getString(R.string.statut_3)),
            Statut(4, getString(R.string.statut_4))
        )
        for (statut: Statut in statuts)
            db.statutDao().insert(statut)

        var medias: List<Media> = listOf(
            Media(1, "TUTU", "", "", 1, 1),
            Media(2, "TATA", "", "", 2, 1),
            Media(3, "TOTO", "", "", 1, 4)
        )
        for (media: Media in medias)
            db.mediaDao().insert(media)

        Log.i("BDD", db.mediaDao().getAll(getString(R.string.onglet_1)).toString())
    }
}

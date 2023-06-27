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
import com.example.mediatracker.bdd.*
import com.example.mediatracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController

    companion object {
        lateinit var db: AppDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeDatabase() // Appel à la fonction d'initialisation de la base de données
        insertData()
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout
        navController = findNavController(R.id.navHostFragment)
        NavigationUI.setupWithNavController(binding.menuView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)


        Log.d("initializeDatabase", "Bob")
        Log.d("insertData", "g,ksjgkgglglfgj,fslkd,gld")

    }

    private fun initializeDatabase() {
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "media_tracker_bdd"
        ).allowMainThreadQueries().build()

        Log.d("MainActivity", "Database initialized successfully")
    }

    private fun insertData() {
        db.runInTransaction {
            db.categorieDao().apply {
                deleteTable()
                insert(Categorie(1, getString(R.string.onglet_1)))
                insert(Categorie(2, getString(R.string.onglet_2)))
                insert(Categorie(3, getString(R.string.onglet_3)))
                Log.i("BDD", getAll().toString())

            }

            db.statutDao().apply {
                deleteTable()
                insert(Statut(1, getString(R.string.statut_1)))
                insert(Statut(2, getString(R.string.statut_2)))
                insert(Statut(3, getString(R.string.statut_3)))
                insert(Statut(4, getString(R.string.statut_4)))
            }

            db.mediaDao().apply {
                deleteTable()
                insert(
                    Media(
                        1,
                        "TUTU",
                        "",
                        "https://th.bing.com/th/id/OIP.eTzI95wbSwa2eRRkD1GNGAHaHa?pid=ImgDet&rs=1",
                        "",
                        1,
                        1
                    )
                )
                insert(
                    Media(
                        2,
                        "TATA",
                        "",
                        "https://th.bing.com/th/id/OIP.eTzI95wbSwa2eRRkD1GNGAHaHa?pid=ImgDet&rs=1",
                        "https://th.bing.com/th/id/OIP.eTzI95wbSwa2eRRkD1GNGAHaHa?pid=ImgDet&rs=1",
                        2,
                        1
                    )
                )
                insert(
                    Media(
                        3,
                        "TOTO",
                        "",
                        "https://th.bing.com/th/id/OIP.eTzI95wbSwa2eRRkD1GNGAHaHa?pid=ImgDet&rs=1",
                        "",
                        1,
                        4
                    )
                )
                Log.i("BDD", getAll(getString(R.string.onglet_1)).toString())
            }

            db.saisonDao().apply {
                deleteTable()
                insert(Saison(1))
                insert(Saison(2))
                insert(Saison(3))
            }

            db.episodeDao().apply {
                deleteTable()
                insert(Episode(10, 1))
                insert(Episode(50, 3))
                insert(Episode(40, 2))
            }

            db.mediaSaisonDao().apply {
                deleteTable()
                insert(MediaSaison(1, 1))
                insert(MediaSaison(2, 3))
                insert(MediaSaison(3, 2))
            }
        }
    }
}
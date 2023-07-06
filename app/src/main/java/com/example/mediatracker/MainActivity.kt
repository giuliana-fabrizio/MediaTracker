package com.example.mediatracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        super.onCreate(savedInstanceState)

        initializeDatabase()
        insertData()

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout
        navController = this.findNavController(R.id.navHostFragment)
        NavigationUI.setupWithNavController(binding.menuView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    private fun initializeDatabase() {
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "media_tracker_bdd"
        ).allowMainThreadQueries().build()
    }

    private fun insertData() {
        db.runInTransaction {
            db.categorieDao().apply {
                deleteTable()
                insert(Categorie(getString(R.string.onglet_1)))
                insert(Categorie(getString(R.string.onglet_2)))
                insert(Categorie(getString(R.string.onglet_3)))
            }

            db.statutDao().apply {
                deleteTable()
                insert(Statut(getString(R.string.statut_1)))
                insert(Statut(getString(R.string.statut_2)))
                insert(Statut(getString(R.string.statut_3)))
                insert(Statut(getString(R.string.statut_4)))
            }

            db.mediaDao().apply {
                deleteTable()
                insert(
                    Media(
                        "TUTU",
                        "TUTU est partie en vacances à Turturfure",
                        "https://th.bing.com/th/id/R.dcc2534c011e5ab3bd07395bc25d26f1?rik=uvGyPLj%2f6TD6CA&pid=ImgRaw&r=0",
                        "https://th.bing.com/th/id/R.dcc2534c011e5ab3bd07395bc25d26f1?rik=uvGyPLj%2f6TD6CA&pid=ImgRaw&r=0",
                        getString(R.string.onglet_1),
                        getString(R.string.statut_1),
                    )
                )
                insert(
                    Media(
                        "TATA",
                        "",
                        "",
                        "https://th.bing.com/th/id/OIP.eTzI95wbSwa2eRRkD1GNGAHaHa?pid=ImgDet&rs=1",
                        getString(R.string.onglet_2),
                        getString(R.string.statut_1)
                    )
                )
                insert(
                    Media(
                        "TOTO",
                        "",
                        "https://i.pinimg.com/originals/cd/91/7f/cd917ffff15a31088e3b385399c84e96.jpg",
                        "",
                        getString(R.string.onglet_1),
                        getString(R.string.statut_4),
                    )
                )
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
                insert(MediaSaison("TUTU", 1))
                insert(MediaSaison("TATA", 3))
                insert(MediaSaison("TOTO", 2))
            }
        }
    }
}
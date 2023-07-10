package com.example.mediatracker

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.room.Room
import com.example.mediatracker.bdd.*
import com.example.mediatracker.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        lateinit var db: AppDatabase
        const val PREFS_NAME = "MediaPrefs"
        const val KEY_DATA_SAVED = "DataSaved"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val dataSaved = sharedPreferences.getBoolean(KEY_DATA_SAVED, false)

        initializeDatabase(dataSaved)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout
        navController = findNavController(R.id.navHostFragment)
        NavigationUI.setupWithNavController(binding.menuView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        navigation()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val currentDestination = navController.currentDestination?.id

            if (currentDestination == R.id.detailFragment) {
                navController.popBackStack()
            } else if (currentDestination != R.id.accueilFragment) {
                navController.navigate(R.id.accueilFragment)
            } else {
                finishAffinity()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            return true
        }

        val navController = findNavController(R.id.navHostFragment)
        val currentDestination = navController.currentDestination?.id

        if (currentDestination != R.id.accueilFragment) {
            navController.popBackStack(R.id.accueilFragment, false)
            return true
        }

        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPreferences.edit().putBoolean(KEY_DATA_SAVED, true).apply()
    }

    private fun initializeDatabase(dataSaved: Boolean) {
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "media_tracker_bdd"
        ).build()

        if (!dataSaved) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
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
                }
                sharedPreferences.edit().putBoolean(KEY_DATA_SAVED, true).apply()
            }
        }
    }

    private fun navigation() {
        val navView: NavigationView = findViewById(R.id.menuView)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.accueilFragment -> {
                    navController.navigate(R.id.accueilFragment)
                    true
                }
                R.id.animesFragment -> {
                    navController.navigate(R.id.animesFragment)
                    true
                }
                R.id.filmsFragment -> {
                    navController.navigate(R.id.filmsFragment)
                    true
                }
                R.id.seriesFragment -> {
                    navController.navigate(R.id.seriesFragment)
                    true
                }
                else -> false
            }
        }
    }
}
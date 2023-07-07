package com.example.mediatracker.bdd

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Categorie::class,
        Media::class,
        Statut::class
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categorieDao(): CategorieDao
    abstract fun mediaDao(): MediaDao
    abstract fun statutDao(): StatutDao
}
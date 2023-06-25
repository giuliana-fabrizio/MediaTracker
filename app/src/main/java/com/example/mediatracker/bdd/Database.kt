package com.example.mediatracker.bdd

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Categorie::class, Episode::class, Media::class, Saison::class, Statut::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categorieDao(): CategorieDao
    abstract fun episodeDao(): EpisodeDao
    abstract fun mediaDao(): MediaDao
    abstract fun statutDao(): StatutDao
}
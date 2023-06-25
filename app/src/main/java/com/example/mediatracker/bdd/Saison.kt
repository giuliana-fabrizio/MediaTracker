package com.example.mediatracker.bdd

import androidx.room.Entity
import androidx.room.*

@Entity
data class Saison(
    @PrimaryKey (autoGenerate = false) val num_saison: Int
)

@Dao
interface SaisonDao {

}
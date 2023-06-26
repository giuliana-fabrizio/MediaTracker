package com.example.mediatracker.bdd

import androidx.room.Entity
import androidx.room.*

@Entity
data class Saison(
    @PrimaryKey (autoGenerate = false) val num_saison: Int
)

@Dao
interface SaisonDao {
    @Query("SELECT * FROM saison")
    fun getAll(): List<Saison>

    @Insert
    fun insert(saison: Saison)

    @Query("DELETE FROM saison")
    fun deleteTable()
}
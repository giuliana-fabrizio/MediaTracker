package com.example.mediatracker.bdd

import androidx.room.*

@Entity
data class Statut(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "libelle_statut")
    val libelle_statut: String
)

@Dao
interface StatutDao {

    @Query("SELECT * FROM statut")
    fun getAllStatut(): List<String>

    @Insert
    fun insert(statut: Statut)

    @Query("DELETE FROM statut")
    fun deleteTable()
}
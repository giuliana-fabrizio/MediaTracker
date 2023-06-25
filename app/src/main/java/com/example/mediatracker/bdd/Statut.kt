package com.example.mediatracker.bdd

import androidx.room.*

@Entity
data class Statut(
    @PrimaryKey(autoGenerate = true) val id_statut: Int,
    @ColumnInfo(name = "libelle_statut") val libelle_statut: String
)

@Dao
interface StatutDao {

    @Insert
    fun insert(statut: Statut)

    @Query("DELETE FROM statut")
    fun deleteTable()
}
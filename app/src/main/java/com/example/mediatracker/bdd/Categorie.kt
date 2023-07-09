package com.example.mediatracker.bdd

import androidx.room.*

@Entity
data class Categorie(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "libelle_categorie")
    val libelle_categorie: String
)

@Dao
interface CategorieDao {

    @Insert
    fun insert(categorie: Categorie)

    @Query("DELETE FROM categorie")
    fun deleteTable()
}
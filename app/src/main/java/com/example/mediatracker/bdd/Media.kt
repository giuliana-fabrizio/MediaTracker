package com.example.mediatracker.bdd

import androidx.room.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = Categorie::class,
        parentColumns = ["id_categorie"],
        childColumns = ["media_categorie"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Statut::class,
        parentColumns = ["id_statut"],
        childColumns = ["media_statut"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Media(
    @PrimaryKey(autoGenerate = true) val id_media: Int,
    @ColumnInfo(name = "nom") val nom: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "lien") val lien: String,
    @ColumnInfo(name = "media_categorie") val media_categorie: Int,
    @ColumnInfo(name = "media_statut") val media_statut: Int,
)

@Dao
interface MediaDao {

    @Query(
        "SELECT * FROM media WHERE media_categorie IN " +
                "(SELECT id_categorie FROM Categorie WHERE libelle_categorie like :categorie)"
    )
    fun getAll(categorie: String): List<Media>

    @Query("SELECT * FROM media WHERE nom LIKE :nom_media")
    fun findByName(nom_media: String): Media

    @Insert
    fun insert(media: Media)

    @Update
    fun update(media: Media)

    @Query(
        "UPDATE media SET description = :newDescription, lien = :newLink, image = :newImg, " +
                "nom = :newName WHERE nom = :lastName"
    )
    fun updateByName(
        newDescription: String,
        newLink: String,
        newImg: String,
        newName: String,
        lastName: String
    )

    @Delete
    fun delete(media: Media)

    @Query("DELETE from media WHERE nom = :name")
    fun deleteByName(name: String)

    @Query("DELETE FROM media")
    fun deleteTable()
}
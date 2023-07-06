package com.example.mediatracker.bdd

import androidx.room.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = Categorie::class,
        parentColumns = ["libelle_categorie"],
        childColumns = ["media_categorie"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Statut::class,
        parentColumns = ["libelle_statut"],
        childColumns = ["media_statut"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Media(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "nom") val nom: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "lien") val lien: String,
    @ColumnInfo(name = "media_categorie") val media_categorie: String,
    @ColumnInfo(name = "media_statut") val media_statut: String
)

@Dao
interface MediaDao {

    @Query(
        "SELECT * FROM media WHERE media_categorie like :categorie"
    )
    fun getAll(categorie: String): List<Media>

    @Query(
        "SELECT media.* " +
                "FROM media " +
                "INNER JOIN `statut` ON `statut`.libelle_statut = media.media_statut " +
                "INNER JOIN mediaSaison ON mediaSaison.mediaNom = media.nom " +
                "INNER JOIN episode ON episode.episode_saison = mediaSaison.saisonNum " +
                "WHERE media.nom LIKE :nom"
    )
    fun getOne(nom: String): Media

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
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
    @ColumnInfo(name = "media_statut") val media_statut: String,
    @ColumnInfo(name = "num_saison") val num_saison: Int?,
    @ColumnInfo(name = "num_episode") val num_episode: Int?,
    @ColumnInfo(name = "date_sortie") val date_sortie: String?
)

data class MediaDetail(
    val nom: String,
    val description: String,
    val image: String,
    val lien: String,
    val media_categorie: String,
    val media_statut: String,
    val libelle_statut: String,
    val num_saison: Int,
    val num_episode: Int,
    val date_sortie: String
)

@Dao
interface MediaDao {

    @Query(
        "SELECT * FROM media WHERE media_categorie like :categorie"
    )
    fun getAll(categorie: String): List<Media>

    @Query(
        "SELECT * " +
                "FROM media " +
                "INNER JOIN categorie ON media.media_categorie = categorie.libelle_categorie " +
                "INNER JOIN statut ON media.media_statut = statut.libelle_statut " +
                "WHERE media.nom LIKE :nom"
    )
    fun getOne(nom: String): MediaDetail

    @Insert
    fun insert(media: Media)

    @Query(
        "UPDATE media SET " +
                "nom = :nom, " +
                "description = :description, " +
                "image = :image, " +
                "lien = :lien, " +
                "media_statut = :media_statut, " +
                "num_saison = :num_saison, " +
                "num_episode = :num_episode, " +
                "date_sortie = :date_sortie " +
                "WHERE nom LIKE :ancienNom "
    )
    fun updateByName(
        nom: String,
        description: String,
        image: String,
        lien: String,
        media_statut: String,
        num_saison: Int?,
        num_episode: Int?,
        date_sortie: String?,
        ancienNom: String
    )

    @Query("DELETE from media WHERE nom = :name")
    fun deleteByName(name: String)

    @Query("DELETE FROM media")
    fun deleteTable()
}
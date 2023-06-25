package com.example.mediatracker.bdd

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    primaryKeys = ["mediaId", "saisonNum"],
    foreignKeys = [ForeignKey(
        entity = Media::class,
        parentColumns = ["id_media"],
        childColumns = ["mediaId"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Saison::class,
        parentColumns = ["num_saison"],
        childColumns = ["saisonNum"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class MediaSaison(
    val mediaId: Int,
    val saisonNum: Int
)

@Dao
interface MediaSaisonDao {
    // Ajoutez vos méthodes DAO ici pour effectuer des opérations sur la table MediaSaison
}
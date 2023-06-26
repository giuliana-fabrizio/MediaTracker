package com.example.mediatracker.bdd

import androidx.room.*

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
    @Insert
    fun insert(mediaSaison: MediaSaison)

    @Query("DELETE FROM mediaSaison")
    fun deleteTable()
}
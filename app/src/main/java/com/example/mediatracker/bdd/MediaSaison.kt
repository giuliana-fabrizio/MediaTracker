package com.example.mediatracker.bdd

import androidx.room.*

@Entity(
    primaryKeys = ["mediaNom", "saisonNum"],
    foreignKeys = [ForeignKey(
        entity = Media::class,
        parentColumns = ["nom"],
        childColumns = ["mediaNom"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Saison::class,
        parentColumns = ["num_saison"],
        childColumns = ["saisonNum"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class MediaSaison(
    val mediaNom: String,
    val saisonNum: Int
)

@Dao
interface MediaSaisonDao {
    @Insert
    fun insert(mediaSaison: MediaSaison)

    @Query("DELETE FROM mediaSaison")
    fun deleteTable()
}
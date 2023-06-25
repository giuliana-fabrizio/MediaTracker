package com.example.mediatracker.bdd

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Media::class,
        parentColumns = ["id_media"],
        childColumns = ["mediaId"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Statut::class,
        parentColumns = ["num_saison"],
        childColumns = ["saisonNum"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Media_Saison(
    @PrimaryKey(autoGenerate = false) val mediaId: Int,
    @PrimaryKey(autoGenerate = false) val saisonNum: Int
)
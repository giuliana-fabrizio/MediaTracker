package com.example.mediatracker.bdd

import androidx.room.*


@Entity(
    foreignKeys = [ForeignKey(
        entity = Saison::class,
        parentColumns = ["num_saison"],
        childColumns = ["episode_saison"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Episode(
    @PrimaryKey(autoGenerate = false) val num_episode: Int,
    @ColumnInfo(name = "episode_saison") val episode_saison: Int,
)

@Dao
interface EpisodeDao {

    @Query(
        "SELECT * FROM episode"
    )
    fun getAll(): List<Episode>

    @Insert
    fun insert(episode: Episode)

    @Query("DELETE FROM Episode")
    fun deleteTable()
}
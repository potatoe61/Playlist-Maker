package com.example.playlistmaker.media.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = PLAYLIST_TABLE)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val imageUri: String,
    val tracksId: String,
    val tracksCount: Int,
    val timestamp: Long,
)

const val PLAYLIST_TABLE = "playlist_table"
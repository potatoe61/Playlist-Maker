package com.example.playlistmaker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.player.data.db.PlaylistDao
import com.example.playlistmaker.player.data.db.PlaylistEntity

@Database(version = 2, entities = [TrackFavoriteEntity::class, PlaylistEntity::class, TrackEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao
    abstract fun trackDaoFavorite(): TrackFavoriteDao
    abstract fun playlistDao(): PlaylistDao
}
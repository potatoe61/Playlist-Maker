package com.example.playlistmaker.media.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface TrackFavoriteDao {

    @Insert(entity = TrackFavoriteEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackFavoriteEntity)

    @Delete
    suspend fun deleteTrack(track: TrackFavoriteEntity)

    @Query("SELECT * FROM $FAVORITE_TABLE ORDER BY timestamp DESC")
    suspend fun getTracks(): List<TrackFavoriteEntity>

    @Query("SELECT id FROM $FAVORITE_TABLE")
    suspend fun getTracksId(): List<Int>

    @Query("SELECT COUNT(*) FROM $FAVORITE_TABLE WHERE id = :trackId")
    suspend fun isTrackFavorite(trackId: Int): Boolean

}
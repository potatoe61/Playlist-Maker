package com.example.playlistmaker.media


import android.content.Context
import android.content.res.Configuration
import android.util.TypedValue
import com.example.playlistmaker.media.data.db.PlaylistEntity
import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.data.db.TrackFavoriteEntity
import com.example.playlistmaker.playlist.model.Playlist
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.model.Track
import java.util.Locale


fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
    ).toInt()
}

fun Track.toDto(): TrackDto = TrackDto(
    trackName,
    artistName,
    trackTimeMillis,
    artworkUrl100,
    trackId,
    collectionName,
    releaseDate,
    primaryGenreName,
    country,
    previewUrl
)

fun TrackDto.toDomain(): Track = Track(
    trackName,
    artistName,
    trackTimeMillis,
    artworkUrl100,
    trackId,
    collectionName,
    releaseDate,
    primaryGenreName,
    country,
    previewUrl,
    isFavorite = false
)

fun Track.toFavoriteEntity(): TrackFavoriteEntity = TrackFavoriteEntity(
    id = trackId,
    trackName = trackName,
    artistName = artistName,
    trackTimeMillis = trackTimeMillis,
    artworkUrl100 = artworkUrl100,
    collectionName = collectionName ?: "",
    releaseDate = releaseDate ?: "",
    primaryGenreName = primaryGenreName,
    country = country,
    previewUrl = previewUrl,
    timestamp = System.currentTimeMillis()
)

fun TrackFavoriteEntity.toTracksDomain(): Track = Track(
    trackName = trackName,
    artistName = artistName,
    trackTimeMillis = trackTimeMillis,
    artworkUrl100 = artworkUrl100,
    trackId = id,
    collectionName = collectionName,
    releaseDate = releaseDate,
    primaryGenreName = primaryGenreName,
    country = country,
    previewUrl = previewUrl,
    isFavorite = true
)
fun getLocalizedContext(base: Context, locale: Locale): Context {
    val config = Configuration(base.resources.configuration)
    config.setLocale(locale)
    return base.createConfigurationContext(config)
}
fun Playlist.toPlaylistEntity(): PlaylistEntity = PlaylistEntity(
    id = id,
    name = name,
    description = description,
    imageUri = imageUri,
    tracksId = tracksId,
    tracksCount = tracksCount,
    timestamp = System.currentTimeMillis()
)

fun PlaylistEntity.toPlaylistDomain(): Playlist = Playlist(
    id = id,
    name = name,
    description = description,
    imageUri = imageUri,
    tracksId = tracksId,
    tracksCount = tracksCount,
)

fun Track.toTrackEntity(): TrackEntity = TrackEntity(
    id = trackId,
    trackName = trackName,
    artistName = artistName,
    trackTimeMillis = trackTimeMillis,
    artworkUrl100 = artworkUrl100,
    collectionName = collectionName ?: "",
    releaseDate = releaseDate ?: "",
    primaryGenreName = primaryGenreName,
    country = country,
    previewUrl = previewUrl,
    timestamp = System.currentTimeMillis()
)
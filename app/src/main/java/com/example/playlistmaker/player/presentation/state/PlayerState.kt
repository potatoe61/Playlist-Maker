package com.example.playlistmaker.player.presentation.state

data class PlayerState (
    val trackId:String = "",
    val trackName:String= "",
    val country:String= "",
    val releaseDate:String= "",
    val collectionName:String= "",
    val primaryGenreName:String= "",
    val artistName:String= "",
    val trackTimeMillis:Long= 0L,
    val artworkUrl100:String= "",
    val currentPosition : String= "00:00"
)
enum class PlayerState2 {
    STATE_PLAYING,
    STATE_PAUSED,
    STATE_PREPARED,
    STATE_DEFAULT
}
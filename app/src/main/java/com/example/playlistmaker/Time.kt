package com.example.playlistmaker

import java.text.SimpleDateFormat
import java.util.*

object Time {
    fun millisToStrFormat(millis: Int): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60)) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}
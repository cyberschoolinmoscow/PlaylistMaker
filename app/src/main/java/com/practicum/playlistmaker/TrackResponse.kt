package com.practicum.playlistmaker

class TrackResponse(
    val searchType: String,
    val expression: String,
    val results: List<Track>
)
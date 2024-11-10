package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.domain.models.Track

class TrackResponse(
    val searchType: String,
    val expression: String,
    val results: List<Track>
) : Response()
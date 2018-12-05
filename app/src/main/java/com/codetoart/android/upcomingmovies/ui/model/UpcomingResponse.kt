package com.codetoart.android.upcomingmovies.ui.model

data class UpcomingResponse(
    val results: List<Movie>,
    val page: Int
)
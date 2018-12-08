package com.codetoart.android.upcomingmovies.data.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

data class UpcomingMovieResponse(

    val results: List<Movie>,

    val page: Int,

    @SerializedName("total_pages")
    @JsonProperty("total_pages")
    val totalPages: Int
)
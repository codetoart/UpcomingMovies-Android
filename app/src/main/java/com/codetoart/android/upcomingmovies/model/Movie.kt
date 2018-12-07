package com.codetoart.android.upcomingmovies.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

data class Movie(

    val id: Int,

    val title: String,

    @SerializedName("release_date")
    @JsonProperty("release_date")
    val releaseDate: String,

    val adult: Boolean
)
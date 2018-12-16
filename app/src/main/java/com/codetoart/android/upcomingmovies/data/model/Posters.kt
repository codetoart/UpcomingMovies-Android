package com.codetoart.android.upcomingmovies.data.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

data class Posters(

    @SerializedName("file_path")
    @JsonProperty("file_path")
    val filePath: String?
)
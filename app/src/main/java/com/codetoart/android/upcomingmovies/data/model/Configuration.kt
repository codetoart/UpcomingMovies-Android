package com.codetoart.android.upcomingmovies.data.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

data class Configuration(

    val images: Images,

    @JsonProperty("change_keys")
    @SerializedName("change_keys")
    val changeKeys: List<String>
)
package com.codetoart.android.upcomingmovies.data.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

data class Configuration(

    val images: Images,

    @JsonProperty("change_keys")
    @SerializedName("change_keys")
    val changeKeys: List<String>
) {

    data class Images(

        @JsonProperty("base_url")
        @SerializedName("base_url")
        val baseUrl: String,

        @JsonProperty("secure_base_url")
        @SerializedName("secure_base_url")
        val secureBaseUrl: String,

        @JsonProperty("backdrop_sizes")
        @SerializedName("backdrop_sizes")
        val backdropSizes: List<String>,

        @JsonProperty("logo_sizes")
        @SerializedName("logo_sizes")
        val logoSizes: List<String>,

        @JsonProperty("poster_sizes")
        @SerializedName("poster_sizes")
        val posterSizes: List<String>,

        @JsonProperty("profile_sizes")
        @SerializedName("profile_sizes")
        val profileSizes: List<String>,

        @JsonProperty("still_sizes")
        @SerializedName("still_sizes")
        val stillSizes: List<String>
    )
}
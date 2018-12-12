package com.codetoart.android.upcomingmovies.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

@Entity(tableName = "upcoming_movies", indices = [Index(value = ["id"], unique = true)])
data class Movie(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "row_id")
    val rowId: Int,

    val id: Int,

    val title: String,

    @SerializedName("release_date")
    @JsonProperty("release_date")
    @ColumnInfo(name = "release_date")
    val releaseDate: String,

    val adult: Boolean
)
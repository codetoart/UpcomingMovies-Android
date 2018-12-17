package com.codetoart.android.upcomingmovies.data.local

import androidx.room.TypeConverter
import com.codetoart.android.upcomingmovies.ObjectMapperSingleton
import com.codetoart.android.upcomingmovies.data.model.Posters
import com.fasterxml.jackson.module.kotlin.readValue

class TmdbTypeConverters {

    private val objectMapper = ObjectMapperSingleton.get()

    @TypeConverter
    fun postersToJsonArray(posters: List<Posters>?): String? {

        return objectMapper.writeValueAsString(posters)
    }

    @TypeConverter
    fun jsonArrayToPosters(jsonArray: String): List<Posters>? {

        return objectMapper.readValue<List<Posters>>(jsonArray)
    }
}
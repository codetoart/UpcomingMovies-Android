package com.codetoart.android.upcomingmovies.data.local

import androidx.room.TypeConverter
import com.codetoart.android.upcomingmovies.data.model.Posters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TmdbTypeConverters {

    @TypeConverter
    fun postersToJsonArray(posters: List<Posters>?): String? {

        return Gson().toJson(posters)
    }

    @TypeConverter
    fun jsonArrayToPosters(jsonArray: String?): List<Posters>? {

        val typeToken = object : TypeToken<List<Posters>>() {}.type
        return Gson().fromJson<List<Posters>>(jsonArray, typeToken)
    }
}
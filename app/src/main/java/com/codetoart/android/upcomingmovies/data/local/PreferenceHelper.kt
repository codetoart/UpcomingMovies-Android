package com.codetoart.android.upcomingmovies.data.local

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.annotation.VisibleForTesting
import com.codetoart.android.upcomingmovies.data.model.Configuration
import com.codetoart.android.upcomingmovies.fromJson
import com.fasterxml.jackson.databind.ObjectMapper

class PreferenceHelper private constructor(
    context: Context,
    private val objectMapper: ObjectMapper
) {

    companion object {

        @Volatile
        private var singleton: PreferenceHelper? = null

        fun init(context: Context, objectMapper: ObjectMapper): PreferenceHelper =
            singleton ?: synchronized(this) {
                singleton ?: PreferenceHelper(context, objectMapper).also { singleton = it }
            }

        fun get(): PreferenceHelper = singleton ?: throw Exception("PreferenceHelper not initialised")

        const val KEY_CONFIGURATION = "KEY_CONFIGURATION"
    }

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    @VisibleForTesting
    fun getSharedPreferences(): SharedPreferences {
        return sharedPreferences
    }

    fun setConfiguration(value: Configuration) {
        sharedPreferences.edit().putString(KEY_CONFIGURATION, objectMapper.writeValueAsString(value)).apply()
    }

    fun getConfiguration(): Configuration? {

        val configurationJson = sharedPreferences.getString(KEY_CONFIGURATION, null)
        return objectMapper.fromJson(configurationJson, Configuration::class.java)
    }
}
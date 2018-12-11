package com.codetoart.android.upcomingmovies.data.local

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.annotation.VisibleForTesting
import com.codetoart.android.upcomingmovies.data.model.Configuration
import com.google.gson.Gson

class PreferenceHelper private constructor(
    context: Context
) {

    companion object {

        @Volatile
        private var singleton: PreferenceHelper? = null

        fun init(context: Context): PreferenceHelper =
            singleton ?: synchronized(this) {
                singleton ?: PreferenceHelper(context).also { singleton = it }
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
        // TODO -> Rethink about GSON or Jackson and singleton initialization
        sharedPreferences.edit().putString(KEY_CONFIGURATION, Gson().toJson(value)).apply()
    }

    fun getConfiguration(): Configuration? {

        val configurationJson = sharedPreferences.getString(KEY_CONFIGURATION, null)
        return Gson().fromJson(configurationJson, Configuration::class.java)
    }
}
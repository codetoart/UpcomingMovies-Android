package com.codetoart.android.upcomingmovies

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.codetoart.android.upcomingmovies.data.local.TmdbDb
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TmdbDbViewer {

    @Suppress("PrivatePropertyName")
    private val LOG_TAG = TmdbDbViewer::class.java.simpleName

    private val appContext = InstrumentationRegistry.getInstrumentation().context
    private val tmdbDb = TmdbDb.init(appContext)

    @Test
    fun viewAllRows_UpcomingMovies() {

        val movies = tmdbDb.upcomingMovieDao().getAllMovies()
        Log.v(LOG_TAG, "-> viewAllRows_UpcomingMovies -> $movies")
    }
}
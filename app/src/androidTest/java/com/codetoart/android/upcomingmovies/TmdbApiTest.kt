package com.codetoart.android.upcomingmovies

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.codetoart.android.upcomingmovies.data.remote.TmdbApi
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class TmdbApiTest {

    val tmdbApi = TmdbApi.get()

    @Test
    fun hit_getUpcomingMovies() {

        val call = tmdbApi.getUpcomingMovies(BuildConfig.TMDB_API_KEY, 1)
        val response = call.execute()
        assertEquals(true, response.isSuccessful)
    }

    @Test
    fun hit_getConfiguration() {

        val call = tmdbApi.getConfiguration(BuildConfig.TMDB_API_KEY)
        val response = call.execute()
        assertEquals(true, response.isSuccessful)
    }

    // TODO -> Add Observable Network tests
}

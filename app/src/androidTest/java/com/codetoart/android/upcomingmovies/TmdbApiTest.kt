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

    @Test
    fun hit_getUpcomingMovies() {

        val tmdbApi = TmdbApi.create()
        val call = tmdbApi.getUpcomingMovies(BuildConfig.TMDB_API_KEY, 1)
        val response = call.execute()

        assertEquals(true, response.isSuccessful)
    }
}

package com.codetoart.android.upcomingmovies

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.codetoart.android.upcomingmovies.data.remote.TmdbApi
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Call

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class TmdbApiTest {

    private val tmdbApi = TmdbApi.get()

    @Test
    fun hit_getConfiguration() {

        val call = tmdbApi.getConfiguration(BuildConfig.TMDB_API_KEY)
        commonRetrofitCall(call)
    }

    @Test
    fun hit_getObservableConfiguration() {

        val observable = tmdbApi.getObservableConfiguration(BuildConfig.TMDB_API_KEY)
        commonRxObservableSubscriber(observable)
    }

    @Test
    fun hit_getObservableUpcomingMovies() {

        val observable = tmdbApi.getObservableUpcomingMovies(BuildConfig.TMDB_API_KEY, 1)
        commonRxObservableSubscriber(observable)
    }

    @Test
    fun hit_getSingleMovieImages() {

        val single = tmdbApi.getSingleMovieImages(MockMinionsMovieData.id, BuildConfig.TMDB_API_KEY)
        commonRxSingleSubscriber(single)
    }

    @Test
    fun hit_getSingleMovieDetails() {

        val single = tmdbApi.getSingleMovieDetails(MockMinionsMovieData.id, BuildConfig.TMDB_API_KEY)
        commonRxSingleSubscriber(single)
    }

    private fun commonRetrofitCall(call: Call<*>) {
        val response = call.execute()
        assertTrue(response.isSuccessful)
    }

    private fun commonRxObservableSubscriber(observable: Observable<*>) {
        var response: Any? = null
        observable.blockingSubscribe({
            response = it
        }, {
            response = it
        })
        assertTrue(response !is Throwable)
    }

    private fun commonRxSingleSubscriber(single: Single<*>) {
        val response = try {
            single.blockingGet()
        } catch (e: Exception) {
            e
        }
        assertTrue(response !is Throwable)
    }
}

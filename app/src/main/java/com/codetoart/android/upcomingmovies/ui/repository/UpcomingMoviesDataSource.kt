package com.codetoart.android.upcomingmovies.ui.repository

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.codetoart.android.upcomingmovies.BuildConfig
import com.codetoart.android.upcomingmovies.ui.api.TmdbApi
import com.codetoart.android.upcomingmovies.ui.model.Movie
import com.codetoart.android.upcomingmovies.ui.model.UpcomingMovieResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingMoviesDataSource(
    private val tmdbApi: TmdbApi
) : PageKeyedDataSource<Int, Movie>() {

    companion object {
        val LOG_TAG: String = UpcomingMoviesDataSource::class.java.simpleName
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
        Log.v(LOG_TAG, "-> loadInitial")

        try {
            val response = tmdbApi.getUpcomingMovies(BuildConfig.TMDB_API_KEY, 1).execute()
            val upcomingMovieResponse = response.body()!!
            val nextPageKey = if (upcomingMovieResponse.page == upcomingMovieResponse.totalPages) {
                null
            } else {
                upcomingMovieResponse.page + 1
            }
            callback.onResult(upcomingMovieResponse.results, null, nextPageKey)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "-> loadInitial", e)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        //Log.v(LOG_TAG, "-> loadAfter -> PageKey = ${params.key}")

        tmdbApi.getUpcomingMovies(BuildConfig.TMDB_API_KEY, params.key)
            .enqueue(object : Callback<UpcomingMovieResponse> {

                override fun onFailure(call: Call<UpcomingMovieResponse>, t: Throwable) {
                    Log.e(LOG_TAG, "-> loadAfter -> onFailure -> PageKey = ${params.key}", t)
                }

                override fun onResponse(call: Call<UpcomingMovieResponse>, response: Response<UpcomingMovieResponse>) {
                    Log.v(LOG_TAG, "-> loadAfter -> onResponse -> PageKey = ${params.key}")
                    if (response.isSuccessful) {
                        val upcomingMovieResponse = response.body()!!
                        val nextPageKey = if (upcomingMovieResponse.page == upcomingMovieResponse.totalPages) {
                            null
                        } else {
                            upcomingMovieResponse.page + 1
                        }
                        callback.onResult(upcomingMovieResponse.results, nextPageKey)
                    }
                }
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        Log.v(LOG_TAG, "-> loadBefore -> PageKey = ${params.key}")
    }
}
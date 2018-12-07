package com.codetoart.android.upcomingmovies.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.codetoart.android.upcomingmovies.BuildConfig
import com.codetoart.android.upcomingmovies.api.TmdbApi
import com.codetoart.android.upcomingmovies.model.Movie
import com.codetoart.android.upcomingmovies.model.UpcomingMovieResponse
import com.codetoart.android.upcomingmovies.model.NetworkState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

class UpcomingMoviesDataSource(
    private val tmdbApi: TmdbApi,
    private val retryExecutor: Executor
) : PageKeyedDataSource<Int, Movie>() {

    companion object {
        val LOG_TAG: String = UpcomingMoviesDataSource::class.java.simpleName
    }

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    val networkState = MutableLiveData<NetworkState>()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
        Log.v(LOG_TAG, "-> loadInitial")

        networkState.postValue(NetworkState.LOADING)
        try {
            val response = tmdbApi.getUpcomingMovies(BuildConfig.TMDB_API_KEY, 1).execute()
            val upcomingMovieResponse = response.body()!!
            val nextPageKey = if (upcomingMovieResponse.page == upcomingMovieResponse.totalPages) {
                null
            } else {
                upcomingMovieResponse.page + 1
            }
            networkState.postValue(NetworkState.LOADED)
            callback.onResult(upcomingMovieResponse.results, null, nextPageKey)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "-> loadInitial", e)
            val error = NetworkState.error(e.message ?: "Unknown error")
            networkState.postValue(error)
            retry = {
                loadInitial(params, callback)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        //Log.v(LOG_TAG, "-> loadAfter -> PageKey = ${params.key}")

        networkState.postValue(NetworkState.LOADING)
        tmdbApi.getUpcomingMovies(BuildConfig.TMDB_API_KEY, params.key)
            .enqueue(object : Callback<UpcomingMovieResponse> {

                override fun onFailure(call: Call<UpcomingMovieResponse>, t: Throwable) {
                    Log.e(LOG_TAG, "-> loadAfter -> onFailure -> PageKey = ${params.key}", t)
                    networkState.postValue(NetworkState.error(t.message ?: "Unknown error"))
                    retry = {
                        loadAfter(params, callback)
                    }
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
                        networkState.postValue(NetworkState.LOADED)
                        callback.onResult(upcomingMovieResponse.results, nextPageKey)
                    } else {
                        networkState.postValue(NetworkState.error("Error code: ${response.code()}"))
                        retry = {
                            loadAfter(params, callback)
                        }
                    }
                }
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        Log.v(LOG_TAG, "-> loadBefore -> PageKey = ${params.key}")
    }

    fun retryFailedCall() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }
}
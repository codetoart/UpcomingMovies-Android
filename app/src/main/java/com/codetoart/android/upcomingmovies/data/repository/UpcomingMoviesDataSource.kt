package com.codetoart.android.upcomingmovies.data.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.codetoart.android.upcomingmovies.BuildConfig
import com.codetoart.android.upcomingmovies.data.model.Movie
import com.codetoart.android.upcomingmovies.data.model.NetworkState
import com.codetoart.android.upcomingmovies.data.remote.TmdbApi
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

class UpcomingMoviesDataSource(
    private val tmdbApi: TmdbApi,
    private val initialKey: Int,
    private val retryExecutor: Executor
) : PageKeyedDataSource<Int, Movie>() {

    companion object {
        val LOG_TAG: String = UpcomingMoviesDataSource::class.java.simpleName
    }

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    val networkState = MutableLiveData<NetworkState>()

    @SuppressLint("CheckResult")
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
        Log.v(LOG_TAG, "-> loadInitial")

        networkState.postValue(NetworkState.LOADING)
        tmdbApi.getObservableUpcomingMovies(BuildConfig.TMDB_API_KEY, initialKey)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({ upcomingMovieResponse ->
                Log.v(LOG_TAG, "-> loadInitial -> onNext -> PageKey = $initialKey")
                val nextPageKey = if (upcomingMovieResponse.page == upcomingMovieResponse.totalPages) {
                    null
                } else {
                    upcomingMovieResponse.page + 1
                }
                networkState.postValue(NetworkState.LOADED)
                callback.onResult(upcomingMovieResponse.results, null, nextPageKey)
            }, { t ->
                Log.e(LOG_TAG, "-> loadInitial -> onError -> PageKey = $initialKey", t)
                networkState.postValue(NetworkState.error(t.message ?: "Unknown error"))
                retry = {
                    loadInitial(params, callback)
                }
            })
    }

    @SuppressLint("CheckResult")
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        //Log.v(LOG_TAG, "-> loadAfter -> PageKey = ${params.key}")

        networkState.postValue(NetworkState.LOADING)
        tmdbApi.getObservableUpcomingMovies(BuildConfig.TMDB_API_KEY, params.key)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({ upcomingMovieResponse ->
                Log.v(LOG_TAG, "-> loadAfter -> onNext -> PageKey = ${params.key}")
                val nextPageKey = if (upcomingMovieResponse.page == upcomingMovieResponse.totalPages) {
                    null
                } else {
                    upcomingMovieResponse.page + 1
                }
                networkState.postValue(NetworkState.LOADED)
                callback.onResult(upcomingMovieResponse.results, nextPageKey)
            }, { t ->
                Log.e(LOG_TAG, "-> loadAfter -> onError -> PageKey = ${params.key}", t)
                networkState.postValue(NetworkState.error(t.message ?: "Unknown error"))
                retry = {
                    loadAfter(params, callback)
                }
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        Log.w(LOG_TAG, "-> loadBefore -> PageKey = ${params.key}, Not yet implemented")
    }

    fun retryFailedCall() {
        Log.v(LOG_TAG, "-> retryFailedCall")

        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }
}
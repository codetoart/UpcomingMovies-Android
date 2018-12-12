package com.codetoart.android.upcomingmovies.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.codetoart.android.upcomingmovies.data.local.TmdbDb
import com.codetoart.android.upcomingmovies.data.remote.TmdbApi
import com.codetoart.android.upcomingmovies.data.model.Movie
import java.util.concurrent.Executor

class UpcomingMoviesDataSourceFactory(
    private val tmdbApi: TmdbApi,
    private val tmdbDb: TmdbDb,
    private val initialKey: Int,
    private val retryExecutor: Executor
) : DataSource.Factory<Int, Movie>() {

    val sourceLiveData = MutableLiveData<UpcomingMoviesDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val source = UpcomingMoviesDataSource(tmdbApi, tmdbDb, initialKey, retryExecutor)
        sourceLiveData.postValue(source)
        return source
    }
}
package com.codetoart.android.upcomingmovies.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.codetoart.android.upcomingmovies.api.TmdbApi
import com.codetoart.android.upcomingmovies.model.Movie
import java.util.concurrent.Executor

class UpcomingMoviesDataSourceFactory(
    private val tmdbApi: TmdbApi,
    private val retryExecutor: Executor
) : DataSource.Factory<Int, Movie>() {

    val sourceLiveData = MutableLiveData<UpcomingMoviesDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val source = UpcomingMoviesDataSource(tmdbApi, retryExecutor)
        sourceLiveData.postValue(source)
        return source
    }
}
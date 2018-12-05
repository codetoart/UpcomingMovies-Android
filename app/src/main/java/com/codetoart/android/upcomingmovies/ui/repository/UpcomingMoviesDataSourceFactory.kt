package com.codetoart.android.upcomingmovies.ui.repository

import androidx.paging.DataSource
import com.codetoart.android.upcomingmovies.ui.api.TmdbApi
import com.codetoart.android.upcomingmovies.ui.model.Movie

class UpcomingMoviesDataSourceFactory(
    private val tmdbApi: TmdbApi
) : DataSource.Factory<Int, Movie>() {

    override fun create(): DataSource<Int, Movie> {
        return UpcomingMoviesDataSource(tmdbApi)
    }
}
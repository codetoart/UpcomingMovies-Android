package com.codetoart.android.upcomingmovies.ui.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.codetoart.android.upcomingmovies.ui.api.TmdbApi
import com.codetoart.android.upcomingmovies.ui.model.Movie

class TmdbRepository(private val tmdbApi: TmdbApi) {

    companion object {
        var singleton: TmdbRepository? = null
        fun get(tmdbApi: TmdbApi): TmdbRepository {
            if (singleton == null) {
                singleton = TmdbRepository(tmdbApi)
            }
            return singleton!!
        }
    }

    fun getUpcomingMoviesPagedList(pageSize: Int): LiveData<PagedList<Movie>> {

        val upcomingMoviesDataSourceFactory = UpcomingMoviesDataSourceFactory(tmdbApi)
        val livePagedListBuilder = LivePagedListBuilder(upcomingMoviesDataSourceFactory, pageSize)
        return livePagedListBuilder.build()
    }
}
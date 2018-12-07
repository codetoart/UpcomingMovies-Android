package com.codetoart.android.upcomingmovies.repository

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.codetoart.android.upcomingmovies.api.TmdbApi
import com.codetoart.android.upcomingmovies.model.Movie
import com.codetoart.android.upcomingmovies.model.Listing
import java.util.concurrent.Executor

class TmdbRepository(
    private val tmdbApi: TmdbApi,
    private val networkExecutor: Executor
) {

    companion object {
        private var singleton: TmdbRepository? = null
        fun get(tmdbApi: TmdbApi, networkExecutor: Executor): TmdbRepository {
            if (singleton == null) {
                singleton = TmdbRepository(tmdbApi, networkExecutor)
            }
            return singleton!!
        }
    }

    fun getUpcomingMoviesPagedList(pageSize: Int): Listing<Movie> {

        val sourceFactory = UpcomingMoviesDataSourceFactory(tmdbApi, networkExecutor)

        val livePagedListBuilder = LivePagedListBuilder(sourceFactory, pageSize)
        val livePagedList = livePagedListBuilder.build()

        val sourceLiveData = sourceFactory.sourceLiveData

        return Listing(
            pagedList = livePagedList,
            networkState = Transformations.switchMap(sourceLiveData) {
                it.networkState
            },
            retry = {
                sourceFactory.sourceLiveData.value?.retryFailedCall()
            }
        )
    }
}
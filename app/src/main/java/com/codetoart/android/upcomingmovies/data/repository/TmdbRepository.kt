package com.codetoart.android.upcomingmovies.data.repository

import android.util.Log
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.codetoart.android.upcomingmovies.BuildConfig
import com.codetoart.android.upcomingmovies.data.local.PreferenceHelper
import com.codetoart.android.upcomingmovies.data.local.TmdbDb
import com.codetoart.android.upcomingmovies.data.model.Configuration
import com.codetoart.android.upcomingmovies.data.model.Listing
import com.codetoart.android.upcomingmovies.data.model.Movie
import com.codetoart.android.upcomingmovies.data.remote.TmdbApi
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

class TmdbRepository(
    private val tmdbApi: TmdbApi,
    private val tmdbDb: TmdbDb,
    private val networkExecutor: Executor,
    private val preferenceHelper: PreferenceHelper
) {

    companion object {

        val LOG_TAG: String = TmdbRepository::class.java.simpleName

        @Volatile
        private var singleton: TmdbRepository? = null

        fun get(): TmdbRepository =
            singleton ?: throw Exception("-> Not yet initialised")

        fun init(
            tmdbApi: TmdbApi,
            tmdbDb: TmdbDb,
            networkExecutor: Executor,
            preferenceHelper: PreferenceHelper
        ): TmdbRepository =
            singleton ?: synchronized(this) {
                singleton ?: TmdbRepository(tmdbApi, tmdbDb, networkExecutor, preferenceHelper).also { singleton = it }
            }
    }

    fun getUpcomingMoviesPagedList(initialKey: Int, pageSize: Int): Listing<Movie> {

        val sourceFactory = UpcomingMoviesDataSourceFactory(tmdbApi, tmdbDb, initialKey, networkExecutor)

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

    fun fetchNewConfiguration(): Observable<Configuration> {

        return tmdbApi.getObservableConfiguration(BuildConfig.TMDB_API_KEY)
            .doOnNext { configuration ->
                Log.v(LOG_TAG, "-> fetchNewConfiguration -> onSuccess")
                preferenceHelper.setConfiguration(configuration)
            }.onErrorResumeNext(Function<Throwable, ObservableSource<Configuration>> {
                Log.e(LOG_TAG, "-> fetchNewConfiguration -> onError -> ", it)
                Observable.empty()
            })
    }

    fun getSingleConfiguration(): Observable<Configuration> {

        return Observable.create<Configuration> { source ->
            val savedConfiguration = PreferenceHelper.get().getConfiguration()
            if (savedConfiguration == null) {
                fetchNewConfiguration()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe { configuration ->
                        source.onNext(configuration)
                        source.onComplete()
                    }
            } else {
                source.onNext(savedConfiguration)
                source.onComplete()
            }
        }
    }
}
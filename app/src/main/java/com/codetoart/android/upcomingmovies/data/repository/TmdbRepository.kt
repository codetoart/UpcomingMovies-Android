package com.codetoart.android.upcomingmovies.data.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.codetoart.android.upcomingmovies.BuildConfig
import com.codetoart.android.upcomingmovies.data.local.PreferenceHelper
import com.codetoart.android.upcomingmovies.data.local.TmdbDb
import com.codetoart.android.upcomingmovies.data.model.Configuration
import com.codetoart.android.upcomingmovies.data.model.Listing
import com.codetoart.android.upcomingmovies.data.model.Movie
import com.codetoart.android.upcomingmovies.data.remote.TmdbApi
import io.reactivex.Single
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
        Log.v(LOG_TAG, "-> getUpcomingMoviesPagedList")

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

    fun fetchNewConfiguration(): Single<Configuration> {
        Log.v(LOG_TAG, "-> fetchNewConfiguration")

        return tmdbApi.getSingleConfiguration(BuildConfig.TMDB_API_KEY)
            .doOnSuccess { configuration ->
                Log.v(LOG_TAG, "-> fetchNewConfiguration -> onSuccess")
                preferenceHelper.setConfiguration(configuration)
            }.doOnError {
                Log.e(LOG_TAG, "-> fetchNewConfiguration -> onError -> ", it)
            }
    }

    fun getSingleConfiguration(): Single<Configuration> {
        Log.v(LOG_TAG, "-> getSingleConfiguration")

        return Single.create<Configuration> { source ->
            val savedConfiguration = preferenceHelper.getConfiguration()
            if (savedConfiguration == null) {
                fetchNewConfiguration()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe({ configuration ->
                        source.onSuccess(configuration)
                    }, {
                        source.onError(it)
                    })
            } else {
                source.onSuccess(savedConfiguration)
            }
        }
    }

    @SuppressLint("CheckResult")
    fun getLiveConfiguration(liveConfiguration: MutableLiveData<Configuration>): Single<Configuration> {
        Log.v(LOG_TAG, "-> getLiveConfiguration")

        return Single.create<Configuration> { source ->
            if (liveConfiguration.value == null) {
                source.onError(Throwable())
            } else {
                source.onSuccess(liveConfiguration.value!!)
            }
        }
            .onErrorResumeNext {
                getSingleConfiguration()
                    .doOnSuccess { configuration ->
                        liveConfiguration.postValue(configuration)
                    }
            }
    }

    fun getMovieDetails(id: Long): Single<Movie> {
        Log.v(LOG_TAG, "-> getMovieDetails")

        return tmdbDb.upcomingMovieDao().getSingleMovie(id)
            .onErrorResumeNext {
                Log.w(LOG_TAG, "-> getMovieDetails -> onErrorResumeNext -> ", it)
                tmdbApi.getSingleMovieDetails(id, BuildConfig.TMDB_API_KEY)
                    .doAfterSuccess { movie ->
                        Log.v(LOG_TAG, "-> getMovieDetails -> doAfterSuccess")
                        tmdbDb.upcomingMovieDao().insert(movie)
                    }
            }
    }

    fun getMovieImagesFromRemote(id: Long): Single<TmdbApi.ImagesResponse> {
        Log.v(LOG_TAG, "-> getMovieImagesFromRemote")

        return tmdbApi.getSingleMovieImages(id, BuildConfig.TMDB_API_KEY)
            .doAfterSuccess { imagesResponse ->
                tmdbDb.upcomingMovieDao().updatePosters(id, imagesResponse.posters)
            }
    }
}
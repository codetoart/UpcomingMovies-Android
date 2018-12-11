package com.codetoart.android.upcomingmovies

import android.app.Application
import android.util.Log
import com.codetoart.android.upcomingmovies.data.local.PreferenceHelper
import com.codetoart.android.upcomingmovies.data.remote.TmdbApi
import com.codetoart.android.upcomingmovies.data.repository.TmdbRepository
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainApplication : Application() {

    companion object {
        val LOG_TAG: String = MainApplication::class.java.simpleName
        private var mainApplication: MainApplication? = null

        fun set(mainApplication: MainApplication) {
            this.mainApplication = mainApplication
        }

        fun get(): MainApplication {
            return mainApplication ?: throw Exception("MainApplication.set() not called")
        }
    }

    private lateinit var tmdbApi: TmdbApi
    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var appExecutors: AppExecutors
    private lateinit var tmdbRepository: TmdbRepository
    private var disposableConfiguration: Disposable? = null

    override fun onCreate() {
        super.onCreate()
        Log.v(LOG_TAG, "-> onCreate")

        set(this)

        tmdbApi = TmdbApi.get()
        preferenceHelper = PreferenceHelper.init(applicationContext)
        appExecutors = AppExecutors.get()
        tmdbRepository = TmdbRepository.init(tmdbApi, appExecutors.networkExecutor, preferenceHelper)

        getConfiguration()
    }

    private fun getConfiguration() {
        Log.v(LOG_TAG, "-> getConfiguration")

        disposableConfiguration = tmdbRepository.fetchNewConfiguration()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe()
    }
}
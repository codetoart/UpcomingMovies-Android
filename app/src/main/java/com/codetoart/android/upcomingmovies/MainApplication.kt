package com.codetoart.android.upcomingmovies

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import com.codetoart.android.upcomingmovies.data.local.PreferenceHelper
import com.codetoart.android.upcomingmovies.data.local.TmdbDb
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
    private lateinit var tmdbDb: TmdbDb
    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var appExecutors: AppExecutors
    private lateinit var tmdbRepository: TmdbRepository

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        Log.v(LOG_TAG, "-> onCreate")

        set(this)

        tmdbApi = TmdbApi.get()
        tmdbDb = TmdbDb.init(applicationContext)
        preferenceHelper = PreferenceHelper.init(applicationContext)
        appExecutors = AppExecutors.get()
        tmdbRepository = TmdbRepository.init(tmdbApi, tmdbDb, appExecutors.networkExecutor, preferenceHelper)

        getConfiguration()
    }

    @SuppressLint("CheckResult")
    private fun getConfiguration() {
        Log.v(LOG_TAG, "-> getConfiguration")

        tmdbRepository.fetchNewConfiguration()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({}, {})
    }
}

// TODO -> Discard drawable/minions_poster_image_w185.jpg(i.e. all tools res) into APK
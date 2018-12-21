package com.codetoart.android.upcomingmovies.ui.details

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.codetoart.android.upcomingmovies.MainApplication
import com.codetoart.android.upcomingmovies.R
import com.codetoart.android.upcomingmovies.data.local.PreferenceHelper
import com.codetoart.android.upcomingmovies.data.model.Configuration
import com.codetoart.android.upcomingmovies.data.model.Movie
import com.codetoart.android.upcomingmovies.data.model.NetworkState
import com.codetoart.android.upcomingmovies.data.repository.TmdbRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DetailsViewModel(
    private val app: MainApplication,
    private val tmdbRepository: TmdbRepository,
    private val preferenceHelper: PreferenceHelper
) : AndroidViewModel(app) {

    companion object {
        val LOG_TAG: String = DetailsViewModel::class.java.simpleName
    }

    var liveConfiguration: MutableLiveData<Configuration> = MutableLiveData()
    val liveMovie: MutableLiveData<Movie> = MutableLiveData()
    val liveNetworkState: MutableLiveData<NetworkState> = MutableLiveData()

    private var disposableMovieDetails: Disposable? = null
    private var disposableMovieImagesFromRemote: Disposable? = null

    fun getMovieDetails(id: Long) {
        Log.v(LOG_TAG, "-> getMovieDetails")

        liveNetworkState.value = NetworkState.LOADING
        liveConfiguration.value = preferenceHelper.getConfiguration()

        disposableMovieDetails = tmdbRepository.getMovieDetails(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ movie ->
                Log.v(LOG_TAG, "-> getMovieDetails -> onSuccess")
                liveMovie.value = movie
                liveNetworkState.value = NetworkState.LOADED
                if (movie.posters == null) {
                    getMovieImagesFromRemote(id)
                }
            }, { t ->
                Log.e(LOG_TAG, "-> getMovieDetails -> onError -> ", t)
                val errorMsg = app.applicationContext.getString(R.string.something_went_wrong)
                liveNetworkState.value = NetworkState.error(errorMsg)
            })
    }

    fun getMovieImagesFromRemote(id: Long) {
        Log.v(LOG_TAG, "-> getMovieImagesFromRemote")

        disposableMovieImagesFromRemote = tmdbRepository.getMovieImagesFromRemote(id)
            .subscribeOn(Schedulers.io())
            .subscribe({ imagesResponse ->
                Log.v(LOG_TAG, "-> getMovieImagesFromRemote -> onSuccess")
                val movie = liveMovie.value
                movie?.posters = imagesResponse.posters
                liveMovie.postValue(movie)
            }, { t ->
                Log.e(LOG_TAG, "-> getMovieImagesFromRemote -> onError -> ", t)
            })
    }

    override fun onCleared() {
        super.onCleared()
        Log.v(LOG_TAG, "-> onCleared")

        disposableMovieDetails?.dispose()
        disposableMovieImagesFromRemote?.dispose()
    }
}

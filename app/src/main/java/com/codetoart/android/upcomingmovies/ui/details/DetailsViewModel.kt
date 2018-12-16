package com.codetoart.android.upcomingmovies.ui.details

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codetoart.android.upcomingmovies.data.local.PreferenceHelper
import com.codetoart.android.upcomingmovies.data.model.Configuration
import com.codetoart.android.upcomingmovies.data.model.Movie
import com.codetoart.android.upcomingmovies.data.repository.TmdbRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DetailsViewModel(
    private val tmdbRepository: TmdbRepository,
    private val preferenceHelper: PreferenceHelper
) : ViewModel() {

    companion object {
        val LOG_TAG: String = DetailsViewModel::class.java.simpleName
    }

    var liveConfiguration: MutableLiveData<Configuration> = MutableLiveData()
    val liveMovie: MutableLiveData<Movie> = MutableLiveData()

    @SuppressLint("CheckResult")
    fun getMovieDetails(id: Long) {
        Log.v(LOG_TAG, "-> getMovieDetails")

        liveConfiguration.value = preferenceHelper.getConfiguration()

        tmdbRepository.getMovieDetails(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ movie ->
                Log.v(LOG_TAG, "-> getMovieDetails -> onSuccess")
                liveMovie.value = movie
                if (movie.posters == null) {
                    getMovieImagesFromRemote(id)
                }
            }, { t ->
                Log.e(LOG_TAG, "-> getMovieDetails -> onError -> ", t)
            })
    }

    @SuppressLint("CheckResult")
    fun getMovieImagesFromRemote(id: Long) {
        Log.v(LOG_TAG, "-> getMovieImagesFromRemote")

        tmdbRepository.getMovieImagesFromRemote(id)
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
}

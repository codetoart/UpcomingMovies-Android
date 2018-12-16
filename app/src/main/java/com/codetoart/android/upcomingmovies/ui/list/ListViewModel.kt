package com.codetoart.android.upcomingmovies.ui.list

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.codetoart.android.upcomingmovies.data.local.PreferenceHelper
import com.codetoart.android.upcomingmovies.data.model.Configuration
import com.codetoart.android.upcomingmovies.data.model.Listing
import com.codetoart.android.upcomingmovies.data.model.Movie
import com.codetoart.android.upcomingmovies.data.model.NetworkState
import com.codetoart.android.upcomingmovies.data.repository.TmdbRepository

class ListViewModel(
    private val tmdbRepository: TmdbRepository,
    private val preferenceHelper: PreferenceHelper
) : ViewModel() {

    companion object {
        val LOG_TAG: String = ListViewModel::class.java.simpleName
    }

    var listing: Listing<Movie>? = null
    val livePagedList: MediatorLiveData<PagedList<Movie>> = MediatorLiveData()
    val networkState: MediatorLiveData<NetworkState> = MediatorLiveData()
    var liveConfiguration: MutableLiveData<Configuration> = MutableLiveData()

    fun getUpcomingMovies() {
        Log.v(LOG_TAG, "-> getUpcomingMovies")

        liveConfiguration.value = preferenceHelper.getConfiguration()

        listing = tmdbRepository.getUpcomingMoviesPagedList(1, 20)

        val livePagedList = listing!!.pagedList

        this.livePagedList.addSource(livePagedList) {
            this.livePagedList.value = it
        }

        this.networkState.addSource(listing!!.networkState!!) {
            this.networkState.value = it
        }
    }

    fun retry() {
        listing?.retry?.invoke()
    }
}

package com.codetoart.android.upcomingmovies.ui.list

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.codetoart.android.upcomingmovies.AppExecutors
import com.codetoart.android.upcomingmovies.api.TmdbApi
import com.codetoart.android.upcomingmovies.model.Movie
import com.codetoart.android.upcomingmovies.repository.TmdbRepository
import com.codetoart.android.upcomingmovies.model.Listing
import com.codetoart.android.upcomingmovies.model.NetworkState

class ListViewModel : ViewModel() {

    companion object {
        val LOG_TAG: String = ListViewModel::class.java.simpleName
    }

    val tmdbRepository = TmdbRepository.get(TmdbApi.get(), AppExecutors.get().networkExecutor)
    var listing: Listing<Movie>? = null
    val livePagedList: MediatorLiveData<PagedList<Movie>> = MediatorLiveData()
    val networkState: MediatorLiveData<NetworkState> = MediatorLiveData()

    fun getUpcomingMovies() {
        Log.v(LOG_TAG, "-> getUpcomingMovies")

        listing = tmdbRepository.getUpcomingMoviesPagedList(20)

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

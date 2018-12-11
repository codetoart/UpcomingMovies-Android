package com.codetoart.android.upcomingmovies.ui.list

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.paging.PagedList
import com.codetoart.android.upcomingmovies.data.model.Listing
import com.codetoart.android.upcomingmovies.data.model.Movie
import com.codetoart.android.upcomingmovies.data.model.NetworkState
import com.codetoart.android.upcomingmovies.data.repository.TmdbRepository

class ListViewModel(
    app: Application,
    private val tmdbRepository: TmdbRepository
) : AndroidViewModel(app) {

    companion object {
        val LOG_TAG: String = ListViewModel::class.java.simpleName
    }

    val context: Context = app
    var listing: Listing<Movie>? = null
    val livePagedList: MediatorLiveData<PagedList<Movie>> = MediatorLiveData()
    val networkState: MediatorLiveData<NetworkState> = MediatorLiveData()

    fun getUpcomingMovies() {
        Log.v(LOG_TAG, "-> getUpcomingMovies")

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

package com.codetoart.android.upcomingmovies.ui.list

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.codetoart.android.upcomingmovies.ui.api.TmdbApi
import com.codetoart.android.upcomingmovies.ui.model.Movie
import com.codetoart.android.upcomingmovies.ui.repository.TmdbRepository

class ListViewModel : ViewModel() {

    val tmdbRepository = TmdbRepository.get(TmdbApi.get())
    var mediatorLivePagedList: MediatorLiveData<PagedList<Movie>> = MediatorLiveData()

    fun getUpcomingMovies() {
        val livePagedList = tmdbRepository.getUpcomingMoviesPagedList(20)
        mediatorLivePagedList.addSource(livePagedList) {
            mediatorLivePagedList.value = it
        }
    }
}

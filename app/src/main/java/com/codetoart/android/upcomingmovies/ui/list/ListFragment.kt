package com.codetoart.android.upcomingmovies.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.codetoart.android.upcomingmovies.AppExecutors
import com.codetoart.android.upcomingmovies.R
import com.codetoart.android.upcomingmovies.data.remote.TmdbApi
import com.codetoart.android.upcomingmovies.data.repository.TmdbRepository
import kotlinx.android.synthetic.main.list_fragment.*

class ListFragment : Fragment() {

    companion object {
        val LOG_TAG: String = ListFragment::class.java.simpleName

        fun newInstance() = ListFragment()
    }

    private lateinit var viewModel: ListViewModel
    private lateinit var listAdapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(LOG_TAG, "-> onActivityCreated")

        viewModel = getListViewModel()

        listAdapter = ListAdapter {
            viewModel.retry()
        }
        recyclerView.adapter = listAdapter

        viewModel.livePagedList.observe(this, Observer {
            listAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            Log.d(LOG_TAG, "-> Network State = $it")
            listAdapter.setNetworkState(it)
        })

        if (savedInstanceState == null)
            viewModel.getUpcomingMovies()
    }

    private fun getListViewModel(): ListViewModel {
        Log.v(LOG_TAG, "-> getListViewModel")

        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val tmdbApi = TmdbApi.get()
                val appExecutors = AppExecutors.get()
                val tmdbRepository = TmdbRepository.get(tmdbApi, appExecutors.networkExecutor)
                @Suppress("UNCHECKED_CAST")
                return ListViewModel(activity!!.application, tmdbRepository) as T
            }
        }).get(ListViewModel::class.java)
    }
}

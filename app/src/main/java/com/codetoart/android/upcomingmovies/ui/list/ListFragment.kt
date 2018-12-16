package com.codetoart.android.upcomingmovies.ui.list

import android.content.Intent
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
import com.codetoart.android.upcomingmovies.R
import com.codetoart.android.upcomingmovies.data.local.PreferenceHelper
import com.codetoart.android.upcomingmovies.data.repository.TmdbRepository
import com.codetoart.android.upcomingmovies.ui.details.DetailsActivity
import com.codetoart.android.upcomingmovies.ui.details.DetailsFragment
import kotlinx.android.synthetic.main.list_fragment.*

class ListFragment : Fragment(), View.OnClickListener {

    companion object {
        val LOG_TAG: String = ListFragment::class.java.simpleName
        fun newInstance() = ListFragment()
    }

    private lateinit var viewModel: ListViewModel
    private lateinit var listAdapter: ListAdapter
    private lateinit var tmdbRepository: TmdbRepository
    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(LOG_TAG, "-> onActivityCreated")

        preferenceHelper = PreferenceHelper.get()
        tmdbRepository = TmdbRepository.get()
        viewModel = getListViewModel(tmdbRepository, preferenceHelper)

        listAdapter = ListAdapter(tmdbRepository, viewModel.liveConfiguration, this) {
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

    private fun getListViewModel(tmdbRepository: TmdbRepository, preferenceHelper: PreferenceHelper): ListViewModel {
        Log.v(LOG_TAG, "-> getListViewModel")

        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ListViewModel(tmdbRepository, preferenceHelper) as T
            }
        }).get(ListViewModel::class.java)
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.listItem -> {
                Log.v(LOG_TAG, "-> onClick -> listItem")

                val intent = Intent(context, DetailsActivity::class.java)
                intent.putExtra(DetailsFragment.BUNDLE_MOVIE_ID, v.tag as Long)
                startActivity(intent)
            }
        }
    }
}

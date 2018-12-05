package com.codetoart.android.upcomingmovies.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.codetoart.android.upcomingmovies.R
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

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)

        listAdapter = ListAdapter()
        recyclerView.adapter = listAdapter
        viewModel.mediatorLivePagedList.observe(this, Observer {
            listAdapter.submitList(it)
        })

        if (savedInstanceState == null)
            viewModel.getUpcomingMovies()
    }
}

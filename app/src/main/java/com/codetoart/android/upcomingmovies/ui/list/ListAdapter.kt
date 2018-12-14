package com.codetoart.android.upcomingmovies.ui.list

import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.codetoart.android.upcomingmovies.R
import com.codetoart.android.upcomingmovies.data.model.Configuration
import com.codetoart.android.upcomingmovies.data.model.Movie
import com.codetoart.android.upcomingmovies.data.model.NetworkState
import com.codetoart.android.upcomingmovies.data.repository.TmdbRepository

class ListAdapter(
    private val tmdbRepository: TmdbRepository,
    private val liveConfiguration: MutableLiveData<Configuration>,
    private val retryCallback: () -> Unit = {}
) : PagedListAdapter<Movie, RecyclerView.ViewHolder>(DIFF_UTIL) {

    companion object {

        val LOG_TAG: String = ListAdapter::class.java.simpleName

        val DIFF_UTIL = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                // TODO
                return false
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                // TODO
                return false
            }
        }
    }

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            R.layout.list_item -> UpcomingMovieHolder.create(parent)
            R.layout.network_state_list_item -> NetworkStateItemViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("-> Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (getItemViewType(position)) {
            R.layout.list_item -> (holder as UpcomingMovieHolder)
                .bind(getItem(position), tmdbRepository, liveConfiguration)
            R.layout.network_state_list_item -> (holder as NetworkStateItemViewHolder).bindTo(networkState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_list_item
        } else {
            R.layout.list_item
        }
    }

    override fun getItemCount(): Int = super.getItemCount() + if (hasExtraRow()) 1 else 0

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
}
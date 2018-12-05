package com.codetoart.android.upcomingmovies.ui.list

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.codetoart.android.upcomingmovies.ui.model.Movie

class ListAdapter : PagedListAdapter<Movie, RecyclerView.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UpcomingMovieHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val upcomingMovieHolder = holder as UpcomingMovieHolder
        upcomingMovieHolder.bind(getItem(position))
    }

    companion object {
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


}
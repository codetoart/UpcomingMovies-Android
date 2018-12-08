package com.codetoart.android.upcomingmovies.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codetoart.android.upcomingmovies.R
import com.codetoart.android.upcomingmovies.data.model.Movie

class UpcomingMovieHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup): UpcomingMovieHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            return UpcomingMovieHolder(view)
        }
    }

    val imageViewPoster: ImageView = view.findViewById(R.id.imageViewPoster)
    val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
    val textViewReleaseDate: TextView = view.findViewById(R.id.textViewReleaseDate)
    val textViewAdult: TextView = view.findViewById(R.id.textViewAdult)
    val context: Context = view.context

    fun bind(movie: Movie?) {
        textViewTitle.text = movie?.title
        textViewReleaseDate.text = movie?.releaseDate
        textViewAdult.text = if (movie?.adult == true) {
            context.getString(R.string.adult)
        } else {
            ""
        }
    }
}
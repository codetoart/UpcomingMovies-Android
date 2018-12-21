package com.codetoart.android.upcomingmovies.ui.list

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.codetoart.android.upcomingmovies.GlideApp
import com.codetoart.android.upcomingmovies.R
import com.codetoart.android.upcomingmovies.data.model.Configuration
import com.codetoart.android.upcomingmovies.data.model.Movie
import com.codetoart.android.upcomingmovies.data.repository.TmdbRepository
import com.codetoart.android.upcomingmovies.util.AppUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class UpcomingMovieHolder(
    view: View,
    private val onClickListener: View.OnClickListener
) : RecyclerView.ViewHolder(view) {

    companion object {

        val LOG_TAG: String = UpcomingMovieHolder::class.java.simpleName

        fun create(parent: ViewGroup, onClickListener: View.OnClickListener): UpcomingMovieHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            return UpcomingMovieHolder(view, onClickListener)
        }
    }

    val imageViewPoster: ImageView = view.findViewById(R.id.imageViewPoster)
    val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
    val textViewReleaseDate: TextView = view.findViewById(R.id.textViewReleaseDate)
    val textViewAdult: TextView = view.findViewById(R.id.textViewAdult)
    val context: Context = view.context

    private var disposableConfiguration: Disposable? = null

    fun bind(movie: Movie?, tmdbRepository: TmdbRepository, liveConfiguration: MutableLiveData<Configuration>) {

        textViewTitle.text = movie?.title
        textViewReleaseDate.text = movie?.releaseDate
        textViewAdult.text = when {
            movie?.adult == true -> context.getString(R.string.adult)
            else -> ""
        }

        disposableConfiguration = tmdbRepository.getLiveConfiguration(liveConfiguration)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ configuration ->
                val imageUri = if (movie?.posterPath.isNullOrEmpty()) {
                    null
                } else {
                    val imageBaseUrl = AppUtil.getImagePosterUrl(configuration, imageViewPoster.width)
                    Uri.parse(imageBaseUrl).buildUpon().appendEncodedPath(movie?.posterPath).build()
                }
                GlideApp.with(itemView)
                    .load(imageUri)
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .into(imageViewPoster)
            }, { t ->
                Log.e(LOG_TAG, "-> bind -> subscribe -> onError", t)
            })

        itemView.tag = movie?.id ?: -1
        itemView.setOnClickListener(onClickListener)
    }

    fun onViewRecycled() {
        disposableConfiguration?.dispose()
    }
}
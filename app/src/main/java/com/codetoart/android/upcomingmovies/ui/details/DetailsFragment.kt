package com.codetoart.android.upcomingmovies.ui.details

import android.net.Uri
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
import com.codetoart.android.upcomingmovies.GlideApp
import com.codetoart.android.upcomingmovies.R
import com.codetoart.android.upcomingmovies.data.local.PreferenceHelper
import com.codetoart.android.upcomingmovies.data.repository.TmdbRepository
import com.codetoart.android.upcomingmovies.util.AppUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.details_fragment.*

class DetailsFragment : Fragment() {

    companion object {

        val LOG_TAG: String = DetailsFragment::class.java.simpleName
        const val BUNDLE_MOVIE_ID = "BUNDLE_MOVIE_ID"

        fun newInstance(id: Long): DetailsFragment {

            val detailsFragment = DetailsFragment()
            val bundle = Bundle()
            bundle.putLong(BUNDLE_MOVIE_ID, id)
            detailsFragment.arguments = bundle
            return detailsFragment
        }
    }

    private lateinit var tmdbRepository: TmdbRepository
    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var viewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(LOG_TAG, "-> onActivityCreated")

        tmdbRepository = TmdbRepository.get()
        preferenceHelper = PreferenceHelper.get()
        viewModel = getDetailsViewModel(tmdbRepository, preferenceHelper)

        viewModel.liveMovie.observe(this, Observer {
            Log.v(LOG_TAG, "-> onActivityCreated -> liveMovie Observer")

            textViewTitle.text = it.title
            textViewOverview.text = it.overview
            ratingBar.rating = it.voteAverage / 2

            carouselView.setImageListener { position, imageView ->
                tmdbRepository.getLiveConfiguration(viewModel.liveConfiguration)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ configuration ->
                        val poster = it.posters?.get(position)?.filePath
                        val imageUri = if (poster.isNullOrEmpty()) {
                            null
                        } else {
                            val imageBaseUrl = AppUtil.getImagePosterUrl(configuration, imageView.width)
                            Uri.parse(imageBaseUrl).buildUpon().appendEncodedPath(poster).build()
                        }
                        GlideApp.with(this)
                            .load(imageUri)
                            .placeholder(R.drawable.ic_movie_placeholder)
                            .into(imageView)
                    }, { t ->
                        Log.v(LOG_TAG, "-> onActivityCreated -> setImageListener -> onError -> ", t)
                    })
            }
            carouselView.pageCount = when {
                it.posters == null -> 1
                it.posters!!.size >= 5 -> 5
                else -> it.posters!!.size
            }
        })

        if (savedInstanceState == null) {
            val id = arguments?.getLong(BUNDLE_MOVIE_ID) ?: -1
            viewModel.getMovieDetails(id)
        }
    }

    private fun getDetailsViewModel(
        tmdbRepository: TmdbRepository,
        preferenceHelper: PreferenceHelper
    ): DetailsViewModel {
        Log.v(LOG_TAG, "-> getDetailsViewModel")

        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return DetailsViewModel(tmdbRepository, preferenceHelper) as T
            }
        }).get(DetailsViewModel::class.java)
    }
}

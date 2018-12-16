package com.codetoart.android.upcomingmovies.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codetoart.android.upcomingmovies.R

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)

        if (savedInstanceState == null) {

            val id = intent.getLongExtra(DetailsFragment.BUNDLE_MOVIE_ID, -1)
            //val id = intent.getLongExtra(DetailsFragment.BUNDLE_MOVIE_ID, 211672)

            supportFragmentManager.beginTransaction()
                .replace(R.id.container, DetailsFragment.newInstance(id))
                .commitNow()
        }
    }
}

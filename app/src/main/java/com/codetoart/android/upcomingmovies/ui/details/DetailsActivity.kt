package com.codetoart.android.upcomingmovies.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codetoart.android.upcomingmovies.R

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, DetailsFragment.newInstance())
                .commitNow()
        }
    }

}

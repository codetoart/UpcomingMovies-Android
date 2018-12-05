package com.codetoart.android.upcomingmovies.ui.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codetoart.android.upcomingmovies.R

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.list_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ListFragment.newInstance())
                .commitNow()
        }
    }

}

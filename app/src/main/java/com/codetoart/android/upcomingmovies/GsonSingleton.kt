package com.codetoart.android.upcomingmovies

import com.google.gson.Gson

class GsonSingleton {

    companion object {

        @Volatile
        private var singleton: Gson? = null

        fun get(): Gson =
            singleton ?: synchronized(this) {
                singleton ?: Gson()
            }
    }
}
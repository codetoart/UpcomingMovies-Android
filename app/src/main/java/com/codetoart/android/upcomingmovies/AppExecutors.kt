package com.codetoart.android.upcomingmovies

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors {

    companion object {
        private var singleton: AppExecutors? = null
        fun get(): AppExecutors {
            if (singleton == null) {
                singleton =
                        AppExecutors()
            }
            return singleton!!
        }
    }

    val networkExecutor: Executor = Executors.newFixedThreadPool(5)
}
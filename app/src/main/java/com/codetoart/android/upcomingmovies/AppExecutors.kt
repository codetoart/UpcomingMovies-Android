package com.codetoart.android.upcomingmovies

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors {

    companion object {

        @Volatile
        private var singleton: AppExecutors? = null

        fun get(): AppExecutors =
            singleton ?: synchronized(this) {
                singleton ?: AppExecutors().also { singleton = it }
            }
    }

    val networkExecutor: Executor = Executors.newFixedThreadPool(5)
}
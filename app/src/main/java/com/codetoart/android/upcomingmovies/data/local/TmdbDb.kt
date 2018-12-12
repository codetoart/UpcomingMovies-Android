package com.codetoart.android.upcomingmovies.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.codetoart.android.upcomingmovies.data.model.Movie

@Database(entities = [Movie::class], version = 1)
abstract class TmdbDb : RoomDatabase() {

    companion object {

        @Volatile
        private var singleton: TmdbDb? = null

        fun get(): TmdbDb =
            singleton ?: throw Exception("-> Not yet initialised")

        fun init(context: Context): TmdbDb =
            singleton ?: synchronized(this) {
                singleton ?: Room.databaseBuilder(context, TmdbDb::class.java, "tmdb.db")
                    .build().also { singleton = it }
            }
    }

    abstract fun upcomingMovieDao(): UpcomingMovieDao
}
package com.codetoart.android.upcomingmovies.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.codetoart.android.upcomingmovies.data.model.Movie

@Dao
interface UpcomingMovieDao {

    @Insert
    fun insert(movies: List<Movie>)

    @Query("DELETE FROM upcoming_movies")
    fun deleteAllRows(): Int

    @Query("SELECT * FROM upcoming_movies")
    fun getAllMovies(): List<Movie>
}
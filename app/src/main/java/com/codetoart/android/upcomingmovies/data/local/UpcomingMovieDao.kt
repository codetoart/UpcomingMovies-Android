package com.codetoart.android.upcomingmovies.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.codetoart.android.upcomingmovies.data.model.Movie
import com.codetoart.android.upcomingmovies.data.model.Posters
import io.reactivex.Single

@Dao
interface UpcomingMovieDao {

    @Insert
    fun insert(movie: Movie)

    @Insert
    fun insert(movies: List<Movie>)

    @Query("SELECT * FROM upcoming_movies WHERE id = :id")
    fun getMovie(id: Long): Movie?

    @Query("SELECT * FROM upcoming_movies WHERE id = :id")
    fun getSingleMovie(id: Long): Single<Movie>

    @Query("DELETE FROM upcoming_movies")
    fun deleteAllRows(): Int

    @Query("SELECT * FROM upcoming_movies")
    fun getAllMovies(): List<Movie>

    @Query("UPDATE upcoming_movies SET posters = :posters WHERE id = :id")
    fun updatePosters(id: Long, posters: List<Posters>): Int
}
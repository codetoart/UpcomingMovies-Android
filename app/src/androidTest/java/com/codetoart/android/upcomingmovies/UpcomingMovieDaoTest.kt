package com.codetoart.android.upcomingmovies

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.codetoart.android.upcomingmovies.data.local.TmdbDb
import com.codetoart.android.upcomingmovies.data.local.UpcomingMovieDao
import com.codetoart.android.upcomingmovies.data.model.Movie
import com.codetoart.android.upcomingmovies.data.remote.TmdbApi
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class UpcomingMovieDaoTest {

    private val appContext = InstrumentationRegistry.getInstrumentation().context

    private lateinit var tmdbDb: TmdbDb

    private lateinit var upcomingMovieDao: UpcomingMovieDao

    private val objectMapper = ObjectMapperSingleton.get()

    @Before
    fun initDb() {

        tmdbDb = Room.inMemoryDatabaseBuilder(appContext, TmdbDb::class.java)
            .allowMainThreadQueries()
            .build()

        upcomingMovieDao = tmdbDb.upcomingMovieDao()
    }

    @After
    fun closeDb() {

        tmdbDb.close()
    }

    @Test
    fun getMovieWithInvalidId_returnsNull() {

        val movieFromDb = upcomingMovieDao.getMovie(101)
        assertNull(movieFromDb)
    }

    @Test
    fun insertAndGet_returnsNotNull() {

        val mockMinionMovie = objectMapper.fromJson(MockMinionsMovieData.detailsResponse, Movie::class.java)!!
        upcomingMovieDao.insert(mockMinionMovie)
        val movieFromDb = upcomingMovieDao.getMovie(mockMinionMovie.id)

        assertNotNull(movieFromDb)
    }

    @Test
    fun updatePostersTest() {

        val mockMinionMovie = objectMapper.fromJson(MockMinionsMovieData.detailsResponse, Movie::class.java)!!
        upcomingMovieDao.insert(mockMinionMovie)

        val mockMinionImageResponse = objectMapper.fromJson(
            MockMinionsMovieData.imagesResponse,
            TmdbApi.ImagesResponse::class.java
        )!!

        val noOfRowsUpdated = upcomingMovieDao.updatePosters(mockMinionMovie.id, mockMinionImageResponse.posters)
        assertEquals(1, noOfRowsUpdated)
    }
}

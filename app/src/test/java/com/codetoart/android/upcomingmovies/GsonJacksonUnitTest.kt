package com.codetoart.android.upcomingmovies

import com.codetoart.android.upcomingmovies.data.model.Movie
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class GsonJacksonUnitTest {

    private val gson = GsonSingleton.get()
    private val objectMapper = ObjectMapperSingleton.get()

    @Test
    fun gsonReturnsNullString() {

        val jsonString: String? = gson.toJson(null)
        assertEquals("null", jsonString)
    }

    @Test
    fun jacksonReturnsNullString() {

        val jsonString: String? = objectMapper.writeValueAsString(null)
        assertEquals("null", jsonString)
    }

    @Test
    fun gsonReturnsNullObject() {

        val json: String? = null
        val movie = gson.fromJson(json, Movie::class.java)
        assertEquals(null, movie)
    }

    @Test
    fun jacksonDoesNotReturnsNullObject() {

        val json: String? = null
        var exceptionThrown = false
        val movie = try {
            objectMapper.readValue(json, Movie::class.java)
        } catch (e: Exception) {
            exceptionThrown = true
            null
        }
        assertEquals(null, movie)
        assertTrue(exceptionThrown)
    }

    @Test
    fun jacksonCustomExtensionReturnsNullObject() {

        val json: String? = null
        val movie = objectMapper.fromJson(json, Movie::class.java)
        assertEquals(null, movie)
    }
}

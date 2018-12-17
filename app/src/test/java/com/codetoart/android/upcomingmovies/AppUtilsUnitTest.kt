package com.codetoart.android.upcomingmovies

import com.codetoart.android.upcomingmovies.data.model.Configuration
import com.codetoart.android.upcomingmovies.util.AppUtil
import org.junit.Assert.assertEquals
import org.junit.Test

class AppUtilsUnitTest {

    private val objectMapper = ObjectMapperSingleton.get()

    @Test
    fun getImagePosterUrl() {

        val configuration = objectMapper.fromJson(MockConfiguration.actualMock, Configuration::class.java)!!

        var url = AppUtil.getImagePosterUrl(configuration, 0)
        println(url)
        assertEquals(configuration.images.secureBaseUrl + "w92", url)

        url = AppUtil.getImagePosterUrl(configuration, 150)
        println(url)
        assertEquals(configuration.images.secureBaseUrl + "w154", url)

        url = AppUtil.getImagePosterUrl(configuration, 300)
        println(url)
        assertEquals(configuration.images.secureBaseUrl + "w342", url)

        url = AppUtil.getImagePosterUrl(configuration, 342)
        println(url)
        assertEquals(configuration.images.secureBaseUrl + "w342", url)

        url = AppUtil.getImagePosterUrl(configuration, 800)
        println(url)
        assertEquals(configuration.images.secureBaseUrl + configuration.images.posterSizes.last(), url)

        url = AppUtil.getImagePosterUrl(configuration)
        println(url)
        assertEquals(configuration.images.secureBaseUrl + configuration.images.posterSizes.last(), url)
    }
}

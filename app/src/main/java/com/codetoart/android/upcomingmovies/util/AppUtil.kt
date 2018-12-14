package com.codetoart.android.upcomingmovies.util

import com.codetoart.android.upcomingmovies.data.model.Configuration

class AppUtil {

    companion object {

        val LOG_TAG: String = AppUtil::class.java.simpleName

        fun getImagePosterUrl(configuration: Configuration, imageWidthPixels: Int? = null): String {

            if (imageWidthPixels == null) {
                return configuration.images.secureBaseUrl + configuration.images.posterSizes.last()
            }

            val posterSize = configuration.images.posterSizes.filter {
                val regex = "w[0-9]+".toRegex()
                regex.matches(it)
            }.map {
                it.substring(1).toInt()
            }.sorted().find {
                it >= imageWidthPixels
            }.let {
                if (it == null) {
                    configuration.images.posterSizes.last()
                } else {
                    "w$it"
                }
            }

            return configuration.images.secureBaseUrl + posterSize
        }
    }
}
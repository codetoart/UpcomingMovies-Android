package com.codetoart.android.upcomingmovies

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

class ObjectMapperSingleton {

    companion object {

        @Volatile
        private var singleton: ObjectMapper? = null

        fun get(): ObjectMapper =
            singleton ?: synchronized(this) {
                singleton ?: ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .registerKotlinModule()
            }
    }
}

// See GsonJacksonUnitTest#jacksonDoesNotReturnsNullObject and
// GsonJacksonUnitTest#jacksonCustomExtensionReturnsNullObject for difference
fun <T> ObjectMapper.fromJson(json: String?, clazz: Class<T>): T? {
    return try {
        this.readValue(json, clazz)
    } catch (e: Exception) {
        null
    }
}
package com.codetoart.android.upcomingmovies.data.remote

import com.codetoart.android.upcomingmovies.data.model.Configuration
import com.codetoart.android.upcomingmovies.data.model.UpcomingMovieResponse
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.reactivex.Observable
import okhttp3.HttpUrl
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {

    @GET("movie/upcoming")
    @GsonConverter
    fun getUpcomingMovies(@Query("api_key") apiKey: String, @Query("page") page: Int): Call<UpcomingMovieResponse>

    @GET("configuration")
    @GsonConverter
    fun getConfiguration(@Query("api_key") apiKey: String): Call<Configuration>

    @GET("movie/upcoming")
    @GsonConverter
    fun getObservableUpcomingMovies(@Query("api_key") apiKey: String, @Query("page") page: Int): Observable<UpcomingMovieResponse>

    @GET("configuration")
    @GsonConverter
    fun getObservableConfiguration(@Query("api_key") apiKey: String): Observable<Configuration>

    companion object {

        const val BASE_URL = "https://api.themoviedb.org/3/"

        private fun create(): TmdbApi = create(HttpUrl.parse(BASE_URL)!!)
        fun create(httpUrl: HttpUrl): TmdbApi {

            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    QualifiedTypeConverterFactory(
                        JacksonConverterFactory.create(
                            ObjectMapper()
                                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                .registerKotlinModule()
                        ),
                        GsonConverterFactory.create()
                    )
                )
                .build()
                .create(TmdbApi::class.java)
        }

        @Volatile
        private var singleton: TmdbApi? = null

        fun get(): TmdbApi =
            singleton ?: synchronized(this) {
                singleton ?: create().also { singleton = it }
            }
    }
}
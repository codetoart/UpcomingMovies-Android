package com.codetoart.android.upcomingmovies.ui.api

import com.codetoart.android.upcomingmovies.ui.model.UpcomingResponse
import okhttp3.HttpUrl
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {

    @GET("movie/upcoming") @GsonConverter
    fun getUpcomingMovies(@Query("api_key") apiKey: String, @Query("page") page: Int): Call<UpcomingResponse>

    companion object {

        const val BASE_URL = "https://api.themoviedb.org/3/"

        fun create(): TmdbApi = create(HttpUrl.parse(BASE_URL)!!)
        fun create(httpUrl: HttpUrl): TmdbApi {

            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .addConverterFactory(
                    QualifiedTypeConverterFactory(
                        JacksonConverterFactory.create(),
                        GsonConverterFactory.create()
                    )
                )
                .build()
                .create(TmdbApi::class.java)
        }
    }
}
package com.codetoart.android.upcomingmovies.data.remote

import com.codetoart.android.upcomingmovies.data.model.Configuration
import com.codetoart.android.upcomingmovies.data.model.Movie
import com.codetoart.android.upcomingmovies.data.model.Posters
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import okhttp3.HttpUrl
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("configuration")
    @GsonConverter
    fun getConfiguration(@Query("api_key") apiKey: String): Call<Configuration>

    @GET("configuration")
    @GsonConverter
    fun getSingleConfiguration(@Query("api_key") apiKey: String): Single<Configuration>

    @GET("movie/upcoming")
    @GsonConverter
    fun getSingleUpcomingMovies(@Query("api_key") apiKey: String, @Query("page") page: Int): Single<UpcomingMovieResponse>

    @GET("movie/{movie_id}/images")
    @GsonConverter
    fun getSingleMovieImages(@Path("movie_id") id: Long, @Query("api_key") apiKey: String): Single<ImagesResponse>

    @GET("movie/{movie_id}")
    @GsonConverter
    fun getSingleMovieDetails(@Path("movie_id") id: Long, @Query("api_key") apiKey: String): Single<Movie>

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

    data class UpcomingMovieResponse(

        val results: List<Movie>,

        val page: Int = -1,

        @SerializedName("total_pages")
        @JsonProperty("total_pages")
        val totalPages: Int = -1
    )

    data class ImagesResponse(

        val posters: List<Posters>
    )
}
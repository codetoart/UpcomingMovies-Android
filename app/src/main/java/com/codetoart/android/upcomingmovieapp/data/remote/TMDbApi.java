package com.codetoart.android.upcomingmovieapp.data.remote;

import android.content.Context;

import com.codetoart.android.upcomingmovieapp.BuildConfig;
import com.codetoart.android.upcomingmovieapp.data.local.PreferencesHelper;
import com.codetoart.android.upcomingmovieapp.data.model.Image;
import com.codetoart.android.upcomingmovieapp.data.model.Movie;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Mahavir on 9/1/16.
 */
public interface TMDbApi {
    public static final String API_KEY = "b7cd3340a794e5a2f35e3abb820b497f";

    /*@GET("configuration")
    public Call<Response.Metadata> getConfiguration(@Query("api_key") String apikey);*/

    @GET("configuration")
    public Observable<Response.Metadata> getConfiguration(@Query("api_key") String apikey);

    /*@GET("movie/upcoming")
    public Call<MovieResponse> upcomingMovies(@Query("api_key") String apikey);*/
    @GET("movie/upcoming")
    public Observable<Response.MovieResponse> upcomingMovies(@Query("api_key") String apikey);

    @GET("movie/{movie_id}/images")
    public Observable<Response.ImageResponse> getImages(@Path("movie_id")
                                                 String movieId, @Query("api_key") String apiKey);

    class Response {
        public static class ImageMetadata {
            @JsonProperty("base_url")
            String baseUrl;
            @JsonProperty("secure_base_url")
            String secureBaseUrl;
            @JsonProperty("poster_sizes")
            ArrayList<String> posterSizes;

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }

            public String getSecureBaseUrl() {
                return secureBaseUrl;
            }

            public void setSecureBaseUrl(String secureBaseUrl) {
                this.secureBaseUrl = secureBaseUrl;
            }

            public ArrayList<String> getPosterSizes() {
                return posterSizes;
            }

            public void setPosterSizes(ArrayList<String> posterSizes) {
                this.posterSizes = posterSizes;
            }
        }

        public static class Metadata {
            ImageMetadata images;

            public ImageMetadata getImages() {
                return images;
            }

            public void setImages(ImageMetadata images) {
                this.images = images;
            }

            public void save(PreferencesHelper preferencesHelper) {
                int len = images.posterSizes.size();
                preferencesHelper.putThumbnailBaseImageUrl(images.baseUrl +
                        images.posterSizes.get(0));
                preferencesHelper.putMediumBaseImageUrl(images.baseUrl +
                        images.posterSizes.get(len - 2));
                preferencesHelper.putOriginalBaseImageUrl(images.baseUrl +
                        images.posterSizes.get(len - 1));
            }
        }

        public static class MovieResponse {
            @JsonProperty("page")
            int page;
            @JsonProperty("results")
            ArrayList<Movie> results;
            @JsonProperty("dates")
            Date dates;
            @JsonProperty("total_pages")
            int totalPages;
            @JsonProperty("total_results")
            int totalResults;

            public  MovieResponse(){}

            public int getTotalPages() {
                return totalPages;
            }

            public void setTotalPages(int totalPages) {
                this.totalPages = totalPages;
            }

            public ArrayList<Movie> getResults() {
                return results;
            }

            public void setResults(ArrayList<Movie> movies) {
                this.results = movies;
            }

            public int getPage() {
                return page;
            }

            public void setPage(int page) {
                this.page = page;
            }

            public Date getDates() {
                return dates;
            }

            public void setDates(Date dates) {
                this.dates = dates;
            }

            public int getTotalResults() {
                return totalResults;
            }

            public void setTotalResults(int totalResults) {
                this.totalResults = totalResults;
            }
        }

        public static class Date {
            String maximum;
            String minimum;

            public  Date(){}

            public String getMaximum() {
                return maximum;
            }

            public void setMaximum(String maximum) {
                this.maximum = maximum;
            }

            public String getMinimum() {
                return minimum;
            }

            public void setMinimum(String minimum) {
                this.minimum = minimum;
            }
        }

        public static class ImageResponse {
            String id;
            ArrayList<Image> backdrops;
            ArrayList<Image> posters;

            public  ImageResponse(){}

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public ArrayList<Image> getBackdrops() {
                return backdrops;
            }

            public void setBackdrops(ArrayList<Image> backdrops) {
                this.backdrops = backdrops;
            }

            public ArrayList<Image> getPosters() {
                return posters;
            }

            public void setPosters(ArrayList<Image> posters) {
                this.posters = posters;
            }
        }
    }
}

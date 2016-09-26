package com.codetoart.android.upcomingmovieapp.data.remote;

import android.content.Context;

import com.codetoart.android.upcomingmovieapp.BuildConfig;
import com.codetoart.android.upcomingmovieapp.data.local.PreferencesHelper;
import com.codetoart.android.upcomingmovieapp.data.model.Image;
import com.codetoart.android.upcomingmovieapp.data.model.Movie;
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

/**
 * Created by Mahavir on 9/1/16.
 */
public interface TMDbApi {
    public String ENDPOINT = "https://api.themoviedb.org/3/";
    public static final String API_KEY ="b7cd3340a794e5a2f35e3abb820b497f" ;

    /*@GET("configuration")
    public Call<Response.Metadata> getConfiguration(@Query("api_key") String apikey);*/

    @GET("configuration")
    public Observable<Response.Metadata> getConfiguration(@Query("api_key") String apikey);

    /*@GET("movie/upcoming")
    public Call<MovieResponse> upcomingMovies(@Query("api_key") String apikey);*/
    @GET("movie/upcoming")
    public Observable<Response.MovieResponse> upcomingMovies(@Query("api_key") String apikey);

    @GET("movie/{movie_id}/images")
    public Observable<Response.ImageResponse> getImages(@Path("movie_id") String movieId, @Query("api_key") String apikey);

    /******** Factory class that sets up a new TMDbApi *******/
    class Factory {

        public static TMDbApi makeTMDbApi(Context context) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                    : HttpLoggingInterceptor.Level.NONE);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TMDbApi.ENDPOINT)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                    .build();
            return retrofit.create(TMDbApi.class);
        }

    }

    class Response {
        public static class ImageMetadata {
            private String base_url;
            private String secure_base_url;
            private ArrayList<String> poster_sizes;

            public String getBase_url() {
                return base_url;
            }

            public void setBase_url(String base_url) {
                this.base_url = base_url;
            }

            public String getSecure_base_url() {
                return secure_base_url;
            }

            public void setSecure_base_url(String secure_base_url) {
                this.secure_base_url = secure_base_url;
            }

            public ArrayList<String> getPoster_sizes() {
                return poster_sizes;
            }

            public void setPoster_sizes(ArrayList<String> poster_sizes) {
                this.poster_sizes = poster_sizes;
            }
        }

        public static class Metadata {
            private ImageMetadata images;

            public ImageMetadata getImages() {
                return images;
            }

            public void setImages(ImageMetadata images) {
                this.images = images;
            }

            public void save(PreferencesHelper preferencesHelper){
                int len = images.poster_sizes.size();
                preferencesHelper.putThumbnailBaseImageUrl(images.base_url+images.poster_sizes.get(0));
                preferencesHelper.putMediumBaseImageUrl(images.base_url+images.poster_sizes.get(len-2));
                preferencesHelper.putOriginalBaseImageUrl(images.base_url+images.poster_sizes.get(len-1));
            }
        }

        public static class MovieResponse {
            private int page;
            private ArrayList<Movie> results;
            private Date dates;
            private int total_pages;
            private int total_results;

            public  MovieResponse(){}

            public int getTotal_pages() {
                return total_pages;
            }

            public void setTotal_pages(int total_pages) {
                this.total_pages = total_pages;
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

            public int getTotal_results() {
                return total_results;
            }

            public void setTotal_results(int total_results) {
                this.total_results = total_results;
            }
        }

        public static class Date {
            private String maximum;
            private String minimum;

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
            private String id;
            private ArrayList<Image> backdrops;
            private ArrayList<Image> posters;

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

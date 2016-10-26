package com.codetoart.android.upcomingmovieapp.common;

import com.codetoart.android.upcomingmovieapp.data.model.Image;
import com.codetoart.android.upcomingmovieapp.data.model.Movie;
import com.codetoart.android.upcomingmovieapp.data.remote.TMDbApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MockModelFactory {

    public static String randomString() {
        return UUID.randomUUID().toString();
    }

    public static List<String> randomStringArrayList(int size){
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            arrayList.add(randomString());
        }

        return arrayList;
    }

    public static TMDbApi.Response.Metadata newMetadata(){
        TMDbApi.Response.Metadata metadata = new TMDbApi.Response.Metadata();
        TMDbApi.Response.ImageMetadata imageMetadata = new TMDbApi.Response.ImageMetadata();
        imageMetadata.setBaseUrl(randomString());
        imageMetadata.setSecureBaseUrl(randomString());
        imageMetadata.setPosterSizes((ArrayList<String>) randomStringArrayList(5));
        metadata.setImages(imageMetadata);
        return metadata;
    }

    public static Movie newMovie(){
        Random random = new Random(10);
        Movie movie = new Movie();
        movie.setAdult(randomString());
        movie.setBackdropPath(randomString());
        movie.setId(randomString());
        movie.setOriginalLanguage(randomString());
        movie.setOriginalTitle(randomString());
        movie.setOverview(randomString());
        movie.setPopularity(random.nextFloat());
        movie.setPosterPath(randomString());
        movie.setBackdropPath(randomString());
        movie.setVoteAverage(random.nextFloat());
        movie.setVoteCount(random.nextInt(1000));
        return movie;
    }

    public static List<Movie> newMovieList(int size){
        ArrayList<Movie> movies = new ArrayList<>();
        for (int i=0; i<size; i++){
            Movie movie = newMovie();
            movies.add(movie);
        }

        return movies;
    }

    public static TMDbApi.Response.MovieResponse newMovieResponse(){
        Random random = new Random();
        TMDbApi.Response.MovieResponse movieResponse = new TMDbApi.Response.MovieResponse();
        movieResponse.setDates(newDate());
        movieResponse.setPage(random.nextInt(10));
        movieResponse.setTotalPages(random.nextInt(100));
        movieResponse.setResults((ArrayList<Movie>) newMovieList(20));
        movieResponse.setTotalResults(random.nextInt(200));
        return movieResponse;
    }

    public static TMDbApi.Response.MovieResponse newEmptyMovieResponse(){
        TMDbApi.Response.MovieResponse movieResponse = new TMDbApi.Response.MovieResponse();
        movieResponse.setDates(newDate());
        movieResponse.setPage(1);
        movieResponse.setTotalPages(1);
        movieResponse.setResults((ArrayList<Movie>) newMovieList(0));
        movieResponse.setTotalResults(0);
        return movieResponse;
    }

    public static TMDbApi.Response.Date newDate(){
        TMDbApi.Response.Date date = new TMDbApi.Response.Date();
        date.setMaximum(randomString());
        date.setMinimum(randomString());
        return date;
    }

    public static Image newImage(){
        Random random = new Random();
        Image image = new Image();
        image.setAspectRatio(randomString());
        image.setFilePath(randomString());
        image.setVoteAverage(random.nextFloat());
        image.setVoteCount(random.nextFloat());
        image.setHeight(random.nextInt(500));
        image.setWidth(random.nextInt(500));
        return image;
    }

    public static TMDbApi.Response.ImageResponse newImageResponse(){
        TMDbApi.Response.ImageResponse imageResponse = new TMDbApi.Response.ImageResponse();
        imageResponse.setId(randomString());

        ArrayList<Image> backDrops = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Image image = newImage();
            backDrops.add(image);
        }
        imageResponse.setBackdrops(backDrops);

        ArrayList<Image> posters = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Image image = newImage();
            posters.add(image);
        }
        imageResponse.setPosters(posters);

        return imageResponse;
    }

}

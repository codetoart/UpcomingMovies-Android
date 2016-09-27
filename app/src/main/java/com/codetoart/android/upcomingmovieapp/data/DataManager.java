package com.codetoart.android.upcomingmovieapp.data;

import com.codetoart.android.upcomingmovieapp.data.local.PreferencesHelper;
import com.codetoart.android.upcomingmovieapp.data.remote.TMDbApi;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by mahavir on 9/1/16.
 */
@Singleton
public class DataManager {
    private final TMDbApi mTMDbApi;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(TMDbApi tmDbApi, PreferencesHelper preferencesHelper) {
        this.mTMDbApi = tmDbApi;
        this.mPreferencesHelper = preferencesHelper;
    }

    /*public void getConfiguration(final MainPresenter.ConfigurationCallback callback){
        Call<TMDbApi.Response.Metadata> metadataCall = mTMDbApi.getConfiguration(TMDbApi.API_KEY);
        metadataCall.enqueue(new Callback<TMDbApi.Response.Metadata>() {
            @Override
            public void onResponse(Call<TMDbApi.Response.Metadata> call,
                Response<TMDbApi.Response.Metadata> response) {
                callback.onConfiguration(response.body());
            }

            @Override
            public void onFailure(Call<TMDbApi.Response.Metadata> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void loadMovies(final MainPresenter.LoadMoviesCallback callback){
        Call<MovieResponse> movieResponseCall = mTMDbApi.upcomingMovies(TMDbApi.API_KEY);
        movieResponseCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                callback.onLoadMovies(response.body().getResults());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }*/

    public Observable<TMDbApi.Response.Metadata> getConfiguration() {
        return mTMDbApi.getConfiguration(TMDbApi.API_KEY);
    }

    public Observable<TMDbApi.Response.MovieResponse> getMovies() {
        return mTMDbApi.upcomingMovies(TMDbApi.API_KEY);
    }

    public Observable<TMDbApi.Response.ImageResponse> getImages(String movieId) {
        return mTMDbApi.getImages(movieId, TMDbApi.API_KEY);
    }
}

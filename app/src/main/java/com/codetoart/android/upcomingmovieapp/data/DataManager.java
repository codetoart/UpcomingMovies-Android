package com.codetoart.android.upcomingmovieapp.data;

import com.codetoart.android.upcomingmovieapp.data.local.DbHelper;
import com.codetoart.android.upcomingmovieapp.data.local.PreferencesHelper;
import com.codetoart.android.upcomingmovieapp.data.model.Movie;
import com.codetoart.android.upcomingmovieapp.data.remote.TMDbApi;

import org.greenrobot.greendao.rx.RxDao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by mahavir on 9/1/16.
 */
@Singleton
public class DataManager {
    private final TMDbApi mTMDbApi;
    private final PreferencesHelper mPreferencesHelper;
    private final DbHelper mDbHelper;

    @Inject
    public DataManager(TMDbApi tmDbApi, PreferencesHelper preferencesHelper, DbHelper dbHelper) {
        this.mTMDbApi = tmDbApi;
        this.mPreferencesHelper = preferencesHelper;
        this.mDbHelper = dbHelper;
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
        return mTMDbApi.getConfiguration(TMDbApi.API_KEY)
                .doOnNext(new Action1<TMDbApi.Response.Metadata>() {
                    @Override
                    public void call(TMDbApi.Response.Metadata metadata) {
                        metadata.save(mPreferencesHelper);
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends TMDbApi.Response.Metadata>>() {
                    @Override
                    public Observable<? extends TMDbApi.Response.Metadata> call(Throwable throwable) {
                        TMDbApi.Response.Metadata metadata = new TMDbApi.Response.Metadata();
                        return Observable.just(metadata);
                    }
                });
    }

    public Observable<TMDbApi.Response.MovieResponse> getMovies() {
        return mTMDbApi.upcomingMovies(TMDbApi.API_KEY)
                .doOnNext(new Action1<TMDbApi.Response.MovieResponse>() {
                    @Override
                    public void call(TMDbApi.Response.MovieResponse movieResponse) {
                        for(Movie movie : movieResponse.getResults()){
                            mDbHelper.insertOrReplace(movie);
                        }
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends TMDbApi.Response.MovieResponse>>() {
                    @Override
                    public Observable<? extends TMDbApi.Response.MovieResponse> call(Throwable throwable) {
                        return mDbHelper.getLocalMovies(throwable);
                    }
                });

        /*return mTMDbApi.upcomingMovies(TMDbApi.API_KEY)
                .map(new Func1<TMDbApi.Response.MovieResponse, List<Movie>>() {
                    @Override
                    public List<Movie> call(TMDbApi.Response.MovieResponse movieResponse) {
                        return movieResponse.getResults();
                    }
                })
                .flatMap(new Func1<List<Movie>, Observable<Movie>>() {
                    @Override
                    public Observable<Movie> call(List<Movie> movies) {
                        return Observable.from(movies);
                    }
                })
                .su*/
    }

    public Observable<TMDbApi.Response.ImageResponse> getImages(String movieId) {
        return mTMDbApi.getImages(movieId, TMDbApi.API_KEY);
    }
}

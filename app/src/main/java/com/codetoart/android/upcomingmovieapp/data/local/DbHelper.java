package com.codetoart.android.upcomingmovieapp.data.local;

import android.content.Context;

import com.codetoart.android.upcomingmovieapp.data.model.DaoMaster;
import com.codetoart.android.upcomingmovieapp.data.model.DaoSession;
import com.codetoart.android.upcomingmovieapp.data.model.Movie;
import com.codetoart.android.upcomingmovieapp.data.remote.TMDbApi;
import com.codetoart.android.upcomingmovieapp.injection.ApplicationContext;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.rx.RxDao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;


/**
 * Created by Mahavir on 10/7/16.
 */
@Singleton
public class DbHelper {
    private DaoSession mDaoSession;

    @Inject
    public DbHelper(@ApplicationContext Context context){
        DbOpenHelper helper = new DbOpenHelper(context);
        Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession(){
        return mDaoSession;
    }

    public RxDao<Movie, String> getMovieDao(){
        return mDaoSession.getMovieDao().rx();
    }

    public Observable<TMDbApi.Response.MovieResponse> getLocalMovies(final Throwable error){
        ArrayList<Movie> movies = (ArrayList<Movie>) mDaoSession.getMovieDao().loadAll();
        final TMDbApi.Response.MovieResponse movieResponse = new TMDbApi.Response.MovieResponse();
        movieResponse.setResults(movies);

        Observable<TMDbApi.Response.MovieResponse> movieResponseObservable = Observable.create(new Observable.OnSubscribe<TMDbApi.Response.MovieResponse>() {
            @Override
            public void call(Subscriber<? super TMDbApi.Response.MovieResponse> subscriber) {
                if (!movieResponse.getResults().isEmpty()){
                    subscriber.onNext(movieResponse);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(error);
                }
            }
        });

        return movieResponseObservable;
    }

    public void insertOrReplace(Movie movie){
        mDaoSession.getMovieDao().insertOrReplace(movie);
    }
}

package com.codetoart.android.upcomingmovieapp.ui.main;

import com.codetoart.android.upcomingmovieapp.data.DataManager;
import com.codetoart.android.upcomingmovieapp.data.local.PreferencesHelper;
import com.codetoart.android.upcomingmovieapp.data.model.Movie;
import com.codetoart.android.upcomingmovieapp.data.remote.TMDbApi;
import com.codetoart.android.upcomingmovieapp.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by Mahavir on 9/1/16.
 */
public class MainPresenter extends BasePresenter<MainMvpView> {
    private final DataManager mDataManager;
    private final PreferencesHelper mPreferenceHelper;
    private Subscription mSubscription;

    /*public static interface LoadMoviesCallback {
        public void onLoadMovies(List<Movie> movies);
        public void onError(Throwable throwable);
    }

    public static interface ConfigurationCallback {
        public void onConfiguration(TMDbApi.Response.Metadata metadata);
        public void onError(Throwable throwable);
    }*/

    @Inject
    public MainPresenter(DataManager dataManager, PreferencesHelper preferencesHelper) {
        this.mDataManager = dataManager;
        this.mPreferenceHelper = preferencesHelper;
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription!=null) mSubscription.unsubscribe();
    }

    /*public void getConfigurationAndLoadMovies(){
        getMvpView().showMovieProgress(true);
        mDataManager.getConfiguration(new ConfigurationCallback() {
            @Override
            public void onConfiguration(TMDbApi.Response.Metadata metadata) {
                metadata.save(mPreferenceHelper);
                loadMovies();
            }

            @Override
            public void onError(Throwable throwable) {
                getMvpView().showMovieProgress(false);
                getMvpView().showMovieLoadError(throwable);
            }
        });
    }

    public void loadMovies(){
        mDataManager.loadMovies(new LoadMoviesCallback() {
            @Override
            public void onLoadMovies(List<Movie> movies) {
                getMvpView().showMovieProgress(false);
                if (movies.isEmpty()){
                    getMvpView().showEmptyMessage();
                } else {
                    getMvpView().showMovies(movies);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                getMvpView().showMovieProgress(false);
                getMvpView().showMovieLoadError(throwable);
            }
        });
    }*/

    /*public void loadMovies(Context context){
        if (Util.isNetworkAvailable(context)){
            getConfigurationAndLoadMovies();
        } else {
            getMoviesFromDb();
        }
    }*/

    /*public void getConfigurationAndLoadMovies() {
        mSubscription = Observable.zip(mDataManager.getConfiguration(), mDataManager.getMovies(),
                new Func2<TMDbApi.Response.Metadata, TMDbApi.Response.MovieResponse,
                        List<Movie>>() {
                    @Override
                    public List<Movie> call(TMDbApi.Response.Metadata metadata,
                                            TMDbApi.Response.MovieResponse movieResponse) {
                        metadata.save(mPreferenceHelper);
                        //mDataManager.getMovieDao().insertOrReplaceInTx(movieResponse.getResults());
                        return movieResponse.getResults();
                    }
                })
                .flatMap(new Func1<List<Movie>, Observable<Iterable<Movie>>>() {
                    @Override
                    public Observable<Iterable<Movie>> call(List<Movie> movies) {
                        return mDataManager.getMovieDao().insertOrReplaceInTx(movies);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Iterable<Movie>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showMovieProgress(false);
                        getMvpView().showMovieLoadError(e);
                    }

                    @Override
                    public void onNext(Iterable<Movie> movies) {
                        getMvpView().showMovieProgress(false);
                        List<Movie> movieList = new ArrayList<>();
                        for(Movie movie:movies){
                            movieList.add(movie);
                        }

                        if (movieList.isEmpty()) {
                            getMvpView().showEmptyMessage();
                        } else {
                            getMvpView().showMovies(movieList);
                        }
                    }
                });
    }*/

    public void getConfigurationAndLoadMovies() {
        mSubscription = Observable.combineLatest(mDataManager.getConfiguration(), mDataManager.getMovies(),
                new Func2<TMDbApi.Response.Metadata, TMDbApi.Response.MovieResponse,
                        List<Movie>>() {
                    @Override
                    public List<Movie> call(TMDbApi.Response.Metadata metadata,
                                            TMDbApi.Response.MovieResponse movieResponse) {
                        if (metadata.getImages()!=null) metadata.save(mPreferenceHelper);
                        return movieResponse.getResults();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Movie>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showMovieProgress(false);
                        getMvpView().showMovieLoadError(e);
                    }

                    @Override
                    public void onNext(List<Movie> movies) {
                        getMvpView().showMovieProgress(false);

                        if (movies.isEmpty()) {
                            getMvpView().showEmptyMessage();
                        } else {
                            getMvpView().showMovies(movies);
                        }
                    }
                });
    }

    /*public void getMoviesFromDb(){
        mSubscription = mDataManager.getMovieDao().loadAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Movie>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showMovieProgress(false);
                        getMvpView().showMovieLoadError(e);
                    }

                    @Override
                    public void onNext(List<Movie> movies) {
                        getMvpView().showMovieProgress(false);
                        if (movies.isEmpty()) {
                            getMvpView().showEmptyMessage();
                        } else {
                            getMvpView().showMovies(movies);
                        }
                    }
                });
    }*/
}

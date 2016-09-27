package com.codetoart.android.upcomingmovieapp.ui.moviedetails;

import com.codetoart.android.upcomingmovieapp.data.DataManager;
import com.codetoart.android.upcomingmovieapp.data.remote.TMDbApi;
import com.codetoart.android.upcomingmovieapp.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Mahavir on 9/6/16.
 */
public class MovieDetailsPresenter extends BasePresenter<MovieDetailsMvpView> {
    private DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public MovieDetailsPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void getMovieDetails(String movieId) {
        mSubscription = mDataManager.getImages(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TMDbApi.Response.ImageResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(TMDbApi.Response.ImageResponse imageResponse) {
                        getMvpView().showMovie(imageResponse);
                    }
                });
    }
}

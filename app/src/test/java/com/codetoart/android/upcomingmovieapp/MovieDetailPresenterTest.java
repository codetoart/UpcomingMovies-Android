package com.codetoart.android.upcomingmovieapp;

import com.codetoart.android.upcomingmovieapp.common.MockModelFactory;
import com.codetoart.android.upcomingmovieapp.data.DataManager;
import com.codetoart.android.upcomingmovieapp.data.remote.TMDbApi;
import com.codetoart.android.upcomingmovieapp.ui.moviedetails.MovieDetailsMvpView;
import com.codetoart.android.upcomingmovieapp.ui.moviedetails.MovieDetailsPresenter;
import com.codetoart.android.upcomingmovieapp.util.RxJavaResetRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * Created by Mahavir on 10/24/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class MovieDetailPresenterTest {
    @Mock
    MovieDetailsMvpView mMockMovieDetailsMvpView;
    @Mock
    DataManager mMockDataManager;

    private MovieDetailsPresenter mMovieDetailsPresenter;

    @Rule
    public final RxJavaResetRule mRxJavaResetRule = new RxJavaResetRule();

    @Before
    public void setup(){
        mMovieDetailsPresenter = new MovieDetailsPresenter(mMockDataManager);
        mMovieDetailsPresenter.attachView(mMockMovieDetailsMvpView);
    }

    @Test
    public void testGetMovieDetails(){
        TMDbApi.Response.ImageResponse imageResponse = MockModelFactory.newImageResponse();
        doReturn(Observable.just(imageResponse))
                .when(mMockDataManager)
                .getImages("adsd");

        mMovieDetailsPresenter.getMovieDetails("adsd");
        verify(mMockMovieDetailsMvpView).showMovie(imageResponse);
    }

    @Test
    public void testGetMovieDetailsFail(){
        Throwable throwable = new RuntimeException();
        doReturn(Observable.error(throwable))
                .when(mMockDataManager)
                .getImages("adsd");

        mMovieDetailsPresenter.getMovieDetails("adsd");
        verify(mMockMovieDetailsMvpView).showError(throwable.getLocalizedMessage());
    }
}

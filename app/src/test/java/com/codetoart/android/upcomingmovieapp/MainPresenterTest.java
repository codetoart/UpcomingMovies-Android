package com.codetoart.android.upcomingmovieapp;

import com.codetoart.android.upcomingmovieapp.common.MockModelFactory;
import com.codetoart.android.upcomingmovieapp.data.DataManager;
import com.codetoart.android.upcomingmovieapp.data.local.PreferencesHelper;
import com.codetoart.android.upcomingmovieapp.data.remote.TMDbApi;
import com.codetoart.android.upcomingmovieapp.ui.main.MainMvpView;
import com.codetoart.android.upcomingmovieapp.ui.main.MainPresenter;
import com.codetoart.android.upcomingmovieapp.util.RxJavaResetRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import rx.Observable;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * Created by Mahavir on 10/21/16.
 */

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {
    @Mock
    MainMvpView mMockMainMvpView;
    @Mock
    DataManager mMockDataManager;
    @Mock
    PreferencesHelper mMockPreferenceHelper;

    private MainPresenter mMainPresenter;

    @Rule
    public final RxJavaResetRule mRxJavaResetRule = new RxJavaResetRule();

    @Before
    public void setup(){
        mMainPresenter = new MainPresenter(mMockDataManager, mMockPreferenceHelper);
        mMainPresenter.attachView(mMockMainMvpView);
    }

    @Test
    public void testGetConfigurationAndLoadMovies(){
        doReturn(Observable.just(MockModelFactory.newMetadata()))
                .when(mMockDataManager)
                .getConfiguration();

        TMDbApi.Response.MovieResponse movieResponse = MockModelFactory.newMovieResponse();
        doReturn(Observable.just(movieResponse))
                .when(mMockDataManager)
                .getMovies();

        mMainPresenter.getConfigurationAndLoadMovies();

        verify(mMockMainMvpView).showMovieProgress(false);
        verify(mMockMainMvpView).showMovies(movieResponse.getResults());
    }

    @Test
    public void testGetConfigurationFailAndLoadMovies(){
        Throwable throwable = new RuntimeException();
        doReturn(Observable.error(throwable))
                .when(mMockDataManager)
                .getConfiguration();

        TMDbApi.Response.MovieResponse movieResponse = MockModelFactory.newMovieResponse();
        doReturn(Observable.just(movieResponse))
                .when(mMockDataManager)
                .getMovies();

        mMainPresenter.getConfigurationAndLoadMovies();

        verify(mMockMainMvpView).showMovieProgress(false);
        verify(mMockMainMvpView).showMovieLoadError(throwable);
    }

    @Test
    public void testGetConfigurationAndLoadMoviesFail(){
        doReturn(Observable.just(MockModelFactory.newMetadata()))
                .when(mMockDataManager)
                .getConfiguration();

        Throwable throwable = new RuntimeException();
        doReturn(Observable.error(throwable))
                .when(mMockDataManager)
                .getMovies();

        mMainPresenter.getConfigurationAndLoadMovies();

        verify(mMockMainMvpView).showMovieProgress(false);
        verify(mMockMainMvpView).showMovieLoadError(throwable);
    }

    @Test
    public void testEmptyMovies(){
        doReturn(Observable.just(MockModelFactory.newMetadata()))
                .when(mMockDataManager)
                .getConfiguration();

        TMDbApi.Response.MovieResponse movieResponse = MockModelFactory.newEmptyMovieResponse();
        doReturn(Observable.just(movieResponse))
                .when(mMockDataManager)
                .getMovies();

        mMainPresenter.getConfigurationAndLoadMovies();

        verify(mMockMainMvpView).showMovieProgress(false);
        verify(mMockMainMvpView).showEmptyMessage();
    }
}

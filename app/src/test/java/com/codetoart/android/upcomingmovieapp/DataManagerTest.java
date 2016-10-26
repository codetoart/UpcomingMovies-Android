package com.codetoart.android.upcomingmovieapp;

import com.codetoart.android.upcomingmovieapp.common.MockModelFactory;
import com.codetoart.android.upcomingmovieapp.data.DataManager;
import com.codetoart.android.upcomingmovieapp.data.local.DbHelper;
import com.codetoart.android.upcomingmovieapp.data.local.PreferencesHelper;
import com.codetoart.android.upcomingmovieapp.data.model.Movie;
import com.codetoart.android.upcomingmovieapp.data.remote.TMDbApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * Created by Mahavir on 10/24/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {
    @Mock
    TMDbApi mMockTmdbApi;
    @Mock
    PreferencesHelper mMockPreferencesHelper;
    @Mock
    DbHelper mMockDbHelper;

    DataManager mDataManager;

    @Before
    public void setup(){
        mDataManager = new DataManager(mMockTmdbApi, mMockPreferencesHelper, mMockDbHelper);
    }

    @Test
    public void testGetConfigurationSuccess(){
        //TMDbApi.Response.Metadata metadata = MockModelFactory.newMetadata();
        TMDbApi.Response.Metadata metadata = Mockito.mock(TMDbApi.Response.Metadata.class);
        doReturn(Observable.just(metadata))
                .when(mMockTmdbApi)
                .getConfiguration(TMDbApi.API_KEY);

        TestSubscriber<TMDbApi.Response.Metadata> testSubscriber = new TestSubscriber<>();
        mDataManager.getConfiguration().subscribe(testSubscriber);

        testSubscriber.assertCompleted();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(metadata));
        verify(metadata).save(mMockPreferencesHelper);
    }

    @Test
    public void testGetConfigurationFail(){
        doReturn(Observable.error(new RuntimeException()))
                .when(mMockTmdbApi)
                .getConfiguration(TMDbApi.API_KEY);

        TestSubscriber<TMDbApi.Response.Metadata> testSubscriber = new TestSubscriber<>();
        mDataManager.getConfiguration().subscribe(testSubscriber);

        testSubscriber.assertCompleted();
        testSubscriber.assertValueCount(1);
    }

    @Test
    public void testGetMoviesSuccess(){
        TMDbApi.Response.MovieResponse movieResponse = MockModelFactory.newMovieResponse();
        doReturn(Observable.just(movieResponse))
                .when(mMockTmdbApi)
                .upcomingMovies(TMDbApi.API_KEY);

        TestSubscriber<TMDbApi.Response.MovieResponse> testSubscriber = new TestSubscriber<>();
        mDataManager.getMovies().subscribe(testSubscriber);

        for (Movie movie : movieResponse.getResults()){
            verify(mMockDbHelper).insertOrReplace(movie);
        }

        testSubscriber.assertCompleted();
        testSubscriber.assertValueCount(1);
    }

    @Test
    public void testGetMoviesFail(){
        Throwable throwable = new RuntimeException();
        doReturn(Observable.error(throwable))
                .when(mMockTmdbApi)
                .upcomingMovies(TMDbApi.API_KEY);
        TMDbApi.Response.MovieResponse movieResponse = MockModelFactory.newMovieResponse();
        doReturn(Observable.just(movieResponse))
                .when(mMockDbHelper)
                .getLocalMovies(throwable);

        TestSubscriber<TMDbApi.Response.MovieResponse> testSubscriber = new TestSubscriber<>();
        mDataManager.getMovies().subscribe(testSubscriber);

        testSubscriber.assertCompleted();
        testSubscriber.assertValueCount(1);
    }
}

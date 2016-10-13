package com.codetoart.android.upcomingmovieapp.ui.main;

import com.codetoart.android.upcomingmovieapp.data.model.Movie;
import com.codetoart.android.upcomingmovieapp.ui.base.MvpView;

import java.util.List;

/**
 * Created by Mahavir on 9/1/16.
 */
public interface MainMvpView extends MvpView {
    void showMovies(List<Movie> movies);

    void showMovieProgress(boolean show);

    void showEmptyMessage();

    void showMovieLoadError(Throwable throwable);
}

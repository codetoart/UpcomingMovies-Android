package com.codetoart.android.upcomingmovieapp.ui.moviedetails;

import com.codetoart.android.upcomingmovieapp.data.remote.TMDbApi;
import com.codetoart.android.upcomingmovieapp.ui.base.MvpView;

/**
 * Created by priyank on 9/6/16.
 */
public interface MovieDetailsMvpView extends MvpView {
    void showMovie(TMDbApi.Response.ImageResponse movie);

    void showError(String error);
}

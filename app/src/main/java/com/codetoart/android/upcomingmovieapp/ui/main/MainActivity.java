package com.codetoart.android.upcomingmovieapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codetoart.android.upcomingmovieapp.R;
import com.codetoart.android.upcomingmovieapp.data.model.Movie;
import com.codetoart.android.upcomingmovieapp.ui.base.BaseActivity;
import com.codetoart.android.upcomingmovieapp.ui.moviedetails.MovieDetailsActivity;
import com.codetoart.android.upcomingmovieapp.util.CImageLoader;
import com.codetoart.android.upcomingmovieapp.util.NetworkUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahavir on 9/1/16.
 */
public class MainActivity extends BaseActivity implements MainMvpView,
        MovieAdapter.MovieAdapterCallback {

    @Inject MainPresenter mMainPresenter;
    @Inject MovieAdapter mMovieAdapter;

    @BindView(R.id.recycler_view_movies) RecyclerView mRecyclerView;
    @BindView(R.id.text_no_movies) TextView mTextNoMovies;
    @BindView(R.id.progress) ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        activityComponent().inject(this);
        mMainPresenter.attachView(this);

        loadMovies();
        mMovieAdapter.setCallback(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mMovieAdapter);
    }

    private void loadMovies(){
        mMainPresenter.getConfigurationAndLoadMovies();
        /*if (NetworkUtil.isNetworkConnected(this)){
            mMainPresenter.getConfigurationAndLoadMovies();
        } else {
            mMainPresenter.getMoviesFromDb();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
        CImageLoader.stopImageLoader();
    }

    @Override
    public void showMovies(List<Movie> movies) {
        mTextNoMovies.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mMovieAdapter.setMovies(movies);
        mMovieAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMovieProgress(boolean show) {
        if (show && mMovieAdapter.getItemCount() == 0) {
            mProgress.setVisibility(View.VISIBLE);
        } else {
            mProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void showEmptyMessage() {
        mTextNoMovies.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showMovieLoadError(Throwable throwable) {
        mTextNoMovies.setText(throwable.getLocalizedMessage());
        mTextNoMovies.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onMovieClicked(Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(Movie.INTENT_MOVIE, movie);
        startActivity(intent);
    }
}

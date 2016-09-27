package com.codetoart.android.upcomingmovieapp.ui.moviedetails;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codetoart.android.upcomingmovieapp.R;
import com.codetoart.android.upcomingmovieapp.data.model.Movie;
import com.codetoart.android.upcomingmovieapp.data.remote.TMDbApi;
import com.codetoart.android.upcomingmovieapp.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by mahavir on 9/6/16.
 */
public class MovieDetailsActivity extends BaseActivity implements MovieDetailsMvpView {

    @Inject MovieDetailsPresenter mMovieDetailsPresenter;
    @Inject ViewPagerAdapter mViewPagerAdapter;

    @BindView(R.id.view_pager_image) ViewPager mViewPager;
    @BindView(R.id.indicator) CircleIndicator mIndicator;
    @BindView(R.id.text_title) TextView mTitleTextView;
    @BindView(R.id.text_description) TextView mDescTextView;
    @BindView(R.id.rating_movie) RatingBar mMovieRatingBar;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        activityComponent().inject(this);
        mMovieDetailsPresenter.attachView(this);

        mViewPager.setAdapter(mViewPagerAdapter);
        mIndicator.setViewPager(mViewPager);
        mViewPagerAdapter.registerDataSetObserver(mIndicator.getDataSetObserver());

        mMovie = getIntent().getParcelableExtra(Movie.INTENT_MOVIE);
        init();
        mMovieDetailsPresenter.getMovieDetails(mMovie.getId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMovieDetailsPresenter.detachView();
    }

    private void init() {
        mTitleTextView.setText(mMovie.getTitle());
        mDescTextView.setText(mMovie.getOverview());
        mMovieRatingBar.setRating(mMovie.getPopularity());
    }

    @Override
    public void showMovie(TMDbApi.Response.ImageResponse imageResponse) {
        mViewPagerAdapter.setImageArrayList(imageResponse.getPosters());
        mViewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String error) {
        Log.e("MovieDetailsActivity", "Error: " + error);
    }
}

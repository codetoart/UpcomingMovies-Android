package com.codetoart.android.upcomingmovieapp.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codetoart.android.upcomingmovieapp.R;
import com.codetoart.android.upcomingmovieapp.data.local.PreferencesHelper;
import com.codetoart.android.upcomingmovieapp.data.model.Movie;
import com.codetoart.android.upcomingmovieapp.util.CImageLoader;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mahavir on 9/1/16.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {
    private List<Movie> mMovies;
    private MovieAdapterCallback mCallback;
    @Inject PreferencesHelper mPreferencesHelper;

    @Inject
    public MovieAdapter() {
        mMovies = new ArrayList<>();
    }

    public void setMovies(List<Movie> movies) {
        this.mMovies.addAll(movies);
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_movie,
                parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        Movie movie = mMovies.get(position);
        holder.setMovie(movie);
        String imageUrl = mPreferencesHelper.getThumbnailBaseImageUrl() + movie.getPosterPath();
        holder.movie_name.setText(movie.getTitle());
        CImageLoader.displayImage(holder.poster_image.getContext(), imageUrl,
                holder.poster_image, R.drawable.place_holder);
        /*Glide.with(holder.poster_image.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.place_holder)
                .into(holder.poster_image);*/
    }

    public void setCallback(MovieAdapterCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    class MovieHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_poster) ImageView poster_image;
        @BindView(R.id.text_name) TextView movie_name;
        Movie movie;

        public MovieHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setMovie(Movie movie) {
            this.movie = movie;
        }

        @OnClick(R.id.row_layout)
        void onItemClicked(View view) {
            if (mCallback != null) mCallback.onMovieClicked(movie);
        }
    }

    public static interface MovieAdapterCallback {
        public void onMovieClicked(Movie movie);
    }
}

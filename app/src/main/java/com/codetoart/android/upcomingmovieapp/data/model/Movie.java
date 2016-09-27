package com.codetoart.android.upcomingmovieapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by jain on 4/12/2016.
 */
public class Movie implements Parcelable {
    public static final String INTENT_MOVIE = "Movie";

    @JsonProperty("poster_path")
    String posterPath;
    @JsonProperty("adult")
    String adult;
    @JsonProperty("overview")
    String overview;
    @JsonProperty("release_date")
    String releaseDate;
    @JsonProperty("id")
    String id;
    @JsonProperty("original_title")
    String originalTitle;
    @JsonProperty("original_language")
    String originalLanguage;
    @JsonProperty("title")
    String title;
    @JsonProperty("backdrop_path")
    String backdropPath;
    @JsonProperty("popularity")
    float popularity;
    @JsonProperty("vote_count")
    int voteCount;
    @JsonProperty("vote_average")
    float voteAverage;

    public Movie(){}

    protected Movie(Parcel in) {
        readFromParcel(in);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getFormattedReleaseDate() {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
        try {
            java.util.Date date = simpleDateFormat1.parse(releaseDate);
            releaseDate = simpleDateFormat2.format(date);
        } catch (ParseException e) {
            Log.e("Movie", "Exception => " + e.getLocalizedMessage());
        }
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in) {
        posterPath = in.readString();
        adult = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        id = in.readString();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        backdropPath = in.readString();
        popularity = in.readFloat();
        voteCount = in.readInt();
        voteAverage = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(adult);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(id);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeFloat(popularity);
        dest.writeInt(voteCount);
        dest.writeFloat(voteAverage);
    }
}

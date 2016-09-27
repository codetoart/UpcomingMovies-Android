package com.codetoart.android.upcomingmovieapp.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by jain on 4/12/2016.
 */
public class Image {
    @JsonProperty("aspect_ratio")
    String aspectRatio;
    @JsonProperty("file_path")
    String filePath;
    @JsonProperty("height")
    int height;
    @JsonProperty("vote_average")
    float voteAverage;
    @JsonProperty("vote_count")
    float voteCount;
    @JsonProperty("width")
    int width;

    public Image(){}

    public String getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(String aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public float getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(float voteCount) {
        this.voteCount = voteCount;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}

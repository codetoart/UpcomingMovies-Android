package com.codetoart.android.upcomingmovieapp.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.codetoart.android.upcomingmovieapp.data.model.Movie;
import com.codetoart.android.upcomingmovieapp.injection.ApplicationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "upcoming_movie_app_pref_file";

    private static final String PREF_KEY_MOVIES = "pref_key_movies";
    private static final String PREF_KEY_THUMBNAIL_BASE_IMAGE_URL = "pref_key_thumbnail_base_image_url";
    private static final String PREF_KEY_MEDIUM_BASE_IMAGE_URL = "pref_key_medium_base_image_url";
    private static final String PREF_KEY_ORIGINAL_BASE_IMAGE_URL = "pref_key_original_base_image_url";

    private final SharedPreferences mPref;
    private final ObjectMapper mObjectMapper;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        mObjectMapper = new ObjectMapper();
    }

    public void clear() {
        mPref.edit().clear().apply();
    }

    public void putThumbnailBaseImageUrl(String imageUrl) {
        mPref.edit().putString(PREF_KEY_THUMBNAIL_BASE_IMAGE_URL, imageUrl).apply();
    }

    public void putMediumBaseImageUrl(String imageUrl) {
        mPref.edit().putString(PREF_KEY_MEDIUM_BASE_IMAGE_URL, imageUrl).apply();
    }

    public void putOriginalBaseImageUrl(String imageUrl) {
        mPref.edit().putString(PREF_KEY_ORIGINAL_BASE_IMAGE_URL, imageUrl).apply();
    }

    public String getThumbnailBaseImageUrl() {
        return mPref.getString(PREF_KEY_THUMBNAIL_BASE_IMAGE_URL, null);
    }

    public String getMediumBaseImageUrl() {
        return mPref.getString(PREF_KEY_MEDIUM_BASE_IMAGE_URL, null);
    }

    public String getOriginalBaseImageUrl() {
        return mPref.getString(PREF_KEY_ORIGINAL_BASE_IMAGE_URL, null);
    }
}

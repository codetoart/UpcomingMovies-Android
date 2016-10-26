package com.codetoart.android.upcomingmovieapp;

import com.codetoart.android.upcomingmovieapp.data.local.PreferencesHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

/**
 * Created by mahavir on 10/26/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk  = 21)
public class PreferencesHelperTest {

    private PreferencesHelper mPreferencesHelper = new PreferencesHelper(RuntimeEnvironment.application);

    @Before
    public void setUp() {
        mPreferencesHelper.clear();
    }

    @Test
    public void putAndGetThumbnailBaseUrl(){
        String baseImageUrl = "thumbnailBaseImageUrl";
        mPreferencesHelper.putThumbnailBaseImageUrl(baseImageUrl);
        assertEquals(baseImageUrl, mPreferencesHelper.getThumbnailBaseImageUrl());
    }

    @Test
    public void putAndGetMediumBaseUrl(){
        String baseImageUrl = "mediumBaseImageUrl";
        mPreferencesHelper.putMediumBaseImageUrl(baseImageUrl);
        assertEquals(baseImageUrl, mPreferencesHelper.getMediumBaseImageUrl());
    }

    @Test
    public void putAndGetOriginalBaseUrl(){
        String baseImageUrl = "originalBaseImageUrl";
        mPreferencesHelper.putOriginalBaseImageUrl(baseImageUrl);
        assertEquals(baseImageUrl, mPreferencesHelper.getOriginalBaseImageUrl());
    }
}

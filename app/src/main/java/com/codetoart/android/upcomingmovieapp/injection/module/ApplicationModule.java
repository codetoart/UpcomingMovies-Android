package com.codetoart.android.upcomingmovieapp.injection.module;

import android.app.Application;
import android.content.Context;

import com.codetoart.android.upcomingmovieapp.data.remote.TMDbApi;
import com.codetoart.android.upcomingmovieapp.injection.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mahavir on 9/1/16.
 */
@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    TMDbApi provideTMDbApi() {
        return TMDbApi.Factory.makeTMDbApi(mApplication);
    }
}

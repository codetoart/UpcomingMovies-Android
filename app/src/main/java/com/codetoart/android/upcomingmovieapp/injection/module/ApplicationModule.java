package com.codetoart.android.upcomingmovieapp.injection.module;

import android.app.Application;
import android.content.Context;

import com.codetoart.android.upcomingmovieapp.data.remote.RetrofitFactory;
import com.codetoart.android.upcomingmovieapp.data.remote.TMDbApi;
import com.codetoart.android.upcomingmovieapp.injection.ApplicationContext;
import com.squareup.otto.Bus;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

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

    @Singleton
    @Provides
    Retrofit provideRetrofit() {
        return RetrofitFactory.getRetrofit(mApplication);
    }

    @Provides
    @Singleton
    public TMDbApi provideTMDbApi(Retrofit retrofit) {
        return retrofit.create(TMDbApi.class);
    }


    @Provides
    @Singleton
    Bus provideEventBus() {
        return new Bus();
    }
}

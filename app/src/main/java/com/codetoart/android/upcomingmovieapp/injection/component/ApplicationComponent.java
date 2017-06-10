package com.codetoart.android.upcomingmovieapp.injection.component;

import android.app.Application;
import android.content.Context;

import com.codetoart.android.upcomingmovieapp.UpcomingMovieApplication;
import com.codetoart.android.upcomingmovieapp.data.DataManager;
import com.codetoart.android.upcomingmovieapp.data.local.PreferencesHelper;
import com.codetoart.android.upcomingmovieapp.data.remote.RetrofitFactory;
import com.codetoart.android.upcomingmovieapp.data.remote.TMDbApi;
import com.codetoart.android.upcomingmovieapp.data.remote.UnauthorisedInterceptor;
import com.codetoart.android.upcomingmovieapp.injection.ApplicationContext;
import com.codetoart.android.upcomingmovieapp.injection.module.ApplicationModule;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by mahavir on 9/1/16.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(UpcomingMovieApplication application);
    void inject(UnauthorisedInterceptor interceptor);

    @ApplicationContext
    Context context();
    Application application();
    PreferencesHelper preferencesHelper();
    DataManager dataManager();
}

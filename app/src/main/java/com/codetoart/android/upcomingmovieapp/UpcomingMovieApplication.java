package com.codetoart.android.upcomingmovieapp;

import android.app.Application;
import android.content.Context;

import com.codetoart.android.upcomingmovieapp.data.model.DaoMaster;
import com.codetoart.android.upcomingmovieapp.data.model.DaoSession;
import com.codetoart.android.upcomingmovieapp.injection.component.ApplicationComponent;
import com.codetoart.android.upcomingmovieapp.injection.component.DaggerApplicationComponent;
import com.codetoart.android.upcomingmovieapp.injection.module.ApplicationModule;

import org.greenrobot.greendao.database.Database;

/**
 * Created by mahavir on 9/1/16.
 */
public class UpcomingMovieApplication extends Application {
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        mApplicationComponent.inject(this);
    }

    public static UpcomingMovieApplication get(Context context) {
        return (UpcomingMovieApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }

    public void setComponent(ApplicationComponent applicationComponent) {
        this.mApplicationComponent = applicationComponent;
    }
}

package com.codetoart.android.upcomingmovieapp.injection.component;

import com.codetoart.android.upcomingmovieapp.injection.PerActivity;
import com.codetoart.android.upcomingmovieapp.injection.module.ActivityModule;
import com.codetoart.android.upcomingmovieapp.ui.main.MainActivity;
import com.codetoart.android.upcomingmovieapp.ui.moviedetails.MovieDetailsActivity;

import dagger.Component;

/**
 * Created by mahavir on 9/1/16.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity mainActivity);
    void inject(MovieDetailsActivity movieDetailsActivity);
}

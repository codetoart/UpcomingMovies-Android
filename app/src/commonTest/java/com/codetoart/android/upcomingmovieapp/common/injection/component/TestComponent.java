package com.codetoart.android.upcomingmovieapp.common.injection.component;

import com.codetoart.android.upcomingmovieapp.common.injection.module.ApplicationTestModule;
import com.codetoart.android.upcomingmovieapp.injection.component.ApplicationComponent;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends ApplicationComponent {

}

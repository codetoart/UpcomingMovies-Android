package com.codetoart.android.upcomingmovieapp.common;

import android.content.Context;

import com.codetoart.android.upcomingmovieapp.UpcomingMovieApplication;
import com.codetoart.android.upcomingmovieapp.common.injection.component.TestComponent;
import com.codetoart.android.upcomingmovieapp.common.injection.component.DaggerTestComponent;
import com.codetoart.android.upcomingmovieapp.common.injection.module.ApplicationTestModule;
import com.codetoart.android.upcomingmovieapp.data.DataManager;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Test rule that creates and sets a Dagger TestComponent into the application overriding the
 * existing application component.
 * Use this rule in your test case in order for the app to use mock dependencies.
 * It also exposes some of the dependencies so they can be easily accessed from the tests, e.g. to
 * stub mocks etc.
 */
public class TestComponentRule implements TestRule {

    private TestComponent mTestComponent;
    private Context mContext;

    public TestComponentRule(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public DataManager getMockDataManager() {
        return mTestComponent.dataManager();
    }

    private void setupDaggerTestComponentInApplication() {
        UpcomingMovieApplication application = UpcomingMovieApplication.get(mContext);
        mTestComponent = DaggerTestComponent.builder()
                .applicationTestModule(new ApplicationTestModule(application))
                .build();
        application.setComponent(mTestComponent);
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    setupDaggerTestComponentInApplication();
                    base.evaluate();
                } finally {
                    mTestComponent = null;
                }
            }
        };
    }
}

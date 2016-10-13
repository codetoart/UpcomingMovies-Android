package com.codetoart.android.upcomingmovieapp.data.remote;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.codetoart.android.upcomingmovieapp.UpcomingMovieApplication;
import com.codetoart.android.upcomingmovieapp.data.BusEvent;
import com.squareup.otto.Bus;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Response;

public class UnauthorisedInterceptor implements Interceptor {

    @Inject Bus eventBus;

    public UnauthorisedInterceptor(Context context) {
        UpcomingMovieApplication.get(context).getComponent().inject(this);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (response.code() == 401) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    eventBus.post(new BusEvent.AuthenticationError());
                }
            });
        }
        return response;
    }
}

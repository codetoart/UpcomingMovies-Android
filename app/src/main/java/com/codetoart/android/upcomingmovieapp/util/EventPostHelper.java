package com.codetoart.android.upcomingmovieapp.util;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

import javax.inject.Inject;

public class EventPostHelper {

    private final Bus mBus;

    @Inject
    public EventPostHelper(Bus bus) {
        mBus = bus;
    }

    /**
     * Helper method to post an event from a different thread to the main one.
     */
    public void postEventSafely(final Object event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mBus.post(event);
            }
        });
    }
}

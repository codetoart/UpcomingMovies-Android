package com.codetoart.android.upcomingmovieapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.codetoart.android.upcomingmovieapp.R;
import com.codetoart.android.upcomingmovieapp.ui.base.BaseActivity;
import com.codetoart.android.upcomingmovieapp.ui.main.MainActivity;

/**
 * Created by Mahavir on 9/1/16.
 */
public class LauncherActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        }, 2000);

    }

    private void startMainActivity() {
        finish();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}

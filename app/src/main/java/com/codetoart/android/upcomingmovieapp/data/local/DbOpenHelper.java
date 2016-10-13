package com.codetoart.android.upcomingmovieapp.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.codetoart.android.upcomingmovieapp.data.model.DaoMaster;
import com.codetoart.android.upcomingmovieapp.data.model.DaoSession;
import com.codetoart.android.upcomingmovieapp.injection.ApplicationContext;

import org.greenrobot.greendao.database.Database;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mahavir on 10/7/16.
 */
@Singleton
public class DbOpenHelper extends DaoMaster.DevOpenHelper {
    public static final String DATABASE_NAME = "movies_app.db";

    @Inject
    public DbOpenHelper(@ApplicationContext Context context) {
        super(context, DATABASE_NAME);

    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {

    }
}

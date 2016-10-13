package com.codetoart.android.upcomingmovieapp.data.local;

import android.content.Context;

import com.codetoart.android.upcomingmovieapp.data.model.DaoMaster;
import com.codetoart.android.upcomingmovieapp.data.model.DaoSession;
import com.codetoart.android.upcomingmovieapp.data.model.Movie;
import com.codetoart.android.upcomingmovieapp.injection.ApplicationContext;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.rx.RxDao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Created by Mahavir on 10/7/16.
 */
@Singleton
public class DbHelper {
    private DaoSession mDaoSession;

    @Inject
    public DbHelper(@ApplicationContext Context context){
        DbOpenHelper helper = new DbOpenHelper(context);
        Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession(){
        return mDaoSession;
    }

    public RxDao<Movie, String> getMovieDao(){
        return mDaoSession.getMovieDao().rx();
    }

}

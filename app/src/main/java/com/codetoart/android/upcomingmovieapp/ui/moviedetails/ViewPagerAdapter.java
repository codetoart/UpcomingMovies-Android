package com.codetoart.android.upcomingmovieapp.ui.moviedetails;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.codetoart.android.upcomingmovieapp.R;
import com.codetoart.android.upcomingmovieapp.data.local.PreferencesHelper;
import com.codetoart.android.upcomingmovieapp.data.model.Image;
import com.codetoart.android.upcomingmovieapp.injection.ActivityContext;
import com.codetoart.android.upcomingmovieapp.util.CImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by Mahavir on 9/24/16.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<Image> mImageArrayList;
    PreferencesHelper mPreferencesHelper;

    @Inject
    public ViewPagerAdapter(@ActivityContext Context context, PreferencesHelper preferencesHelper) {
        this.mContext = context;
        this.mPreferencesHelper = preferencesHelper;
    }

    @Override
    public int getCount() {
        if (mImageArrayList == null) return 0;

        if (mImageArrayList.size() >= 5) {
            return 5;
        } else {
            return  mImageArrayList.size();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    public void setImageArrayList(ArrayList<Image> imageArrayList) {
        this.mImageArrayList = imageArrayList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View itemView = LayoutInflater.from(mContext).inflate(R.layout.row_pager,
                container, false);
        final ImageView imageView = (ImageView) itemView.findViewById(R.id.img_movie_poster);
        final ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
        String imageUrl = mPreferencesHelper.getMediumBaseImageUrl() +
                mImageArrayList.get(position).getFilePath();
        CImageLoader.displayImage(mContext, imageUrl, imageView, R.drawable.place_holder,
                new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        imageView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        imageView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}

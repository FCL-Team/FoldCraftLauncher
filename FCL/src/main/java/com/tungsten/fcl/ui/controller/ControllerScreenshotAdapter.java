package com.tungsten.fcl.ui.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tungsten.fcllibrary.component.FCLAdapter;

import java.util.ArrayList;

public class ControllerScreenshotAdapter extends FCLAdapter {

    private final ArrayList<String> urls;

    public ControllerScreenshotAdapter(Context context, ArrayList<String> urls) {
        super(context);
        this.urls = urls;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int i) {
        return urls.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ImageView imageView;
        if (view == null) {
            imageView = new ImageView(getContext());
        } else {
            imageView = (ImageView) view;
        }
        String url = urls.get(i);
        Glide.with(getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
        return imageView;
    }

}

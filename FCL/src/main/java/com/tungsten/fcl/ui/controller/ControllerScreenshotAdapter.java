package com.tungsten.fcl.ui.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.component.FCLAdapter;

import java.util.ArrayList;

public class ControllerScreenshotAdapter extends FCLAdapter {

    /**
     * 列表预览的最大解码尺寸。控制器截图原图通常为 2400x1080 甚至更大，
     * 按原图加载会让 ListView 的 item 被撑到原始高度并产生错位，同时浪费内存。
     */
    private static final int SCREENSHOT_MAX_SIZE = 1280;

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
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_controller_screenshot, viewGroup, false);
            imageView = view.findViewById(R.id.image);
            view.setTag(imageView);
        } else {
            imageView = (ImageView) view.getTag();
        }
        String url = urls.get(i);
        Glide.with(getContext())
                .load(url)
                .override(SCREENSHOT_MAX_SIZE, SCREENSHOT_MAX_SIZE)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
        return view;
    }

}

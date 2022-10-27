package com.tungsten.fcllibrary.component;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class FCLAdapter extends BaseAdapter {

    private final Context context;

    public FCLAdapter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}

package com.tungsten.fcl.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tungsten.fcl.R;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import java.util.ArrayList;

public class ArticleAdapter extends FCLAdapter {

    private final ArrayList<DocIndex.Item> list;

    public ArticleAdapter(Context context, ArrayList<DocIndex.Item> list) {
        super(context);
        this.list = list;
    }

    private static class ViewHolder {
        FCLLinearLayout parent;
        FCLTextView title;
        FCLTextView subtitle;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_article, null);
            viewHolder.parent = view.findViewById(R.id.parent);
            viewHolder.title = view.findViewById(R.id.title);
            viewHolder.subtitle = view.findViewById(R.id.subtitle);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        DocIndex.Item item = list.get(i);
        viewHolder.parent.setOnClickListener(v -> {
            AndroidUtils.openLink(getContext(), "https://fcl-team.github.io/pages/documentation.html?path=" + item.getPath());
        });
        viewHolder.title.setText(item.getTitle());
        viewHolder.subtitle.setText(item.getSubtitle());
        return view;
    }
}

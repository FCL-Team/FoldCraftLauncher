package com.tungsten.fcl.ui.download;

import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mio.util.AnimUtil;
import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.util.List;

public class ModGameVersionAdapter extends FCLAdapter {

    private List<String> list;
    private Callback callback;

    public ModGameVersionAdapter(Context context, List<String> list, Callback callback) {
        super(context);
        this.list = list;
        this.callback = callback;
    }

    private static class ViewHolder {
        FCLLinearLayout parent;
        FCLTextView version;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = new FCLLinearLayout(getContext());
            viewHolder.parent = new FCLLinearLayout(getContext());
            viewHolder.version = new FCLTextView(getContext());
            ((FCLLinearLayout) view).addView(viewHolder.parent, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            int padding = ConvertUtils.dip2px(getContext(), 10);
            viewHolder.parent.setPadding(padding, padding, padding, padding);
            viewHolder.parent.setBackground(getContext().getDrawable(R.drawable.bg_container_transparent_clickable));
            viewHolder.parent.addView(viewHolder.version, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            viewHolder.version.setSingleLine(true);
            viewHolder.version.setAutoTint(true);
            viewHolder.version.setTextColor(ThemeEngine.getInstance().getTheme().getAutoTint());
            viewHolder.parent.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getContext(), com.tungsten.fcllibrary.R.xml.anim_scale));
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.parent.setOnClickListener(v -> callback.onSelect(list.get(i)));
        viewHolder.version.setText((list.get(i).contains(getContext().getString(R.string.recommend_version)) ? "" : "Minecraft ") + list.get(i));
        AnimUtil.playTranslationX(view, ThemeEngine.getInstance().getTheme().getAnimationSpeed() * 30L, -100f, 0f).start();
        return view;
    }

    public interface Callback {
        void onSelect(String version);
    }
}

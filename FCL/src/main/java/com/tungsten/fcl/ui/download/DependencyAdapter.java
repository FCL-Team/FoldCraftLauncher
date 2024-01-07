package com.tungsten.fcl.ui.download;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.tungsten.fcl.R;
import com.tungsten.fcl.util.ModTranslations;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.util.LocaleUtils;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.util.List;

public class DependencyAdapter extends FCLAdapter {

    private final DownloadPage downloadPage;
    private final List<RemoteMod> list;
    private final Callback callback;

    public DependencyAdapter(Context context, DownloadPage page, List<RemoteMod> list, Callback callback) {
        super(context);
        this.downloadPage = page;
        this.list = list;
        this.callback = callback;
    }

    private static class ViewHolder {
        FCLLinearLayout parent;
        FCLTextView name;
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
            viewHolder.name = new FCLTextView(getContext());
            ((FCLLinearLayout) view).addView(viewHolder.parent, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            int padding = ConvertUtils.dip2px(getContext(), 10);
            viewHolder.parent.setPadding(padding, padding, padding, padding);
            viewHolder.parent.setBackground(getContext().getDrawable(R.drawable.bg_container_transparent_clickable));
            viewHolder.parent.addView(viewHolder.name, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            viewHolder.name.setSingleLine(true);
            viewHolder.name.setAutoTint(true);
            viewHolder.name.setTextColor(ThemeEngine.getInstance().getTheme().getAutoTint());
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        RemoteMod remoteMod = list.get(i);
        viewHolder.parent.setOnClickListener(v -> callback.onItemSelect(remoteMod));
        ModTranslations.Mod mod = ModTranslations.getTranslationsByRepositoryType(downloadPage.repository.getType()).getModByCurseForgeId(remoteMod.getSlug());
        viewHolder.name.setText(getContext().getString(R.string.mods_dependency) + ": " + (mod != null && LocaleUtils.isChinese(getContext()) ? mod.getDisplayName() : remoteMod.getTitle()));
        return view;
    }

    public interface Callback {
        void onItemSelect(RemoteMod mod);
    }
}

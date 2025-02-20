package com.tungsten.fcl.ui.download;

import static com.tungsten.fcllibrary.util.LocaleUtils.formatDateTime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mio.util.AnimUtil;
import com.tungsten.fcl.R;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclcore.download.RemoteVersion;
import com.tungsten.fclcore.download.fabric.FabricAPIRemoteVersion;
import com.tungsten.fclcore.download.fabric.FabricRemoteVersion;
import com.tungsten.fclcore.download.forge.ForgeRemoteVersion;
import com.tungsten.fclcore.download.game.GameRemoteVersion;
import com.tungsten.fclcore.download.liteloader.LiteLoaderRemoteVersion;
import com.tungsten.fclcore.download.neoforge.NeoForgeRemoteVersion;
import com.tungsten.fclcore.download.optifine.OptiFineRemoteVersion;
import com.tungsten.fclcore.download.quilt.QuiltAPIRemoteVersion;
import com.tungsten.fclcore.download.quilt.QuiltRemoteVersion;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLImageView;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import java.util.ArrayList;
import java.util.List;

public class RemoteVersionListAdapter extends FCLAdapter {

    private final ArrayList<RemoteVersion> list;
    private final OnRemoteVersionSelectListener listener;

    public RemoteVersionListAdapter(Context context, ArrayList<RemoteVersion> list, OnRemoteVersionSelectListener listener) {
        super(context);
        this.list = list;
        this.listener = listener;
    }

    private static class ViewHolder {
        FCLLinearLayout parent;
        FCLImageView icon;
        FCLTextView version;
        FCLTextView tag;
        FCLTextView date;
        FCLImageButton wiki;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_remote_version, null);
            viewHolder.parent = view.findViewById(R.id.parent);
            viewHolder.icon = view.findViewById(R.id.icon);
            viewHolder.version = view.findViewById(R.id.version);
            viewHolder.tag = view.findViewById(R.id.tag);
            viewHolder.date = view.findViewById(R.id.date);
            viewHolder.wiki = view.findViewById(R.id.wiki);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        RemoteVersion remoteVersion = list.get(i);
        viewHolder.parent.setOnClickListener(view1 -> listener.onSelect(remoteVersion));
        viewHolder.icon.setBackground(getIcon(remoteVersion));
        viewHolder.version.setText(remoteVersion.getSelfVersion());
        viewHolder.tag.setBackground(getContext().getDrawable(R.drawable.bg_container_white));
        viewHolder.tag.setAutoBackgroundTint(true);
        viewHolder.tag.setBackgroundTintList(new ColorStateList(new int[][]{{}}, new int[]{ThemeEngine.getInstance().getTheme().getColor()}));
        viewHolder.tag.setText(getTag(remoteVersion));
        viewHolder.date.setVisibility(remoteVersion.getReleaseDate() == null ? View.GONE : View.VISIBLE);
        viewHolder.date.setText(remoteVersion.getReleaseDate() == null ? "" : formatDateTime(getContext(), remoteVersion.getReleaseDate()));
        if (remoteVersion instanceof GameRemoteVersion && (remoteVersion.getVersionType() == RemoteVersion.Type.RELEASE || remoteVersion.getVersionType() == RemoteVersion.Type.SNAPSHOT)) {
            viewHolder.wiki.setVisibility(View.VISIBLE);
            viewHolder.wiki.setOnClickListener(v -> AndroidUtils.openLink(getContext(), remoteVersion.getVersionType() == RemoteVersion.Type.RELEASE ? String.format(getContext().getString(R.string.wiki_release), remoteVersion.getGameVersion()) : String.format(getContext().getString(R.string.wiki_snapshot), remoteVersion.getGameVersion())));
        } else {
            viewHolder.wiki.setVisibility(View.GONE);
        }
        AnimUtil.playTranslationX(view, ThemeEngine.getInstance().getTheme().getAnimationSpeed() * 30L, -100f, 0f).start();
        return view;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Drawable getIcon(RemoteVersion remoteVersion) {
        if (remoteVersion instanceof LiteLoaderRemoteVersion)
            return getContext().getDrawable(R.drawable.img_chicken);
        else if (remoteVersion instanceof OptiFineRemoteVersion)
            return getContext().getDrawable(R.drawable.img_optifine);
        else if (remoteVersion instanceof ForgeRemoteVersion)
            return getContext().getDrawable(R.drawable.img_forge);
        else if (remoteVersion instanceof NeoForgeRemoteVersion)
            return getContext().getDrawable(R.drawable.img_neoforge);
        else if (remoteVersion instanceof FabricRemoteVersion || remoteVersion instanceof FabricAPIRemoteVersion)
            return getContext().getDrawable(R.drawable.img_fabric);
        else if (remoteVersion instanceof QuiltRemoteVersion || remoteVersion instanceof QuiltAPIRemoteVersion)
            return getContext().getDrawable(R.drawable.img_quilt);
        else if (remoteVersion instanceof GameRemoteVersion) {
            switch (remoteVersion.getVersionType()) {
                case RELEASE:
                    return getContext().getDrawable(R.drawable.img_grass);
                case SNAPSHOT:
                    return getContext().getDrawable(R.drawable.img_command);
                default:
                    return getContext().getDrawable(R.drawable.img_craft_table);
            }
        } else {
            return getContext().getDrawable(R.drawable.img_grass);
        }
    }

    private String getTag(RemoteVersion remoteVersion) {
        if (remoteVersion instanceof GameRemoteVersion) {
            switch (remoteVersion.getVersionType()) {
                case RELEASE:
                    return getContext().getString(R.string.version_game_release);
                case SNAPSHOT:
                    return getContext().getString(R.string.version_game_snapshot);
                default:
                    return getContext().getString(R.string.version_game_old);
            }
        } else {
            return remoteVersion.getGameVersion();
        }
    }

    public List<RemoteVersion> getList() {
        return list;
    }

    public interface OnRemoteVersionSelectListener {
        void onSelect(RemoteVersion remoteVersion);
    }
}

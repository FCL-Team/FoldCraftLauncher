package com.tungsten.fcl.ui.download;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mio.util.AnimUtil;
import com.tungsten.fcl.R;
import com.tungsten.fcl.util.ModTranslations;
import com.tungsten.fclcore.mod.LocalModFile;
import com.tungsten.fclcore.mod.ModManager;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLImageView;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.LocaleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RemoteModListAdapter extends FCLAdapter {

    private final DownloadPage downloadPage;
    private final ArrayList<RemoteMod> list;
    private final Callback callback;
    private final List<String> modIdList = new ArrayList<>();

    public RemoteModListAdapter(Context context, DownloadPage downloadPage, ArrayList<RemoteMod> list, Callback callback) {
        super(context);
        this.downloadPage = downloadPage;
        this.list = list;
        this.callback = callback;
        Task.runAsync(() -> {
            ModManager modManager = ((ModDownloadPage) downloadPage).getModManager();
            List<LocalModFile> modFiles = modManager.getMods().parallelStream().collect(Collectors.toList());
            for (LocalModFile localModFile : modFiles) {
                try {
                    Optional<RemoteMod.Version> remoteVersionOptional = downloadPage.getRepository().getRemoteVersionByLocalFile(localModFile, localModFile.getFile());
                    remoteVersionOptional.ifPresent(localModFile::setRemoteVersion);
                    RemoteMod.Version remoteVersion = localModFile.getRemoteVersion();
                    if (remoteVersion != null) {
                        String modId = remoteVersion.getModid();
                        modIdList.add(modId);
                    }
                } catch (Throwable ignore) {
                }
            }

        }).start();
    }

    private static class ViewHolder {
        FCLLinearLayout parent;
        FCLImageView icon;
        FCLTextView name;
        FCLTextView tag;
        FCLTextView description;
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
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_remote_version, null);
            viewHolder.parent = view.findViewById(R.id.parent);
            viewHolder.icon = view.findViewById(R.id.icon);
            viewHolder.name = view.findViewById(R.id.version);
            viewHolder.tag = view.findViewById(R.id.tag);
            viewHolder.description = view.findViewById(R.id.date);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        RemoteMod remoteMod = list.get(i);
        viewHolder.parent.setOnClickListener(v -> callback.onItemSelect(remoteMod));
        viewHolder.icon.setImageDrawable(null);
        viewHolder.icon.setTag(i);
        Glide.with(getContext()).load(remoteMod.getIconUrl()).into(viewHolder.icon);
        ModTranslations.Mod mod = ModTranslations.getTranslationsByRepositoryType(downloadPage.repository.getType()).getModByCurseForgeId(remoteMod.getSlug());
        viewHolder.name.setText(mod != null && LocaleUtils.isChinese(getContext()) ? mod.getDisplayName() : remoteMod.getTitle());
        List<String> categories = remoteMod.getCategories().stream().map(downloadPage::getLocalizedCategory).collect(Collectors.toList());
        StringBuilder stringBuilder = new StringBuilder();
        categories.forEach(it -> stringBuilder.append(it).append("   "));
        String tag = StringUtils.removeSuffix(stringBuilder.toString(), "   ");
        viewHolder.tag.setText(tag);
        viewHolder.description.setText(remoteMod.getDescription());
        AnimUtil.playTranslationX(view, ThemeEngine.getInstance().getTheme().getAnimationSpeed() * 30L, -100f, 0f).start();
        if (downloadPage instanceof ModDownloadPage) {
            if (!modIdList.isEmpty() && modIdList.contains(remoteMod.getModID())) {
                String text = viewHolder.name.getText().toString();
                if (!text.startsWith(getContext().getString(R.string.installed))) {
                    viewHolder.name.setText(String.format("[%s] %s", getContext().getString(R.string.installed), text));
                }
            }
        }
        return view;
    }

    public interface Callback {
        void onItemSelect(RemoteMod mod);
    }
}

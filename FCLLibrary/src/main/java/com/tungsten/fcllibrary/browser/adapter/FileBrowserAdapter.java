package com.tungsten.fcllibrary.browser.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.FileProvider;

import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.browser.FileBrowser;
import com.tungsten.fcllibrary.browser.FileOperator;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileBrowserAdapter extends FCLAdapter {

    private final FileBrowser fileBrowser;
    private final FileBrowserListener listener;
    private final List<File> list;

    private ArrayList<String> selectedFiles;

    private final DateFormat formatter;

    @SuppressLint("SimpleDateFormat")
    public FileBrowserAdapter(Context context, FileBrowser fileBrowser, Path path, ArrayList<String> selectedFiles, FileBrowserListener listener) {
        super(context);
        this.fileBrowser = fileBrowser;
        this.selectedFiles = selectedFiles;
        this.listener = listener;
        this.list = FileOperator.getFileList(path, fileBrowser);

        this.formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public void setSelectedFiles(ArrayList<String> selectedFiles) {
        this.selectedFiles = selectedFiles;
    }

    public ArrayList<String> getSelectedFiles() {
        return selectedFiles;
    }

    private static class ViewHolder {
        LinearLayoutCompat parent;
        ImageView icon;
        FCLTextView name;
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
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_file_browser, null);
            viewHolder.parent = view.findViewById(R.id.parent);
            viewHolder.icon = view.findViewById(R.id.icon);
            viewHolder.name = view.findViewById(R.id.name);
            viewHolder.description = view.findViewById(R.id.description);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        File file = list.get(i);
        StringBuilder stringBuilder = new StringBuilder();
        String displayTime;
        try {
            long time = FileUtils.lastModified(file);
            Date date = new Date();
            date.setTime(time);
            displayTime = formatter.format(date);
        } catch (IOException e) {
            e.printStackTrace();
            displayTime = "Unknown";
        }
        stringBuilder.append(displayTime);
        if (file.isFile()) {
            String fileSize = FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(file));
            stringBuilder.append("    ").append(fileSize);
        }
        String description = stringBuilder.toString();
        @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = file.isFile() ? getContext().getDrawable(R.drawable.ic_baseline_file_24) : getContext().getDrawable(R.drawable.ic_baseline_folder_24);
        drawable.setTint(ThemeEngine.getInstance().getTheme().getColor());
        viewHolder.icon.setImageDrawable(drawable);
        viewHolder.name.setText(file.getName());
        viewHolder.description.setText(description);
        if (selectedFiles.contains(file.getAbsolutePath()) && file.isFile()) {
            viewHolder.parent.setBackgroundColor(Color.GRAY);
        } else {
            viewHolder.parent.setBackground(getContext().getDrawable(R.drawable.clickable_parent));
        }
        viewHolder.parent.setOnClickListener(view1 -> {
            if (file.isDirectory()) {
                listener.onEnterDir(file.getAbsolutePath());
            }
            if (fileBrowser.getLibMode() != LibMode.FILE_BROWSER && fileBrowser.getLibMode() != LibMode.FOLDER_CHOOSER && file.isFile()) {
                listener.onSelect(this, file.getAbsolutePath());
            }
        });
        viewHolder.parent.setOnLongClickListener(view12 -> {
            if (file.isFile()) {
                FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                builder.setAlertLevel(FCLAlertDialog.AlertLevel.INFO);
                builder.setTitle(getContext().getString(R.string.file_browser_share_title));
                builder.setMessage(getContext().getString(R.string.file_browser_share_message));
                builder.setPositiveButton(() -> {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    Uri uri = FileProvider.getUriForFile(getContext(), getContext().getString(R.string.file_browser_provider), file);
                    intent.setType("*/*");
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    getContext().startActivity(Intent.createChooser(intent, getContext().getString(R.string.file_browser_share_title)));
                });
                builder.setNegativeButton(null);
                builder.create().show();
            }
            return false;
        });
        return view;
    }

}

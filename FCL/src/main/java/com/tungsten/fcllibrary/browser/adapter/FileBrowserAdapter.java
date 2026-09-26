package com.tungsten.fcllibrary.browser.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.browser.FileBrowser;
import com.tungsten.fcllibrary.browser.FileOperator;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.browser.options.SortMode;
import com.tungsten.fcllibrary.component.FCLAdapter;
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
        this(context, fileBrowser, path, selectedFiles, SortMode.NAME, true, listener);
    }

    @SuppressLint("SimpleDateFormat")
    public FileBrowserAdapter(Context context, FileBrowser fileBrowser, Path path, ArrayList<String> selectedFiles, SortMode sortMode, boolean showHidden, FileBrowserListener listener) {
        super(context);
        this.fileBrowser = fileBrowser;
        this.selectedFiles = selectedFiles;
        this.listener = listener;
        this.list = FileOperator.getFileList(path, fileBrowser, sortMode, showHidden);

        this.formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public void setSelectedFiles(ArrayList<String> selectedFiles) {
        this.selectedFiles = selectedFiles;
    }

    public ArrayList<String> getSelectedFiles() {
        return selectedFiles;
    }

    /**
     * 当前目录下展示的文件列表
     */
    public List<File> getFiles() {
        return list;
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
        if (selectedFiles.contains(file.getAbsolutePath())) {
            viewHolder.parent.setBackgroundColor(Color.GRAY);
        } else {
            viewHolder.parent.setBackground(getContext().getDrawable(R.drawable.clickable_parent));
        }
        viewHolder.parent.setOnClickListener(view1 -> {
            if (file.isDirectory()) {
                listener.onEnterDir(file.getAbsolutePath());
            }
            if (file.isFile() && fileBrowser.getLibMode() != LibMode.FOLDER_CHOOSER) {
                listener.onSelect(this, file.getAbsolutePath());
            }
        });
        viewHolder.parent.setOnLongClickListener(view12 -> {
            listener.onItemLongClick(file);
            return true;
        });
        return view;
    }

}

package com.tungsten.fcllibrary.browser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.browser.options.SelectionMode;
import com.tungsten.fcllibrary.component.ResultListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class FileBrowser implements Serializable {

    public static final String SELECTED_FILES = "SELECTED_FILES";

    public static ArrayList<String> getSelectedFiles(Intent intentData) {
        if (intentData == null) {
            return null;
        }
        ArrayList<Uri> selectedFiles  = intentData.getParcelableArrayListExtra(SELECTED_FILES);
        return (ArrayList<String>) selectedFiles.stream().map(Uri::toString).collect(Collectors.toList());
    }

    private LibMode libMode = LibMode.FILE_BROWSER;
    private SelectionMode selectionMode = SelectionMode.SINGLE_SELECTION;
    private String initDir = Environment.getExternalStorageDirectory().getAbsolutePath();
    private ArrayList<String> suffixes = new ArrayList<>();
    private String title;

    public FileBrowser(String title) {
        this.title = title;
    }

    public LibMode getLibMode() {
        return libMode;
    }

    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    public String getInitDir() {
        return initDir;
    }

    public ArrayList<String> getSuffixes() {
        return suffixes;
    }

    public String getTitle() {
        return title;
    }

    public void browse(Activity activity, int code, ResultListener.Listener listener) {
        Intent intent = new Intent(activity, FileBrowserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("config", this);
        intent.putExtras(bundle);
        ResultListener.startActivityForResult(activity, intent, code, listener);
    }

    public static class Builder {

        private FileBrowser fileBrowser;

        public Builder(Context context) {
            fileBrowser = new FileBrowser(context.getString(R.string.file_browser_title));
        }

        public FileBrowser create() {
            return fileBrowser;
        }

        public Builder setLibMode(LibMode libMode) {
            fileBrowser.libMode = libMode;
            return this;
        }

        public Builder setSelectionMode(SelectionMode selectionMode) {
            fileBrowser.selectionMode = selectionMode;
            return this;
        }

        public Builder setInitDir(String initDir) {
            fileBrowser.initDir = initDir;
            return this;
        }

        public Builder setSuffix(ArrayList<String> suffix) {
            fileBrowser.suffixes = suffix;
            return this;
        }

        public Builder setTitle(String title) {
            fileBrowser.title = title;
            return this;
        }

        public LibMode getLibMode() {
            return fileBrowser.libMode;
        }

        public SelectionMode getSelectionMode() {
            return fileBrowser.selectionMode;
        }

        public String getInitDir() {
            return fileBrowser.initDir;
        }

        public ArrayList<String> getSuffix() {
            return fileBrowser.suffixes;
        }

        public String getTitle() {
            return fileBrowser.title;
        }

    }

}

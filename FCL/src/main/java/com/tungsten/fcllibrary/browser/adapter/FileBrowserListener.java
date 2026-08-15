package com.tungsten.fcllibrary.browser.adapter;

public interface FileBrowserListener {
    void onEnterDir(String path);
    void onSelect(FileBrowserAdapter adapter, String path);
}

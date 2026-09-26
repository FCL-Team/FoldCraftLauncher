package com.tungsten.fcllibrary.browser.adapter;

import java.io.File;

public interface FileBrowserListener {
    void onEnterDir(String path);

    void onSelect(FileBrowserAdapter adapter, String path);

    /**
     * 长按列表条目时触发，默认不响应
     */
    default void onItemLongClick(File file) {
    }
}

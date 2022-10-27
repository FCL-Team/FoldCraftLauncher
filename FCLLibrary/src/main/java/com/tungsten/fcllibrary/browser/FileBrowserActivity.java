package com.tungsten.fcllibrary.browser;

import android.os.Bundle;
import android.os.Environment;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.browser.adapter.FileBrowserAdapter;
import com.tungsten.fcllibrary.browser.adapter.FileBrowserListener;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.view.FCLTitleView;

import java.io.File;
import java.nio.file.Path;

public class FileBrowserActivity extends FCLActivity {

    private FileBrowser fileBrowser;

    private FCLTitleView titleView;

    private ListView listView;

    private Path currentPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);

        fileBrowser = (FileBrowser) getIntent().getExtras().getSerializable("config");

        titleView = findViewById(R.id.title);
        titleView.setTitle(fileBrowser.getTitle());

        listView = findViewById(R.id.list);
        refreshList(new File(fileBrowser.getInitDir()).toPath());
    }

    private void refreshList(Path path) {
        currentPath = path;
        switch (fileBrowser.getLibMode()) {
            case FILE_CHOOSER:

                break;
            case FOLDER_CHOOSER:

                break;
            default:
                FileBrowserAdapter adapter = new FileBrowserAdapter(this, fileBrowser, path, new FileBrowserListener() {
                    @Override
                    public void onEnterDir(String path) {
                        refreshList(new File(path).toPath());
                    }

                    @Override
                    public void onSelect(String path) {
                        // ignore
                    }
                });
                listView.setAdapter(adapter);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (currentPath.getParent() != null && !currentPath.toString().equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
            refreshList(currentPath.getParent());
        }
        else {
            super.onBackPressed();
        }
    }
}

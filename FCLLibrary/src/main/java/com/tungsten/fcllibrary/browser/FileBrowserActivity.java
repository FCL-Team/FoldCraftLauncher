package com.tungsten.fcllibrary.browser;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.browser.adapter.FileBrowserAdapter;
import com.tungsten.fcllibrary.browser.adapter.FileBrowserListener;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.browser.options.SelectionMode;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class FileBrowserActivity extends FCLActivity implements View.OnClickListener {

    private FileBrowser fileBrowser;

    private FCLButton back;
    private FCLButton close;
    private FCLTextView mode;
    private FCLTextView type;

    private FCLTextView currentText;
    private ListView listView;

    private FCLButton sharedDir;
    private FCLButton privateDir;
    private FCLButton openExternal;
    private FCLButton confirm;

    private Path currentPath;

    private ArrayList<String> selectedFiles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);

        fileBrowser = (FileBrowser) getIntent().getExtras().getSerializable("config");

        /*
        titleView = findViewById(R.id.title);
        titleView.setTitle(fileBrowser.getTitle());

         */
        mode = findViewById(R.id.mode);
        type = findViewById(R.id.type);
        mode.setText(getMode());
        type.setText(getType());
        back = findViewById(R.id.back);
        close = findViewById(R.id.close);
        back.setOnClickListener(this);
        close.setOnClickListener(this);

        sharedDir = findViewById(R.id.shared_dir);
        privateDir = findViewById(R.id.private_dir);
        openExternal = findViewById(R.id.open_external);
        confirm = findViewById(R.id.confirm);
        sharedDir.setOnClickListener(this);
        privateDir.setOnClickListener(this);
        openExternal.setOnClickListener(this);
        confirm.setOnClickListener(this);

        selectedFiles = new ArrayList<>();
        currentText = findViewById(R.id.current_folder);
        listView = findViewById(R.id.list);
        refreshList(currentPath != null ? currentPath : new File(fileBrowser.getInitDir()).toPath());
    }

    private String getMode() {
        switch (fileBrowser.getLibMode()) {
            case FILE_CHOOSER:
                return getString(R.string.file_browser_mode_file);
            case FOLDER_CHOOSER:
                return getString(R.string.file_browser_mode_folder);
            default:
                return getString(R.string.file_browser_mode_browse);
        }
    }

    private String getType() {
        if (fileBrowser.getSelectionMode() == SelectionMode.SINGLE_SELECTION) {
            return getString(R.string.file_browser_selection_simple);
        }
        return getString(R.string.file_browser_selection_multiple);
    }

    private void refreshList(Path path) {
        if (fileBrowser.getLibMode() == LibMode.FOLDER_CHOOSER && !selectedFiles.contains(path.toString())) {
            selectedFiles = new ArrayList<>();
            selectedFiles.add(path.toString());
        }
        currentPath = path;
        currentText.setText(path.toString());
        ThemeEngine.getInstance().registerEvent(currentText, () -> currentText.setBackgroundColor(ThemeEngine.getInstance().getTheme().getColor()));
        FileBrowserAdapter adapter = new FileBrowserAdapter(this, fileBrowser, path, selectedFiles, new FileBrowserListener() {
            @Override
            public void onEnterDir(String path) {
                refreshList(new File(path).toPath());
            }

            @Override
            public void onSelect(FileBrowserAdapter adapter1, String path) {
                if (selectedFiles.stream().anyMatch(s -> s.equals(path))) {
                    selectedFiles.remove(path);
                } else {
                    if (fileBrowser.getSelectionMode() == SelectionMode.SINGLE_SELECTION) {
                        selectedFiles = new ArrayList<>();
                    }
                    selectedFiles.add(path);
                }
                adapter1.setSelectedFiles(selectedFiles);
                adapter1.notifyDataSetChanged();
            }
        });
        listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (currentPath.getParent() != null && !currentPath.toString().equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
            refreshList(currentPath.getParent());
        } else {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == back) {
            if (currentPath.getParent() != null && !currentPath.toString().equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                refreshList(currentPath.getParent());
            } else {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        }
        if (view == close) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
        if (view == sharedDir) {
            refreshList(Environment.getExternalStorageDirectory().toPath());
        }
        if (view == privateDir) {
            if (getExternalCacheDir().getParent() != null) {
                refreshList(new File(getExternalCacheDir().getParent()).toPath());
            } else {
                Toast.makeText(this, getString(R.string.file_browser_private_alert), Toast.LENGTH_SHORT).show();
            }
        }
        if (view == openExternal) {
            if (currentPath.toFile().getAbsolutePath().equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                return;
            }
            Uri uri = FileProvider.getUriForFile(this, getString(R.string.file_browser_provider), currentPath.toFile());
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setDataAndType(uri, "*/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivity(intent);
        }
        if (view == confirm) {
            if (selectedFiles.size() == 0 && fileBrowser.getLibMode() != LibMode.FILE_BROWSER) {
                Toast.makeText(this, getString(R.string.file_browser_positive_alert), Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(FileBrowser.SELECTED_FILES, (ArrayList<? extends Parcelable>) selectedFiles.stream().map(Uri::parse).collect(Collectors.toList()));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }
}

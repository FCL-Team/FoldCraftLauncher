package com.tungsten.fcllibrary.browser;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.mio.dialog.ItemSelectionDialog;
import com.tungsten.fcl.R;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fcllibrary.browser.adapter.FileBrowserAdapter;
import com.tungsten.fcllibrary.browser.adapter.FileBrowserListener;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.browser.options.SelectionMode;
import com.tungsten.fcllibrary.browser.options.SortMode;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.dialog.EditDialog;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class FileBrowserActivity extends FCLActivity implements View.OnClickListener {

    private static final String PREFERENCES_NAME = "launcher";
    private static final String KEY_SORT_MODE = "file_browser_sort_mode";
    private static final String KEY_SHOW_HIDDEN = "file_browser_show_hidden";

    private FileBrowser fileBrowser;
    private FileBrowserAdapter adapter;

    private FCLButton back;
    private FCLButton close;
    private FCLTextView mode;
    private FCLTextView type;

    private FCLTextView currentText;
    private ListView listView;

    private FCLButton sharedDir;
    private FCLButton privateDir;
    private FCLButton manage;
    private FCLButton openExternal;
    private FCLButton selectExternal;
    private FCLButton confirm;

    private Path currentPath;
    private SortMode sortMode;
    private boolean showHidden;

    private ArrayList<String> selectedFiles;
    private ArrayList<Uri> extSelected;

    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    private final ActivityResultLauncher<Object> launcher = registerForActivityResult(new ActivityResultContract<Object, Uri>() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Object o) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, fileBrowser.getSelectionMode() == SelectionMode.MULTIPLE_SELECTION);
            return intent;
        }

        @Override
        public Uri parseResult(int resultCode, @Nullable Intent data) {
            if (data == null || resultCode != Activity.RESULT_OK) {
                return null;
            }
            ClipData clipData = data.getClipData();
            if (clipData != null && clipData.getItemCount() > 0) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri uri = clipData.getItemAt(i).getUri();
                    if (uri != null) {
                        extSelected.add(uri);
                    }
                }
            } else {
                extSelected.add(data.getData());
            }
            return null;
        }
    }, result -> {
        if (!extSelected.isEmpty()) {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(FileBrowser.SELECTED_FILES, extSelected);
            FileBrowserActivity.this.setResult(Activity.RESULT_OK, intent);
            FileBrowserActivity.this.finish();
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);

        fileBrowser = (FileBrowser) getIntent().getExtras().getSerializable("config");

        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        sortMode = parseSortMode(preferences.getString(KEY_SORT_MODE, null));
        showHidden = preferences.getBoolean(KEY_SHOW_HIDDEN, false);

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
        manage = findViewById(R.id.manage);
        openExternal = findViewById(R.id.open_external);
        selectExternal = findViewById(R.id.select_external);
        confirm = findViewById(R.id.confirm);
        sharedDir.setOnClickListener(this);
        privateDir.setOnClickListener(this);
        manage.setOnClickListener(this);
        openExternal.setOnClickListener(this);
        selectExternal.setOnClickListener(this);
        confirm.setOnClickListener(this);

        selectedFiles = new ArrayList<>();
        extSelected = new ArrayList<>();
        currentText = findViewById(R.id.current_folder);
        listView = findViewById(R.id.list);
        refreshList(currentPath != null ? currentPath : new File(fileBrowser.getInitDir()).toPath());

        if (fileBrowser.getLibMode() != LibMode.FILE_CHOOSER) {
            selectExternal.setVisibility(View.GONE);
        }
        if (fileBrowser.getLibMode() != LibMode.FILE_BROWSER) {
            openExternal.setVisibility(View.GONE);
        }
        switch (fileBrowser.getCode()) {
            case 100:
            case 150:
            case 200:
            case 500:
            case 600:
            case 700:
            case 750:
                selectExternal.setVisibility(View.GONE);
                break;
                default:
        }
        if (!fileBrowser.isExternalSelection()) {
            selectExternal.setVisibility(View.GONE);
            openExternal.setVisibility(View.GONE);
        }

        currentText.setOnClickListener(v -> {
            new EditDialog(this, path -> {
                File file = new File(path);
                if (file.exists() && file.isDirectory()) {
                    refreshList(file.toPath());
                }
            }).show();
        });
    }

    private SortMode parseSortMode(String value) {
        if (value != null) {
            try {
                return SortMode.valueOf(value);
            } catch (IllegalArgumentException ignored) {
            }
        }
        return SortMode.NAME;
    }

    private String getMode() {
        return switch (fileBrowser.getLibMode()) {
            case FILE_CHOOSER -> getString(R.string.file_browser_mode_file);
            case FOLDER_CHOOSER -> getString(R.string.file_browser_mode_folder);
            default -> getString(R.string.file_browser_mode_browse);
        };
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
        adapter = new FileBrowserAdapter(this, fileBrowser, path, selectedFiles, sortMode, showHidden, new FileBrowserListener() {
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

            @Override
            public void onItemLongClick(File file) {
                showItemMenu(file);
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
        if (view == manage) {
            showManageMenu();
        }
        if (view == openExternal) {
            if (currentPath.toFile().getAbsolutePath().equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                currentPath = currentPath.resolve("FCL");
            }
            Uri uri = FileProvider.getUriForFile(this, getApplication().getPackageName() + ".provider", currentPath.toFile());
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setDataAndType(uri, "*/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, getString(R.string.file_browser_open_external)));
        }
        if (view == selectExternal) {
            launcher.launch(null);
        }
        if (view == confirm) {
            if (selectedFiles.isEmpty() && fileBrowser.getLibMode() != LibMode.FILE_BROWSER) {
                Toast.makeText(this, getString(R.string.file_browser_positive_alert), Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(FileBrowser.SELECTED_FILES, (ArrayList<? extends Parcelable>) selectedFiles.stream().map(Uri::parse).collect(Collectors.toList()));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }

    private void toast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

    /**
     * 允许抛出异常的文件操作，交由 {@link #runFileOperation} 统一处理失败
     */
    private interface FileOperation {
        void run() throws Exception;
    }

    /**
     * 在 IO 线程执行文件操作，成功后回主线程刷新列表，失败时提示
     */
    private void runFileOperation(FileOperation operation) {
        Schedulers.io().execute(() -> {
            try {
                operation.run();
                Schedulers.androidUIThread().execute(() -> {
                    if (!isDestroyed() && !isFinishing()) {
                        refreshList(currentPath);
                    }
                });
            } catch (Exception e) {
                Logging.LOG.log(Level.WARNING, "File browser operation failed", e);
                Schedulers.androidUIThread().execute(() -> {
                    if (!isDestroyed() && !isFinishing()) {
                        toast(R.string.file_browser_operation_failed);
                    }
                });
            }
        });
    }

    /**
     * 保存排序方式并刷新列表
     */
    private void setSortMode(SortMode sortMode) {
        this.sortMode = sortMode;
        getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE).edit().putString(KEY_SORT_MODE, sortMode.name()).apply();
        refreshList(currentPath);
    }

    private void showSortMenu() {
        List<String> items = Arrays.asList(
                getString(R.string.file_browser_sort_name),
                getString(R.string.file_browser_sort_size),
                getString(R.string.file_browser_sort_date));
        ItemSelectionDialog.show(this, getString(R.string.file_browser_sort), items, true, sortMode.ordinal(),
                (position, item) -> setSortMode(SortMode.values()[position]));
    }

    private void toggleShowHidden() {
        showHidden = !showHidden;
        getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE).edit().putBoolean(KEY_SHOW_HIDDEN, showHidden).apply();
        refreshList(currentPath);
    }

    /**
     * 管理菜单：新建、粘贴、排序、隐藏文件开关、批量操作与刷新
     */
    private void showManageMenu() {
        List<String> items = new ArrayList<>();
        List<Runnable> actions = new ArrayList<>();
        items.add(getString(R.string.file_browser_new_folder));
        actions.add(() -> showCreateDialog(true));
        items.add(getString(R.string.file_browser_new_file));
        actions.add(() -> showCreateDialog(false));
        items.add(getString(R.string.file_browser_paste));
        actions.add(this::pasteClipboard);
        items.add(getString(R.string.file_browser_sort));
        actions.add(this::showSortMenu);
        items.add(getString(showHidden ? R.string.file_browser_hide_hidden_files : R.string.file_browser_show_hidden_files));
        actions.add(this::toggleShowHidden);
        if (fileBrowser.getLibMode() != LibMode.FOLDER_CHOOSER && adapter != null && adapter.getCount() > 0) {
            items.add(getString(R.string.file_browser_batch));
            actions.add(this::showBatchMenu);
        }
        items.add(getString(R.string.file_browser_refresh));
        actions.add(() -> refreshList(currentPath));
        ItemSelectionDialog.show(this, getString(R.string.file_browser_manage), items, true, -1,
                (position, item) -> actions.get(position).run());
    }

    /**
     * 批量操作菜单：作用于当前勾选的文件
     */
    private void showBatchMenu() {
        List<String> items = new ArrayList<>();
        List<Runnable> actions = new ArrayList<>();
        items.add(getString(R.string.file_browser_select_all));
        actions.add(this::selectAll);
        items.add(getString(R.string.file_browser_copy));
        actions.add(() -> clipboardSelected(false));
        items.add(getString(R.string.file_browser_cut));
        actions.add(() -> clipboardSelected(true));
        items.add(getString(R.string.file_browser_delete));
        actions.add(this::confirmDeleteSelected);
        ItemSelectionDialog.show(this, getString(R.string.file_browser_batch), items, true, -1,
                (position, item) -> actions.get(position).run());
    }

    /**
     * 单个文件/文件夹的操作菜单
     */
    private void showItemMenu(File file) {
        List<String> items = new ArrayList<>();
        List<Runnable> actions = new ArrayList<>();
        items.add(getString(R.string.file_browser_details));
        actions.add(() -> showDetails(file));
        items.add(getString(R.string.file_browser_rename));
        actions.add(() -> showRenameDialog(file));
        items.add(getString(R.string.file_browser_delete));
        actions.add(() -> confirmDelete(List.of(file)));
        items.add(getString(R.string.file_browser_copy));
        actions.add(() -> FileClipboard.copy(List.of(file)));
        items.add(getString(R.string.file_browser_cut));
        actions.add(() -> FileClipboard.cut(List.of(file)));
        if (file.isFile()) {
            items.add(getString(R.string.file_browser_share));
            actions.add(() -> shareFile(file));
        }
        ItemSelectionDialog.show(this, file.getName(), items, true, -1,
                (position, item) -> actions.get(position).run());
    }

    private void showCreateDialog(boolean directory) {
        File dir = currentPath.toFile();
        EditDialog dialog = new EditDialog(this, "", name -> {
            if (!FileOperator.isValidFileName(name)) {
                toast(R.string.file_browser_invalid_name);
                return;
            }
            if (new File(dir, name).exists()) {
                toast(R.string.file_browser_exists);
                return;
            }
            runFileOperation(() -> {
                if (directory) {
                    FileOperator.createDirectory(dir, name);
                } else {
                    FileOperator.createFile(dir, name);
                }
            });
        });
        dialog.setTitle(getString(directory ? R.string.file_browser_new_folder : R.string.file_browser_new_file));
        dialog.show();
    }

    private void showRenameDialog(File file) {
        EditDialog dialog = new EditDialog(this, file.getName(), name -> {
            if (!FileOperator.isValidFileName(name)) {
                toast(R.string.file_browser_invalid_name);
                return;
            }
            File target = new File(file.getParentFile(), name);
            if (target.exists()) {
                toast(R.string.file_browser_exists);
                return;
            }
            runFileOperation(() -> FileOperator.rename(file, name));
        });
        dialog.setTitle(getString(R.string.file_browser_rename));
        dialog.show();
    }

    private void confirmDelete(List<File> files) {
        FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(this);
        builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
        builder.setTitle(getString(R.string.file_browser_delete));
        builder.setMessage(getString(R.string.file_browser_delete_message));
        builder.setPositiveButton(() -> {
            List<String> paths = files.stream().map(File::getAbsolutePath).collect(Collectors.toList());
            runFileOperation(() -> {
                for (File file : files) {
                    FileOperator.delete(file);
                }
                Schedulers.androidUIThread().execute(() -> selectedFiles.removeAll(paths));
            });
        });
        builder.setNegativeButton(null);
        builder.create().show();
    }

    private void confirmDeleteSelected() {
        if (selectedFiles.isEmpty()) {
            toast(R.string.file_browser_no_selection);
            return;
        }
        confirmDelete(selectedFiles.stream().map(File::new).collect(Collectors.toList()));
    }

    private void pasteClipboard() {
        if (FileClipboard.isEmpty()) {
            toast(R.string.file_browser_clipboard_empty);
            return;
        }
        List<File> files = new ArrayList<>(FileClipboard.getFiles());
        boolean cut = FileClipboard.isCut();
        File dir = currentPath.toFile();
        runFileOperation(() -> {
            if (cut) {
                FileOperator.moveTo(files, dir);
                FileClipboard.clear();
            } else {
                FileOperator.copyTo(files, dir);
            }
        });
    }

    private void clipboardSelected(boolean cut) {
        if (selectedFiles.isEmpty()) {
            toast(R.string.file_browser_no_selection);
            return;
        }
        List<File> files = selectedFiles.stream().map(File::new).collect(Collectors.toList());
        if (cut) {
            FileClipboard.cut(files);
        } else {
            FileClipboard.copy(files);
        }
    }

    private void selectAll() {
        selectedFiles = new ArrayList<>();
        for (File file : adapter.getFiles()) {
            if (file.isFile()) {
                selectedFiles.add(file.getAbsolutePath());
            }
        }
        adapter.setSelectedFiles(selectedFiles);
        adapter.notifyDataSetChanged();
    }

    private void showDetails(File file) {
        Schedulers.io().execute(() -> {
            String detail = getString(R.string.file_browser_name) + ": " + file.getName() + "\n"
                    + getString(R.string.file_browser_type) + ": "
                    + getString(file.isDirectory() ? R.string.file_browser_type_folder : R.string.file_browser_type_file) + "\n"
                    + getString(R.string.file_browser_size) + ": " + FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(file)) + "\n"
                    + getString(R.string.file_browser_last_modified) + ": " + formatter.format(new Date(file.lastModified())) + "\n"
                    + getString(R.string.file_browser_path) + ": " + file.getAbsolutePath();
            Schedulers.androidUIThread().execute(() -> {
                if (isDestroyed() || isFinishing()) {
                    return;
                }
                FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(this);
                builder.setAlertLevel(FCLAlertDialog.AlertLevel.INFO);
                builder.setTitle(getString(R.string.file_browser_details));
                builder.setMessage(detail);
                builder.setPositiveButton(() -> { });
                builder.create().show();
            });
        });
    }

    private void shareFile(File file) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        Uri uri = FileProvider.getUriForFile(this, getString(R.string.file_browser_provider), file);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, getString(R.string.file_browser_share_title)));
    }

}

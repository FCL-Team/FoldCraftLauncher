package com.tungsten.fcl.ui.download.version;

import static com.tungsten.fclcore.util.Logging.LOG;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.DownloadProviders;
import com.tungsten.fclcore.download.ComponentRemoteVersion;
import com.tungsten.fclcore.game.GameComponentType;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLPage;
import com.tungsten.fcllibrary.component.ui.PageFlows;
import com.tungsten.fcllibrary.component.view.FCLCheckBox;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 组件（forge/fabric 等）远端版本选择页：只渲染 {@link DownloadListViewModel} 的清单状态并触发加载。
 * VM 按 libraryId 取 key，同一组件的临时页反复弹出时直接复用已加载的清单。
 */
public class InstallerListPage extends FCLPage implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private final String gameVersion;
    private final String libraryId;
    private final Callback callback;
    private RemoteVersionListAdapter.OnRemoteVersionSelectListener listener;

    private FCLCheckBox checkRelease;
    private FCLCheckBox checkSnapShot;
    private FCLCheckBox checkOld;
    private FCLImageButton refresh;
    private FCLImageButton failedRefresh;
    private FCLProgressBar progressBar;
    private RecyclerView recyclerView;

    private RemoteVersionListAdapter adapter;
    private DownloadListViewModel viewModel;

    /** 最近一次 Loaded 的全量清单（未按复选框过滤），过滤重算的数据源 */
    private List<ComponentRemoteVersion> versions = Collections.emptyList();

    public InstallerListPage(Context context, int id, String gameVersion, String libraryId, Callback callback) {
        super(context, id, R.layout.page_install_version);
        this.gameVersion = gameVersion;
        this.libraryId = libraryId;
        this.callback = callback;
        create();
    }

    public void create() {
        FCLLinearLayout checkBar = findViewById(R.id.bar);
        checkBar.setVisibility(DownloadProviders.getDownloadProvider().getVersionList(GameComponentType.fromPatchId(libraryId)).hasType() ? View.VISIBLE : View.GONE);

        checkRelease = findViewById(R.id.release);
        checkSnapShot = findViewById(R.id.snapshot);
        checkOld = findViewById(R.id.old);
        refresh = findViewById(R.id.refresh);
        failedRefresh = findViewById(R.id.failed_refresh);
        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.list);

        checkRelease.setChecked(true);

        checkRelease.setOnCheckedChangeListener(this);
        checkSnapShot.setOnCheckedChangeListener(this);
        checkOld.setOnCheckedChangeListener(this);
        refresh.setOnClickListener(this);
        failedRefresh.setOnClickListener(this);

        listener = callback::onSelect;

        findViewById(R.id.april_fools).setVisibility(View.INVISIBLE);
        findViewById(R.id.search).setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new RemoteVersionListAdapter(getContext(), listener);
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(getActivity()).get("download_list_" + libraryId, DownloadListViewModel.class);
        PageFlows.observe(this, viewModel.getState(), this::render);
        viewModel.load(gameVersion, GameComponentType.fromPatchId(libraryId), false);
    }

    private void render(DownloadListViewModel.State state) {
        if (state instanceof DownloadListViewModel.State.Loaded loaded) {
            versions = loaded.getVersions();
            if (versions.isEmpty()) {
                Toast.makeText(getContext(), getContext().getString(R.string.download_failed_empty), Toast.LENGTH_SHORT).show();
                recyclerView.setVisibility(View.GONE);
                failedRefresh.setVisibility(View.VISIBLE);
            } else {
                // 过滤后为空通常是复选框把所有类型都关了：放开类型过滤（回调会重新过滤）
                if (loadVersions().isEmpty()) {
                    checkRelease.setChecked(true);
                    checkSnapShot.setChecked(true);
                    checkOld.setChecked(true);
                }
                applyFilters();
                recyclerView.setVisibility(View.VISIBLE);
                failedRefresh.setVisibility(View.GONE);
            }
            progressBar.setVisibility(View.GONE);
            refresh.setEnabled(true);
        } else if (state instanceof DownloadListViewModel.State.Failed failed) {
            progressBar.setVisibility(View.GONE);
            refresh.setEnabled(true);
            if (failed.getLastList().isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                failedRefresh.setVisibility(View.VISIBLE);
            } else {
                // 保留已加载的旧列表（adapter 未被触碰），仅显示重试入口
                recyclerView.setVisibility(View.VISIBLE);
                failedRefresh.setVisibility(View.VISIBLE);
            }
        } else {
            // Idle / Loading：清单拉取中
            recyclerView.setVisibility(View.GONE);
            failedRefresh.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            refresh.setEnabled(false);
        }
    }

    private List<ComponentRemoteVersion> loadVersions() {
        return versions.stream()
                .filter(it -> switch (it.getVersionType()) {
                    case RELEASE -> checkRelease.isChecked();
                    case SNAPSHOT -> checkSnapShot.isChecked();
                    case OLD -> checkOld.isChecked();
                    default -> true;
                })
                .sorted().collect(Collectors.toList());
    }

    private void applyFilters() {
        adapter.submitList(new ArrayList<>(loadVersions()));
    }

    @Override
    public Task<?> refresh(Object... param) {
        return Task.runAsync(() -> {

        });
    }

    @Override
    public void onClick(View view) {
        if (view == refresh || view == failedRefresh) {
            viewModel.load(gameVersion, GameComponentType.fromPatchId(libraryId), true);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton == checkRelease || compoundButton == checkSnapShot || compoundButton == checkOld) {
            applyFilters();
        }
    }

    public interface Callback {
        void onSelect(ComponentRemoteVersion remoteVersion);
    }
}

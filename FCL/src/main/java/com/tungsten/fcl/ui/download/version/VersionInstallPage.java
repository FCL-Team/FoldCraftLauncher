package com.tungsten.fcl.ui.download.version;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tungsten.fcl.R;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fclcore.download.ComponentRemoteVersion;
import com.tungsten.fclcore.game.GameComponentType;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.versioning.GameVersionNumber;
import com.tungsten.fcllibrary.component.ui.FCLPage;
import com.tungsten.fcllibrary.component.ui.PageFlows;
import com.tungsten.fcllibrary.component.view.FCLCheckBox;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 游戏版本安装页：只渲染 {@link DownloadListViewModel} 的清单状态并触发加载。
 * 清单数据与加载状态挂 Activity 级 ViewModel，页面随 ViewPager 回收重建时直接恢复，
 * 不重新网络拉取（刷新按钮才是显式 force 通道）。
 */
public class VersionInstallPage extends FCLPage implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String VM_KEY = "download_list_game";

    private FCLCheckBox checkRelease;
    private FCLCheckBox checkSnapShot;
    private FCLCheckBox checkOld;
    private FCLCheckBox checkAprilFools;
    private FCLImageButton refresh;
    private FCLImageButton failedRefresh;
    private FCLProgressBar progressBar;
    private RecyclerView recyclerView;
    private FCLEditText search;

    private RemoteVersionListAdapter.OnRemoteVersionSelectListener listener;
    private RemoteVersionListAdapter adapter;
    private DownloadListViewModel viewModel;

    /** 最近一次 Loaded 的全量清单（未按复选框/搜索过滤），过滤重算的数据源 */
    private List<ComponentRemoteVersion> versions = Collections.emptyList();

    public VersionInstallPage(Context context, int id) {
        super(context, id, R.layout.page_install_version);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        checkRelease = findViewById(R.id.release);
        checkSnapShot = findViewById(R.id.snapshot);
        checkOld = findViewById(R.id.old);
        checkAprilFools = findViewById(R.id.april_fools);
        refresh = findViewById(R.id.refresh);
        failedRefresh = findViewById(R.id.failed_refresh);
        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.list);
        search = findViewById(R.id.search);

        checkRelease.setChecked(true);

        checkRelease.setOnCheckedChangeListener(this);
        checkSnapShot.setOnCheckedChangeListener(this);
        checkOld.setOnCheckedChangeListener(this);
        checkAprilFools.setOnCheckedChangeListener(this);
        refresh.setOnClickListener(this);
        failedRefresh.setOnClickListener(this);

        listener = remoteVersion -> {
            VersionInstallInfoPage page = new VersionInstallInfoPage(getContext(), FCLPage.PAGE_ID_TEMP, remoteVersion.getGameVersion());
            UIManager.getInstance().getDownloadUI().showTempPage(page);
        };

        search.stringProperty().addListener(observable -> applyFilters());

        adapter = new RemoteVersionListAdapter(getContext(), listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(getActivity()).get(VM_KEY, DownloadListViewModel.class);
        PageFlows.observe(this, viewModel.getState(), this::render);
        viewModel.load("", GameComponentType.GAME, false);
    }

    private void render(DownloadListViewModel.State state) {
        if (state instanceof DownloadListViewModel.State.Loaded loaded) {
            versions = loaded.getVersions();
            // 过滤后为空通常是复选框把所有类型都关了：放开类型过滤（回调会重新过滤）
            if (loadVersions().isEmpty()) {
                checkRelease.setChecked(true);
                checkSnapShot.setChecked(true);
                checkOld.setChecked(true);
            }
            applyFilters();
            recyclerView.setVisibility(View.VISIBLE);
            failedRefresh.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            refresh.setEnabled(true);
        } else if (state instanceof DownloadListViewModel.State.Failed failed) {
            progressBar.setVisibility(View.GONE);
            refresh.setEnabled(true);
            if (failed.getLastList().isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                failedRefresh.setVisibility(View.VISIBLE);
            } else {
                // 保留已加载的旧列表（adapter 未被触碰），仅显示重试入口，瞬时网络故障不掩盖可用数据
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
                    case PENDING, UNOBFUSCATED, SNAPSHOT -> {
                        if (checkSnapShot.isChecked()) yield true;
                        else if (checkAprilFools.isChecked())
                            yield GameVersionNumber.asGameVersion(it.getGameVersion()).isAprilFools();
                        yield false;
                    }
                    case OLD -> {
                        if (checkOld.isChecked()) yield true;
                        else if (checkAprilFools.isChecked())
                            yield GameVersionNumber.asGameVersion(it.getGameVersion()).isAprilFools();
                        yield false;
                    }
                    default -> true;
                })
                .filter(it -> it.getGameVersion().contains(search.getStringValue()))
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
            search.setText("");
            viewModel.load("", GameComponentType.GAME, true);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton == checkRelease || compoundButton == checkSnapShot || compoundButton == checkOld || compoundButton == checkAprilFools) {
            applyFilters();
        }
    }
}

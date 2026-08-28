package com.tungsten.fcl.ui.download;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.material.tabs.TabLayout;
import com.tungsten.fcl.R;
import com.tungsten.fcl.ui.download.common.DownloadPage;
import com.tungsten.fcl.ui.download.common.RemoteModInfoPage;
import com.tungsten.fcl.ui.download.version.VersionInstallPage;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLCommonUI;
import com.tungsten.fcllibrary.component.ui.FCLPage;
import com.tungsten.fcllibrary.component.view.FCLTabLayout;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;

/**
 * 下载 UI：游戏安装页与 5 个下载模式（Mod/整合包/资源包/世界/光影）共享一个
 * DownloadPage 实例，tab 切换只更新数据源与恢复状态，不重复创建页面。
 */
public class DownloadUI extends FCLCommonUI {

    public static final int PAGE_ID_DOWNLOAD_GAME = 15010;
    public static final int PAGE_ID_DOWNLOAD_MODPACK = 15011;
    public static final int PAGE_ID_DOWNLOAD_MOD = 15012;
    public static final int PAGE_ID_DOWNLOAD_RESOURCE_PACK = 15013;
    public static final int PAGE_ID_DOWNLOAD_WORLD = 15014;
    public static final int PAGE_ID_DOWNLOAD_SHADER_PACK = 15015;

    private static final int TEMP_PAGE_ANIM_DURATION = 200;

    public FCLTabLayout tabLayout;
    public FCLUILayout container;

    private FrameLayout contentContainer;
    private FrameLayout overlay;

    private VersionInstallPage versionInstallPage;
    private DownloadPage downloadPage;

    private final ArrayList<FCLPage> tempPageStack = new ArrayList<>();
    private int currentPageId = PAGE_ID_DOWNLOAD_GAME;

    public DownloadUI(Context context, int id) {
        super(context, id);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tabLayout = findViewById(R.id.tab_layout);
        container = findViewById(R.id.container);

        // 内容层：游戏安装页 + 共享下载页
        contentContainer = new FrameLayout(getContext());
        container.addView(contentContainer, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        versionInstallPage = new VersionInstallPage(getContext(), PAGE_ID_DOWNLOAD_GAME);
        downloadPage = new DownloadPage(getContext());
        contentContainer.addView(versionInstallPage.getContentView(), new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        contentContainer.addView(downloadPage.getContentView(), new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        downloadPage.getContentView().setVisibility(View.GONE);

        // 临时页覆盖层
        overlay = new FrameLayout(getContext());
        overlay.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        overlay.setVisibility(View.GONE);
        container.addView(overlay);

        // tab 切换：游戏页独立，5 个下载模式共享 DownloadPage
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switchTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // 页面离开屏幕仅是 detach（实例保留，重新可见时不重跑 onCreate），
        // 因此重新可见时需刷新打开中的详情页推荐版本（目录/版本可能在其他页面被切换）
        getContentView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(@NonNull View v) {
                for (FCLPage page : tempPageStack) {
                    if (page instanceof RemoteModInfoPage) {
                        ((RemoteModInfoPage) page).reloadVersions();
                    }
                }
            }

            @Override
            public void onViewDetachedFromWindow(@NonNull View v) {

            }
        });
    }

    private void switchTab(int position) {
        // 切换 tab 时关闭临时页（临时页属于原页面上下文，避免覆盖层遮挡新页面）
        dismissAllTempPages();
        if (position == 0) {
            if (currentPageId == PAGE_ID_DOWNLOAD_GAME) return;
            currentPageId = PAGE_ID_DOWNLOAD_GAME;
            versionInstallPage.getContentView().setVisibility(View.VISIBLE);
            downloadPage.getContentView().setVisibility(View.GONE);
            playEnterAnimation(versionInstallPage.getContentView());
        } else {
            int pageId = tabPositionToPageId(position);
            if (currentPageId == pageId) return;
            currentPageId = pageId;
            downloadPage.getContentView().setVisibility(View.VISIBLE);
            versionInstallPage.getContentView().setVisibility(View.GONE);
            downloadPage.switchType(pageId);
            // 5 个下载模式页共用同一视图，内容更新后播放过渡动画（游戏页 ↔ 模式页、模式页之间均适用）
            playEnterAnimation(downloadPage.getContentView());
        }
    }

    private static int tabPositionToPageId(int position) {
        return switch (position) {
            case 1 -> PAGE_ID_DOWNLOAD_MODPACK;
            case 2 -> PAGE_ID_DOWNLOAD_MOD;
            case 3 -> PAGE_ID_DOWNLOAD_RESOURCE_PACK;
            case 4 -> PAGE_ID_DOWNLOAD_WORLD;
            default -> PAGE_ID_DOWNLOAD_SHADER_PACK;
        };
    }

    private static int pageIdToTabPosition(int pageId) {
        return switch (pageId) {
            case PAGE_ID_DOWNLOAD_MODPACK -> 1;
            case PAGE_ID_DOWNLOAD_MOD -> 2;
            case PAGE_ID_DOWNLOAD_RESOURCE_PACK -> 3;
            case PAGE_ID_DOWNLOAD_WORLD -> 4;
            default -> 5;
        };
    }

    /**
     * 页面切换过渡动画：淡入 + 上滑进入（同步执行，页面首次可见即为动画起点，避免先显示后置透明的闪烁）
     */
    private void playEnterAnimation(View view) {
        view.animate().cancel();
        view.setAlpha(0f);
        view.setTranslationY(view.getResources().getDisplayMetrics().density * 30f);
        view.animate().alpha(1f).translationY(0f).setDuration(250).start();
    }

    /**
     * 返回过渡：下层上滑进入（仅位移不做淡入，避免与临时页淡出叠加时 alpha 硬件层切换闪烁）
     */
    private void slideIn(View view) {
        view.animate().cancel();
        view.setTranslationY(view.getResources().getDisplayMetrics().density * 30f);
        view.animate().translationY(0f).setDuration(250).start();
    }

    /**
     * 供外部跳转（如模组管理页）：切换到指定下载模式并显示下载页
     */
    public void showDownloadPage(int pageId) {
        TabLayout.Tab tab = tabLayout.getTabAt(pageIdToTabPosition(pageId));
        if (tab != null) {
            tab.select();
        }
    }

    public DownloadPage getDownloadPage() {
        return downloadPage;
    }

    public boolean canReturn() {
        return !tempPageStack.isEmpty();
    }

    /**
     * 在覆盖层上显示临时页并压入导航栈（隐藏下层内容，临时页独占显示）
     */
    public void showTempPage(FCLPage page) {
        if (overlay == null) return;
        // 隐藏当前栈顶临时页与下层内容，避免透明背景下层内容透出
        if (!tempPageStack.isEmpty()) {
            tempPageStack.get(tempPageStack.size() - 1).getContentView().setVisibility(View.GONE);
        }
        contentContainer.setVisibility(View.GONE);
        // 新临时页淡入
        View view = page.getContentView();
        view.setAlpha(0f);
        overlay.setVisibility(View.VISIBLE);
        overlay.addView(view, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        view.animate().alpha(1f).setDuration(TEMP_PAGE_ANIM_DURATION).start();
        tempPageStack.add(page);
    }

    /**
     * 弹栈顶临时页（淡出后移除并恢复下层）
     */
    public void dismissCurrentTempPage() {
        if (tempPageStack.isEmpty()) return;
        FCLPage page = tempPageStack.remove(tempPageStack.size() - 1);
        View view = page.getContentView();
        // 恢复下层（与临时页淡出交叉进行，形成返回过渡动画）
        if (!tempPageStack.isEmpty()) {
            View lowerView = tempPageStack.get(tempPageStack.size() - 1).getContentView();
            lowerView.setVisibility(View.VISIBLE);
            slideIn(lowerView);
        } else {
            contentContainer.setVisibility(View.VISIBLE);
            slideIn(contentContainer);
        }
        view.animate().alpha(0f).setDuration(TEMP_PAGE_ANIM_DURATION).withEndAction(() -> {
            overlay.removeView(view);
            if (tempPageStack.isEmpty()) {
                overlay.setVisibility(View.GONE);
            }
        }).start();
    }

    /**
     * 清空全部临时页
     */
    public void dismissAllTempPages() {
        while (!tempPageStack.isEmpty()) {
            dismissCurrentTempPage();
        }
    }

    @Override
    public void onBackPressed() {
        if (canReturn()) {
            dismissCurrentTempPage();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }
}

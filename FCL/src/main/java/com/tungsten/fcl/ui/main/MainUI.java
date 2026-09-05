package com.tungsten.fcl.ui.main;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.mio.skin.AnimationDialog;
import com.mio.skin.SkinAnimations;
import com.mio.skin.SkinRenderer;
import com.mio.skin.SkinTextureLoader;
import com.mio.skin.SkinViewer;

import static com.mio.skin.SkinAnimationsKt.restoreSkinAnimation;
import static com.mio.skin.SkinAnimationsKt.saveSkinAnimation;
import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fclcore.auth.Account;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.HttpRequest;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.ui.FCLCommonUI;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.LocaleUtils;

import java.util.logging.Level;

public class MainUI extends FCLCommonUI implements View.OnClickListener {

    public static final String ANNOUNCEMENT_URL = "https://raw.githubusercontent.com/FCL-Team/FCL-Repo/refs/heads/main/res/announcement_v2.txt";
    public static final String ANNOUNCEMENT_URL_CN = "https://gitee.com/fcl-team/FCL-Repo/raw/main/res/announcement_v2.txt";

    private LinearLayoutCompat announcementContainer;
    private LinearLayoutCompat announcementLayout;
    private FCLTextView title;
    private FCLTextView announcementView;
    private FCLTextView date;
    private FCLButton hide;
    private Announcement announcement = null;

    private SkinViewer skinViewer;
    private SkinRenderer renderer;
    private SkinTextureLoader skinLoader;

    /** 选中账户变化时重载皮肤（attach 时注册、detach 时注销） */
    private final InvalidationListener accountListener = o ->
            skinLoader.load(Accounts.getSelectedAccount(), false);

    public MainUI(Context context, int id) {
        super(context, id);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        announcementContainer = findViewById(R.id.announcement_container);
        announcementLayout = findViewById(R.id.announcement_layout);
        title = findViewById(R.id.title);
        announcementView = findViewById(R.id.announcement);
        date = findViewById(R.id.date);
        hide = findViewById(R.id.hide);
        ThemeEngine.getInstance().registerEvent(announcementLayout, () -> announcementLayout.getBackground().setTint(ThemeEngine.getInstance().getTheme().getColor()));
        hide.setOnClickListener(this);

        skinViewer = findViewById(R.id.skin_viewer);
        renderer = new SkinRenderer(getContext());
        skinViewer.setRenderer(renderer, 5f);
        skinLoader = new SkinTextureLoader(renderer);
        skinLoader.load(Accounts.getSelectedAccount(), false);
        // 恢复上次选择的动画
        restoreSkinAnimation(getContext(), renderer);
        // 双击模型弹出动画切换窗口
        skinViewer.setOnDoubleClick(() -> {
            Context context = getContext();
            if (context instanceof Activity && !((Activity) context).isDestroyed() && !((Activity) context).isFinishing()) {
                new AnimationDialog(context, renderer.getAnimationId(), clipId -> {
                    renderer.playAnimation(clipId);
                    saveSkinAnimation(context, renderer);
                }).show();
            }
        });
        checkAnnouncement();

        // 皮肤渲染随页面挂载/回收恢复与暂停（替代原 onStart/onStop 生命周期）
        getContentView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(@NonNull View v) {
                if (skinViewer != null) {
                    if (!ThemeEngine.getInstance().getTheme().isCloseSkinModel()) {
                        skinViewer.setVisibility(View.VISIBLE);
                        skinViewer.onResume();
                        // 纹理由渲染线程 onSurfaceCreated 从 renderer.texture 自行重建，
                        // 此处不重喂纹理；同账户已在 onCreate 加载过，load 会自动跳过
                    } else {
                        skinViewer.onPause();
                        skinViewer.setVisibility(View.GONE);
                    }
                }
                if (skinLoader != null) {
                    // detach 期间选中的账户可能已切换（监听已注销），重新对齐一次
                    Accounts.selectedAccountProperty().addListener(accountListener);
                    skinLoader.load(Accounts.getSelectedAccount(), false);
                }
            }

            @Override
            public void onViewDetachedFromWindow(@NonNull View v) {
                Accounts.selectedAccountProperty().removeListener(accountListener);
                if (skinLoader != null) {
                    skinLoader.release();
                }
                if (skinViewer != null) {
                    skinViewer.onPause();
                    skinViewer.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (skinViewer != null) {
            skinViewer.onPause();
            skinViewer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (skinViewer != null && isShowing() && !ThemeEngine.getInstance().getTheme().isCloseSkinModel()) {
            skinViewer.setVisibility(View.VISIBLE);
            skinViewer.onResume();
        }
    }

    @Override
    public Task<?> refresh(Object... param) {
        return Task.runAsync(() -> {

        });
    }

    private void checkAnnouncement() {
        try {
            String url = LocaleUtils.isChinese(getContext()) ? ANNOUNCEMENT_URL_CN : ANNOUNCEMENT_URL;
            Task.supplyAsync(() -> HttpRequest.HttpGetRequest.GET(url).getJson(Announcement.class))
                    .thenAcceptAsync(Schedulers.androidUIThread(), announcement -> {
                        this.announcement = announcement;
                        if (!announcement.shouldDisplay(getContext()))
                            return;
                        announcementContainer.setVisibility(View.VISIBLE);
                        title.setText(getContext().getString(R.string.announcement, announcement.getDisplayTitle(getContext())));
                        announcementView.setText(announcement.getDisplayContent(getContext()));
                        date.setText(getContext().getString(R.string.update_date, announcement.getDate()));
                    }).start();
        } catch (Exception e) {
            Logging.LOG.log(Level.WARNING, "Failed to get announcement!", e);
        }
    }

    private void hideAnnouncement() {
        announcementContainer.setVisibility(View.GONE);
        if (announcement != null) {
            announcement.hide(getContext());
        }
    }

    public void refreshSkin(Account account) {
        Schedulers.androidUIThread().execute(() -> {
            if (skinLoader != null) {
                skinLoader.load(account, true);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == hide) {
            if (announcement != null && announcement.isSignificant()) {
                FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                builder.setCancelable(false);
                builder.setMessage(getContext().getString(R.string.announcement_significant));
                builder.setPositiveButton(this::hideAnnouncement);
                builder.setNegativeButton(null);
                builder.create().show();
            } else {
                hideAnnouncement();
            }
        }
    }
}

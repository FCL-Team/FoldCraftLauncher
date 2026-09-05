package com.tungsten.fcl.ui.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.mio.skin.SkinRenderer;
import com.mio.skin.SkinViewer;
import com.tungsten.fcl.R;
import com.tungsten.fcl.game.TexturesLoader;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fclcore.auth.Account;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.binding.ObjectBinding;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
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

    private ObjectProperty<Account> currentAccount;
    private ObjectBinding<Bitmap[]> skinBinding;

    private final InvalidationListener skinBindingListener = observable -> {
        Bitmap[] texture = skinBinding.get();
        if (texture != null) {
            renderer.updateTexture(texture[0], texture[1]);
        }
    };

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
        checkAnnouncement();
        setupSkinDisplay();

        // 皮肤渲染随页面挂载/回收恢复与暂停（替代原 onStart/onStop 生命周期）
        getContentView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(@NonNull View v) {
                if (skinViewer != null) {
                    if (!ThemeEngine.getInstance().getTheme().isCloseSkinModel()) {
                        skinViewer.setVisibility(View.VISIBLE);
                        skinViewer.onResume();
                        Bitmap[] texture = renderer.getTexture();
                        if (texture != null) {
                            renderer.updateTexture(texture[0], texture[1]);
                        }
                    } else {
                        skinViewer.onPause();
                        skinViewer.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onViewDetachedFromWindow(@NonNull View v) {
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

    private void setupSkinDisplay() {
        currentAccount = new SimpleObjectProperty<>() {

            @Override
            protected void invalidated() {
                Account account = get();
                if (skinBinding != null) {
                    skinBinding.removeListener(skinBindingListener);
                    skinBinding = null;
                }
                if (account == null) {
                    renderer.updateTexture(BitmapFactory.decodeStream(MainUI.class.getResourceAsStream("/assets/img/alex.png")), null);
                } else {
                    skinBinding = TexturesLoader.textureBinding(account);
                    skinBinding.addListener(skinBindingListener);
                    // fakefx bind() 会立即同步当前值并触发懒加载绑定；仅 addListener 时
                    // 皮肤已缓存的情况下不会再回调，需主动取一次当前值
                    Bitmap[] texture = skinBinding.get();
                    if (texture != null) {
                        renderer.updateTexture(texture[0], texture[1]);
                    }
                }
            }
        };
        currentAccount.bind(Accounts.selectedAccountProperty());
    }

    public void refreshSkin(Account account) {
        Schedulers.androidUIThread().execute(() -> {
            if (currentAccount.get() == account) {
                if (skinBinding != null) {
                    skinBinding.removeListener(skinBindingListener);
                }
                skinBinding = TexturesLoader.textureBinding(currentAccount.get());
                skinBinding.addListener(skinBindingListener);
                Bitmap[] texture = skinBinding.get();
                if (texture != null) {
                    renderer.updateTexture(texture[0], texture[1]);
                }
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

package com.tungsten.fcl.ui.main;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.tungsten.fcl.R;
import com.tungsten.fcl.game.TexturesLoader;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclcore.auth.Account;
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
import com.tungsten.fcllibrary.component.view.FCLUILayout;
import com.tungsten.fcllibrary.skin.SkinCanvas;
import com.tungsten.fcllibrary.skin.SkinRenderer;
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

    private RelativeLayout skinContainer;
    private SkinCanvas skinCanvas;
    private SkinRenderer renderer;

    private ObjectProperty<Account> currentAccount;

    public MainUI(Context context, FCLUILayout parent, int id) {
        super(context, parent, id);
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

        skinContainer = findViewById(R.id.skin_container);
        renderer = new SkinRenderer(getContext());
        ViewGroup.LayoutParams layoutParamsSkin = skinContainer.getLayoutParams();
        layoutParamsSkin.width = (int) (((View) skinContainer.getParent().getParent()).getMeasuredWidth() * 0.5f);
        layoutParamsSkin.height = (int) Math.min(((View) skinContainer.getParent().getParent()).getMeasuredWidth() * 0.5f, ((View) skinContainer.getParent().getParent()).getMeasuredHeight());
        skinContainer.setLayoutParams(layoutParamsSkin);

        checkAnnouncement();

        setupSkinDisplay();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!ThemeEngine.getInstance().theme.isCloseSkinModel()) {
            if (skinCanvas == null) {
                skinCanvas = new SkinCanvas(getContext());
                skinCanvas.setRenderer(renderer, 5f);
            } else {
                skinCanvas.onResume();
                renderer.updateTexture(renderer.getTexture()[0], renderer.getTexture()[1]);
            }

            skinContainer.addView(skinCanvas);
            skinContainer.setVisibility(View.VISIBLE);
        } else {
            if (skinCanvas != null) skinCanvas.onPause();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (skinCanvas != null) {
            skinCanvas.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isShowing() && skinCanvas != null) {
            skinCanvas.onResume();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (skinCanvas != null) {
            skinCanvas.onPause();
        }
        skinContainer.removeView(skinCanvas);
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
                        title.setText(AndroidUtils.getLocalizedText(getContext(), "announcement", announcement.getDisplayTitle(getContext())));
                        announcementView.setText(announcement.getDisplayContent(getContext()));
                        date.setText(AndroidUtils.getLocalizedText(getContext(), "update_date", announcement.getDate()));
                    }).start();
        } catch (Exception e) {
            e.printStackTrace();
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
        currentAccount = new SimpleObjectProperty<Account>() {

            @Override
            protected void invalidated() {
                Account account = get();
                renderer.textureProperty().unbind();
                if (account == null) {
                    renderer.updateTexture(BitmapFactory.decodeStream(MainUI.class.getResourceAsStream("/assets/img/alex.png")), null);
                } else {
                    renderer.textureProperty().bind(TexturesLoader.textureBinding(account));
                }
            }
        };
        currentAccount.bind(Accounts.selectedAccountProperty());
    }

    public void refreshSkin(Account account) {
        Schedulers.androidUIThread().execute(() -> {
            if (currentAccount.get() == account) {
                renderer.textureProperty().unbind();
                renderer.textureProperty().bind(TexturesLoader.textureBinding(currentAccount.get()));
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

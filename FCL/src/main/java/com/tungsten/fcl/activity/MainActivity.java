package com.tungsten.fcl.activity;

import static com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType.MINECRAFT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.tungsten.fcl.R;
import com.tungsten.fcl.game.TexturesLoader;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fcl.setting.ConfigHolder;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fcl.ui.version.VersionListItem;
import com.tungsten.fclcore.auth.offline.OfflineAccount;
import com.tungsten.fclcore.download.LibraryAnalyzer;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.binding.StringBinding;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty;
import com.tungsten.fclcore.fakefx.beans.property.StringProperty;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.function.ExceptionalConsumer;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLDynamicIsland;
import com.tungsten.fcllibrary.component.view.FCLImageView;
import com.tungsten.fcllibrary.component.view.FCLMenuView;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.component.view.FCLUILayout;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class MainActivity extends FCLActivity implements FCLMenuView.OnSelectListener, View.OnClickListener {

    private static MainActivity instance;
    private boolean ready;

    public ConstraintLayout background;
    private FCLDynamicIsland titleView;

    private UIManager uiManager;
    public FCLUILayout uiLayout;

    private LinearLayoutCompat leftMenu;
    private FCLMenuView home;
    private FCLMenuView manage;
    private FCLMenuView download;
    private FCLMenuView multiplayer;
    private FCLMenuView setting;

    private LinearLayoutCompat account;
    private FCLImageView avatar;
    private FCLTextView accountName;
    private FCLTextView accountHint;
    private LinearLayoutCompat version;
    private FCLImageView icon;
    private FCLTextView versionName;
    private FCLTextView versionHint;
    private FCLButton launch;

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        background = findViewById(R.id.background);
        background.setBackground(ThemeEngine.getInstance().getTheme().getBackground(this));

        titleView = findViewById(R.id.title);

        try {
            ConfigHolder.init();
        } catch (IOException e) {
            Logging.LOG.log(Level.WARNING, e.getMessage());
        }

        uiLayout = findViewById(R.id.ui_layout);
        uiLayout.post(() -> {
            leftMenu = findViewById(R.id.left_menu);
            ThemeEngine.getInstance().registerEvent(leftMenu, () -> leftMenu.setBackgroundColor(ThemeEngine.getInstance().getTheme().getColor()));

            account = findViewById(R.id.account);
            avatar = findViewById(R.id.avatar);
            accountName = findViewById(R.id.account_name);
            accountHint = findViewById(R.id.account_hint);
            version = findViewById(R.id.version);
            icon = findViewById(R.id.icon);
            versionName = findViewById(R.id.version_name);
            versionHint = findViewById(R.id.version_hint);
            launch = findViewById(R.id.launch);
            account.setOnClickListener(this);
            version.setOnClickListener(this);
            launch.setOnClickListener(this);

            uiManager = new UIManager(this, uiLayout);
            uiManager.init(() -> {
                home = findViewById(R.id.home);
                manage = findViewById(R.id.manage);
                download = findViewById(R.id.download);
                multiplayer = findViewById(R.id.multiplayer);
                setting = findViewById(R.id.setting);
                home.setOnSelectListener(this);
                manage.setOnSelectListener(this);
                download.setOnSelectListener(this);
                multiplayer.setOnSelectListener(this);
                setting.setOnSelectListener(this);
                home.setSelected(true);

                ready = true;
                refresh(null).start();
            });
        });
    }

    @Override
    public void onBackPressed() {
        if (uiManager.getCurrentUI() == uiManager.getMainUI()) {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
        }
        else {
            home.setSelected(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (uiManager != null) {
            uiManager.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (uiManager != null) {
            uiManager.onResume();
        }
        if (ready) {
            refresh(null).start();
        }
    }

    @Override
    public void onSelect(FCLMenuView view) {
        refreshMenuView(view);
        if (view == home) {
            titleView.setTextWithAnim(getString(R.string.app_name));
            uiManager.switchUI(uiManager.getMainUI());
        }
        if (view == manage) {
            titleView.setTextWithAnim(getString(R.string.manage));
            uiManager.switchUI(uiManager.getManageUI());
        }
        if (view == download) {
            titleView.setTextWithAnim(getString(R.string.download));
            uiManager.switchUI(uiManager.getDownloadUI());
        }
        if (view == multiplayer) {
            titleView.setTextWithAnim(getString(R.string.multiplayer));
            uiManager.switchUI(uiManager.getMultiplayerUI());
        }
        if (view == setting) {
            titleView.setTextWithAnim(getString(R.string.setting));
            uiManager.switchUI(uiManager.getSettingUI());
        }
    }

    private void refreshMenuView(FCLMenuView view) {
        FCLMenuView[] views = {
                home,
                manage,
                download,
                multiplayer,
                setting
        };
        for (FCLMenuView v : views) {
            if (v != view) {
                v.setSelected(false);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == account && uiManager.getCurrentUI() != uiManager.getAccountUI()) {
            refreshMenuView(null);
            titleView.setTextWithAnim(getString(R.string.account));
            uiManager.switchUI(uiManager.getAccountUI());
        }
        if (view == version && uiManager.getCurrentUI() != uiManager.getVersionUI()) {
            refreshMenuView(null);
            titleView.setTextWithAnim(getString(R.string.version));
            uiManager.switchUI(uiManager.getVersionUI());
        }
        if (view == launch) {

        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public Task<?> refresh(ObjectProperty<Drawable> avatarProperty) {
        return Task.runAsync(Schedulers.androidUIThread(), () -> {
            if (avatar.getDrawableObjectProperty() == null) {
                avatar.setBackground(TexturesLoader.avatarBinding(Accounts.getSelectedAccount(), ConvertUtils.dip2px(MainActivity.this, 30)).get());
            }
            accountHint.setText(Accounts.getSelectedAccount() == null ? getString(R.string.account_state_add) : Accounts.getLocalizedLoginTypeName(this, Accounts.getAccountFactory(Accounts.getSelectedAccount())));
        }).thenSupplyAsync(() -> {
            // refresh username and avatar
            String nameStr = getString(R.string.account_state_no_account);
            if (Accounts.getSelectedAccount() != null) {
                StringProperty name = new SimpleStringProperty();
                StringBinding characterName = Bindings.createStringBinding(Accounts.getSelectedAccount()::getCharacter, Accounts.getSelectedAccount());
                if (Accounts.getSelectedAccount() instanceof OfflineAccount) {
                    name.bind(characterName);
                } else {
                    name.bind(
                            Accounts.getSelectedAccount().getUsername().isEmpty() ? characterName :
                                    Bindings.concat(Accounts.getSelectedAccount().getUsername(), " - ", characterName));
                }
                nameStr = name.get();
            }
            ObjectProperty<Drawable> image = new SimpleObjectProperty<>();
            image.bind(TexturesLoader.avatarBinding(Accounts.getSelectedAccount(), ConvertUtils.dip2px(this, 30f)));

            // refresh version icon, name and libraries
            if (Profiles.getSelectedVersion() == null) {
                return null;
            }
            String game = Profiles.getSelectedProfile().getRepository().getGameVersion(Profiles.getSelectedVersion()).orElse(getString(R.string.message_unknown));
            StringBuilder libraries = new StringBuilder(game);
            LibraryAnalyzer analyzer = LibraryAnalyzer.analyze(Profiles.getSelectedProfile().getRepository().getResolvedPreservingPatchesVersion(Profiles.getSelectedVersion()));
            for (LibraryAnalyzer.LibraryMark mark : analyzer) {
                String libraryId = mark.getLibraryId();
                String libraryVersion = mark.getLibraryVersion();
                if (libraryId.equals(MINECRAFT.getPatchId())) continue;
                int resId = getResources().getIdentifier("install_installer_" + libraryId.replace("-", "_"), "string", getPackageName());
                if (resId != 0 && getString(resId) != null) {
                    libraries.append(", ").append(getString(resId));
                    if (libraryVersion != null)
                        libraries.append(": ").append(libraryVersion.replaceAll("(?i)" + libraryId, ""));
                }
            }
            ArrayList<Object> results = new ArrayList<>();
            results.add(image);
            results.add(nameStr);
            results.add(new VersionListItem(Profiles.getSelectedProfile(), Profiles.getSelectedVersion(), libraries.toString(), Profiles.getSelectedProfile().getRepository().getVersionIconImage(Profiles.getSelectedVersion())));
            return results;
        }).thenAcceptAsync(Schedulers.androidUIThread(), (ExceptionalConsumer<ArrayList<Object>, Exception>) results -> {
            if (avatar.getDrawableObjectProperty() == null) {
                avatar.bind((ObjectProperty<Drawable>) results.get(0));
            } else if (avatarProperty != null) {
                avatar.bind(avatarProperty);
            }
            accountName.setText(Accounts.getSelectedAccount() == null ? getString(R.string.account_state_no_account) : (String) results.get(1));

            VersionListItem versionListItem = (VersionListItem) results.get(2);
            icon.setBackground(versionListItem == null ? getDrawable(R.drawable.img_grass) : versionListItem.getDrawable());
            versionName.setText(versionListItem == null ? getString(R.string.version_no_version) : versionListItem.getVersion());
            versionHint.setText(versionListItem == null ? getString(R.string.version_manage) : versionListItem.getLibraries());
        });
    }
}
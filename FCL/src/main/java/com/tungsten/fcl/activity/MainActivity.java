package com.tungsten.fcl.activity;

import static com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType.MINECRAFT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
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
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLDynamicIsland;
import com.tungsten.fcllibrary.component.view.FCLMenuView;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.component.view.FCLUILayout;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.io.IOException;
import java.util.logging.Level;

public class MainActivity extends FCLActivity implements FCLMenuView.OnSelectListener, View.OnClickListener {

    private static MainActivity instance;

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
    private AppCompatImageView avatar;
    private FCLTextView accountName;
    private FCLTextView accountHint;
    private LinearLayoutCompat version;
    private AppCompatImageView icon;
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

        uiLayout = findViewById(R.id.ui_layout);
        uiLayout.post(() -> {
            uiManager = new UIManager(this, uiLayout);
            uiManager.init();

            leftMenu = findViewById(R.id.left_menu);
            ThemeEngine.getInstance().registerEvent(leftMenu, () -> leftMenu.setBackgroundColor(ThemeEngine.getInstance().getTheme().getColor()));
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

            refresh();
        });

        try {
            ConfigHolder.init();
        } catch (IOException e) {
            Logging.LOG.log(Level.WARNING, e.getMessage());
        }
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
    public void refresh() {
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
        avatar.setBackground(TexturesLoader.avatarBinding(Accounts.getSelectedAccount(), ConvertUtils.dip2px(this, 30)).get());
        image.addListener((observable, oldValue, newValue) -> runOnUiThread(() -> avatar.setBackground(newValue)));
        accountName.setText(Accounts.getSelectedAccount() == null ? getString(R.string.account_state_no_account) : nameStr);
        accountHint.setText(Accounts.getSelectedAccount() == null ? getString(R.string.account_state_add) : Accounts.getLocalizedLoginTypeName(this, Accounts.getAccountFactory(Accounts.getSelectedAccount())));
        Task.supplyAsync(() -> {
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
            return new VersionListItem(Profiles.getSelectedProfile(), Profiles.getSelectedVersion(), libraries.toString(), Profiles.getSelectedProfile().getRepository().getVersionIconImage(Profiles.getSelectedVersion()));
        }).thenAcceptAsync(Schedulers.androidUIThread(), versionListItem -> {
            icon.setBackground(versionListItem == null ? getDrawable(R.drawable.img_grass) : versionListItem.getDrawable());
            versionName.setText(versionListItem == null ? getString(R.string.version_no_version) : versionListItem.getVersion());
            versionHint.setText(versionListItem == null ? getString(R.string.version_manage) : versionListItem.getLibraries());
        }).start();
    }
}
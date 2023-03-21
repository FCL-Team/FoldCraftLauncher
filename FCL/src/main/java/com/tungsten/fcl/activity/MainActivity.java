package com.tungsten.fcl.activity;

import static com.tungsten.fcl.setting.Accounts.getAccountFactory;
import static com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType.MINECRAFT;
import static com.tungsten.fclcore.fakefx.beans.binding.Bindings.createStringBinding;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.tungsten.fcl.R;
import com.tungsten.fcl.game.TexturesLoader;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fcl.setting.ConfigHolder;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fcl.ui.version.Versions;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.FXUtils;
import com.tungsten.fcl.util.WeakListenerHolder;
import com.tungsten.fclcore.auth.Account;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorAccount;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorServer;
import com.tungsten.fclcore.auth.yggdrasil.TextureModel;
import com.tungsten.fclcore.download.LibraryAnalyzer;
import com.tungsten.fclcore.event.Event;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.fakefx.beans.value.ObservableValue;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.fakefx.BindingMapping;
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
import java.util.function.Consumer;
import java.util.logging.Level;

public class MainActivity extends FCLActivity implements FCLMenuView.OnSelectListener, View.OnClickListener {

    private static MainActivity instance;

    public ConstraintLayout background;
    public FCLDynamicIsland titleView;

    private UIManager uiManager;
    public FCLUILayout uiLayout;

    private ScrollView leftMenu;
    public FCLMenuView home;
    public FCLMenuView manage;
    public FCLMenuView download;
    public FCLMenuView controller;
    public FCLMenuView multiplayer;
    public FCLMenuView setting;

    private LinearLayoutCompat account;
    private FCLImageView avatar;
    private FCLTextView accountName;
    private FCLTextView accountHint;
    private LinearLayoutCompat version;
    private FCLImageView icon;
    private FCLTextView versionName;
    private FCLTextView versionHint;
    private FCLButton launch;

    private ObjectProperty<Account> currentAccount;
    private final WeakListenerHolder holder = new WeakListenerHolder();
    private Profile profile;
    private Consumer<Event> onVersionIconChangedListener;

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
            leftMenu = findViewById(R.id.left_scroll);
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
            launch.setOnLongClickListener(view ->{
                startActivity(new Intent(MainActivity.this,ShellActivity.class));
                return true;
            });

            uiManager = new UIManager(this, uiLayout);
            uiManager.registerDefaultBackEvent(backToMainUI);
            uiManager.init(() -> {
                home = findViewById(R.id.home);
                manage = findViewById(R.id.manage);
                download = findViewById(R.id.download);
                controller = findViewById(R.id.controller);
                multiplayer = findViewById(R.id.multiplayer);
                setting = findViewById(R.id.setting);
                home.setOnSelectListener(this);
                manage.setOnSelectListener(this);
                download.setOnSelectListener(this);
                controller.setOnSelectListener(this);
                multiplayer.setOnSelectListener(this);
                setting.setOnSelectListener(this);
                home.setSelected(true);

                setupAccountDisplay();
                setupVersionDisplay();
            });
        });
    }

    public Runnable backToMainUI = () -> {
        if (uiManager.getCurrentUI() == uiManager.getMainUI()) {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
        }
        else {
            home.setSelected(true);
        }
    };

    @Override
    public void onBackPressed() {
        if (uiManager != null) {
            uiManager.onBackPressed();
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
            String version = Profiles.getSelectedVersion();
            if (version == null) {
                refreshMenuView(null);
                titleView.setTextWithAnim(getString(R.string.version));
                uiManager.switchUI(uiManager.getVersionUI());
            } else {
                titleView.setTextWithAnim(getString(R.string.manage));
                uiManager.getManageUI().setVersion(version, Profiles.getSelectedProfile());
                uiManager.switchUI(uiManager.getManageUI());
            }
        }
        if (view == download) {
            titleView.setTextWithAnim(getString(R.string.download));
            uiManager.switchUI(uiManager.getDownloadUI());
        }
        if (view == controller) {
            titleView.setTextWithAnim(getString(R.string.controller));
            uiManager.switchUI(uiManager.getControllerUI());
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

    public void refreshMenuView(FCLMenuView view) {
        FCLMenuView[] views = {
                home,
                manage,
                download,
                controller,
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
            Versions.launch(this, Profiles.getSelectedProfile());
        }
    }

    private static ObservableValue<String> accountSubtitle(Context context, Account account) {
        if (account instanceof AuthlibInjectorAccount) {
            return BindingMapping.of(((AuthlibInjectorAccount) account).getServer(), AuthlibInjectorServer::getName);
        } else {
            return createStringBinding(() -> Accounts.getLocalizedLoginTypeName(context, getAccountFactory(account)));
        }
    }

    private void setupAccountDisplay() {
        currentAccount = new SimpleObjectProperty<Account>() {

            @Override
            protected void invalidated() {
                Account account = get();
                if (account == null) {
                    accountName.stringProperty().unbind();
                    accountHint.stringProperty().unbind();
                    avatar.imageProperty().unbind();
                    accountName.setText(getString(R.string.account_state_no_account));
                    accountHint.setText(getString(R.string.account_state_add));
                    avatar.setBackgroundDrawable(new BitmapDrawable(TexturesLoader.toAvatar(TexturesLoader.getDefaultSkin(TextureModel.ALEX).getImage(), ConvertUtils.dip2px(MainActivity.this, 30))));
                } else {
                    accountName.stringProperty().bind(BindingMapping.of(account, Account::getCharacter));
                    accountHint.stringProperty().bind(accountSubtitle(MainActivity.this, account));
                    avatar.imageProperty().bind(TexturesLoader.avatarBinding(account, ConvertUtils.dip2px(MainActivity.this, 30)));
                }
            }
        };
        currentAccount.bind(Accounts.selectedAccountProperty());
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void loadVersion(String version) {
        if (Profiles.getSelectedProfile() != profile) {
            profile = Profiles.getSelectedProfile();
            if (profile != null) {
                onVersionIconChangedListener = profile.getRepository().onVersionIconChanged.registerWeak(event -> this.loadVersion(Profiles.getSelectedVersion()));
            }
        }
        if (version != null && Profiles.getSelectedProfile() != null && Profiles.getSelectedProfile().getRepository().hasVersion(version)) {
            String game = Profiles.getSelectedProfile().getRepository().getGameVersion(version).orElse(getString(R.string.message_unknown));
            StringBuilder libraries = new StringBuilder(game);
            LibraryAnalyzer analyzer = LibraryAnalyzer.analyze(Profiles.getSelectedProfile().getRepository().getResolvedPreservingPatchesVersion(version));
            for (LibraryAnalyzer.LibraryMark mark : analyzer) {
                String libraryId = mark.getLibraryId();
                String libraryVersion = mark.getLibraryVersion();
                if (libraryId.equals(MINECRAFT.getPatchId())) continue;
                if (AndroidUtils.hasStringId(this, "install_installer_" + libraryId.replace("-", "_"))) {
                    libraries.append(", ").append(AndroidUtils.getLocalizedText(this, "install_installer_" + libraryId.replace("-", "_")));
                    if (libraryVersion != null)
                        libraries.append(": ").append(libraryVersion.replaceAll("(?i)" + libraryId, ""));
                }
            }
            versionName.setText(version);
            versionHint.setText(libraries.toString());
            icon.setBackgroundDrawable(Profiles.getSelectedProfile().getRepository().getVersionIconImage(version));
        } else {
            versionName.setText(getString(R.string.version_no_version));
            versionHint.setText(getString(R.string.version_manage));
            icon.setBackgroundDrawable(getDrawable(R.drawable.img_grass));
        }
    }

    private void setupVersionDisplay() {
        holder.add(FXUtils.onWeakChangeAndOperate(Profiles.selectedVersionProperty(), s -> Schedulers.androidUIThread().execute(() -> loadVersion(s))));
    }
}
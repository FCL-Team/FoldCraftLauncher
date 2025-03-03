package com.tungsten.fcl.ui.manage;

import static com.tungsten.fcl.setting.ConfigHolder.config;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.VersionSetting;
import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fcl.util.FXUtils;
import com.tungsten.fcl.util.RequestCodes;
import com.tungsten.fclcore.auth.Account;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorServer;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty;
import com.tungsten.fclcore.mod.ModAdviser;
import com.tungsten.fclcore.mod.ModpackExportInfo;
import com.tungsten.fclcore.mod.mcbbs.McbbsModpackManifest;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.platform.OperatingSystem;
import com.tungsten.fcllibrary.browser.FileBrowser;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.browser.options.SelectionMode;
import com.tungsten.fcllibrary.component.ui.FCLTempPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLNumberSeekBar;
import com.tungsten.fcllibrary.component.view.FCLSeekBar;
import com.tungsten.fcllibrary.component.view.FCLSpinner;
import com.tungsten.fcllibrary.component.view.FCLSwitch;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModpackInfoPage extends FCLTempPage implements View.OnClickListener {

    private final Profile profile;
    private final String versionName;
    private final String type;
    private final ModpackExportInfo.Options options;

    private final ModpackExportInfo exportInfo = new ModpackExportInfo();

    private final SimpleStringProperty path = new SimpleStringProperty("");
    private final SimpleStringProperty fileName = new SimpleStringProperty("");

    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty author = new SimpleStringProperty("");
    private final SimpleStringProperty version = new SimpleStringProperty("1.0");
    private final SimpleStringProperty description = new SimpleStringProperty("");
    private final SimpleStringProperty url = new SimpleStringProperty("");
    private final SimpleBooleanProperty forceUpdate = new SimpleBooleanProperty();
    private final SimpleStringProperty fileApi = new SimpleStringProperty("");
    private final SimpleIntegerProperty minMemory = new SimpleIntegerProperty(0);
    private final SimpleStringProperty authlibInjectorServer = new SimpleStringProperty();
    private final SimpleStringProperty launchArguments = new SimpleStringProperty("");
    private final SimpleStringProperty javaArguments = new SimpleStringProperty("");
    private final SimpleStringProperty mcbbsThreadId = new SimpleStringProperty("");

    private FCLImageButton pathButton;
    private FCLButton next;

    public ModpackInfoPage(Context context, int id, FCLUILayout parent, int resId, Profile profile, String version, String type, ModpackExportInfo.Options options) {
        super(context, id, parent, resId);
        this.profile = profile;
        this.versionName = version;
        this.type = type;
        this.options = options;

        name.set(version);
        author.set(Optional.ofNullable(Accounts.getSelectedAccount()).map(Account::getUsername).orElse(""));

        VersionSetting versionSetting = profile.getRepository().getVersionSetting(versionName);
        minMemory.set(Optional.ofNullable(versionSetting.getMinMemory()).orElse(0));
        launchArguments.set(versionSetting.getMinecraftArgs());
        javaArguments.set(versionSetting.getJavaArgs());
    }

    @Override
    public void onStart() {
        super.onStart();

        FCLLinearLayout fileApiLayout = findViewById(R.id.file_api_layout);
        FCLLinearLayout launchArgsLayout = findViewById(R.id.minecraft_args_layout);
        FCLLinearLayout jvmArgsLayout = findViewById(R.id.jvm_args_layout);
        FCLLinearLayout originUrlLayout = findViewById(R.id.origin_url_layout);
        FCLLinearLayout mcbbsLayout = findViewById(R.id.mcbbs_layout);
        FCLLinearLayout memoryLayout = findViewById(R.id.memory_layout);
        FCLLinearLayout serverLayout = findViewById(R.id.server_layout);
        FCLLinearLayout forceUpdateLayout = findViewById(R.id.force_update_layout);
        View splitF = findViewById(R.id.split_1);
        View splitS = findViewById(R.id.split_2);
        View splitT = findViewById(R.id.split_3);

        FCLTextView versionNameText = findViewById(R.id.game_version);
        FCLEditText nameText = findViewById(R.id.name);
        FCLEditText authorText = findViewById(R.id.author);
        FCLEditText versionText = findViewById(R.id.version);
        FCLEditText fileApiText = findViewById(R.id.file_api);
        FCLEditText launchArgsText = findViewById(R.id.minecraft_args);
        FCLEditText jvmArgsText = findViewById(R.id.jvm_args);
        FCLEditText originUrlText = findViewById(R.id.origin_url);
        FCLEditText mcbbsText = findViewById(R.id.mcbbs);
        FCLNumberSeekBar memorySeekbar = findViewById(R.id.memory);
        FCLEditText descText = findViewById(R.id.desc);
        FCLSpinner<String> serverSpinner = findViewById(R.id.server);
        FCLSwitch forceUpdateSwitch = findViewById(R.id.force_update);
        FCLTextView pathText = findViewById(R.id.path_text);
        pathButton = findViewById(R.id.path);
        FCLEditText fileNameText = findViewById(R.id.file_name);
        next = findViewById(R.id.next);

        versionNameText.setText(versionName);
        nameText.setText(name.get());
        nameText.stringProperty().bindBidirectional(name);
        authorText.setText(author.get());
        authorText.stringProperty().bindBidirectional(author);
        versionText.setText(version.get());
        versionText.stringProperty().bindBidirectional(version);
        if (options.isRequireFileApi()) {
            if (options.isValidateFileApi()) {
                fileApiText.setHint(getContext().getString(R.string.input_hint_not_empty));
            } else {
                fileApiText.setHint("");
            }
            fileApiText.stringProperty().bindBidirectional(fileApi);
        }
        fileApiLayout.setVisibility(options.isRequireFileApi() ? View.VISIBLE : View.GONE);
        if (options.isRequireLaunchArguments()) {
            launchArgsText.setText(launchArguments.get());
            launchArgsText.stringProperty().bindBidirectional(launchArguments);
        }
        launchArgsLayout.setVisibility(options.isRequireLaunchArguments() ? View.VISIBLE : View.GONE);
        if (options.isRequireJavaArguments()) {
            jvmArgsText.setText(javaArguments.get());
            jvmArgsText.stringProperty().bindBidirectional(javaArguments);
        }
        jvmArgsLayout.setVisibility(options.isRequireJavaArguments() ? View.VISIBLE : View.GONE);
        if (options.isRequireUrl()) {
            originUrlText.stringProperty().bindBidirectional(url);
        }
        originUrlLayout.setVisibility(options.isRequireUrl() ? View.VISIBLE : View.GONE);
        if (options.isRequireOrigins()) {
            mcbbsText.stringProperty().bindBidirectional(mcbbsThreadId);
        }
        mcbbsLayout.setVisibility(options.isRequireOrigins() ? View.VISIBLE : View.GONE);
        if (options.isRequireMinMemory()) {
            memorySeekbar.setProgress(minMemory.get());
            memorySeekbar.addProgressListener();
            memorySeekbar.progressProperty().bindBidirectional(minMemory);
        }
        memoryLayout.setVisibility(options.isRequireMinMemory() ? View.VISIBLE : View.GONE);
        splitF.setVisibility(options.isRequireMinMemory() ? View.VISIBLE : View.GONE);
        descText.stringProperty().bindBidirectional(description);
        if (options.isRequireAuthlibInjectorServer()) {
            ArrayList<String> list = (ArrayList<String>) config().getAuthlibInjectorServers().stream().map(AuthlibInjectorServer::getName).collect(Collectors.toList());
            Map<String, String> map = new HashMap<>();
            list.add(0, "");
            map.put("", null);
            config().getAuthlibInjectorServers().forEach(it -> map.put(it.getName(), it.getUrl()));
            serverSpinner.setDataList(list);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, list);
            adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
            serverSpinner.setAdapter(adapter);
            SimpleStringProperty serverName = new SimpleStringProperty("");
            FXUtils.bindSelection(serverSpinner, serverName);
            serverName.addListener(observable -> authlibInjectorServer.set(map.get(serverName.get())));
        }
        serverLayout.setVisibility(options.isRequireAuthlibInjectorServer() ? View.VISIBLE : View.GONE);
        splitS.setVisibility(options.isRequireAuthlibInjectorServer() ? View.VISIBLE : View.GONE);
        if (options.isRequireForceUpdate()) {
            forceUpdateSwitch.addCheckedChangeListener();
            forceUpdateSwitch.checkProperty().bindBidirectional(forceUpdate);
        }
        forceUpdateLayout.setVisibility(options.isRequireForceUpdate() ? View.VISIBLE : View.GONE);
        splitT.setVisibility(options.isRequireForceUpdate() ? View.VISIBLE : View.GONE);
        pathText.stringProperty().bind(path);
        pathButton.setOnClickListener(this);
        fileNameText.stringProperty().bindBidirectional(fileName);
        next.setOnClickListener(this);
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onRestart() {

    }

    private void selectPath() {
        FileBrowser.Builder builder = new FileBrowser.Builder(getContext());
        builder.setLibMode(LibMode.FOLDER_CHOOSER);
        builder.setSelectionMode(SelectionMode.SINGLE_SELECTION);
        builder.create().browse(getActivity(), RequestCodes.SELECT_EXPORT_FOLDER_CODE, ((requestCode, resultCode, data) -> {
            if (requestCode == RequestCodes.SELECT_EXPORT_FOLDER_CODE && resultCode == Activity.RESULT_OK && data != null) {
                String p = FileBrowser.getSelectedFiles(data).get(0);
                path.set(p);
            }
        }));
    }

    @Override
    public void onClick(View v) {
        if (v == pathButton) {
            selectPath();
        }
        if (v == next) {
            boolean urlValid = false;
            if (StringUtils.isNotBlank(fileApi.get())) {
                try {
                    new URL(fileApi.get()).toURI();
                    urlValid = true;
                } catch (IOException | URISyntaxException ignored) {
                }
            }
            if (StringUtils.isBlank(name.get()) || StringUtils.isBlank(author.get()) || StringUtils.isBlank(version.get()) || StringUtils.isBlank(fileName.get())
                    || (options.isRequireFileApi() && options.isValidateFileApi() && StringUtils.isBlank(fileApi.get()))) {
                Toast.makeText(getContext(), getContext().getString(R.string.input_not_empty), Toast.LENGTH_SHORT).show();
            } else if (options.isRequireFileApi() && StringUtils.isNotBlank(fileApi.get()) && !urlValid) {
                Toast.makeText(getContext(), getContext().getString(R.string.input_url), Toast.LENGTH_SHORT).show();
            } else if (options.isRequireOrigins() && StringUtils.isNotBlank(mcbbsThreadId.get()) && Lang.toIntOrNull(mcbbsThreadId.get()) == null) {
                Toast.makeText(getContext(), getContext().getString(R.string.input_number), Toast.LENGTH_SHORT).show();
            } else if (!OperatingSystem.isNameValid(fileName.get())) {
                Toast.makeText(getContext(), getContext().getString(R.string.install_new_game_malformed), Toast.LENGTH_SHORT).show();
            } else if (StringUtils.isBlank(path.get())) {
                selectPath();
            } else {
                File file = new File(path.get(), fileName.get() + ".zip");

                if (file.exists()) {
                    Toast.makeText(getContext(), getContext().getString(R.string.message_file_exist), Toast.LENGTH_SHORT).show();
                    return;
                }

                exportInfo.setName(name.get());
                exportInfo.setFileApi(fileApi.get());
                exportInfo.setVersion(version.get());
                exportInfo.setAuthor(author.get());
                exportInfo.setDescription(description.get());
                exportInfo.setPackWithLauncher(false);
                exportInfo.setUrl(url.get());
                exportInfo.setForceUpdate(forceUpdate.get());
                exportInfo.setMinMemory(minMemory.get());
                exportInfo.setLaunchArguments(launchArguments.get());
                exportInfo.setJavaArguments(javaArguments.get());
                exportInfo.setAuthlibInjectorServer(authlibInjectorServer.get());

                if (StringUtils.isNotBlank(mcbbsThreadId.get())) {
                    exportInfo.setOrigins(Collections.singletonList(new McbbsModpackManifest.Origin(
                            "mcbbs", Integer.parseInt(mcbbsThreadId.get())
                    )));
                }

                ModpackFileSelectionPage page = new ModpackFileSelectionPage(getContext(), PageManager.PAGE_ID_TEMP, getParent(), R.layout.page_modpack_file, profile, versionName, type, ModAdviser::suggestMod, exportInfo, file);
                ManagePageManager.getInstance().showTempPage(page);
            }
        }
    }
}

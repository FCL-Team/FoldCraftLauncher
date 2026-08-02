package com.tungsten.fcl.ui.manage;

import static com.tungsten.fcl.setting.ConfigHolder.config;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.VersionSetting;
import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fclcore.auth.Account;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorServer;
import com.tungsten.fclcore.mod.ModAdviser;
import com.tungsten.fclcore.mod.ModpackExportInfo;
import com.tungsten.fclcore.mod.mcbbs.McbbsModpackManifest;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.flow.FlowBindings;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fclcore.util.platform.OperatingSystem;
import com.tungsten.fcllibrary.component.ui.FCLTempPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLNumberSeekBar;
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

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public class ModpackInfoPage extends FCLTempPage implements View.OnClickListener {

    private final Profile profile;
    private final String versionName;
    private final String type;
    private final ModpackExportInfo.Options options;

    private final ModpackExportInfo exportInfo = new ModpackExportInfo();

    private final MutableStateFlow<String> path = StateFlowKt.MutableStateFlow("");
    private final MutableStateFlow<String> fileName = StateFlowKt.MutableStateFlow("");

    private final MutableStateFlow<String> name = StateFlowKt.MutableStateFlow("");
    private final MutableStateFlow<String> author = StateFlowKt.MutableStateFlow("");
    private final MutableStateFlow<String> version = StateFlowKt.MutableStateFlow("1.0");
    private final MutableStateFlow<String> description = StateFlowKt.MutableStateFlow("");
    private final MutableStateFlow<String> url = StateFlowKt.MutableStateFlow("");
    private final MutableStateFlow<Boolean> forceUpdate = StateFlowKt.MutableStateFlow(false);
    private final MutableStateFlow<String> fileApi = StateFlowKt.MutableStateFlow("");
    private final MutableStateFlow<Integer> minMemory = StateFlowKt.MutableStateFlow(0);
    private final MutableStateFlow<String> authlibInjectorServer = StateFlowKt.MutableStateFlow(null);
    private final MutableStateFlow<String> launchArguments = StateFlowKt.MutableStateFlow("");
    private final MutableStateFlow<String> javaArguments = StateFlowKt.MutableStateFlow("");
    private final MutableStateFlow<String> mcbbsThreadId = StateFlowKt.MutableStateFlow("");

    private FCLImageButton pathButton;
    private FCLButton next;

    public ModpackInfoPage(Context context, int id, FCLUILayout parent, int resId, Profile profile, String version, String type, ModpackExportInfo.Options options) {
        super(context, id, parent, resId);
        this.profile = profile;
        this.versionName = version;
        this.type = type;
        this.options = options;

        name.setValue(version);
        author.setValue(Optional.ofNullable(Accounts.getSelectedAccount()).map(Account::getUsername).orElse(""));

        VersionSetting versionSetting = profile.getRepository().getVersionSetting(versionName);
        minMemory.setValue(Optional.ofNullable(versionSetting.getMinMemory()).orElse(0));
        launchArguments.setValue(versionSetting.getMinecraftArgs());
        javaArguments.setValue(versionSetting.getJavaArgs());
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
        nameText.setText(name.getValue());
        FlowBindings.bindBidirectional(nameText.stringFlow(), name);
        authorText.setText(author.getValue());
        FlowBindings.bindBidirectional(authorText.stringFlow(), author);
        versionText.setText(version.getValue());
        FlowBindings.bindBidirectional(versionText.stringFlow(), version);
        if (options.isRequireFileApi()) {
            if (options.isValidateFileApi()) {
                fileApiText.setHint(getContext().getString(R.string.input_hint_not_empty));
            } else {
                fileApiText.setHint("");
            }
            FlowBindings.bindBidirectional(fileApiText.stringFlow(), fileApi);
        }
        fileApiLayout.setVisibility(options.isRequireFileApi() ? View.VISIBLE : View.GONE);
        if (options.isRequireLaunchArguments()) {
            launchArgsText.setText(launchArguments.getValue());
            FlowBindings.bindBidirectional(launchArgsText.stringFlow(), launchArguments);
        }
        launchArgsLayout.setVisibility(options.isRequireLaunchArguments() ? View.VISIBLE : View.GONE);
        if (options.isRequireJavaArguments()) {
            jvmArgsText.setText(javaArguments.getValue());
            FlowBindings.bindBidirectional(jvmArgsText.stringFlow(), javaArguments);
        }
        jvmArgsLayout.setVisibility(options.isRequireJavaArguments() ? View.VISIBLE : View.GONE);
        if (options.isRequireUrl()) {
            FlowBindings.bindBidirectional(originUrlText.stringFlow(), url);
        }
        originUrlLayout.setVisibility(options.isRequireUrl() ? View.VISIBLE : View.GONE);
        if (options.isRequireOrigins()) {
            FlowBindings.bindBidirectional(mcbbsText.stringFlow(), mcbbsThreadId);
        }
        mcbbsLayout.setVisibility(options.isRequireOrigins() ? View.VISIBLE : View.GONE);
        if (options.isRequireMinMemory()) {
            memorySeekbar.setProgress(minMemory.getValue());
            memorySeekbar.addProgressListener();
            FlowBindings.bindBidirectional(memorySeekbar.progressFlow(), minMemory);
        }
        memoryLayout.setVisibility(options.isRequireMinMemory() ? View.VISIBLE : View.GONE);
        splitF.setVisibility(options.isRequireMinMemory() ? View.VISIBLE : View.GONE);
        FlowBindings.bindBidirectional(descText.stringFlow(), description);
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
            MutableStateFlow<String> serverName = StateFlowKt.MutableStateFlow("");
            FlowBindings.bindBidirectional(serverSpinner.selectedItemFlow(), serverName);
            FlowSubscriptions.subscribe(serverName, v -> authlibInjectorServer.setValue(map.get(v)));
        }
        serverLayout.setVisibility(options.isRequireAuthlibInjectorServer() ? View.VISIBLE : View.GONE);
        splitS.setVisibility(options.isRequireAuthlibInjectorServer() ? View.VISIBLE : View.GONE);
        if (options.isRequireForceUpdate()) {
            forceUpdateSwitch.addCheckedChangeListener();
            FlowBindings.bindBidirectional(forceUpdateSwitch.checkFlow(), forceUpdate);
        }
        forceUpdateLayout.setVisibility(options.isRequireForceUpdate() ? View.VISIBLE : View.GONE);
        splitT.setVisibility(options.isRequireForceUpdate() ? View.VISIBLE : View.GONE);
        pathText.stringFlow().setValue(path.getValue());
        FlowSubscriptions.subscribe(path, v -> pathText.stringFlow().setValue(v));
        pathButton.setOnClickListener(this);
        FlowBindings.bindBidirectional(fileNameText.stringFlow(), fileName);
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
        MainActivity.getInstance().fileLauncher.launchSingleSelection(null, null, true, files -> path.setValue(files.get(0)));
    }

    @Override
    public void onClick(View v) {
        if (v == pathButton) {
            selectPath();
        }
        if (v == next) {
            boolean urlValid = false;
            if (StringUtils.isNotBlank(fileApi.getValue())) {
                try {
                    new URL(fileApi.getValue()).toURI();
                    urlValid = true;
                } catch (IOException | URISyntaxException ignored) {
                }
            }
            if (StringUtils.isBlank(name.getValue()) || StringUtils.isBlank(author.getValue()) || StringUtils.isBlank(version.getValue()) || StringUtils.isBlank(fileName.getValue())
                    || (options.isRequireFileApi() && options.isValidateFileApi() && StringUtils.isBlank(fileApi.getValue()))) {
                Toast.makeText(getContext(), getContext().getString(R.string.input_not_empty), Toast.LENGTH_SHORT).show();
            } else if (options.isRequireFileApi() && StringUtils.isNotBlank(fileApi.getValue()) && !urlValid) {
                Toast.makeText(getContext(), getContext().getString(R.string.input_url), Toast.LENGTH_SHORT).show();
            } else if (options.isRequireOrigins() && StringUtils.isNotBlank(mcbbsThreadId.getValue()) && Lang.toIntOrNull(mcbbsThreadId.getValue()) == null) {
                Toast.makeText(getContext(), getContext().getString(R.string.input_number), Toast.LENGTH_SHORT).show();
            } else if (!OperatingSystem.isNameValid(fileName.getValue())) {
                Toast.makeText(getContext(), getContext().getString(R.string.install_new_game_malformed), Toast.LENGTH_SHORT).show();
            } else if (StringUtils.isBlank(path.getValue())) {
                selectPath();
            } else {
                File file = new File(path.getValue(), fileName.getValue() + ".zip");

                if (file.exists()) {
                    Toast.makeText(getContext(), getContext().getString(R.string.message_file_exist), Toast.LENGTH_SHORT).show();
                    return;
                }

                exportInfo.setName(name.getValue());
                exportInfo.setFileApi(fileApi.getValue());
                exportInfo.setVersion(version.getValue());
                exportInfo.setAuthor(author.getValue());
                exportInfo.setDescription(description.getValue());
                exportInfo.setPackWithLauncher(false);
                exportInfo.setUrl(url.getValue());
                exportInfo.setForceUpdate(forceUpdate.getValue());
                exportInfo.setMinMemory(minMemory.getValue());
                exportInfo.setLaunchArguments(launchArguments.getValue());
                exportInfo.setJavaArguments(javaArguments.getValue());
                exportInfo.setAuthlibInjectorServer(authlibInjectorServer.getValue());

                if (StringUtils.isNotBlank(mcbbsThreadId.getValue())) {
                    exportInfo.setOrigins(Collections.singletonList(new McbbsModpackManifest.Origin(
                            "mcbbs", Integer.parseInt(mcbbsThreadId.getValue())
                    )));
                }

                ModpackFileSelectionPage page = new ModpackFileSelectionPage(getContext(), PageManager.PAGE_ID_TEMP, getParent(), R.layout.page_modpack_file, profile, versionName, type, ModAdviser::suggestMod, exportInfo, file);
                ManagePageManager.getInstance().showTempPage(page);
            }
        }
    }
}

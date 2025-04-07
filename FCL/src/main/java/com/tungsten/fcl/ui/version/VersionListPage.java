package com.tungsten.fcl.ui.version;

import static com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType.MINECRAFT;
import static com.tungsten.fclcore.util.Logging.LOG;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.JsonParseException;
import com.tungsten.fcl.R;
import com.tungsten.fcl.databinding.PageVersionListBinding;
import com.tungsten.fcl.game.FCLGameRepository;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclcore.download.LibraryAnalyzer;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.mod.ModpackConfiguration;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class VersionListPage extends FCLCommonPage implements View.OnClickListener {

    private PageVersionListBinding binding;
    private VersionListAdapter adapter;
    private List<VersionListItem> children;
    private TextWatcher textWatcher;

    public VersionListPage(Context context, int id, FCLUILayout parent, int resId) {
        super(context, id, parent, resId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        binding = PageVersionListBinding.bind(getContentView());
        binding.refresh.setOnClickListener(this);
        binding.newProfile.setOnClickListener(this);
        Profiles.registerVersionsListener(this::loadVersions);
        refreshProfile();
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.isEmpty()) {
                    binding.versionList.setAdapter(adapter);
                } else {
                    List<VersionListItem> collect = children.stream().filter(version -> version.getVersion().toLowerCase().contains(text.toLowerCase())).collect(Collectors.toList());
                    VersionListAdapter newAdapter = new VersionListAdapter(getContext(), (ArrayList<VersionListItem>) collect);
                    binding.versionList.setAdapter(newAdapter);
                }
            }
        };
    }

    @Override
    public Task<?> refresh(Object... param) {
        return Task.runAsync(() -> {

        });
    }

    public void refreshProfile() {
        ProfileListAdapter adapter = new ProfileListAdapter(getContext(), Profiles.getProfiles());
        binding.profileList.setAdapter(adapter);
    }

    private void loadVersions(Profile profile) {
        Schedulers.androidUIThread().execute(() -> {
            binding.search.removeTextChangedListener(textWatcher);
            binding.search.setText("");
            binding.refresh.setEnabled(false);
            binding.versionList.setVisibility(View.GONE);
            binding.progress.setVisibility(View.VISIBLE);
        });
        FCLGameRepository repository = profile.getRepository();
        Schedulers.defaultScheduler().execute(() -> {
            if (profile == Profiles.getSelectedProfile()) {
                children = repository.getDisplayVersions()
                        .parallel()
                        .map(version -> {
                            Optional<String> game = profile.getRepository().getGameVersion(version.getId());
                            StringBuilder libraries = new StringBuilder(game.orElse(getContext().getString(R.string.message_unknown)));
                            LibraryAnalyzer analyzer = LibraryAnalyzer.analyze(profile.getRepository().getResolvedPreservingPatchesVersion(version.getId()), game.orElse(null));
                            for (LibraryAnalyzer.LibraryMark mark : analyzer) {
                                String libraryId = mark.getLibraryId();
                                String libraryVersion = mark.getLibraryVersion();
                                if (libraryId.equals(MINECRAFT.getPatchId())) continue;
                                if (AndroidUtils.hasStringId(getContext(), "install_installer_" + libraryId.replace("-", "_"))) {
                                    libraries.append(", ").append(AndroidUtils.getLocalizedText(getContext(), "install_installer_" + libraryId.replace("-", "_")));
                                    if (libraryVersion != null)
                                        libraries.append(": ").append(libraryVersion.replaceAll("(?i)" + libraryId, ""));
                                }
                            }
                            String tag = null;
                            try {
                                ModpackConfiguration<?> config = profile.getRepository().readModpackConfiguration(version.getId());
                                if (config != null)
                                    tag = config.getVersion();
                            } catch (IOException | JsonParseException e) {
                                LOG.log(Level.WARNING, "Failed to read modpack configuration from " + version, e);
                            }
                            return new VersionListItem(profile, version.getId(), libraries.toString(), tag, repository.getVersionIconImage(version.getId()));
                        })
                        .collect(Collectors.toList());
                Schedulers.androidUIThread().execute(() -> {
                    if (profile == Profiles.getSelectedProfile()) {
                        adapter = new VersionListAdapter(getContext(), (ArrayList<VersionListItem>) children);
                        binding.versionList.setAdapter(adapter);
                        binding.refresh.setEnabled(true);
                        binding.versionList.setVisibility(View.VISIBLE);
                        binding.progress.setVisibility(View.GONE);
                        binding.search.addTextChangedListener(textWatcher);
                    }
                });
                children.forEach(it -> it.selectedProperty().bind(Bindings.createBooleanBinding(() -> profile.selectedVersionProperty().get().equals(it.getVersion()), profile.selectedVersionProperty())));
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == binding.refresh) {
            Profiles.getSelectedProfile().getRepository().refreshVersionsAsync().start();
        }
        if (view == binding.newProfile) {
            AddProfileDialog dialog = new AddProfileDialog(getContext());
            dialog.show();
        }
    }
}

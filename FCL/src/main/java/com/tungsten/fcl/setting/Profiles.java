package com.tungsten.fcl.setting;

import static com.tungsten.fcl.setting.ConfigHolder.config;
import static com.tungsten.fcl.util.FXUtils.onInvalidating;
import static com.tungsten.fclcore.fakefx.collections.FXCollections.observableArrayList;

import com.tungsten.fcl.R;
import com.tungsten.fcl.util.WeakListenerHolder;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.event.EventBus;
import com.tungsten.fclcore.event.RefreshedVersionsEvent;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyListProperty;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyListWrapper;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyStringProperty;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyStringWrapper;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Consumer;

public final class Profiles {

    private Profiles() {
    }

    private static final ObservableList<Profile> profiles = observableArrayList(profile -> new Observable[] { profile });
    private static final ReadOnlyListWrapper<Profile> profilesWrapper = new ReadOnlyListWrapper<>(profiles);

    private static ObjectProperty<Profile> selectedProfile = new SimpleObjectProperty<Profile>() {
        {
            profiles.addListener(onInvalidating(this::invalidated));
        }

        @Override
        protected void invalidated() {
            if (!initialized)
                return;

            Profile profile = get();

            if (profiles.isEmpty()) {
                if (profile != null) {
                    set(null);
                    return;
                }
            } else {
                if (!profiles.contains(profile)) {
                    set(profiles.get(0));
                    return;
                }
            }

            config().setSelectedProfile(profile == null ? "" : profile.getName());
            if (profile != null) {
                if (profile.getRepository().isLoaded())
                    selectedVersion.bind(profile.selectedVersionProperty());
                else {
                    selectedVersion.unbind();
                    selectedVersion.set(null);
                    // bind when repository was reloaded.
                    profile.getRepository().refreshVersionsAsync().start();
                }
            } else {
                selectedVersion.unbind();
                selectedVersion.set(null);
            }
        }
    };

    private static void checkProfiles() {
        if (profiles.isEmpty()) {
            Profile current = new Profile(FCLPath.CONTEXT.getString(R.string.profile_shared), new File(FCLPath.SHARED_COMMON_DIR), new VersionSetting(), null);
            Profile home = new Profile(FCLPath.CONTEXT.getString(R.string.profile_private), new File(FCLPath.PRIVATE_COMMON_DIR));
            profiles.addAll(current, home);
        }
    }

    /**
     * True if {@link #init()} hasn't been called.
     */
    private static boolean initialized = false;

    static {
        profiles.addListener(onInvalidating(Profiles::updateProfileStorages));
        profiles.addListener(onInvalidating(Profiles::checkProfiles));

        selectedProfile.addListener((a, b, newValue) -> {
            if (newValue != null)
                newValue.getRepository().refreshVersionsAsync().start();
        });
    }

    private static void updateProfileStorages() {
        // don't update the underlying storage before data loading is completed
        // otherwise it might cause data loss
        if (!initialized)
            return;
        // update storage
        TreeMap<String, Profile> newConfigurations = new TreeMap<>();
        for (Profile profile : profiles) {
            newConfigurations.put(profile.getName(), profile);
        }
        config().getConfigurations().setValue(FXCollections.observableMap(newConfigurations));
    }

    /**
     * Called when it's ready to load profiles from {@link ConfigHolder#config()}.
     */
    private static final WeakListenerHolder holder = new WeakListenerHolder();

    static void init() {
        if (initialized)
            throw new IllegalStateException("Already initialized");

        HashSet<String> names = new HashSet<>();
        config().getConfigurations().forEach((name, profile) -> {
            if (!names.add(name)) return;
            profiles.add(profile);
            profile.setName(name);
        });
        checkProfiles();

        initialized = true;

        selectedProfile.set(
                profiles.stream()
                        .filter(it -> it.getName().equals(config().getSelectedProfile()))
                        .findFirst()
                        .orElse(profiles.get(0)));

        holder.add(EventBus.EVENT_BUS.channel(RefreshedVersionsEvent.class).registerWeak(event -> {
            Profile profile = selectedProfile.get();
            if (profile != null && profile.getRepository() == event.getSource()) {
                selectedVersion.bind(profile.selectedVersionProperty());
                for (Consumer<Profile> listener : versionsListeners)
                    listener.accept(profile);
            }
        }));
    }

    public static ObservableList<Profile> getProfiles() {
        return profiles;
    }

    public static ReadOnlyListProperty<Profile> profilesProperty() {
        return profilesWrapper.getReadOnlyProperty();
    }

    public static Profile getSelectedProfile() {
        return selectedProfile.get();
    }

    public static void setSelectedProfile(Profile profile) {
        selectedProfile.set(profile);
    }

    public static ObjectProperty<Profile> selectedProfileProperty() {
        return selectedProfile;
    }

    private static final ReadOnlyStringWrapper selectedVersion = new ReadOnlyStringWrapper();

    public static ReadOnlyStringProperty selectedVersionProperty() {
        return selectedVersion.getReadOnlyProperty();
    }

    // Guaranteed that the repository is loaded.
    public static String getSelectedVersion() {
        return selectedVersion.get();
    }

    private static final List<Consumer<Profile>> versionsListeners = new ArrayList<>(4);

    public static void registerVersionsListener(Consumer<Profile> listener) {
        Profile profile = getSelectedProfile();
        if (profile != null && profile.getRepository().isLoaded())
            listener.accept(profile);
        versionsListeners.add(listener);
    }
}

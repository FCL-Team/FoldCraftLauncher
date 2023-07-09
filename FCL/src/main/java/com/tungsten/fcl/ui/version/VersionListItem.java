package com.tungsten.fcl.ui.version;

import android.graphics.drawable.Drawable;

import com.tungsten.fcl.setting.Profile;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;

public class VersionListItem {

    private final Profile profile;
    private final String version;
    private final boolean isModpack;
    private final BooleanProperty selected = new SimpleBooleanProperty();
    private final String libraries;
    private final String tag;
    private final Drawable drawable;

    public VersionListItem(Profile profile, String id, String libraries, String tag, Drawable drawable) {
        this.profile = profile;
        this.version = id;
        this.libraries = libraries;
        this.drawable = drawable;
        this.tag = tag;
        this.isModpack = profile.getRepository().isModpack(id);

        selected.set(id.equals(profile.getSelectedVersion()));
    }

    public Profile getProfile() {
        return profile;
    }

    public String getVersion() {
        return version;
    }

    public String getLibraries() {
        return libraries;
    }

    public String getTag() {
        return tag;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public boolean canUpdate() {
        return isModpack;
    }

}

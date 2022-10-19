package com.tungsten.fclcore.game;

import com.google.gson.annotations.SerializedName;
import com.tungsten.fclcore.util.ToStringBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class AssetIndex {

    @SerializedName("virtual")
    private final boolean virtual;

    @SerializedName("map_to_resources")
    private final boolean mapToResources;

    @SerializedName("objects")
    private final Map<String, AssetObject> objects;

    public AssetIndex() {
        this(false, Collections.emptyMap());
    }

    public AssetIndex(boolean virtual, Map<String, AssetObject> objects) {
        this.virtual = this.mapToResources = virtual;
        this.objects = new HashMap<>(objects);
    }

    public boolean isVirtual() {
        return virtual || mapToResources;
    }

    public Map<String, AssetObject> getObjects() {
        return Collections.unmodifiableMap(objects);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("virtual", virtual).append("objects", objects).toString();
    }
}

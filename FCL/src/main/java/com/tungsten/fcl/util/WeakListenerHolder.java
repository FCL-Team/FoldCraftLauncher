package com.tungsten.fcl.util;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.WeakInvalidationListener;
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener;
import com.tungsten.fclcore.fakefx.beans.value.WeakChangeListener;
import com.tungsten.fclcore.fakefx.collections.ListChangeListener;
import com.tungsten.fclcore.fakefx.collections.WeakListChangeListener;

import java.util.ArrayList;
import java.util.List;

public class WeakListenerHolder {
    private final List<Object> refs = new ArrayList<>(0);

    public WeakListenerHolder() {
    }

    public WeakInvalidationListener weak(InvalidationListener listener) {
        refs.add(listener);
        return new WeakInvalidationListener(listener);
    }

    public <T> WeakChangeListener<T> weak(ChangeListener<T> listener) {
        refs.add(listener);
        return new WeakChangeListener<>(listener);
    }

    public <T> WeakListChangeListener<T> weak(ListChangeListener<T> listener) {
        refs.add(listener);
        return new WeakListChangeListener<>(listener);
    }

    public void add(Object obj) {
        refs.add(obj);
    }

    public boolean remove(Object obj) {
        return refs.remove(obj);
    }
}

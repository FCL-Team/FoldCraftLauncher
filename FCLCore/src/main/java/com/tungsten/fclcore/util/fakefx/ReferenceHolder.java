package com.tungsten.fclcore.util.fakefx;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.Observable;

class ReferenceHolder implements InvalidationListener {
    @SuppressWarnings("unused")
    private Object ref;

    public ReferenceHolder(Object ref) {
        this.ref = ref;
    }

    @Override
    public void invalidated(Observable observable) {
        // no-op
    }
}
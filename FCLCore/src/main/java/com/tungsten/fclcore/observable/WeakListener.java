package com.tungsten.fclcore.observable;

/**
 * Minimal reimplementation of {@code fakefx.beans.WeakListener}.
 */
public interface WeakListener {

    boolean wasGarbageCollected();
}

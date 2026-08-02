package com.tungsten.fclcore.observable;

/**
 * Minimal reimplementation of {@code WeakListener}.
 */
public interface WeakListener {

    boolean wasGarbageCollected();
}

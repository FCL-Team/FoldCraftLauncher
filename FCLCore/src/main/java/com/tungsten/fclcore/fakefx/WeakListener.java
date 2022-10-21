package com.tungsten.fclcore.fakefx;

public interface WeakListener {
    /**
     * Returns {@code true} if the linked listener was garbage-collected.
     * In this case, the listener can be removed from the observable.
     *
     * @return {@code true} if the linked listener was garbage-collected.
     */
    boolean wasGarbageCollected();
}
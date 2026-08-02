package com.tungsten.fclcore.observable;

/**
 * Minimal reimplementation of {@code fakefx.beans.InvalidationListener}.
 */
@FunctionalInterface
public interface InvalidationListener {

    void invalidated(Observable observable);
}

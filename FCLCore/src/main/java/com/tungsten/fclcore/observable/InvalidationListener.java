package com.tungsten.fclcore.observable;

/**
 * Minimal reimplementation of {@code InvalidationListener}.
 */
@FunctionalInterface
public interface InvalidationListener {

    void invalidated(Observable observable);
}

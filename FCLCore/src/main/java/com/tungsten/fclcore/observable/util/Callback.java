package com.tungsten.fclcore.observable.util;

/**
 * Minimal reimplementation of {@code Callback}.
 */
@FunctionalInterface
public interface Callback<P, R> {

    R call(P param);
}

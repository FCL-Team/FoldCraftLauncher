package com.tungsten.fclcore.observable.util;

/**
 * Minimal reimplementation of {@code fakefx.util.Callback}.
 */
@FunctionalInterface
public interface Callback<P, R> {

    R call(P param);
}

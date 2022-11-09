package com.tungsten.fclcore.util;

import java.util.function.Consumer;

@FunctionalInterface
public interface FutureCallback<T> {

    /**
     * Callback of future, called after future finishes.
     * This callback gives the feedback whether the result of future is acceptable or not,
     * if not, giving the reason, and future will be relaunched when necessary.
     * @param result result of the future
     * @param resolve accept the result
     * @param reject reject the result with failure reason
     */
    void call(T result, Runnable resolve, Consumer<String> reject);
}
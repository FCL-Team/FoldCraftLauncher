package com.tungsten.fcl.util;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.beans.WeakInvalidationListener;
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener;
import com.tungsten.fclcore.fakefx.beans.value.ObservableValue;
import com.tungsten.fclcore.fakefx.beans.value.WeakChangeListener;

import java.util.function.Consumer;

public final class FXUtils {

    public static InvalidationListener onInvalidating(Runnable action) {
        return arg -> action.run();
    }

    public static <T> void onChangeAndOperate(ObservableValue<T> value, Consumer<T> consumer) {
        consumer.accept(value.getValue());
        onChange(value, consumer);
    }

    public static <T> ChangeListener<T> onWeakChangeAndOperate(ObservableValue<T> value, Consumer<T> consumer) {
        consumer.accept(value.getValue());
        return onWeakChange(value, consumer);
    }

    public static <T> void onChange(ObservableValue<T> value, Consumer<T> consumer) {
        value.addListener((a, b, c) -> consumer.accept(c));
    }

    public static <T> ChangeListener<T> onWeakChange(ObservableValue<T> value, Consumer<T> consumer) {
        ChangeListener<T> listener = (a, b, c) -> consumer.accept(c);
        value.addListener(new WeakChangeListener<>(listener));
        return listener;
    }

    public static InvalidationListener observeWeak(Runnable runnable, Observable... observables) {
        InvalidationListener originalListener = observable -> runnable.run();
        WeakInvalidationListener listener = new WeakInvalidationListener(originalListener);
        for (Observable observable : observables) {
            observable.addListener(listener);
        }
        runnable.run();
        return originalListener;
    }

}

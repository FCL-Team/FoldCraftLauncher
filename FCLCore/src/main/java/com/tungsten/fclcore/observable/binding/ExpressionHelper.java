package com.tungsten.fclcore.observable.binding;

import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.WeakListener;
import com.tungsten.fclcore.observable.value.ChangeListener;
import com.tungsten.fclcore.observable.value.ObservableValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Simplified reimplementation of {@code fakefx.binding.ExpressionHelper}，语义对齐：
 * <ul>
 * <li>invalidation 监听先于 change 监听触发，且总是触发；</li>
 * <li>change 监听只在当前值（getValue()，equals 比较）相对上次触发时真正变化后触发，
 * oldValue/newValue 取自触发前后的缓存；</li>
 * <li>触发期间快照监听列表，增删监听不影响本次触发；</li>
 * <li>监听抛出的异常转交当前线程的 uncaughtExceptionHandler（与 fakefx 一致）；</li>
 * <li>添加监听时顺带清理已被 GC 的弱监听（对应 fakefx 的 trim）。</li>
 * </ul>
 */
public final class ExpressionHelper<T> {

    private final ObservableValue<T> observable;
    private final List<InvalidationListener> invalidationListeners = new ArrayList<>(1);
    private final List<ChangeListener<? super T>> changeListeners = new ArrayList<>(1);
    private T currentValue;

    public ExpressionHelper(ObservableValue<T> observable) {
        this.observable = observable;
    }

    public void addListener(InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        purgeGarbageCollected(invalidationListeners);
        invalidationListeners.add(listener);
    }

    public void removeListener(InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        invalidationListeners.remove(listener);
    }

    public void addListener(ChangeListener<? super T> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        purgeGarbageCollected(changeListeners);
        if (changeListeners.isEmpty()) {
            currentValue = observable.getValue();
        }
        changeListeners.add(listener);
    }

    public void removeListener(ChangeListener<? super T> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        changeListeners.remove(listener);
    }

    public void fireValueChangedEvent() {
        for (InvalidationListener listener : new ArrayList<>(invalidationListeners)) {
            try {
                listener.invalidated(observable);
            } catch (Exception e) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
            }
        }
        if (!changeListeners.isEmpty()) {
            final T oldValue = currentValue;
            currentValue = observable.getValue();
            final boolean changed = (currentValue == null) ? (oldValue != null) : !currentValue.equals(oldValue);
            if (changed) {
                for (ChangeListener<? super T> listener : new ArrayList<>(changeListeners)) {
                    try {
                        listener.changed(observable, oldValue, currentValue);
                    } catch (Exception e) {
                        Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                    }
                }
            }
        }
    }

    private static void purgeGarbageCollected(List<?> listeners) {
        listeners.removeIf(l -> l instanceof WeakListener && ((WeakListener) l).wasGarbageCollected());
    }
}

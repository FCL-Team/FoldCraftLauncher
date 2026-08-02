package com.tungsten.fclcore.observable.binding;

import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.Observable;
import com.tungsten.fclcore.observable.collections.FXCollections;
import com.tungsten.fclcore.observable.collections.ObservableList;
import com.tungsten.fclcore.observable.value.ChangeListener;
import com.tungsten.fclcore.observable.value.ObservableValue;

/**
 * Minimal reimplementation of {@code fakefx.beans.binding.ObjectBinding}：
 * 惰性求值（依赖失效前缓存计算结果），依赖失效时向监听者冒泡失效事件。
 */
public abstract class ObjectBinding<T> implements ObservableValue<T>, BindingHelperObserver.Binding {

    private T value;
    private boolean valid = false;
    private ExpressionHelper<T> helper;
    private BindingHelperObserver observer;

    @Override
    public void addListener(InvalidationListener listener) {
        if (helper == null) {
            helper = new ExpressionHelper<>(this);
        }
        helper.addListener(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        if (helper != null) {
            helper.removeListener(listener);
        }
    }

    @Override
    public void addListener(ChangeListener<? super T> listener) {
        if (helper == null) {
            helper = new ExpressionHelper<>(this);
        }
        helper.addListener(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super T> listener) {
        if (helper != null) {
            helper.removeListener(listener);
        }
    }

    /**
     * Start observing the dependencies for changes. If the value of one of the
     * dependencies changes, the binding is marked as invalid.
     */
    protected final void bind(Observable... dependencies) {
        if ((dependencies != null) && (dependencies.length > 0)) {
            if (observer == null) {
                observer = new BindingHelperObserver(this);
            }
            for (final Observable dep : dependencies) {
                dep.addListener(observer);
            }
        }
    }

    /**
     * Stop observing the dependencies for changes.
     */
    protected final void unbind(Observable... dependencies) {
        if (observer != null) {
            for (final Observable dep : dependencies) {
                dep.removeListener(observer);
            }
            observer = null;
        }
    }

    public void dispose() {
    }

    public ObservableList<?> getDependencies() {
        return FXCollections.emptyObservableList();
    }

    /**
     * Returns the result of {@link #computeValue()}. The method
     * {@code computeValue()} is only called if the binding is invalid.
     */
    public final T get() {
        if (!valid) {
            value = computeValue();
            valid = true;
        }
        return value;
    }

    @Override
    public final T getValue() {
        return get();
    }

    /**
     * Called when this binding becomes invalid. Can be overridden by extending
     * classes to react to the invalidation. The default implementation is empty.
     */
    protected void onInvalidating() {
    }

    @Override
    public final void invalidate() {
        if (valid) {
            valid = false;
            onInvalidating();
            if (helper != null) {
                helper.fireValueChangedEvent();
            }
            if (!valid) {
                value = null;
            }
        }
    }

    public final boolean isValid() {
        return valid;
    }

    /**
     * Checks if the binding has at least one listener registered on it.
     */
    protected final boolean isObserved() {
        return helper != null;
    }

    protected abstract T computeValue();

    @Override
    public String toString() {
        return valid ? "ObjectBinding [value: " + get() + "]" : "ObjectBinding [invalid]";
    }
}

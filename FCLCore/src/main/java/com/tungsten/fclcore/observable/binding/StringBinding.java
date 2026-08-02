package com.tungsten.fclcore.observable.binding;

import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.Observable;
import com.tungsten.fclcore.observable.collections.FXCollections;
import com.tungsten.fclcore.observable.collections.ObservableList;
import com.tungsten.fclcore.observable.value.ChangeListener;
import com.tungsten.fclcore.observable.value.ObservableValue;

/**
 * Minimal reimplementation of {@code StringBinding}：
 * 惰性求值的 String 绑定，语义同 {@link ObjectBinding}。
 */
public abstract class StringBinding implements ObservableValue<String>, BindingHelperObserver.Binding {

    private String value;
    private boolean valid = false;
    private ExpressionHelper<String> helper;
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
    public void addListener(ChangeListener<? super String> listener) {
        if (helper == null) {
            helper = new ExpressionHelper<>(this);
        }
        helper.addListener(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super String> listener) {
        if (helper != null) {
            helper.removeListener(listener);
        }
    }

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

    public final String get() {
        if (!valid) {
            value = computeValue();
            valid = true;
        }
        return value;
    }

    @Override
    public final String getValue() {
        return get();
    }

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

    protected final boolean isObserved() {
        return helper != null;
    }

    protected abstract String computeValue();

    @Override
    public String toString() {
        return valid ? "StringBinding [value: " + get() + "]" : "StringBinding [invalid]";
    }
}

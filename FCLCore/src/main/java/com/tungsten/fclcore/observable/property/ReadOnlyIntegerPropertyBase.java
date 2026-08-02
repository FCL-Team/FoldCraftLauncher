package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.binding.ExpressionHelper;
import com.tungsten.fclcore.observable.value.ChangeListener;

/**
 * Port of {@code ReadOnlyIntegerPropertyBase}.
 */
public abstract class ReadOnlyIntegerPropertyBase extends ReadOnlyIntegerProperty {

    private ExpressionHelper<Number> helper = null;

    public ReadOnlyIntegerPropertyBase() {
    }

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
    public void addListener(ChangeListener<? super Number> listener) {
        if (helper == null) {
            helper = new ExpressionHelper<>(this);
        }
        helper.addListener(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super Number> listener) {
        if (helper != null) {
            helper.removeListener(listener);
        }
    }

    protected void fireValueChangedEvent() {
        if (helper != null) {
            helper.fireValueChangedEvent();
        }
    }
}

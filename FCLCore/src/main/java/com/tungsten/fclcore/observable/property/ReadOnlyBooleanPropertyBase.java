package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.binding.ExpressionHelper;
import com.tungsten.fclcore.observable.value.ChangeListener;

/**
 * Port of {@code ReadOnlyBooleanPropertyBase}.
 */
public abstract class ReadOnlyBooleanPropertyBase extends ReadOnlyBooleanProperty {

    private ExpressionHelper<Boolean> helper = null;

    public ReadOnlyBooleanPropertyBase() {
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
    public void addListener(ChangeListener<? super Boolean> listener) {
        if (helper == null) {
            helper = new ExpressionHelper<>(this);
        }
        helper.addListener(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super Boolean> listener) {
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

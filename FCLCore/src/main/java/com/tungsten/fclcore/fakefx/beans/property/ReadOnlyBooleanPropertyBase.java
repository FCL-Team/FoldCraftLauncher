package com.tungsten.fclcore.fakefx.beans.property;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener;
import com.tungsten.fclcore.fakefx.binding.ExpressionHelper;

/**
 * Base class for all readonly properties wrapping a {@code boolean}. This class provides a default
 * implementation to attach listener.
 *
 * @see ReadOnlyBooleanProperty
 * @since JavaFX 2.0
 */
public abstract class ReadOnlyBooleanPropertyBase extends ReadOnlyBooleanProperty {

    ExpressionHelper<Boolean> helper;

    /**
     * Creates a default {@code ReadOnlyBooleanPropertyBase}.
     */
    public ReadOnlyBooleanPropertyBase() {
    }

    @Override
    public void addListener(InvalidationListener listener) {
        helper = ExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        helper = ExpressionHelper.removeListener(helper, listener);
    }

    @Override
    public void addListener(ChangeListener<? super Boolean> listener) {
        helper = ExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(ChangeListener<? super Boolean> listener) {
        helper = ExpressionHelper.removeListener(helper, listener);
    }

    protected void fireValueChangedEvent() {
        ExpressionHelper.fireValueChangedEvent(helper);
    }

}

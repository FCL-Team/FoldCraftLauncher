package com.tungsten.fclcore.fakefx.beans.property;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener;
import com.tungsten.fclcore.fakefx.binding.SetExpressionHelper;
import com.tungsten.fclcore.fakefx.collections.ObservableSet;
import com.tungsten.fclcore.fakefx.collections.SetChangeListener;

/**
 * Base class for all readonly properties wrapping an {@link ObservableSet}.
 * This class provides a default implementation to attach listener.
 *
 * @see ReadOnlySetProperty
 *
 * @param <E> the type of the {@code Set} elements
 * @since JavaFX 2.1
 */
public abstract class ReadOnlySetPropertyBase<E> extends ReadOnlySetProperty<E> {

    private SetExpressionHelper<E> helper;

    /**
     * Creates a default {@code ReadOnlySetPropertyBase}.
     */
    public ReadOnlySetPropertyBase() {
    }

    @Override
    public void addListener(InvalidationListener listener) {
        helper = SetExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        helper = SetExpressionHelper.removeListener(helper, listener);
    }

    @Override
    public void addListener(ChangeListener<? super ObservableSet<E>> listener) {
        helper = SetExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(ChangeListener<? super ObservableSet<E>> listener) {
        helper = SetExpressionHelper.removeListener(helper, listener);
    }

    @Override
    public void addListener(SetChangeListener<? super E> listener) {
        helper = SetExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(SetChangeListener<? super E> listener) {
        helper = SetExpressionHelper.removeListener(helper, listener);
    }

    protected void fireValueChangedEvent() {
        SetExpressionHelper.fireValueChangedEvent(helper);
    }

    protected void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
        SetExpressionHelper.fireValueChangedEvent(helper, change);
    }



}

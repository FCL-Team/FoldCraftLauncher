package com.tungsten.fclcore.fakefx.beans.property;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.beans.WeakListener;
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener;
import com.tungsten.fclcore.fakefx.beans.value.ObservableValue;
import com.tungsten.fclcore.fakefx.binding.ExpressionHelper;

import java.lang.ref.WeakReference;

/**
 * The class {@code ObjectPropertyBase} is the base class for a property
 * wrapping an arbitrary {@code Object}.
 *
 * It provides all the functionality required for a property except for the
 * {@link #getBean()} and {@link #getName()} methods, which must be implemented
 * by extending classes.
 *
 * @see ObjectProperty
 *
 *
 * @param <T>
 *            the type of the wrapped value
 * @since JavaFX 2.0
 */
public abstract class ObjectPropertyBase<T> extends ObjectProperty<T> {

    private T value;
    private ObservableValue<? extends T> observable = null;;
    private InvalidationListener listener = null;
    private boolean valid = true;
    private ExpressionHelper<T> helper = null;

    /**
     * The constructor of the {@code ObjectPropertyBase}.
     */
    public ObjectPropertyBase() {
    }

    /**
     * The constructor of the {@code ObjectPropertyBase}.
     *
     * @param initialValue
     *            the initial value of the wrapped {@code Object}
     */
    public ObjectPropertyBase(T initialValue) {
        this.value = initialValue;
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
    public void addListener(ChangeListener<? super T> listener) {
        helper = ExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(ChangeListener<? super T> listener) {
        helper = ExpressionHelper.removeListener(helper, listener);
    }

    protected void fireValueChangedEvent() {
        ExpressionHelper.fireValueChangedEvent(helper);
    }

    private void markInvalid() {
        if (valid) {
            valid = false;
            invalidated();
            fireValueChangedEvent();
        }
    }

    /**
     * The method {@code invalidated()} can be overridden to receive
     * invalidation notifications. This is the preferred option in
     * {@code Objects} defining the property, because it requires less memory.
     *
     * The default implementation is empty.
     */
    protected void invalidated() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T get() {
        valid = true;
        return observable == null ? value : observable.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(T newValue) {
        if (isBound()) {
            throw new RuntimeException((getBean() != null && getName() != null ?
                    getBean().getClass().getSimpleName() + "." + getName() + " : ": "") + "A bound value cannot be set.");
        }
        if (value != newValue) {
            value = newValue;
            markInvalid();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBound() {
        return observable != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bind(final ObservableValue<? extends T> newObservable) {
        if (newObservable == null) {
            throw new NullPointerException("Cannot bind to null");
        }

        if (!newObservable.equals(this.observable)) {
            unbind();
            observable = newObservable;
            if (listener == null) {
                listener = new Listener(this);
            }
            observable.addListener(listener);
            markInvalid();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unbind() {
        if (observable != null) {
            value = observable.getValue();
            observable.removeListener(listener);
            observable = null;
        }
    }

    /**
     * Returns a string representation of this {@code ObjectPropertyBase} object.
     * @return a string representation of this {@code ObjectPropertyBase} object.
     */
    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder("ObjectProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if ((name != null) && (!name.equals(""))) {
            result.append("name: ").append(name).append(", ");
        }
        if (isBound()) {
            result.append("bound, ");
            if (valid) {
                result.append("value: ").append(get());
            } else {
                result.append("invalid");
            }
        } else {
            result.append("value: ").append(get());
        }
        result.append("]");
        return result.toString();
    }

    private static class Listener implements InvalidationListener, WeakListener {

        private final WeakReference<ObjectPropertyBase<?>> wref;

        public Listener(ObjectPropertyBase<?> ref) {
            this.wref = new WeakReference<ObjectPropertyBase<?>>(ref);
        }

        @Override
        public void invalidated(Observable observable) {
            ObjectPropertyBase<?> ref = wref.get();
            if (ref == null) {
                observable.removeListener(this);
            } else {
                ref.markInvalid();
            }
        }

        @Override
        public boolean wasGarbageCollected() {
            return wref.get() == null;
        }
    }
}

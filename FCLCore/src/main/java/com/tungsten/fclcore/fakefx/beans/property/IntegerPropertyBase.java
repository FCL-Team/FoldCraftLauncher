package com.tungsten.fclcore.fakefx.beans.property;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.beans.WeakListener;
import com.tungsten.fclcore.fakefx.beans.binding.IntegerBinding;
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener;
import com.tungsten.fclcore.fakefx.beans.value.ObservableIntegerValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableNumberValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableValue;
import com.tungsten.fclcore.fakefx.binding.ExpressionHelper;

import java.lang.ref.WeakReference;

/**
 * The class {@code IntegerPropertyBase} is the base class for a property
 * wrapping a {@code int} value.
 *
 * It provides all the functionality required for a property except for the
 * {@link #getBean()} and {@link #getName()} methods, which must be implemented
 * by extending classes.
 *
 * @see IntegerProperty
 *
 *
 * @since JavaFX 2.0
 */
public abstract class IntegerPropertyBase extends IntegerProperty {

    private int value;
    private ObservableIntegerValue observable = null;;
    private InvalidationListener listener = null;
    private boolean valid = true;
    private ExpressionHelper<Number> helper = null;

    /**
     * The constructor of the {@code IntegerPropertyBase}.
     */
    public IntegerPropertyBase() {
    }

    /**
     * The constructor of the {@code IntegerPropertyBase}.
     *
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public IntegerPropertyBase(int initialValue) {
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
    public void addListener(ChangeListener<? super Number> listener) {
        helper = ExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(ChangeListener<? super Number> listener) {
        helper = ExpressionHelper.removeListener(helper, listener);
    }

    /**
     * Sends notifications to all attached
     * {@link InvalidationListener InvalidationListeners} and
     * {@link javafx.beans.value.ChangeListener ChangeListeners}.
     *
     * This method is called when the value is changed, either manually by
     * calling {@link #set(int)} or in case of a bound property, if the
     * binding becomes invalid.
     */
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
    public int get() {
        valid = true;
        return observable == null ? value : observable.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(int newValue) {
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
    public void bind(final ObservableValue<? extends Number> rawObservable) {
        if (rawObservable == null) {
            throw new NullPointerException("Cannot bind to null");
        }

        ObservableIntegerValue newObservable;
        if (rawObservable instanceof ObservableIntegerValue) {
            newObservable = (ObservableIntegerValue)rawObservable;
        } else if (rawObservable instanceof ObservableNumberValue) {
            final ObservableNumberValue numberValue = (ObservableNumberValue)rawObservable;
            newObservable = new ValueWrapper(rawObservable) {

                @Override
                protected int computeValue() {
                    return numberValue.intValue();
                }
            };
        } else {
            newObservable = new ValueWrapper(rawObservable) {

                @Override
                protected int computeValue() {
                    final Number value = rawObservable.getValue();
                    return (value == null)? 0 : value.intValue();
                }
            };
        }

        if (!newObservable.equals(observable)) {
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
            value = observable.get();
            observable.removeListener(listener);
            if (observable instanceof ValueWrapper) {
                ((ValueWrapper)observable).dispose();
            }
            observable = null;
        }
    }

    /**
     * Returns a string representation of this {@code IntegerPropertyBase} object.
     * @return a string representation of this {@code IntegerPropertyBase} object.
     */
    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder("IntegerProperty [");
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

        private final WeakReference<IntegerPropertyBase> wref;

        public Listener(IntegerPropertyBase ref) {
            this.wref = new WeakReference<>(ref);
        }

        @Override
        public void invalidated(Observable observable) {
            IntegerPropertyBase ref = wref.get();
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

    private abstract class ValueWrapper extends IntegerBinding {

        private ObservableValue<? extends Number> observable;

        public ValueWrapper(ObservableValue<? extends Number> observable) {
            this.observable = observable;
            bind(observable);
        }

        @Override
        public void dispose() {
            unbind(observable);
        }
    }
}

package com.tungsten.fclcore.fakefx.beans.binding;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyBooleanPropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyIntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyIntegerPropertyBase;
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener;
import com.tungsten.fclcore.fakefx.binding.BindingHelperObserver;
import com.tungsten.fclcore.fakefx.binding.SetExpressionHelper;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.fakefx.collections.ObservableSet;
import com.tungsten.fclcore.fakefx.collections.SetChangeListener;

/**
 * Base class that provides most of the functionality needed to implement a
 * {@link Binding} of an {@link ObservableSet}.
 * <p>
 * {@code SetBinding} provides a simple invalidation-scheme. An extending
 * class can register dependencies by calling {@link #bind(Observable...)}.
 * If one of the registered dependencies becomes invalid, this
 * {@code SetBinding} is marked as invalid. With
 * {@link #unbind(Observable...)} listening to dependencies can be stopped.
 * <p>
 * To provide a concrete implementation of this class, the method
 * {@link #computeValue()} has to be implemented to calculate the value of this
 * binding based on the current state of the dependencies. It is called when
 * {@link #get()} is called for an invalid binding.
 * <p>
 * See {@link DoubleBinding} for an example how this base class can be extended.
 *
 * @see Binding
 * @see SetExpression
 *
 * @param <E>
 *            the type of the {@code Set} elements
 * @since JavaFX 2.1
 */
public abstract class SetBinding<E> extends SetExpression<E> implements Binding<ObservableSet<E>> {

    /**
     * Creates a default {@code SetBinding}.
     */
    public SetBinding() {
    }

    private final SetChangeListener<E> setChangeListener = new SetChangeListener<E>() {
        @Override
        public void onChanged(Change<? extends E> change) {
            invalidateProperties();
            onInvalidating();
            SetExpressionHelper.fireValueChangedEvent(helper, change);
        }
    };

    private ObservableSet<E> value;
    private boolean valid = false;
    private BindingHelperObserver observer;
    private SetExpressionHelper<E> helper = null;

    private SizeProperty size0;
    private EmptyProperty empty0;

    @Override
    public ReadOnlyIntegerProperty sizeProperty() {
        if (size0 == null) {
            size0 = new SizeProperty();
        }
        return size0;
    }

    private class SizeProperty extends ReadOnlyIntegerPropertyBase {
        @Override
        public int get() {
            return size();
        }

        @Override
        public Object getBean() {
            return SetBinding.this;
        }

        @Override
        public String getName() {
            return "size";
        }

        protected void fireValueChangedEvent() {
            super.fireValueChangedEvent();
        }
    }

    @Override
    public ReadOnlyBooleanProperty emptyProperty() {
        if (empty0 == null) {
            empty0 = new EmptyProperty();
        }
        return empty0;
    }

    private class EmptyProperty extends ReadOnlyBooleanPropertyBase {

        @Override
        public boolean get() {
            return isEmpty();
        }

        @Override
        public Object getBean() {
            return SetBinding.this;
        }

        @Override
        public String getName() {
            return "empty";
        }

        protected void fireValueChangedEvent() {
            super.fireValueChangedEvent();
        }
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

    /**
     * Start observing the dependencies for changes. If the value of one of the
     * dependencies changes, the binding is marked as invalid.
     *
     * @param dependencies
     *            the dependencies to observe
     */
    protected final void bind(Observable... dependencies) {
        if ((dependencies != null) && (dependencies.length > 0)) {
            if (observer == null) {
                observer = new BindingHelperObserver(this);
            }
            for (final Observable dep : dependencies) {
                if (dep != null) {
                    dep.addListener(observer);
                }
            }
        }
    }

    /**
     * Stop observing the dependencies for changes.
     *
     * @param dependencies
     *            the dependencies to stop observing
     */
    protected final void unbind(Observable... dependencies) {
        if (observer != null) {
            for (final Observable dep : dependencies) {
                if (dep != null) {
                    dep.removeListener(observer);
                }
            }
            observer = null;
        }
    }

    /**
     * A default implementation of {@code dispose()} that is empty.
     */
    @Override
    public void dispose() {
    }

    /**
     * A default implementation of {@code getDependencies()} that returns an
     * empty {@link ObservableList}.
     *
     * @return an empty {@code ObservableList}
     */
    @Override
    public ObservableList<?> getDependencies() {
        return FXCollections.emptyObservableList();
    }

    /**
     * Returns the result of {@link #computeValue()}. The method
     * {@code computeValue()} is only called if the binding is invalid. The
     * result is cached and returned if the binding did not become invalid since
     * the last call of {@code get()}.
     *
     * @return the current value
     */
    @Override
    public final ObservableSet<E> get() {
        if (!valid) {
            value = computeValue();
            valid = true;
            if (value != null) {
                value.addListener(setChangeListener);
            }
        }
        return value;
    }

    /**
     * The method onInvalidating() can be overridden by extending classes to
     * react, if this binding becomes invalid. The default implementation is
     * empty.
     */
    protected void onInvalidating() {
    }

    private void invalidateProperties() {
        if (size0 != null) {
            size0.fireValueChangedEvent();
        }
        if (empty0 != null) {
            empty0.fireValueChangedEvent();
        }
    }

    @Override
    public final void invalidate() {
        if (valid) {
            if (value != null) {
                value.removeListener(setChangeListener);
            }
            valid = false;
            invalidateProperties();
            onInvalidating();
            SetExpressionHelper.fireValueChangedEvent(helper);
        }
    }

    @Override
    public final boolean isValid() {
        return valid;
    }

    /**
     * Calculates the current value of this binding.
     * <p>
     * Classes extending {@code SetBinding} have to provide an implementation
     * of {@code computeValue}.
     *
     * @return the current value
     */
    protected abstract ObservableSet<E> computeValue();

    /**
     * Returns a string representation of this {@code SetBinding} object.
     * @return a string representation of this {@code SetBinding} object.
     */
    @Override
    public String toString() {
        return valid ? "SetBinding [value: " + get() + "]"
                : "SetBinding [invalid]";
    }

}

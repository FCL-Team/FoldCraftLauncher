package com.tungsten.fclcore.fakefx.beans.binding;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener;
import com.tungsten.fclcore.fakefx.binding.BindingHelperObserver;
import com.tungsten.fclcore.fakefx.binding.ExpressionHelper;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableList;

public abstract class ObjectBinding<T> extends ObjectExpression<T> implements
        Binding<T> {

    private T value;
    private boolean valid = false;
    private BindingHelperObserver observer;
    private ExpressionHelper<T> helper = null;

    /**
     * Creates a default {@code ObjectBinding}.
     */
    public ObjectBinding() {
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
                dep.addListener(observer);
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
                dep.removeListener(observer);
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
    public final T get() {
        if (!valid) {
            T computed = computeValue();

            if (!allowValidation()) {
                return computed;
            }

            value = computed;
            valid = true;
        }
        return value;
    }

    /**
     * Called when this binding becomes invalid. Can be overridden by extending classes to react to the invalidation.
     * The default implementation is empty.
     */
    protected void onInvalidating() {
    }

    @Override
    public final void invalidate() {
        if (valid) {
            valid = false;
            onInvalidating();
            ExpressionHelper.fireValueChangedEvent(helper);

            /*
             * Cached value should be cleared to avoid a strong reference to stale data,
             * but only if this binding didn't become valid after firing the event:
             */

            if (!valid) {
                value = null;
            }
        }
    }

    @Override
    public final boolean isValid() {
        return valid;
    }

    /**
     * Checks if the binding has at least one listener registered on it. This
     * is useful for subclasses which want to conserve resources when not observed.
     *
     * @return {@code true} if this binding currently has one or more
     *     listeners registered on it, otherwise {@code false}
     * @since 19
     */
    protected final boolean isObserved() {
        return helper != null;
    }

    /**
     * Checks if the binding is allowed to become valid. Overriding classes can
     * prevent a binding from becoming valid. This is useful in subclasses which
     * do not always listen for invalidations of their dependencies and prefer to
     * recompute the current value instead. This can also be useful if caching of
     * the current computed value is not desirable.
     * <p>
     * The default implementation always allows bindings to become valid.
     *
     * @return {@code true} if this binding is allowed to become valid, otherwise
     *     {@code false}
     * @since 19
     */
    protected boolean allowValidation() {
        return true;
    }

    /**
     * Calculates the current value of this binding.
     * <p>
     * Classes extending {@code ObjectBinding} have to provide an implementation
     * of {@code computeValue}.
     *
     * @return the current value
     */
    protected abstract T computeValue();

    /**
     * Returns a string representation of this {@code ObjectBinding} object.
     * @return a string representation of this {@code ObjectBinding} object.
     */
    @Override
    public String toString() {
        return valid ? "ObjectBinding [value: " + get() + "]"
                : "ObjectBinding [invalid]";
    }
}

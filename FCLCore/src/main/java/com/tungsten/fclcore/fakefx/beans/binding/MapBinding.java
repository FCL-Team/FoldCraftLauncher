package com.tungsten.fclcore.fakefx.beans.binding;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyBooleanPropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyIntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyIntegerPropertyBase;
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener;
import com.tungsten.fclcore.fakefx.binding.BindingHelperObserver;
import com.tungsten.fclcore.fakefx.binding.MapExpressionHelper;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.MapChangeListener;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.fakefx.collections.ObservableMap;

public abstract class MapBinding<K, V> extends MapExpression<K, V> implements Binding<ObservableMap<K, V>> {

    private final MapChangeListener<K, V> mapChangeListener = new MapChangeListener<K, V>() {
        @Override
        public void onChanged(Change<? extends K, ? extends V> change) {
            invalidateProperties();
            onInvalidating();
            MapExpressionHelper.fireValueChangedEvent(helper, change);
        }
    };

    private ObservableMap<K, V> value;
    private boolean valid = false;
    private BindingHelperObserver observer;
    private MapExpressionHelper<K, V> helper = null;

    private SizeProperty size0;
    private EmptyProperty empty0;

    /**
     * Creates a default {@code MapBinding}.
     */
    public MapBinding() {
    }

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
            return MapBinding.this;
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
            return MapBinding.this;
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
        helper = MapExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        helper = MapExpressionHelper.removeListener(helper, listener);
    }

    @Override
    public void addListener(ChangeListener<? super ObservableMap<K, V>> listener) {
        helper = MapExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(ChangeListener<? super ObservableMap<K, V>> listener) {
        helper = MapExpressionHelper.removeListener(helper, listener);
    }

    @Override
    public void addListener(MapChangeListener<? super K, ? super V> listener) {
        helper = MapExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(MapChangeListener<? super K, ? super V> listener) {
        helper = MapExpressionHelper.removeListener(helper, listener);
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
    public final ObservableMap<K, V> get() {
        if (!valid) {
            value = computeValue();
            valid = true;
            if (value != null) {
                value.addListener(mapChangeListener);
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
                value.removeListener(mapChangeListener);
            }
            valid = false;
            invalidateProperties();
            onInvalidating();
            MapExpressionHelper.fireValueChangedEvent(helper);
        }
    }

    @Override
    public final boolean isValid() {
        return valid;
    }

    /**
     * Calculates the current value of this binding.
     * <p>
     * Classes extending {@code MapBinding} have to provide an implementation
     * of {@code computeValue}.
     *
     * @return the current value
     */
    protected abstract ObservableMap<K, V> computeValue();

    /**
     * Returns a string representation of this {@code MapBinding} object.
     * @return a string representation of this {@code MapBinding} object.
     */
    @Override
    public String toString() {
        return valid ? "MapBinding [value: " + get() + "]"
                : "MapBinding [invalid]";
    }

}

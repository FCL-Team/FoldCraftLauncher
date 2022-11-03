package com.tungsten.fclcore.fakefx.beans.property;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.beans.WeakListener;
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener;
import com.tungsten.fclcore.fakefx.beans.value.ObservableValue;
import com.tungsten.fclcore.fakefx.binding.MapExpressionHelper;
import com.tungsten.fclcore.fakefx.collections.MapChangeListener;
import com.tungsten.fclcore.fakefx.collections.ObservableMap;

import java.lang.ref.WeakReference;

public abstract class MapPropertyBase<K, V> extends MapProperty<K, V> {

    private final MapChangeListener<K, V> mapChangeListener = change -> {
        invalidateProperties();
        invalidated();
        fireValueChangedEvent(change);
    };

    private ObservableMap<K, V> value;
    private ObservableValue<? extends ObservableMap<K, V>> observable = null;
    private InvalidationListener listener = null;
    private boolean valid = true;
    private MapExpressionHelper<K, V> helper = null;

    private SizeProperty size0;
    private EmptyProperty empty0;

    /**
     * The Constructor of {@code MapPropertyBase}
     */
    public MapPropertyBase() {}

    /**
     * The constructor of the {@code MapPropertyBase}.
     *
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public MapPropertyBase(ObservableMap<K, V> initialValue) {
        this.value = initialValue;
        if (initialValue != null) {
            initialValue.addListener(mapChangeListener);
        }
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
            return MapPropertyBase.this;
        }

        @Override
        public String getName() {
            return "size";
        }

        @Override
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
            return MapPropertyBase.this;
        }

        @Override
        public String getName() {
            return "empty";
        }

        @Override
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

    protected void fireValueChangedEvent() {
        MapExpressionHelper.fireValueChangedEvent(helper);
    }

    protected void fireValueChangedEvent(MapChangeListener.Change<? extends K, ? extends V> change) {
        MapExpressionHelper.fireValueChangedEvent(helper, change);
    }

    private void invalidateProperties() {
        if (size0 != null) {
            size0.fireValueChangedEvent();
        }
        if (empty0 != null) {
            empty0.fireValueChangedEvent();
        }
    }

    private void markInvalid(ObservableMap<K, V> oldValue) {
        if (valid) {
            if (oldValue != null) {
                oldValue.removeListener(mapChangeListener);
            }
            valid = false;
            invalidateProperties();
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

    @Override
    public ObservableMap<K, V> get() {
        if (!valid) {
            value = observable == null ? value : observable.getValue();
            valid = true;
            if (value != null) {
                value.addListener(mapChangeListener);
            }
        }
        return value;
    }

    @Override
    public void set(ObservableMap<K, V> newValue) {
        if (isBound()) {
            throw new RuntimeException((getBean() != null && getName() != null ?
                    getBean().getClass().getSimpleName() + "." + getName() + " : ": "") + "A bound value cannot be set.");
        }
        if (value != newValue) {
            final ObservableMap<K, V> oldValue = value;
            value = newValue;
            markInvalid(oldValue);
        }
    }

    @Override
    public boolean isBound() {
        return observable != null;
    }

    @Override
    public void bind(final ObservableValue<? extends ObservableMap<K, V>> newObservable) {
        if (newObservable == null) {
            throw new NullPointerException("Cannot bind to null");
        }
        if (newObservable != observable) {
            unbind();
            observable = newObservable;
            if (listener == null) {
                listener = new Listener<>(this);
            }
            observable.addListener(listener);
            markInvalid(value);
        }
    }

    @Override
    public void unbind() {
        if (observable != null) {
            value = observable.getValue();
            observable.removeListener(listener);
            observable = null;
        }
    }

    /**
     * Returns a string representation of this {@code MapPropertyBase} object.
     * @return a string representation of this {@code MapPropertyBase} object.
     */
    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder("MapProperty [");
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

    private static class Listener<K,V> implements InvalidationListener, WeakListener {

        private final WeakReference<MapPropertyBase<K,V>> wref;

        public Listener(MapPropertyBase<K,V> ref) {
            this.wref = new WeakReference<>(ref);
        }

        @Override
        public void invalidated(Observable observable) {
            MapPropertyBase<K,V> ref = wref.get();
            if (ref == null) {
                observable.removeListener(this);
            } else {
                ref.markInvalid(ref.value);
            }
        }

        @Override
        public boolean wasGarbageCollected() {
            return wref.get() == null;
        }
    }
}

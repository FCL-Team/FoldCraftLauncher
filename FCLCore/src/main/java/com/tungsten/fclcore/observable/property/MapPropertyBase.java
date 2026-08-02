package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.Observable;
import com.tungsten.fclcore.observable.WeakListener;
import com.tungsten.fclcore.observable.collections.MapChangeListener;
import com.tungsten.fclcore.observable.collections.ObservableMap;
import com.tungsten.fclcore.observable.value.ChangeListener;
import com.tungsten.fclcore.observable.value.ObservableValue;

import java.lang.ref.WeakReference;

/**
 * Port of {@code fakefx.beans.property.MapPropertyBase}，语义同 ListPropertyBase。
 */
public abstract class MapPropertyBase<K, V> extends MapProperty<K, V> {

    private final MapChangeListener<K, V> mapChangeListener = change -> {
        invalidated();
        fireValueChangedEvent(change);
    };

    private ObservableMap<K, V> value;
    private ObservableValue<? extends ObservableMap<K, V>> observable = null;
    private InvalidationListener listener = null;
    private boolean valid = true;
    private MapPropertyHelper<K, V> helper = null;

    public MapPropertyBase() {
    }

    public MapPropertyBase(ObservableMap<K, V> initialValue) {
        this.value = initialValue;
        if (initialValue != null) {
            initialValue.addListener(mapChangeListener);
        }
    }

    @Override
    public void addListener(InvalidationListener listener) {
        if (helper == null) {
            helper = new MapPropertyHelper<>(this);
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
    public void addListener(ChangeListener<? super ObservableMap<K, V>> listener) {
        if (helper == null) {
            helper = new MapPropertyHelper<>(this);
        }
        helper.addListener(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super ObservableMap<K, V>> listener) {
        if (helper != null) {
            helper.removeListener(listener);
        }
    }

    @Override
    public void addListener(MapChangeListener<? super K, ? super V> listener) {
        if (helper == null) {
            helper = new MapPropertyHelper<>(this);
        }
        helper.addListener(listener);
    }

    @Override
    public void removeListener(MapChangeListener<? super K, ? super V> listener) {
        if (helper != null) {
            helper.removeListener(listener);
        }
    }

    protected void fireValueChangedEvent() {
        if (helper != null) {
            helper.fireValueChangedEvent();
        }
    }

    protected void fireValueChangedEvent(MapChangeListener.Change<? extends K, ? extends V> change) {
        if (helper != null) {
            helper.fireValueChangedEvent(change);
        }
    }

    private void markInvalid(ObservableMap<K, V> oldValue) {
        if (valid) {
            if (oldValue != null) {
                oldValue.removeListener(mapChangeListener);
            }
            valid = false;
            invalidated();
            fireValueChangedEvent();
        }
    }

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
                    getBean().getClass().getSimpleName() + "." + getName() + " : " : "") + "A bound value cannot be set.");
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

    private static class Listener<K, V> implements InvalidationListener, WeakListener {

        private final WeakReference<MapPropertyBase<K, V>> wref;

        public Listener(MapPropertyBase<K, V> ref) {
            this.wref = new WeakReference<>(ref);
        }

        @Override
        public void invalidated(Observable observable) {
            MapPropertyBase<K, V> ref = wref.get();
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

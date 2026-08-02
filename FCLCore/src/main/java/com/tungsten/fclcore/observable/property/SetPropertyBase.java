package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.Observable;
import com.tungsten.fclcore.observable.WeakListener;
import com.tungsten.fclcore.observable.collections.ObservableSet;
import com.tungsten.fclcore.observable.collections.SetChangeListener;
import com.tungsten.fclcore.observable.value.ChangeListener;
import com.tungsten.fclcore.observable.value.ObservableValue;

import java.lang.ref.WeakReference;

/**
 * Port of {@code SetPropertyBase}，语义同 MapPropertyBase。
 */
public abstract class SetPropertyBase<E> extends SetProperty<E> {

    private final SetChangeListener<E> setChangeListener = change -> {
        invalidated();
        fireValueChangedEvent(change);
    };

    private ObservableSet<E> value;
    private ObservableValue<? extends ObservableSet<E>> observable = null;
    private InvalidationListener listener = null;
    private boolean valid = true;
    private SetPropertyHelper<E> helper = null;

    public SetPropertyBase() {
    }

    public SetPropertyBase(ObservableSet<E> initialValue) {
        this.value = initialValue;
        if (initialValue != null) {
            initialValue.addListener(setChangeListener);
        }
    }

    @Override
    public void addListener(InvalidationListener listener) {
        if (helper == null) {
            helper = new SetPropertyHelper<>(this);
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
    public void addListener(ChangeListener<? super ObservableSet<E>> listener) {
        if (helper == null) {
            helper = new SetPropertyHelper<>(this);
        }
        helper.addListener(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super ObservableSet<E>> listener) {
        if (helper != null) {
            helper.removeListener(listener);
        }
    }

    @Override
    public void addListener(SetChangeListener<? super E> listener) {
        if (helper == null) {
            helper = new SetPropertyHelper<>(this);
        }
        helper.addListener(listener);
    }

    @Override
    public void removeListener(SetChangeListener<? super E> listener) {
        if (helper != null) {
            helper.removeListener(listener);
        }
    }

    protected void fireValueChangedEvent() {
        if (helper != null) {
            helper.fireValueChangedEvent();
        }
    }

    protected void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
        if (helper != null) {
            helper.fireValueChangedEvent(change);
        }
    }

    private void markInvalid(ObservableSet<E> oldValue) {
        if (valid) {
            if (oldValue != null) {
                oldValue.removeListener(setChangeListener);
            }
            valid = false;
            invalidated();
            fireValueChangedEvent();
        }
    }

    protected void invalidated() {
    }

    @Override
    public ObservableSet<E> get() {
        if (!valid) {
            value = observable == null ? value : observable.getValue();
            valid = true;
            if (value != null) {
                value.addListener(setChangeListener);
            }
        }
        return value;
    }

    @Override
    public void set(ObservableSet<E> newValue) {
        if (isBound()) {
            throw new RuntimeException((getBean() != null && getName() != null ?
                    getBean().getClass().getSimpleName() + "." + getName() + " : " : "") + "A bound value cannot be set.");
        }
        if (value != newValue) {
            final ObservableSet<E> oldValue = value;
            value = newValue;
            markInvalid(oldValue);
        }
    }

    @Override
    public boolean isBound() {
        return observable != null;
    }

    @Override
    public void bind(final ObservableValue<? extends ObservableSet<E>> newObservable) {
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

    private static class Listener<E> implements InvalidationListener, WeakListener {

        private final WeakReference<SetPropertyBase<E>> wref;

        public Listener(SetPropertyBase<E> ref) {
            this.wref = new WeakReference<>(ref);
        }

        @Override
        public void invalidated(Observable observable) {
            SetPropertyBase<E> ref = wref.get();
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

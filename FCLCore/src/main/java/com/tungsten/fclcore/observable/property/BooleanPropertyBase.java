package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.Observable;
import com.tungsten.fclcore.observable.WeakListener;
import com.tungsten.fclcore.observable.binding.ExpressionHelper;
import com.tungsten.fclcore.observable.value.ChangeListener;
import com.tungsten.fclcore.observable.value.ObservableBooleanValue;
import com.tungsten.fclcore.observable.value.ObservableValue;

import java.lang.ref.WeakReference;

/**
 * Port of {@code fakefx.beans.property.BooleanPropertyBase}：
 * set 用 != 比较（boolean 即值比较），其余语义同 ObjectPropertyBase。
 */
public abstract class BooleanPropertyBase extends BooleanProperty {

    private boolean value;
    private ObservableBooleanValue observable = null;
    private InvalidationListener listener = null;
    private boolean valid = true;
    private ExpressionHelper<Boolean> helper = null;

    public BooleanPropertyBase() {
    }

    public BooleanPropertyBase(boolean initialValue) {
        this.value = initialValue;
    }

    @Override
    public void addListener(InvalidationListener listener) {
        if (helper == null) {
            helper = new ExpressionHelper<>(this);
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
    public void addListener(ChangeListener<? super Boolean> listener) {
        if (helper == null) {
            helper = new ExpressionHelper<>(this);
        }
        helper.addListener(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super Boolean> listener) {
        if (helper != null) {
            helper.removeListener(listener);
        }
    }

    protected void fireValueChangedEvent() {
        if (helper != null) {
            helper.fireValueChangedEvent();
        }
    }

    private void markInvalid() {
        if (valid) {
            valid = false;
            invalidated();
            fireValueChangedEvent();
        }
    }

    protected void invalidated() {
    }

    @Override
    public boolean get() {
        valid = true;
        return observable == null ? value : observable.get();
    }

    @Override
    public void set(boolean newValue) {
        if (isBound()) {
            throw new RuntimeException((getBean() != null && getName() != null ?
                    getBean().getClass().getSimpleName() + "." + getName() + " : " : "") + "A bound value cannot be set.");
        }
        if (value != newValue) {
            value = newValue;
            markInvalid();
        }
    }

    @Override
    public boolean isBound() {
        return observable != null;
    }

    @Override
    public void bind(final ObservableValue<? extends Boolean> rawObservable) {
        if (rawObservable == null) {
            throw new NullPointerException("Cannot bind to null");
        }
        ObservableBooleanValue newObservable;
        if (rawObservable instanceof ObservableBooleanValue) {
            newObservable = (ObservableBooleanValue) rawObservable;
        } else {
            // 与 fakefx 一致：包装非 ObservableBooleanValue 的 source
            final ObservableValue<? extends Boolean> source = rawObservable;
            newObservable = new ObservableBooleanValue() {
                @Override
                public boolean get() {
                    final Boolean v = source.getValue();
                    return v != null && v;
                }

                @Override
                public void addListener(InvalidationListener listener) {
                    source.addListener(listener);
                }

                @Override
                public void removeListener(InvalidationListener listener) {
                    source.removeListener(listener);
                }

                @Override
                public void addListener(ChangeListener<? super Boolean> listener) {
                    source.addListener(listener);
                }

                @Override
                public void removeListener(ChangeListener<? super Boolean> listener) {
                    source.removeListener(listener);
                }

                @Override
                public Boolean getValue() {
                    return get();
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

    @Override
    public void unbind() {
        if (observable != null) {
            value = observable.get();
            observable.removeListener(listener);
            observable = null;
        }
    }

    private static class Listener implements InvalidationListener, WeakListener {

        private final WeakReference<BooleanPropertyBase> wref;

        public Listener(BooleanPropertyBase ref) {
            this.wref = new WeakReference<>(ref);
        }

        @Override
        public void invalidated(Observable observable) {
            BooleanPropertyBase ref = wref.get();
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

package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.Observable;
import com.tungsten.fclcore.observable.WeakListener;
import com.tungsten.fclcore.observable.binding.ExpressionHelper;
import com.tungsten.fclcore.observable.value.ChangeListener;
import com.tungsten.fclcore.observable.value.ObservableLongValue;
import com.tungsten.fclcore.observable.value.ObservableValue;

import java.lang.ref.WeakReference;

/**
 * Port of {@code LongPropertyBase}，语义同 IntegerPropertyBase。
 */
public abstract class LongPropertyBase extends LongProperty {

    private long value;
    private ObservableLongValue observable = null;
    private InvalidationListener listener = null;
    private boolean valid = true;
    private ExpressionHelper<Number> helper = null;

    public LongPropertyBase() {
    }

    public LongPropertyBase(long initialValue) {
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
    public void addListener(ChangeListener<? super Number> listener) {
        if (helper == null) {
            helper = new ExpressionHelper<>(this);
        }
        helper.addListener(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super Number> listener) {
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
    public long get() {
        valid = true;
        return observable == null ? value : observable.get();
    }

    @Override
    public void set(long newValue) {
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
    public void bind(final ObservableValue<? extends Number> rawObservable) {
        if (rawObservable == null) {
            throw new NullPointerException("Cannot bind to null");
        }
        ObservableLongValue newObservable;
        if (rawObservable instanceof ObservableLongValue) {
            newObservable = (ObservableLongValue) rawObservable;
        } else {
            final ObservableValue<? extends Number> source = rawObservable;
            newObservable = new ObservableLongValue() {
                @Override
                public long get() {
                    final Number v = source.getValue();
                    return v == null ? 0L : v.longValue();
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
                public void addListener(ChangeListener<? super Number> listener) {
                    source.addListener(listener);
                }

                @Override
                public void removeListener(ChangeListener<? super Number> listener) {
                    source.removeListener(listener);
                }

                @Override
                public Number getValue() {
                    return get();
                }

                @Override
                public int intValue() {
                    return (int) get();
                }

                @Override
                public long longValue() {
                    return get();
                }

                @Override
                public float floatValue() {
                    return get();
                }

                @Override
                public double doubleValue() {
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

        private final WeakReference<LongPropertyBase> wref;

        public Listener(LongPropertyBase ref) {
            this.wref = new WeakReference<>(ref);
        }

        @Override
        public void invalidated(Observable observable) {
            LongPropertyBase ref = wref.get();
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

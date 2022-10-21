package com.tungsten.fclcore.fakefx;

import java.lang.ref.WeakReference;

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
        helper = ExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        helper = ExpressionHelper.removeListener(helper, listener);
    }

    @Override
    public void addListener(ChangeListener<? super Boolean> listener) {
        helper = ExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(ChangeListener<? super Boolean> listener) {
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
            throw new java.lang.RuntimeException((getBean() != null && getName() != null ?
                    getBean().getClass().getSimpleName() + "." + getName() + " : ": "") + "A bound value cannot be set.");
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

        final ObservableBooleanValue newObservable = (rawObservable instanceof ObservableBooleanValue) ? (ObservableBooleanValue) rawObservable
                : new ValueWrapper(rawObservable);

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
            if (observable instanceof ValueWrapper) {
                ((ValueWrapper)observable).dispose();
            }
            observable = null;
        }
    }

    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder("BooleanProperty [");
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

    private class ValueWrapper extends BooleanBinding {
        private ObservableValue<? extends Boolean> observable;

        public ValueWrapper(ObservableValue<? extends Boolean> observable) {
            this.observable = observable;
            bind(observable);
        }

        @Override
        protected boolean computeValue() {
            final Boolean value = observable.getValue();
            return (value == null) ? false : value;
        }

        @Override
        public void dispose() {
            unbind(observable);
        }
    }
}
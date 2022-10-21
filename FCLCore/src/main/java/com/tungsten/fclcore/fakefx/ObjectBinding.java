package com.tungsten.fclcore.fakefx;

public abstract class ObjectBinding<T> extends ObjectExpression<T> implements Binding<T> {

    private T value;
    private boolean valid = false;
    private BindingHelperObserver observer;
    private ExpressionHelper<T> helper = null;

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

    protected final void unbind(Observable... dependencies) {
        if (observer != null) {
            for (final Observable dep : dependencies) {
                dep.removeListener(observer);
            }
            observer = null;
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public final T get() {
        if (!valid) {
            value = computeValue();
            valid = true;
        }
        return value;
    }

    protected void onInvalidating() {
    }

    @Override
    public final void invalidate() {
        if (valid) {
            valid = false;
            onInvalidating();
            ExpressionHelper.fireValueChangedEvent(helper);
        }
    }

    @Override
    public final boolean isValid() {
        return valid;
    }

    protected abstract T computeValue();

    @Override
    public String toString() {
        return valid ? "ObjectBinding [value: " + get() + "]"
                : "ObjectBinding [invalid]";
    }
}
package com.tungsten.fclcore.fakefx;

public abstract class BooleanBinding extends BooleanExpression implements
        Binding<Boolean> {

    public BooleanBinding() {
    }

    private boolean value;
    private boolean valid = false;
    private BindingHelperObserver observer;
    private ExpressionHelper<Boolean> helper = null;

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
    public final boolean get() {
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

    protected abstract boolean computeValue();

    @Override
    public String toString() {
        return valid ? "BooleanBinding [value: " + get() + "]"
                : "BooleanBinding [invalid]";
    }

}

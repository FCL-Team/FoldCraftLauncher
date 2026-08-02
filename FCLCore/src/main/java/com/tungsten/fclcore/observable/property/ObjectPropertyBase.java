package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.Observable;
import com.tungsten.fclcore.observable.WeakListener;
import com.tungsten.fclcore.observable.binding.ExpressionHelper;
import com.tungsten.fclcore.observable.value.ChangeListener;
import com.tungsten.fclcore.observable.value.ObservableValue;

import java.lang.ref.WeakReference;

/**
 * Port of {@code fakefx.beans.property.ObjectPropertyBase}，语义逐条对齐：
 * <ul>
 * <li>set 用引用比较（!=），同引用不触发事件；</li>
 * <li>bound 状态下 set 抛 RuntimeException("A bound value cannot be set.")；</li>
 * <li>bind 立即失效并单向同步 source 的值；unbind 时把当前值固化；</li>
 * <li>markInvalid 只在 valid→invalid 边沿触发一次 invalidated() + 事件。</li>
 * </ul>
 */
public abstract class ObjectPropertyBase<T> extends ObjectProperty<T> {

    private T value;
    private ObservableValue<? extends T> observable = null;
    private InvalidationListener listener = null;
    private boolean valid = true;
    private ExpressionHelper<T> helper = null;

    public ObjectPropertyBase() {
    }

    public ObjectPropertyBase(T initialValue) {
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
    public void addListener(ChangeListener<? super T> listener) {
        if (helper == null) {
            helper = new ExpressionHelper<>(this);
        }
        helper.addListener(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super T> listener) {
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

    /**
     * The method {@code invalidated()} can be overridden to receive
     * invalidation notifications. The default implementation is empty.
     */
    protected void invalidated() {
    }

    @Override
    public T get() {
        valid = true;
        return observable == null ? value : observable.getValue();
    }

    @Override
    public void set(T newValue) {
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
    public void bind(final ObservableValue<? extends T> newObservable) {
        if (newObservable == null) {
            throw new NullPointerException("Cannot bind to null");
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
            value = observable.getValue();
            observable.removeListener(listener);
            observable = null;
        }
    }

    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder("ObjectProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if ((name != null) && (!name.equals(""))) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append(get()).append("]");
        return result.toString();
    }

    private static class Listener implements InvalidationListener, WeakListener {

        private final WeakReference<ObjectPropertyBase<?>> wref;

        public Listener(ObjectPropertyBase<?> ref) {
            this.wref = new WeakReference<>(ref);
        }

        @Override
        public void invalidated(Observable observable) {
            ObjectPropertyBase<?> ref = wref.get();
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

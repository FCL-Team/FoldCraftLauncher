package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.binding.ExpressionHelper;
import com.tungsten.fclcore.observable.value.ChangeListener;

/**
 * Port of {@code ReadOnlyObjectPropertyBase}：
 * 只读属性的监听基础设施，配合 {@link ReadOnlyObjectWrapper} 使用。
 */
public abstract class ReadOnlyObjectPropertyBase<T> extends ReadOnlyObjectProperty<T> {

    private ExpressionHelper<T> helper = null;

    public ReadOnlyObjectPropertyBase() {
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
}

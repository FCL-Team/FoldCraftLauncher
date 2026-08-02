package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.collections.ListChangeListener;
import com.tungsten.fclcore.observable.collections.ObservableList;
import com.tungsten.fclcore.observable.value.ChangeListener;

/**
 * Port of {@code fakefx.beans.property.ReadOnlyListPropertyBase}：
 * 只读列表属性的监听基础设施，配合 {@link ReadOnlyListWrapper} 使用。
 */
public abstract class ReadOnlyListPropertyBase<E> extends ReadOnlyListProperty<E> {

    private ListPropertyHelper<E> helper = null;

    public ReadOnlyListPropertyBase() {
    }

    @Override
    public void addListener(InvalidationListener listener) {
        if (helper == null) {
            helper = new ListPropertyHelper<>(this);
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
    public void addListener(ChangeListener<? super ObservableList<E>> listener) {
        if (helper == null) {
            helper = new ListPropertyHelper<>(this);
        }
        helper.addListener(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super ObservableList<E>> listener) {
        if (helper != null) {
            helper.removeListener(listener);
        }
    }

    @Override
    public void addListener(ListChangeListener<? super E> listener) {
        if (helper == null) {
            helper = new ListPropertyHelper<>(this);
        }
        helper.addListener(listener);
    }

    @Override
    public void removeListener(ListChangeListener<? super E> listener) {
        if (helper != null) {
            helper.removeListener(listener);
        }
    }

    protected void fireValueChangedEvent() {
        if (helper != null) {
            helper.fireValueChangedEvent();
        }
    }

    protected void fireValueChangedEvent(ListChangeListener.Change<? extends E> change) {
        if (helper != null) {
            helper.fireValueChangedEvent(change);
        }
    }
}

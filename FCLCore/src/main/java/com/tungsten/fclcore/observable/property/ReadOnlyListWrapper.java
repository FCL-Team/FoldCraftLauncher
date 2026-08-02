package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.collections.ListChangeListener;
import com.tungsten.fclcore.observable.collections.ObservableList;

/**
 * Port of {@code fakefx.beans.property.ReadOnlyListWrapper}：
 * 可写包装 + 同步事件的只读视图；内容变更重发前先 reset。
 */
public class ReadOnlyListWrapper<E> extends SimpleListProperty<E> {

    private ReadOnlyPropertyImpl readOnlyProperty;

    public ReadOnlyListWrapper() {
    }

    public ReadOnlyListWrapper(ObservableList<E> initialValue) {
        super(initialValue);
    }

    public ReadOnlyListWrapper(Object bean, String name) {
        super(bean, name);
    }

    public ReadOnlyListWrapper(Object bean, String name, ObservableList<E> initialValue) {
        super(bean, name, initialValue);
    }

    public ReadOnlyListProperty<E> getReadOnlyProperty() {
        if (readOnlyProperty == null) {
            readOnlyProperty = new ReadOnlyPropertyImpl();
        }
        return readOnlyProperty;
    }

    @Override
    protected void fireValueChangedEvent() {
        super.fireValueChangedEvent();
        if (readOnlyProperty != null) {
            readOnlyProperty.fireValueChangedEvent();
        }
    }

    @Override
    protected void fireValueChangedEvent(ListChangeListener.Change<? extends E> change) {
        super.fireValueChangedEvent(change);
        if (readOnlyProperty != null) {
            change.reset();
            readOnlyProperty.fireValueChangedEvent(change);
        }
    }

    private class ReadOnlyPropertyImpl extends ReadOnlyListPropertyBase<E> {

        @Override
        public ObservableList<E> get() {
            return ReadOnlyListWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlyListWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlyListWrapper.this.getName();
        }

        @Override
        public ReadOnlyIntegerProperty sizeProperty() {
            return ReadOnlyListWrapper.this.sizeProperty();
        }

        @Override
        public ReadOnlyBooleanProperty emptyProperty() {
            return ReadOnlyListWrapper.this.emptyProperty();
        }
    }
}

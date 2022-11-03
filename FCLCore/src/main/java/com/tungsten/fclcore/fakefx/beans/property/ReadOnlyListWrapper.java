package com.tungsten.fclcore.fakefx.beans.property;

import com.tungsten.fclcore.fakefx.collections.ListChangeListener;
import com.tungsten.fclcore.fakefx.collections.ObservableList;

/**
 * This class provides a convenient class to define read-only properties. It
 * creates two properties that are synchronized. One property is read-only
 * and can be passed to external users. The other property is read- and
 * writable and should be used internally only.
 *
 * @since JavaFX 2.1
 */
public class ReadOnlyListWrapper<E> extends SimpleListProperty<E> {

    private ReadOnlyPropertyImpl readOnlyProperty;

    /**
     * The constructor of {@code ReadOnlyListWrapper}
     */
    public ReadOnlyListWrapper() {
    }

    /**
     * The constructor of {@code ReadOnlyListWrapper}
     *
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public ReadOnlyListWrapper(ObservableList<E> initialValue) {
        super(initialValue);
    }

    /**
     * The constructor of {@code ReadOnlyListWrapper}
     *
     * @param bean
     *            the bean of this {@code ReadOnlyListWrapper}
     * @param name
     *            the name of this {@code ReadOnlyListWrapper}
     */
    public ReadOnlyListWrapper(Object bean, String name) {
        super(bean, name);
    }

    /**
     * The constructor of {@code ReadOnlyListWrapper}
     *
     * @param bean
     *            the bean of this {@code ReadOnlyListWrapper}
     * @param name
     *            the name of this {@code ReadOnlyListWrapper}
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public ReadOnlyListWrapper(Object bean, String name,
            ObservableList<E> initialValue) {
        super(bean, name, initialValue);
    }

    /**
     * Returns the readonly property, that is synchronized with this
     * {@code ReadOnlyListWrapper}.
     *
     * @return the readonly property
     */
    public ReadOnlyListProperty<E> getReadOnlyProperty() {
        if (readOnlyProperty == null) {
            readOnlyProperty = new ReadOnlyPropertyImpl();
        }
        return readOnlyProperty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void fireValueChangedEvent() {
        super.fireValueChangedEvent();
        if (readOnlyProperty != null) {
            readOnlyProperty.fireValueChangedEvent();
        }
    }

    /**
     * {@inheritDoc}
     */
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

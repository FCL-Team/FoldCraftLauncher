package com.tungsten.fclcore.fakefx.beans.property;

import com.tungsten.fclcore.fakefx.collections.ObservableSet;
import com.tungsten.fclcore.fakefx.collections.SetChangeListener;

/**
 * This class provides a convenient class to define read-only properties. It
 * creates two properties that are synchronized. One property is read-only
 * and can be passed to external users. The other property is read- and
 * writable and should be used internally only.
 *
 * @since JavaFX 2.1
 */
public class ReadOnlySetWrapper<E> extends SimpleSetProperty<E> {

    private ReadOnlyPropertyImpl readOnlyProperty;

    /**
     * The constructor of {@code ReadOnlySetWrapper}
     */
    public ReadOnlySetWrapper() {
    }

    /**
     * The constructor of {@code ReadOnlySetWrapper}
     *
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public ReadOnlySetWrapper(ObservableSet<E> initialValue) {
        super(initialValue);
    }

    /**
     * The constructor of {@code ReadOnlySetWrapper}
     *
     * @param bean
     *            the bean of this {@code ReadOnlySetWrapper}
     * @param name
     *            the name of this {@code ReadOnlySetWrapper}
     */
    public ReadOnlySetWrapper(Object bean, String name) {
        super(bean, name);
    }

    /**
     * The constructor of {@code ReadOnlySetWrapper}
     *
     * @param bean
     *            the bean of this {@code ReadOnlySetWrapper}
     * @param name
     *            the name of this {@code ReadOnlySetWrapper}
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public ReadOnlySetWrapper(Object bean, String name,
                              ObservableSet<E> initialValue) {
        super(bean, name, initialValue);
    }

    /**
     * Returns the readonly property, that is synchronized with this
     * {@code ReadOnlySetWrapper}.
     *
     * @return the readonly property
     */
    public ReadOnlySetProperty<E> getReadOnlyProperty() {
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
    protected void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
        super.fireValueChangedEvent(change);
        if (readOnlyProperty != null) {
            readOnlyProperty.fireValueChangedEvent(change);
        }
    }

    private class ReadOnlyPropertyImpl extends ReadOnlySetPropertyBase<E> {

        @Override
        public ObservableSet<E> get() {
            return ReadOnlySetWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlySetWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlySetWrapper.this.getName();
        }

        @Override
        public ReadOnlyIntegerProperty sizeProperty() {
            return ReadOnlySetWrapper.this.sizeProperty();
        }

        @Override
        public ReadOnlyBooleanProperty emptyProperty() {
            return ReadOnlySetWrapper.this.emptyProperty();
        }
    }
}

package com.tungsten.fclcore.observable.property;

/**
 * Port of {@code ReadOnlyDoubleWrapper}.
 */
public class ReadOnlyDoubleWrapper extends SimpleDoubleProperty {

    private ReadOnlyPropertyImpl readOnlyProperty;

    public ReadOnlyDoubleWrapper() {
    }

    public ReadOnlyDoubleWrapper(double initialValue) {
        super(initialValue);
    }

    public ReadOnlyDoubleWrapper(Object bean, String name) {
        super(bean, name);
    }

    public ReadOnlyDoubleWrapper(Object bean, String name, double initialValue) {
        super(bean, name, initialValue);
    }

    public ReadOnlyDoubleProperty getReadOnlyProperty() {
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

    private class ReadOnlyPropertyImpl extends ReadOnlyDoublePropertyBase {

        @Override
        public double get() {
            return ReadOnlyDoubleWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlyDoubleWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlyDoubleWrapper.this.getName();
        }
    }
}

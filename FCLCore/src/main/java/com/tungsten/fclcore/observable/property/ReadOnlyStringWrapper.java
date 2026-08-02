package com.tungsten.fclcore.observable.property;

/**
 * Port of {@code fakefx.beans.property.ReadOnlyStringWrapper}.
 */
public class ReadOnlyStringWrapper extends SimpleStringProperty {

    private ReadOnlyPropertyImpl readOnlyProperty;

    public ReadOnlyStringWrapper() {
    }

    public ReadOnlyStringWrapper(String initialValue) {
        super(initialValue);
    }

    public ReadOnlyStringWrapper(Object bean, String name) {
        super(bean, name);
    }

    public ReadOnlyStringWrapper(Object bean, String name, String initialValue) {
        super(bean, name, initialValue);
    }

    public ReadOnlyStringProperty getReadOnlyProperty() {
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

    private class ReadOnlyPropertyImpl extends ReadOnlyStringPropertyBase {

        @Override
        public String get() {
            return ReadOnlyStringWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlyStringWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlyStringWrapper.this.getName();
        }
    }
}

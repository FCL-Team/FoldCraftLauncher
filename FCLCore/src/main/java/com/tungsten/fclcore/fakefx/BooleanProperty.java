package com.tungsten.fclcore.fakefx;

public abstract class BooleanProperty extends ReadOnlyBooleanProperty implements
        Property<Boolean>, WritableBooleanValue {

    public BooleanProperty() {
    }

    @Override
    public void setValue(Boolean v) {
        if (v == null) {
            set(false);
        } else {
            set(v.booleanValue());
        }
    }

    @Override
    public void bindBidirectional(Property<Boolean> other) {

    }

    @Override
    public void unbindBidirectional(Property<Boolean> other) {

    }

    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder(
                "BooleanProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if ((name != null) && (!name.equals(""))) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append(get()).append("]");
        return result.toString();
    }
}

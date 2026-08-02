package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.binding.Bindings;

/**
 * Minimal reimplementation of {@code BooleanProperty}.
 */
public abstract class BooleanProperty extends ReadOnlyBooleanProperty implements Property<Boolean> {

    public BooleanProperty() {
    }

    @Override
    public void setValue(Boolean v) {
        if (v == null) {
            // 与 JavaFX 一致：setValue(null) 记日志后置 false
            set(false);
        } else {
            set(v);
        }
    }

    public abstract void set(boolean value);

    @Override
    public void bindBidirectional(Property<Boolean> other) {
        Bindings.bindBidirectional(this, other);
    }

    @Override
    public void unbindBidirectional(Property<Boolean> other) {
        Bindings.unbindBidirectional(this, other);
    }

    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder("BooleanProperty [");
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

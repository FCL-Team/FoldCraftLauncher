package com.tungsten.fclcore.fakefx.beans.property;

import com.tungsten.fclcore.fakefx.beans.binding.ObjectExpression;

public abstract class ReadOnlyObjectProperty<T> extends ObjectExpression<T>
        implements ReadOnlyProperty<T> {

    /**
     * The constructor of {@code ReadOnlyObjectProperty}.
     */
    public ReadOnlyObjectProperty() {
    }

    /**
     * Returns a string representation of this {@code ReadOnlyObjectProperty} object.
     * @return a string representation of this {@code ReadOnlyObjectProperty} object.
     */
    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder(
                "ReadOnlyObjectProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if ((name != null) && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append(get()).append("]");
        return result.toString();
    }

}

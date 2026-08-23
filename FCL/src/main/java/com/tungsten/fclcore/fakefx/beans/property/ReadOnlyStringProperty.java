package com.tungsten.fclcore.fakefx.beans.property;

import com.tungsten.fclcore.fakefx.beans.binding.StringExpression;

public abstract class ReadOnlyStringProperty extends StringExpression implements
        ReadOnlyProperty<String> {

    /**
     * The constructor of {@code ReadOnlyStringProperty}.
     */
    public ReadOnlyStringProperty() {
    }

    /**
     * Returns a string representation of this {@code ReadOnlyStringProperty} object.
     * @return a string representation of this {@code ReadOnlyStringProperty} object.
     */
    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder(
                "ReadOnlyStringProperty [");
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

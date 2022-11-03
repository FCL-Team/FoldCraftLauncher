package com.tungsten.fclcore.fakefx.beans.property;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.WeakInvalidationListener;
import com.tungsten.fclcore.fakefx.beans.binding.IntegerExpression;

public abstract class ReadOnlyIntegerProperty extends IntegerExpression
        implements ReadOnlyProperty<Number> {

    /**
     * The constructor of {@code ReadOnlyIntegerProperty}.
     */
    public ReadOnlyIntegerProperty() {
    }


    /**
     * Returns a string representation of this {@code ReadOnlyIntegerProperty} object.
     * @return a string representation of this {@code ReadOnlyIntegerProperty} object.
     */
    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder(
                "ReadOnlyIntegerProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if ((name != null) && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append(get()).append("]");
        return result.toString();
    }

    public static <T extends Number> ReadOnlyIntegerProperty readOnlyIntegerProperty(final ReadOnlyProperty<T> property) {
        if (property == null) {
            throw new NullPointerException("Property cannot be null");
        }

        return property instanceof ReadOnlyIntegerProperty ? (ReadOnlyIntegerProperty) property:
           new ReadOnlyIntegerPropertyBase() {
            private boolean valid = true;
            private final InvalidationListener listener = observable -> {
                if (valid) {
                    valid = false;
                    fireValueChangedEvent();
                }
            };

            {
                property.addListener(new WeakInvalidationListener(listener));
            }

            @Override
            public int get() {
                valid = true;
                final T value = property.getValue();
                return value == null ? 0 : value.intValue();
            }

            @Override
            public Object getBean() {
                return null; // Virtual property, no bean
            }

            @Override
            public String getName() {
                return property.getName();
            }
        };
    }

    @Override
    public ReadOnlyObjectProperty<Integer> asObject() {
        return new ReadOnlyObjectPropertyBase<Integer>() {

            private boolean valid = true;
            private final InvalidationListener listener = observable -> {
                if (valid) {
                    valid = false;
                    fireValueChangedEvent();
                }
            };

            {
                ReadOnlyIntegerProperty.this.addListener(new WeakInvalidationListener(listener));
            }

            @Override
            public Object getBean() {
                return null; // Virtual property, does not exist on a bean
            }

            @Override
            public String getName() {
                return ReadOnlyIntegerProperty.this.getName();
            }

            @Override
            public Integer get() {
                valid = true;
                return ReadOnlyIntegerProperty.this.getValue();
            }
        };
    };

}

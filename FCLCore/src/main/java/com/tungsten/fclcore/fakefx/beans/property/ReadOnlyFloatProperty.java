package com.tungsten.fclcore.fakefx.beans.property;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.WeakInvalidationListener;
import com.tungsten.fclcore.fakefx.beans.binding.FloatExpression;

public abstract class ReadOnlyFloatProperty extends FloatExpression implements
        ReadOnlyProperty<Number> {

    /**
     * The constructor of {@code ReadOnlyFloatProperty}.
     */
    public ReadOnlyFloatProperty() {
    }

    /**
     * Returns a string representation of this {@code ReadOnlyFloatProperty} object.
     * @return a string representation of this {@code ReadOnlyFloatProperty} object.
     */
    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder(
                "ReadOnlyFloatProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if ((name != null) && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append(get()).append("]");
        return result.toString();
    }

    public static <T extends Number> ReadOnlyFloatProperty readOnlyFloatProperty(final ReadOnlyProperty<T> property) {
        if (property == null) {
            throw new NullPointerException("Property cannot be null");
        }

        return property instanceof ReadOnlyFloatProperty ? (ReadOnlyFloatProperty) property:
           new ReadOnlyFloatPropertyBase() {
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
            public float get() {
                valid = true;
                final T value = property.getValue();
                return value == null ? 0f : value.floatValue();
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
    public ReadOnlyObjectProperty<Float> asObject() {
        return new ReadOnlyObjectPropertyBase<Float>() {

            private boolean valid = true;
            private final InvalidationListener listener = observable -> {
                if (valid) {
                    valid = false;
                    fireValueChangedEvent();
                }
            };

            {
                ReadOnlyFloatProperty.this.addListener(new WeakInvalidationListener(listener));
            }

            @Override
            public Object getBean() {
                return null; // Virtual property, does not exist on a bean
            }

            @Override
            public String getName() {
                return ReadOnlyFloatProperty.this.getName();
            }

            @Override
            public Float get() {
                valid = true;
                return ReadOnlyFloatProperty.this.getValue();
            }
        };
    };

}

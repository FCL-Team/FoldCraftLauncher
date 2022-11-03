package com.tungsten.fclcore.fakefx.property;

import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyProperty;
import com.tungsten.fclcore.fakefx.reflect.ReflectUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class PropertyReference<T> {
    private String name;
    private Method getter;
    private Method setter;
    private Method propertyGetter;
    private Class<?> clazz;
    private Class<?> type;
    private boolean reflected = false;

    public PropertyReference(Class<?> clazz, String name) {
        if (name == null)
            throw new NullPointerException("Name must be specified");
        if (name.trim().length() == 0)
            throw new IllegalArgumentException("Name must be specified");
        if (clazz == null)
            throw new NullPointerException("Class must be specified");
        ReflectUtil.checkPackageAccess(clazz);
        this.name = name;
        this.clazz = clazz;
    }

    public boolean isWritable() {
        reflect();
        return setter != null;
    }

    public boolean isReadable() {
        reflect();
        return getter != null;
    }

    public boolean hasProperty() {
        reflect();
        return propertyGetter != null;
    }

    public String getName() {
        return name;
    }

    public Class<?> getContainingClass() {
        return clazz;
    }

    public Class<?> getType() {
        reflect();
        return type;
    }

    public void set(Object bean, T value) {
        if (!isWritable())
            throw new IllegalStateException(
                    "Cannot write to readonly property " + name);
        assert setter != null;
        try {
            MethodHelper.invoke(setter, bean, new Object[] {value});
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public T get(Object bean) {
        if (!isReadable())
            throw new IllegalStateException(
                    "Cannot read from unreadable property " + name);
        assert getter != null;
        try {
            return (T)MethodHelper.invoke(getter, bean, (Object[])null);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public ReadOnlyProperty<T> getProperty(Object bean) {
        if (!hasProperty())
            throw new IllegalStateException("Cannot get property " + name);
        assert propertyGetter != null;
        try {
            return (ReadOnlyProperty<T>)MethodHelper.invoke(propertyGetter, bean, (Object[])null);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return name;
    }

    private void reflect() {
        // If both the getter and setter are null then we have not reflected
        // on this property before
        if (!reflected) {
            reflected = true;
            try {
                // Since we use it in several places, construct the
                // first-letter-capitalized version of name
                final String properName = name.length() == 1 ?
                        name.substring(0, 1).toUpperCase() :
                        Character.toUpperCase(name.charAt(0))
                        + name.substring(1);

                // Now look for the getter. It will be named either
                // "get" + name with the first letter of name
                // capitalized, or it will be named "is" + name with
                // the first letter of the name capitalized. However it
                // is only named with "is" as a prefix if the type is
                // boolean.
                type = null;
                // first we check for getXXX
                String getterName = "get" + properName;
                try {
                    final Method m = clazz.getMethod(getterName);
                    if (Modifier.isPublic(m.getModifiers())) {
                        getter = m;
                    }
                } catch (NoSuchMethodException ex) {
                    // This is a legitimate error
                }

                // Then if it wasn't found we look for isXXX
                if (getter == null) {
                    getterName = "is" + properName;
                    try {
                        final Method m = clazz.getMethod(getterName);
                        if (Modifier.isPublic(m.getModifiers())) {
                            getter = m;
                        }
                    } catch (NoSuchMethodException ex) {
                        // This is a legitimate error
                    }
                }

                // Now attempt to look for the setter. It is simply
                // "set" + name with the first letter of name
                // capitalized.
                final String setterName = "set" + properName;

                // If we found the getter, we can get the type
                // and the setter easily.
                if (getter != null) {
                    type = getter.getReturnType();
                    try {
                        final Method m = clazz.getMethod(setterName, type);
                        if (Modifier.isPublic(m.getModifiers())) {
                            setter = m;
                        }
                    } catch (NoSuchMethodException ex) {
                        // This is a legitimate error
                    }
                } else { // no getter found
                    final Method[] methods = clazz.getMethods();
                    for (final Method m : methods) {
                        final Class<?>[] parameters = m.getParameterTypes();
                        if (setterName.equals(m.getName())
                                && (parameters.length == 1)
                                && Modifier.isPublic(m.getModifiers()))
                        {
                            setter = m;
                            type = parameters[0];
                            break;
                        }
                    }
                }

                // Now attempt to look for the property-getter.
                final String propertyGetterName = name + "Property";
                try {
                    final Method m = clazz.getMethod(propertyGetterName);
                    if (Modifier.isPublic(m.getModifiers())) {
                        propertyGetter = m;
                    } else
                        propertyGetter = null;
                } catch (NoSuchMethodException ex) {
                    // This is a legitimate error
                }
            } catch (RuntimeException e) {
                System.err.println("Failed to introspect property " + name);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PropertyReference)) {
            return false;
        }
        final PropertyReference<?> other = (PropertyReference<?>) obj;
        if (this.name != other.name
                && (this.name == null || !this.name.equals(other.name))) {
            return false;
        }
        if (this.clazz != other.clazz
                && (this.clazz == null || !this.clazz.equals(other.clazz))) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 97 * hash + (this.clazz != null ? this.clazz.hashCode() : 0);
        return hash;
    }
}

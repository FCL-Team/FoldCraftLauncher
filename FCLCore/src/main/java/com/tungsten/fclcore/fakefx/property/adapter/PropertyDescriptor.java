package com.tungsten.fclcore.fakefx.property.adapter;

import com.tungsten.fclcore.fakefx.beans.property.Property;
import com.tungsten.fclcore.fakefx.beans.property.adapter.ReadOnlyJavaBeanProperty;
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener;
import com.tungsten.fclcore.fakefx.beans.value.ObservableValue;
import com.tungsten.fclcore.fakefx.property.MethodHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 */
public class PropertyDescriptor extends ReadOnlyPropertyDescriptor {

    private static final String ADD_VETOABLE_LISTENER_METHOD_NAME = "addVetoableChangeListener";
    private static final String REMOVE_VETOABLE_LISTENER_METHOD_NAME = "removeVetoableChangeListener";
    private static final String ADD_PREFIX = "add";
    private static final String REMOVE_PREFIX = "remove";
    private static final String SUFFIX = "Listener";

    private static final int ADD_VETOABLE_LISTENER_TAKES_NAME = 1;
    private static final int REMOVE_VETOABLE_LISTENER_TAKES_NAME = 2;

    private final Method setter;
    private final Method addVetoListener;
    private final Method removeVetoListener;
    private final int flags;

    public Method getSetter() {return setter;}

    public PropertyDescriptor(String propertyName, Class<?> beanClass, Method getter, Method setter) {
        super(propertyName, beanClass, getter);
        this.setter = setter;

        Method tmpAddVetoListener = null;
        Method tmpRemoveVetoListener = null;
        int tmpFlags = 0;

        // reflect addVetoListenerMethod
        final String addMethodName = ADD_PREFIX + capitalizedName(name) + SUFFIX;
        try {
            tmpAddVetoListener = beanClass.getMethod(addMethodName, VetoableChangeListener.class);
        } catch (NoSuchMethodException e) {
            try {
                tmpAddVetoListener = beanClass.getMethod(ADD_VETOABLE_LISTENER_METHOD_NAME, String.class, VetoableChangeListener.class);
                tmpFlags |= ADD_VETOABLE_LISTENER_TAKES_NAME;
            } catch (NoSuchMethodException e1) {
                try {
                    tmpAddVetoListener = beanClass.getMethod(ADD_VETOABLE_LISTENER_METHOD_NAME, VetoableChangeListener.class);
                } catch (NoSuchMethodException e2) {
                    // ignore
                }
            }
        }

        // reflect removeVetoListenerMethod
        final String removeMethodName = REMOVE_PREFIX + capitalizedName(name) + SUFFIX;
        try {
            tmpRemoveVetoListener = beanClass.getMethod(removeMethodName, VetoableChangeListener.class);
        } catch (NoSuchMethodException e) {
            try {
                tmpRemoveVetoListener = beanClass.getMethod(REMOVE_VETOABLE_LISTENER_METHOD_NAME, String.class, VetoableChangeListener.class);
                tmpFlags |= REMOVE_VETOABLE_LISTENER_TAKES_NAME;
            } catch (NoSuchMethodException e1) {
                try {
                    tmpRemoveVetoListener = beanClass.getMethod(REMOVE_VETOABLE_LISTENER_METHOD_NAME, VetoableChangeListener.class);
                } catch (NoSuchMethodException e2) {
                    // ignore
                }
            }
        }

        addVetoListener = tmpAddVetoListener;
        removeVetoListener = tmpRemoveVetoListener;
        flags = tmpFlags;
    }

    @Override
    public void addListener(ReadOnlyListener listener) {
        super.addListener(listener);
        if (addVetoListener != null) {
            try {
                if ((flags & ADD_VETOABLE_LISTENER_TAKES_NAME) > 0) {
                    addVetoListener.invoke(listener.getBean(), name, listener);
                } else {
                    addVetoListener.invoke(listener.getBean(), listener);
                }
            } catch (IllegalAccessException e) {
                // ignore
            } catch (InvocationTargetException e) {
                // ignore
            }
        }
    }



    @Override
    public void removeListener(ReadOnlyListener listener) {
        super.removeListener(listener);
        if (removeVetoListener != null) {
            try {
                if ((flags & REMOVE_VETOABLE_LISTENER_TAKES_NAME) > 0) {
                    removeVetoListener.invoke(listener.getBean(), name, listener);
                } else {
                    removeVetoListener.invoke(listener.getBean(), listener);
                }
            } catch (IllegalAccessException e) {
                // ignore
            } catch (InvocationTargetException e) {
                // ignore
            }
        }
    }

    public class Listener<T> extends ReadOnlyListener<T> implements ChangeListener<T>, VetoableChangeListener {

        private boolean updating;

        public Listener(Object bean, ReadOnlyJavaBeanProperty<T> property) {
            super(bean, property);
        }

        @Override
        public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
            final ReadOnlyJavaBeanProperty<T> property = checkRef();
            if (property == null) {
                observable.removeListener(this);
            } else if (!updating) {
                updating = true;
                try {
                    MethodHelper.invoke(setter, bean, new Object[] {newValue});
                    property.fireValueChangedEvent();
                } catch (IllegalAccessException e) {
                    // ignore
                } catch (InvocationTargetException e) {
                    // ignore
                } finally {
                    updating = false;
                }
            }
        }


        @Override
        public void vetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
            if (bean.equals(propertyChangeEvent.getSource()) && name.equals(propertyChangeEvent.getPropertyName())) {
                final ReadOnlyJavaBeanProperty<T> property = checkRef();
                if ((property instanceof Property) && (((Property)property).isBound()) && !updating) {
                    throw new PropertyVetoException("A bound value cannot be set.", propertyChangeEvent);
                }
            }
        }
    }

}

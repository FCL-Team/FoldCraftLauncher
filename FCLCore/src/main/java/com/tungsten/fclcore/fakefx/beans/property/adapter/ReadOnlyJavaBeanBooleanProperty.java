package com.tungsten.fclcore.fakefx.beans.property.adapter;

import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyBooleanPropertyBase;
import com.tungsten.fclcore.fakefx.property.MethodHelper;
import com.tungsten.fclcore.fakefx.property.adapter.Disposer;
import com.tungsten.fclcore.fakefx.property.adapter.ReadOnlyPropertyDescriptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import java.security.AccessController;
import java.security.AccessControlContext;
import java.security.PrivilegedAction;

public final class ReadOnlyJavaBeanBooleanProperty extends ReadOnlyBooleanPropertyBase implements ReadOnlyJavaBeanProperty<Boolean> {

    private final ReadOnlyPropertyDescriptor descriptor;
    private final ReadOnlyPropertyDescriptor.ReadOnlyListener<Boolean> listener;

    @SuppressWarnings("removal")
    private final AccessControlContext acc = AccessController.getContext();

    ReadOnlyJavaBeanBooleanProperty(ReadOnlyPropertyDescriptor descriptor, Object bean) {
        this.descriptor = descriptor;
        this.listener = descriptor.new ReadOnlyListener<Boolean>(bean, this);
        descriptor.addListener(listener);
        Disposer.addRecord(this, new DescriptorListenerCleaner(descriptor, listener));
    }

    /**
     * {@inheritDoc}
     *
     * @throws UndeclaredThrowableException if calling the getter of the Java Bean
     * property throws an {@code IllegalAccessException} or an
     * {@code InvocationTargetException}.
     */
    @SuppressWarnings("removal")
    @Override
    public boolean get() {
        return AccessController.doPrivileged((PrivilegedAction<Boolean>) () -> {
            try {
                return (Boolean) MethodHelper.invoke(descriptor.getGetter(), getBean(), (Object[])null);
            } catch (IllegalAccessException e) {
                throw new UndeclaredThrowableException(e);
            } catch (InvocationTargetException e) {
                throw new UndeclaredThrowableException(e);
            }
        }, acc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getBean() {
        return listener.getBean();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return descriptor.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fireValueChangedEvent() {
        super.fireValueChangedEvent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        descriptor.removeListener(listener);
    }
}

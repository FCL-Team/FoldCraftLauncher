package com.tungsten.fclcore.fakefx.property.adapter;

import com.tungsten.fclcore.fakefx.beans.property.adapter.ReadOnlyJavaBeanObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.adapter.ReadOnlyJavaBeanObjectPropertyBuilder;

public final class JavaBeanQuickAccessor {

    private JavaBeanQuickAccessor() {
    }

    public static <T> ReadOnlyJavaBeanObjectProperty<T> createReadOnlyJavaBeanObjectProperty(Object bean, String name) throws NoSuchMethodException {
        return ReadOnlyJavaBeanObjectPropertyBuilder.<T>create().bean(bean).name(name).build();
    }

}

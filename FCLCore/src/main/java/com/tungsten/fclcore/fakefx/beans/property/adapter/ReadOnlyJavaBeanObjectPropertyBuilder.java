package com.tungsten.fclcore.fakefx.beans.property.adapter;

import com.tungsten.fclcore.fakefx.property.adapter.ReadOnlyJavaBeanPropertyBuilderHelper;
import com.tungsten.fclcore.fakefx.property.adapter.ReadOnlyPropertyDescriptor;

import java.lang.reflect.Method;

/**
 * A {@code ReadOnlyJavaBeanObjectPropertyBuilder} can be used to create
 * {@link ReadOnlyJavaBeanObjectProperty ReadOnlyJavaBeanObjectProperties}. To create
 * a {@code ReadOnlyJavaBeanObjectProperty} one first has to call {@link #create()}
 * to generate a builder, set the required properties, and then one can
 * call {@link #build()} to generate the property.
 * <p>
 * Not all properties of a builder have to specified, there are several
 * combinations possible. As a minimum the {@link #name(String)} of
 * the property and the {@link #bean(Object)} have to be specified.
 * If the name of the getter follows the conventions, this is sufficient.
 * Otherwise it is possible to specify an alternative name for the getter
 * ({@link #getter(String)}) or
 * the getter {@code Methods} directly ({@link #getter(Method)}).
 * <p>
 * All methods to change properties return a reference to this builder, to enable
 * method chaining.
 * <p>
 * If you have to generate adapters for the same property of several instances
 * of the same class, you can reuse a {@code ReadOnlyJavaBeanObjectPropertyBuilder}.
 * by switching the Java Bean instance (with {@link #bean(Object)} and
 * calling {@link #build()}.
 *
 * @see ReadOnlyJavaBeanObjectProperty
 *
 * @param <T> the type of the wrapped {@code Object}
 * @since JavaFX 2.1
 */
public final class ReadOnlyJavaBeanObjectPropertyBuilder<T> {

    private final ReadOnlyJavaBeanPropertyBuilderHelper helper = new ReadOnlyJavaBeanPropertyBuilderHelper();

    private ReadOnlyJavaBeanObjectPropertyBuilder() {}

    /**
     * Create a new instance of {@code ReadOnlyJavaBeanObjectPropertyBuilder}
     *
     * @param <T> the type of the wrapped {@code Object}
     * @return the new {@code ReadOnlyJavaBeanObjectPropertyBuilder}
     */
    public static <T> ReadOnlyJavaBeanObjectPropertyBuilder<T> create() {
        return new ReadOnlyJavaBeanObjectPropertyBuilder<T>();
    }

    /**
     * Generate a new {@link ReadOnlyJavaBeanObjectProperty} with the current settings.
     *
     * @return the new {@code ReadOnlyJavaBeanObjectProperty}
     * @throws NoSuchMethodException if the settings were not sufficient to find
     * the getter of the Java Bean property
     */
    public ReadOnlyJavaBeanObjectProperty<T> build() throws NoSuchMethodException {
        final ReadOnlyPropertyDescriptor descriptor = helper.getDescriptor();
        return new ReadOnlyJavaBeanObjectProperty<T>(descriptor, helper.getBean());
    }

    /**
     * Set the name of the property
     *
     * @param name the name of the property
     * @return a reference to this builder to enable method chaining
     */
    public ReadOnlyJavaBeanObjectPropertyBuilder<T> name(String name) {
        helper.name(name);
        return this;
    }

    /**
     * Set the Java Bean instance the adapter should connect to
     *
     * @param bean the Java Bean instance
     * @return a reference to this builder to enable method chaining
     */
    public ReadOnlyJavaBeanObjectPropertyBuilder<T> bean(Object bean) {
        helper.bean(bean);
        return this;
    }

    /**
     * Set the Java Bean class in which the getter should be searched.
     * This can be useful, if the builder should generate adapters for several
     * Java Beans of different types.
     *
     * @param beanClass the Java Bean class
     * @return a reference to this builder to enable method chaining
     */
    public ReadOnlyJavaBeanObjectPropertyBuilder<T> beanClass(Class<?> beanClass) {
        helper.beanClass(beanClass);
        return this;
    }

    /**
     * Set an alternative name for the getter. This can be omitted, if the
     * name of the getter follows Java Bean naming conventions.
     *
     * @param getter the alternative name of the getter
     * @return a reference to this builder to enable method chaining
     */
    public ReadOnlyJavaBeanObjectPropertyBuilder<T> getter(String getter) {
        helper.getterName(getter);
        return this;
    }

    /**
     * Set the getter method directly. This can be omitted, if the
     * name of the getter follows Java Bean naming conventions.
     *
     * @param getter the getter
     * @return a reference to this builder to enable method chaining
     */
    public ReadOnlyJavaBeanObjectPropertyBuilder<T> getter(Method getter) {
        helper.getter(getter);
        return this;
    }
}

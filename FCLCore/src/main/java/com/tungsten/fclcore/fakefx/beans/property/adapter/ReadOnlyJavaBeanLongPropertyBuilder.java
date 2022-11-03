package com.tungsten.fclcore.fakefx.beans.property.adapter;

import com.tungsten.fclcore.fakefx.property.adapter.ReadOnlyJavaBeanPropertyBuilderHelper;
import com.tungsten.fclcore.fakefx.property.adapter.ReadOnlyPropertyDescriptor;

import java.lang.reflect.Method;

/**
 * A {@code ReadOnlyJavaBeanLongPropertyBuilder} can be used to create
 * {@link ReadOnlyJavaBeanLongProperty ReadOnlyJavaBeanLongProperties}. To create
 * a {@code ReadOnlyJavaBeanLongProperty} one first has to call {@link #create()}
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
 * of the same class, you can reuse a {@code ReadOnlyJavaBeanLongPropertyBuilder}.
 * by switching the Java Bean instance (with {@link #bean(Object)} and
 * calling {@link #build()}.
 *
 * @see ReadOnlyJavaBeanLongProperty
 * @since JavaFX 2.1
 */
public final class ReadOnlyJavaBeanLongPropertyBuilder {

    private final ReadOnlyJavaBeanPropertyBuilderHelper helper = new ReadOnlyJavaBeanPropertyBuilderHelper();

    private ReadOnlyJavaBeanLongPropertyBuilder() {}

    /**
     * Create a new instance of {@code ReadOnlyJavaBeanLongPropertyBuilder}
     *
     * @return the new {@code ReadOnlyJavaBeanLongPropertyBuilder}
     */
    public static ReadOnlyJavaBeanLongPropertyBuilder create() {
        return new ReadOnlyJavaBeanLongPropertyBuilder();
    }

    /**
     * Generate a new {@link ReadOnlyJavaBeanLongProperty} with the current settings.
     *
     * @return the new {@code ReadOnlyJavaBeanLongProperty}
     * @throws NoSuchMethodException if the settings were not sufficient to find
     * the getter of the Java Bean property
     * @throws IllegalArgumentException if the Java Bean property is not of type
     * {@code long} or {@code Long}
     */
    public ReadOnlyJavaBeanLongProperty build() throws NoSuchMethodException {
        final ReadOnlyPropertyDescriptor descriptor = helper.getDescriptor();
        if (!long.class.equals(descriptor.getType()) && !Number.class.isAssignableFrom(descriptor.getType())) {
            throw new IllegalArgumentException("Not a long property");
        }
        return new ReadOnlyJavaBeanLongProperty(descriptor, helper.getBean());
    }

    /**
     * Set the name of the property
     *
     * @param name the name of the property
     * @return a reference to this builder to enable method chaining
     */
    public ReadOnlyJavaBeanLongPropertyBuilder name(String name) {
        helper.name(name);
        return this;
    }

    /**
     * Set the Java Bean instance the adapter should connect to
     *
     * @param bean the Java Bean instance
     * @return a reference to this builder to enable method chaining
     */
    public ReadOnlyJavaBeanLongPropertyBuilder bean(Object bean) {
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
    public ReadOnlyJavaBeanLongPropertyBuilder beanClass(Class<?> beanClass) {
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
    public ReadOnlyJavaBeanLongPropertyBuilder getter(String getter) {
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
    public ReadOnlyJavaBeanLongPropertyBuilder getter(Method getter) {
        helper.getter(getter);
        return this;
    }
}

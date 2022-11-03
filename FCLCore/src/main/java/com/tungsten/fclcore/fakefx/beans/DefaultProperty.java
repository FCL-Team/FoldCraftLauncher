package com.tungsten.fclcore.fakefx.beans;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies a property to which child elements will be added or set when an
 * explicit property is not given.
 *
 * @since JavaFX 2.0
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DefaultProperty {
    /**
     * The name of the default property.
     * @return the name of the property
     */
    public String value();
}

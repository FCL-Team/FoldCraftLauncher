package com.tungsten.fclcore.observable;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Minimal reimplementation of {@code NamedArg}.
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface NamedArg {

    String value();

    String defaultValue() default "";
}

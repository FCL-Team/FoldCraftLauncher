package com.tungsten.fclcore.util.gson;

/**
 * This exception gets thrown by implementations of {@link Validation#validate()} if you want to replace
 * the nullable JSON-parsed object which does not satisfy the constraint with null value.
 * @see Validation
 */
public final class TolerableValidationException extends Exception {

    public TolerableValidationException() {
    }
}

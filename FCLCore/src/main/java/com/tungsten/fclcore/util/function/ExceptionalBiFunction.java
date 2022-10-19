package com.tungsten.fclcore.util.function;

import static java.util.Objects.requireNonNull;

public interface ExceptionalBiFunction<T, U, R, E extends Exception> {
    R apply(T t, U u) throws E;

    default <V> ExceptionalBiFunction<T, U, V, ?> andThen(ExceptionalFunction<? super R, ? extends V, ?> after) {
        requireNonNull(after);
        return (t, u) -> after.apply(apply(t, u));
    }
}

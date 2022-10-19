package com.tungsten.fclcore.util.function;

import static java.util.Objects.requireNonNull;

public interface ExceptionalBiConsumer<T, U, E extends Exception> {
    void accept(T t, U u) throws E;

    default ExceptionalBiConsumer<T, U, ?> andThen(ExceptionalBiConsumer<? super T, ? super U, ?> after) {
        requireNonNull(after);

        return (l, r) -> {
            this.accept(l, r);
            after.accept(l, r);
        };
    }
}

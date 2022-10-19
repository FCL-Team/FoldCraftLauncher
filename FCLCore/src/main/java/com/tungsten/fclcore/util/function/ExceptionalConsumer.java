package com.tungsten.fclcore.util.function;

public interface ExceptionalConsumer<T, E extends Exception> {
    void accept(T t) throws E;

    static <T, E extends Exception> ExceptionalConsumer<T, E> fromRunnable(ExceptionalRunnable<E> runnable) {
        return new ExceptionalConsumer<T, E>() {
            @Override
            public void accept(T o) throws E {
                runnable.run();
            }

            @Override
            public String toString() {
                return runnable.toString();
            }
        };
    }

    static <T> ExceptionalConsumer<T, ?> empty() {
        return s -> {
        };
    }
}

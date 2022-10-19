package com.tungsten.fclcore.util.function;

public interface ExceptionalPredicate<T, E extends Exception> {
    boolean test(T t) throws E;
}

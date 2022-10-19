package com.tungsten.fclcore.util.function;

import java.util.concurrent.Callable;

public interface ExceptionalSupplier<R, E extends Exception> {
    R get() throws E;
    
    default Callable<R> toCallable() {
        return this::get;
    }
}

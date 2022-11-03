package com.tungsten.fclcore.util;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class InvocationDispatcher<ARG> implements Consumer<ARG> {

    public static <ARG> InvocationDispatcher<ARG> runOn(Executor executor, Consumer<ARG> action) {
        return new InvocationDispatcher<>(arg -> executor.execute(() -> {
            synchronized (action) {
                action.accept(arg.get());
            }
        }));
    }

    private Consumer<Supplier<ARG>> handler;

    private AtomicReference<Optional<ARG>> pendingArg = new AtomicReference<>();

    public InvocationDispatcher(Consumer<Supplier<ARG>> handler) {
        this.handler = handler;
    }

    @Override
    public void accept(ARG arg) {
        if (pendingArg.getAndSet(Optional.ofNullable(arg)) == null) {
            handler.accept(() -> pendingArg.getAndSet(null).orElse(null));
        }
    }
}
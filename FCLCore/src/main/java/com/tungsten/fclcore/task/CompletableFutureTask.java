package com.tungsten.fclcore.task;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public abstract class CompletableFutureTask<T> extends Task<T> {

    @Override
    public void execute() throws Exception {
    }

    public abstract CompletableFuture<T> getFuture(TaskCompletableFuture executor);

    protected static Throwable resolveException(Throwable e) {
        if (e instanceof ExecutionException || e instanceof CompletionException)
            return resolveException(e.getCause());
        else
            return e;
    }

    public static class CustomException extends RuntimeException {}

    protected static CompletableFuture<Void> breakable(CompletableFuture<?> future) {
        return future.thenApplyAsync(unused1 -> (Void) null).exceptionally(throwable -> {
            if (resolveException(throwable) instanceof CustomException) return null;
            else throw new CompletionException(throwable);
        });
    }

}

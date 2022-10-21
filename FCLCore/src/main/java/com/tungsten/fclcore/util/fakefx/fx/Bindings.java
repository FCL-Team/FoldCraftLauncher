package com.tungsten.fclcore.util.fakefx.fx;

import java.util.concurrent.Callable;

public final class Bindings {

    private Bindings() {
    }

    public static <T> ObjectBinding<T> createObjectBinding(final Callable<T> func, final Observable... dependencies) {
        return new ObjectBinding<T>() {
            {
                bind(dependencies);
            }

            @Override
            protected T computeValue() {
                try {
                    return func.call();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void dispose() {
                super.unbind(dependencies);
            }
        };
    }
}
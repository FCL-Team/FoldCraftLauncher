package org.lwjgl.glfw;

import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.system.Callback;

import javax.annotation.Nullable;

public abstract class FCLInjectorCallback extends Callback implements FCLInjectorCallbackI {

    public static FCLInjectorCallback create(long functionPointer) {
        FCLInjectorCallbackI instance = Callback.get(functionPointer);
        return instance instanceof FCLInjectorCallback
                ? (FCLInjectorCallback)instance
                : new Container(functionPointer, instance);
    }

    /** Like {@link #create(long) create}, but returns {@code null} if {@code functionPointer} is {@code NULL}. */
    @Nullable
    public static FCLInjectorCallback createSafe(long functionPointer) {
        return functionPointer == NULL ? null : create(functionPointer);
    }

    /** Creates a {@code GLFWCursorEnterCallback} instance that delegates to the specified {@code GLFWCursorEnterCallbackI} instance. */
    public static FCLInjectorCallback create(FCLInjectorCallbackI instance) {
        return instance instanceof FCLInjectorCallback
                ? (FCLInjectorCallback)instance
                : new Container(instance.address(), instance);
    }

    protected FCLInjectorCallback() {
        super(CIF);
    }

    FCLInjectorCallback(long functionPointer) {
        super(functionPointer);
    }

    private static final class Container extends FCLInjectorCallback {

        private final FCLInjectorCallbackI delegate;

        Container(long functionPointer, FCLInjectorCallbackI delegate) {
            super(functionPointer);
            this.delegate = delegate;
        }

        @Override
        public void invoke() {
            delegate.invoke();
        }

    }

}
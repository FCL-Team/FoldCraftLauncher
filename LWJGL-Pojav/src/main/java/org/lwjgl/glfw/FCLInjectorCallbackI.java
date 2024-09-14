package org.lwjgl.glfw;

import static org.lwjgl.system.APIUtil.apiCreateCIF;
import static org.lwjgl.system.libffi.LibFFI.FFI_DEFAULT_ABI;
import static org.lwjgl.system.libffi.LibFFI.ffi_type_pointer;
import static org.lwjgl.system.libffi.LibFFI.ffi_type_uint32;
import static org.lwjgl.system.libffi.LibFFI.ffi_type_void;

import org.lwjgl.system.CallbackI;
import org.lwjgl.system.NativeType;
import org.lwjgl.system.libffi.FFICIF;

@FunctionalInterface
    @NativeType("FCLinjectorfun")
    public interface FCLInjectorCallbackI extends CallbackI {

        FFICIF CIF = apiCreateCIF(
                FFI_DEFAULT_ABI,
                ffi_type_void,
                ffi_type_pointer, ffi_type_uint32
        );

        @Override
        default FFICIF getCallInterface() { return CIF; }

        @Override
        default void callback(long ret, long args) {
            invoke();
        }

        void invoke();

    }
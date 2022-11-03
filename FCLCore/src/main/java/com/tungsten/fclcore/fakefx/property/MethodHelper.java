package com.tungsten.fclcore.fakefx.property;

import com.tungsten.fclcore.fakefx.reflect.MethodUtil;
import com.tungsten.fclcore.fakefx.reflect.ReflectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Utility class to wrap method invocation.
 */
public class MethodHelper {
    @SuppressWarnings("removal")
    private static final boolean logAccessErrors
            = AccessController.doPrivileged((PrivilegedAction<Boolean>) ()
                    -> Boolean.getBoolean("sun.reflect.debugModuleAccessChecks"));

    public static Object invoke(Method m, Object obj, Object[] params)
            throws InvocationTargetException, IllegalAccessException {

        final Class<?> clazz = m.getDeclaringClass();
        return MethodUtil.invoke(m, obj, params);
    }

    // Utility class, do not instantiate
    private MethodHelper() {
    }

}

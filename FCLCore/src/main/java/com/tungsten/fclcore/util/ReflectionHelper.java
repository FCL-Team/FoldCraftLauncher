package com.tungsten.fclcore.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Predicate;

public final class ReflectionHelper {
    private ReflectionHelper() {
    }

    private static Method accessible0;

    static {
        try {
            accessible0 = AccessibleObject.class.getDeclaredMethod("setAccessible0", boolean.class);
            accessible0.setAccessible(true);
        } catch (Throwable ignored) {
        }
    }

    public static void setAccessible(AccessibleObject obj) throws InvocationTargetException, IllegalAccessException {
        accessible0.invoke(obj, true);
    }

    /**
     * Get caller, this method is caller sensitive.
     *
     * @param packageFilter returns false if we consider the given package is internal calls, not the caller
     * @return the caller, method name, source file, line number
     */
    public static StackTraceElement getCaller(Predicate<String> packageFilter) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        // element[0] is Thread.currentThread().getStackTrace()
        // element[1] is ReflectionHelper.getCaller(packageFilter)
        // so element[2] is caller of this method.
        StackTraceElement caller = elements[2];
        for (int i = 3; i < elements.length; ++i) {
            if (packageFilter.test(StringUtils.substringBeforeLast(elements[i].getClassName(), '.')) &&
                    !caller.getClassName().equals(elements[i].getClassName()))
                return elements[i];
        }
        return caller;
    }
}

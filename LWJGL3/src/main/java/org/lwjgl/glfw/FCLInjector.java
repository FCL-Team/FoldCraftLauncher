package org.lwjgl.glfw;

import org.lwjgl.system.*;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.lwjgl.system.APIUtil.apiGetFunctionAddress;
import static org.lwjgl.system.APIUtil.apiLog;

/**
 * By Tungsten
 * This class is for Fold Craft Launcher.
 */
public class FCLInjector {

    public static final long GetInjectorMode = apiGetFunctionAddress(GLFW.GLFW, "glfwGetInjectorMode");
    public static final long SetInjectorMode = apiGetFunctionAddress(GLFW.GLFW, "glfwSetInjectorMode");
    public static final long SetHitResultType = apiGetFunctionAddress(GLFW.GLFW, "glfwSetHitResultType");

    private static boolean get = false;
    private static ClassLoader classLoader;

    private static final String HIT_RESULT_TYPE_UNKNOWN      = "UNKNOWN";
    private static final String HIT_RESULT_TYPE_MISS         = "MISS";
    private static final String HIT_RESULT_TYPE_BLOCK        = "BLOCK";
    private static final String HIT_RESULT_TYPE_ENTITY       = "ENTITY";

    private static final int INJECTOR_MODE_ENABLE            = 1;
    private static final int INJECTOR_MODE_DISABLE           = 0;

    private static boolean highVersion = false;
    @Nullable
    private static String param0 = null;
    @Nullable
    private static String param1 = null;
    @Nullable
    private static String param2 = null;
    @Nullable
    private static String param3 = null;

    public static void setClassLoader(ClassLoader classLoader) {
        String prop = System.getProperty("fcl.injector");
        if (!get && prop != null && !prop.isEmpty()) {
            FCLInjector.classLoader = classLoader;
            String[] props = prop.split(":");
            if (props.length == 5 && (props[0].equals("true") || props[0].equals("false"))) {
                boolean highVersion = Boolean.parseBoolean(props[0]);
                String param0 = props[1];
                String param1 = props[2];
                String param2 = props[3];
                String param3 = props[4];
                setup(highVersion, param0, param1, param2, param3);
            }
        }
    }

    public static void setup(boolean highVersion, String param0, String param1, String param2, String param3) {
        FCLInjector.highVersion = highVersion;
        FCLInjector.param0 = param0;
        FCLInjector.param1 = param1;
        FCLInjector.param2 = param2;
        FCLInjector.param3 = param3;
        get = true;
        new Thread(() -> {
            while (true) {
                if (nglfwGetInjectorMode() == INJECTOR_MODE_ENABLE) {
                    getHitResultType();
                    nglfwSetInjectorMode(INJECTOR_MODE_DISABLE);
                }
            }
        }).start();
    }

    public static int nglfwGetInjectorMode() {
        return JNI.invokeI(GetInjectorMode);
    }

    public static void nglfwSetInjectorMode(int mode) {
        JNI.invokeV(mode, SetInjectorMode);
    }

    public static void nglfwSetHitResultType(String type) {
        int typeInt;
        switch (type) {
            case HIT_RESULT_TYPE_MISS:
                typeInt = 1;
                break;
            case HIT_RESULT_TYPE_BLOCK:
                typeInt = 2;
                break;
            case HIT_RESULT_TYPE_ENTITY:
                typeInt = 3;
                break;
            default:
                typeInt = 0;
                break;
        };
        JNI.invokeV(typeInt, FCLInjector.SetHitResultType);
    }

    public static void getHitResultType() {
        if (!get) {
            nglfwSetHitResultType(HIT_RESULT_TYPE_UNKNOWN);
            apiLog("FCL Injector not initialized!");
            return;
        }
        if (param0 != null && param1 != null && param2 != null && param3 != null) {
            Object type = null;
            try {
                Class<?> minecraftClass = Class.forName(param0, true, classLoader);
                Method method = minecraftClass.getDeclaredMethod(param1);
                method.setAccessible(true);
                Object minecraft = method.invoke(null);
                Field targetField = minecraftClass.getDeclaredField(param2);
                targetField.setAccessible(true);
                Object target = targetField.get(minecraft);
                if (target != null) {
                    if (!highVersion) {
                        Field typeField = target.getClass().getDeclaredField(param3);
                        typeField.setAccessible(true);
                        type = typeField.get(target);
                    } else {
                        Method typeMethod = target.getClass().getDeclaredMethod(param3);
                        typeMethod.setAccessible(true);
                        type = typeMethod.invoke(target);
                    }
                }
            } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                apiLog(e.getMessage());
            }
            if (type != null && (type.toString().equals(HIT_RESULT_TYPE_MISS) || type.toString().equals(HIT_RESULT_TYPE_BLOCK) || type.toString().equals(HIT_RESULT_TYPE_ENTITY))) {
                nglfwSetHitResultType(type.toString());
            } else {
                nglfwSetHitResultType(HIT_RESULT_TYPE_UNKNOWN);
            }
        }
    }

}

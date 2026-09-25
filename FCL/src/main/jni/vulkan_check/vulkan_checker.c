//
// Vulkan 设备能力检测：dlopen 系统 libvulkan 或经 liblinkerhook 加载 Turnip 驱动，
// 创建实例并枚举物理设备，读取 apiVersion、设备扩展与特性，回传给 Kotlin 层评估
//

#include <jni.h>
#include <dlfcn.h>
#include <string.h>
#include <stdlib.h>
#include <stdarg.h>
#include <stdbool.h>
#include <vulkan/vulkan.h>
#include <android/api-level.h>

#include <androidnsbypass/nsbypass_t.h>
#include <androidnsbypass/nsbypass.h>

#ifdef ADRENO_POSSIBLE
#include <EGL/egl.h>
#include <GLES2/gl2.h>
#endif

static jobject g_logCallback = NULL;
static jmethodID g_logMethod = NULL;

static void vulkan_log(JNIEnv *env, const char *level, const char *fmt, ...) {
    if (!g_logCallback || !g_logMethod || !env) return;

    char buffer[1024];
    va_list args;
    va_start(args, fmt);
    vsnprintf(buffer, sizeof(buffer), fmt, args);
    va_end(args);

    jstring jLevel = (*env)->NewStringUTF(env, level);
    jstring jMsg = (*env)->NewStringUTF(env, buffer);

    (*env)->CallVoidMethod(env, g_logCallback, g_logMethod, jLevel, jMsg);

    if ((*env)->ExceptionCheck(env)) {
        (*env)->ExceptionClear(env);
    }

    (*env)->DeleteLocalRef(env, jLevel);
    (*env)->DeleteLocalRef(env, jMsg);
}

#define LOG_I(...) vulkan_log(env, "INFO", __VA_ARGS__)
#define LOG_W(...) vulkan_log(env, "WARN", __VA_ARGS__)
#define LOG_E(...) vulkan_log(env, "ERROR", __VA_ARGS__)

/* apiVersion 的 patch 字段宽 12 位；较新 Vulkan 头文件中名为 VK_MAX_PATCH_VERSION */
#define FCL_MAX_PATCH_VERSION 4095

#if defined(__aarch64__) || defined(__x86_64__)
#define VULKAN_LOADER_PATH "/system/lib64/libvulkan.so"
#elif defined(__arm__) || defined(__i386__)
#define VULKAN_LOADER_PATH "/system/lib/libvulkan.so"
#endif

JNIEXPORT void JNICALL
Java_com_mio_device_VulkanChecker_nativeSetLogCallback(
        JNIEnv *env,
        jclass clazz,
        jobject callback
) {
    (void) clazz;

    if (g_logCallback != NULL) {
        (*env)->DeleteGlobalRef(env, g_logCallback);
        g_logCallback = NULL;
        g_logMethod = NULL;
    }

    if (callback != NULL) {
        g_logCallback = (*env)->NewGlobalRef(env, callback);
        jclass cbClass = (*env)->GetObjectClass(env, callback);
        g_logMethod = (*env)->GetMethodID(env, cbClass, "log", "(Ljava/lang/String;Ljava/lang/String;)V");
        (*env)->DeleteLocalRef(env, cbClass);
    }
}

#ifdef ADRENO_POSSIBLE

/* 与 egl_bridge.c 的 checkAdrenoGraphics 一致：建 GLES3 上下文读取 GL_VENDOR/GL_RENDERER */
static bool check_adreno_graphics() {
    EGLDisplay eglDisplay = eglGetDisplay(EGL_DEFAULT_DISPLAY);
    if (eglDisplay == EGL_NO_DISPLAY || eglInitialize(eglDisplay, NULL, NULL) != EGL_TRUE)
        return false;

    EGLint egl_attributes[] = {
            EGL_BLUE_SIZE, 8, EGL_GREEN_SIZE, 8, EGL_RED_SIZE, 8,
            EGL_ALPHA_SIZE, 8, EGL_DEPTH_SIZE, 24, EGL_SURFACE_TYPE, EGL_PBUFFER_BIT,
            EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT, EGL_NONE
    };

    EGLint num_configs = 0;
    if (eglChooseConfig(eglDisplay, egl_attributes, NULL, 0, &num_configs) != EGL_TRUE || num_configs == 0) {
        eglTerminate(eglDisplay);
        return false;
    }

    EGLConfig eglConfig;
    eglChooseConfig(eglDisplay, egl_attributes, &eglConfig, 1, &num_configs);

    const EGLint egl_context_attributes[] = {EGL_CONTEXT_CLIENT_VERSION, 3, EGL_NONE};
    EGLContext context = eglCreateContext(eglDisplay, eglConfig, EGL_NO_CONTEXT, egl_context_attributes);
    if (context == EGL_NO_CONTEXT) {
        eglTerminate(eglDisplay);
        return false;
    }

    if (eglMakeCurrent(eglDisplay, EGL_NO_SURFACE, EGL_NO_SURFACE, context) != EGL_TRUE) {
        eglDestroyContext(eglDisplay, context);
        eglTerminate(eglDisplay);
        return false;
    }

    const char *vendor = (const char *) glGetString(GL_VENDOR);
    const char *renderer = (const char *) glGetString(GL_RENDERER);

    bool is_adreno = (vendor && renderer && strcmp(vendor, "Qualcomm") == 0 && strstr(renderer, "Adreno") != NULL);

    eglMakeCurrent(eglDisplay, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT);
    eglDestroyContext(eglDisplay, context);
    eglTerminate(eglDisplay);

    return is_adreno;
}

/**
 * 与 egl_bridge.c 的 loadTurnipVulkan 同链路：创建隔离命名空间并预载 liblinkerhook，
 * 由 hook 拦截系统 libvulkan 的驱动加载转至 DRIVER_PATH 下的 Turnip；
 * DRIVER_PATH 环境变量仅检测期间生效，结束后恢复原值
 */
static void *load_turnip_vulkan(const char *driver_path, const char *cache_dir) {
    if (!check_adreno_graphics())
        return NULL;

    struct android_namespace_t *ns = private_create_namespace(
            "vulkan-check-loader-NS",
            NULL,
            NULL,
            ANDROID_NAMESPACE_TYPE_SHARED_ISOLATED,
            NULL,
            NULL,
            __builtin_return_address(0)
    );
    if (!ns) return NULL;

    // 先加载 hook，使其符号优先进入符号表从而拦截 android_dlopen_ext
    linker_ns_dlopen("liblinkerhook.so", RTLD_LOCAL | RTLD_NOW, ns);
    // 授予命名空间访问系统库的权限
    private_link_namespaces_all_libs(ns, get_escape_namespace());

    const char *old_path = getenv("DRIVER_PATH");
    char *old_copy = old_path ? strdup(old_path) : NULL;
    setenv("DRIVER_PATH", driver_path, 1);
    void *handle = linker_ns_dlopen_unique(VULKAN_LOADER_PATH, cache_dir, RTLD_LOCAL | RTLD_NOW, ns);
    if (old_copy) {
        setenv("DRIVER_PATH", old_copy, 1);
        free(old_copy);
    } else {
        unsetenv("DRIVER_PATH");
    }
    return handle;
}

#endif

#define LOAD_VK_FUNC(name) PFN_##name p##name = (PFN_##name)dlsym(vulkan_handle, #name)

JNIEXPORT jobject JNICALL
Java_com_mio_device_VulkanChecker_nativeCheckVulkan(
        JNIEnv *env,
        jclass clazz,
        jboolean jUseTurnip,
        jstring jDriverPath,
        jstring jCacheDir
) {
    (void) clazz;

    const char *driverPath = jDriverPath ? (*env)->GetStringUTFChars(env, jDriverPath, NULL) : NULL;
    const char *cacheDir = jCacheDir ? (*env)->GetStringUTFChars(env, jCacheDir, NULL) : NULL;

    void *vulkan_handle = NULL;
    bool customDriver = false;
    if (jUseTurnip == JNI_TRUE && driverPath && cacheDir) {
#ifdef ADRENO_POSSIBLE
        if (android_get_device_api_level() >= 28) {
            vulkan_handle = load_turnip_vulkan(driverPath, cacheDir);
            customDriver = vulkan_handle != NULL;
        }
#endif
    }
    if (!vulkan_handle) {
        vulkan_handle = dlopen("libvulkan.so", RTLD_NOW | RTLD_LOCAL);
    }

    if (driverPath) (*env)->ReleaseStringUTFChars(env, jDriverPath, driverPath);
    if (cacheDir) (*env)->ReleaseStringUTFChars(env, jCacheDir, cacheDir);

    if (!vulkan_handle) {
        LOG_E("Failed to load Vulkan library.");
        return NULL;
    }

    LOG_I("Vulkan library loaded: %s", customDriver ? "custom driver (Turnip)" : "system loader");
    if (jUseTurnip == JNI_TRUE && !customDriver) {
        LOG_W("Custom driver was requested but failed to load; using the system loader instead.");
    }

    LOAD_VK_FUNC(vkGetInstanceProcAddr);
    LOAD_VK_FUNC(vkEnumerateInstanceVersion);
    LOAD_VK_FUNC(vkCreateInstance);
    LOAD_VK_FUNC(vkDestroyInstance);
    LOAD_VK_FUNC(vkEnumeratePhysicalDevices);
    LOAD_VK_FUNC(vkGetPhysicalDeviceFeatures);
    LOAD_VK_FUNC(vkEnumerateDeviceExtensionProperties);
    LOAD_VK_FUNC(vkGetPhysicalDeviceFeatures2);
    LOAD_VK_FUNC(vkGetPhysicalDeviceProperties);
    LOAD_VK_FUNC(vkGetPhysicalDeviceProperties2);

    // 若驱动仅暴露 KHR 别名，尝试二次加载
    if (!pvkGetPhysicalDeviceFeatures2) {
        pvkGetPhysicalDeviceFeatures2 = (PFN_vkGetPhysicalDeviceFeatures2)
                dlsym(vulkan_handle, "vkGetPhysicalDeviceFeatures2KHR");
    }
    if (!pvkGetPhysicalDeviceProperties2) {
        pvkGetPhysicalDeviceProperties2 = (PFN_vkGetPhysicalDeviceProperties2)
                dlsym(vulkan_handle, "vkGetPhysicalDeviceProperties2KHR");
    }

    if (!pvkCreateInstance || !pvkDestroyInstance || !pvkEnumeratePhysicalDevices ||
        !pvkEnumerateDeviceExtensionProperties || !pvkGetPhysicalDeviceProperties) {
        LOG_E("Essential Vulkan functions missing, aborting.");
        dlclose(vulkan_handle);
        return NULL;
    }

    // 查询实例版本
    uint32_t instanceApiVersion = VK_API_VERSION_1_0;
    if (!pvkEnumerateInstanceVersion && pvkGetInstanceProcAddr) {
        // 部分加载器不导出该符号，规范要求经 vkGetInstanceProcAddr 解析实例级入口（实例为 NULL）
        pvkEnumerateInstanceVersion = (PFN_vkEnumerateInstanceVersion)
                pvkGetInstanceProcAddr(NULL, "vkEnumerateInstanceVersion");
    }
    if (pvkEnumerateInstanceVersion) {
        if (pvkEnumerateInstanceVersion(&instanceApiVersion) == VK_SUCCESS) {
            LOG_I("Instance reports Vulkan %u.%u.%u",
                  (unsigned int) VK_API_VERSION_MAJOR(instanceApiVersion),
                  (unsigned int) VK_API_VERSION_MINOR(instanceApiVersion),
                  (unsigned int) VK_API_VERSION_PATCH(instanceApiVersion));
        } else {
            LOG_W("vkEnumerateInstanceVersion failed, fallback to 1.0");
        }
    } else {
        LOG_I("vkEnumerateInstanceVersion unavailable, assume Vulkan 1.0");
    }

    // 创建 VkInstance
    VkApplicationInfo appInfo = {
            .sType = VK_STRUCTURE_TYPE_APPLICATION_INFO,
            .pApplicationName = "FoldCraftLauncher",
            .applicationVersion = VK_MAKE_VERSION(1, 0, 0),
            .apiVersion = VK_MAKE_VERSION(
                    VK_API_VERSION_MAJOR(instanceApiVersion),
                    VK_API_VERSION_MINOR(instanceApiVersion),
                    FCL_MAX_PATCH_VERSION)
    };
    VkInstanceCreateInfo createInfo = {
            .sType = VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO,
            .pApplicationInfo = &appInfo
    };

    VkInstance instance = VK_NULL_HANDLE;
    VkResult vkRes = pvkCreateInstance(&createInfo, NULL, &instance);
    if (vkRes != VK_SUCCESS) {
        LOG_I("Probe with max patch version failed (result=%d), fallback to reported instance version.", vkRes);
        appInfo.apiVersion = instanceApiVersion;
        vkRes = pvkCreateInstance(&createInfo, NULL, &instance);
        if (vkRes != VK_SUCCESS) {
            LOG_E("vkCreateInstance failed, result=%d", vkRes);
            dlclose(vulkan_handle);
            return NULL;
        }
    }

    // 实例创建后经 vkGetInstanceProcAddr 重新解析 1.1+ 入口（部分加载器不经 dlsym 导出）
    if (pvkGetInstanceProcAddr) {
        if (!pvkGetPhysicalDeviceFeatures2) {
            pvkGetPhysicalDeviceFeatures2 = (PFN_vkGetPhysicalDeviceFeatures2)
                    pvkGetInstanceProcAddr(instance, "vkGetPhysicalDeviceFeatures2");
        }
        if (!pvkGetPhysicalDeviceProperties2) {
            pvkGetPhysicalDeviceProperties2 = (PFN_vkGetPhysicalDeviceProperties2)
                    pvkGetInstanceProcAddr(instance, "vkGetPhysicalDeviceProperties2");
        }
        if (!pvkGetPhysicalDeviceFeatures2) {
            pvkGetPhysicalDeviceFeatures2 = (PFN_vkGetPhysicalDeviceFeatures2)
                    pvkGetInstanceProcAddr(instance, "vkGetPhysicalDeviceFeatures2KHR");
        }
        if (!pvkGetPhysicalDeviceProperties2) {
            pvkGetPhysicalDeviceProperties2 = (PFN_vkGetPhysicalDeviceProperties2)
                    pvkGetInstanceProcAddr(instance, "vkGetPhysicalDeviceProperties2KHR");
        }
    }

    // 枚举物理设备
    uint32_t deviceCount = 0;
    vkRes = pvkEnumeratePhysicalDevices(instance, &deviceCount, NULL);
    if (vkRes != VK_SUCCESS || deviceCount == 0) {
        LOG_E("No Vulkan physical device found (result=%d, count=%u)", vkRes, deviceCount);
        pvkDestroyInstance(instance, NULL);
        dlclose(vulkan_handle);
        return NULL;
    }

    VkPhysicalDevice *devices = (VkPhysicalDevice *) malloc(sizeof(VkPhysicalDevice) * deviceCount);
    if (!devices) {
        LOG_E("malloc failed for device array");
        pvkDestroyInstance(instance, NULL);
        dlclose(vulkan_handle);
        return NULL;
    }

    vkRes = pvkEnumeratePhysicalDevices(instance, &deviceCount, devices);
    if (vkRes != VK_SUCCESS) {
        LOG_E("vkEnumeratePhysicalDevices (second pass) failed, result=%d", vkRes);
        free(devices);
        pvkDestroyInstance(instance, NULL);
        dlclose(vulkan_handle);
        return NULL;
    }

    /* 设备可能有多个（软件实现、不同后端等），逐个查询 apiVersion 并取最高者，
     * 避免盲取第一个设备读到低版本实现 */
    VkPhysicalDevice physicalDevice = VK_NULL_HANDLE;
    uint32_t deviceApiVersion = VK_API_VERSION_1_0;
    const char *versionVia = "vkGetPhysicalDeviceProperties";
    for (uint32_t i = 0; i < deviceCount; i++) {
        uint32_t apiVersion = VK_API_VERSION_1_0;
        const char *via = "vkGetPhysicalDeviceProperties";
        if (pvkGetPhysicalDeviceProperties2) {
            VkPhysicalDeviceProperties2 props2 = {
                    .sType = VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_PROPERTIES_2,
                    .pNext = NULL
            };
            pvkGetPhysicalDeviceProperties2(devices[i], &props2);
            apiVersion = props2.properties.apiVersion;
            via = "vkGetPhysicalDeviceProperties2";
        } else {
            VkPhysicalDeviceProperties props;
            pvkGetPhysicalDeviceProperties(devices[i], &props);
            apiVersion = props.apiVersion;
        }

        LOG_I("Physical device %u reports Vulkan %u.%u.%u (via %s)",
              (unsigned int) i,
              (unsigned int) VK_API_VERSION_MAJOR(apiVersion),
              (unsigned int) VK_API_VERSION_MINOR(apiVersion),
              (unsigned int) VK_API_VERSION_PATCH(apiVersion),
              via);

        if (physicalDevice == VK_NULL_HANDLE || apiVersion > deviceApiVersion) {
            physicalDevice = devices[i];
            deviceApiVersion = apiVersion;
            versionVia = via;
        }
    }
    free(devices);
    LOG_I("Found %u physical device(s), inspecting the one with the highest api version (via %s).",
          (unsigned int) deviceCount, versionVia);

    // 部分驱动仅在实例版本中携带真实补丁号，设备 apiVersion 的 patch 恒为 0
    if (VK_API_VERSION_MAJOR(deviceApiVersion) == VK_API_VERSION_MAJOR(instanceApiVersion) &&
        VK_API_VERSION_MINOR(deviceApiVersion) == VK_API_VERSION_MINOR(instanceApiVersion) &&
        VK_API_VERSION_PATCH(instanceApiVersion) > VK_API_VERSION_PATCH(deviceApiVersion)) {
        LOG_I("Device patch version promoted from %u to %u (instance reports the higher patch).",
              (unsigned int) VK_API_VERSION_PATCH(deviceApiVersion),
              (unsigned int) VK_API_VERSION_PATCH(instanceApiVersion));
        deviceApiVersion = instanceApiVersion;
    }

    // 枚举设备扩展
    uint32_t extCount = 0;
    vkRes = pvkEnumerateDeviceExtensionProperties(physicalDevice, NULL, &extCount, NULL);
    if (vkRes != VK_SUCCESS) {
        LOG_E("vkEnumerateDeviceExtensionProperties (count) failed, result=%d", vkRes);
        pvkDestroyInstance(instance, NULL);
        dlclose(vulkan_handle);
        return NULL;
    }

    VkExtensionProperties *exts = (VkExtensionProperties *) malloc(sizeof(VkExtensionProperties) * extCount);
    if (!exts) {
        LOG_E("malloc failed for extension array");
        pvkDestroyInstance(instance, NULL);
        dlclose(vulkan_handle);
        return NULL;
    }

    vkRes = pvkEnumerateDeviceExtensionProperties(physicalDevice, NULL, &extCount, exts);
    if (vkRes != VK_SUCCESS) {
        LOG_E("vkEnumerateDeviceExtensionProperties (data) failed, result=%d", vkRes);
        free(exts);
        pvkDestroyInstance(instance, NULL);
        dlclose(vulkan_handle);
        return NULL;
    }

    jclass listClass = (*env)->FindClass(env, "java/util/ArrayList");
    jmethodID listInit = (*env)->GetMethodID(env, listClass, "<init>", "()V");
    jmethodID listAdd = (*env)->GetMethodID(env, listClass, "add", "(Ljava/lang/Object;)Z");
    jobject extensionsList = (*env)->NewObject(env, listClass, listInit);

    for (uint32_t i = 0; i < extCount; i++) {
        jstring extName = (*env)->NewStringUTF(env, exts[i].extensionName);
        (*env)->CallBooleanMethod(env, extensionsList, listAdd, extName);
        (*env)->DeleteLocalRef(env, extName);
    }
    free(exts);
    LOG_I("Enumerated %u device extensions.", extCount);

    // 查询设备特性
    jclass mapClass = (*env)->FindClass(env, "java/util/HashMap");
    jmethodID mapInit = (*env)->GetMethodID(env, mapClass, "<init>", "()V");
    jmethodID mapPut = (*env)->GetMethodID(env, mapClass, "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    jobject featuresMap = (*env)->NewObject(env, mapClass, mapInit);

    jclass boolClass = (*env)->FindClass(env, "java/lang/Boolean");
    jmethodID boolValueOf = (*env)->GetStaticMethodID(env, boolClass, "valueOf", "(Z)Ljava/lang/Boolean;");

    VkBool32 multiDrawIndirect = VK_FALSE;
    VkBool32 fillModeNonSolid = VK_FALSE;
    VkBool32 drawIndirectFirstInstance = VK_FALSE;
    VkBool32 samplerAnisotropy = VK_FALSE;
    VkBool32 shaderDrawParameters = VK_FALSE;
    VkBool32 timelineSemaphore = VK_FALSE;
    VkBool32 hostQueryReset = VK_FALSE;
    VkBool32 synchronization2 = VK_FALSE;
    VkBool32 dynamicRendering = VK_FALSE;
    VkBool32 vertexAttributeInstanceRateDivisor = VK_FALSE;

    if (pvkGetPhysicalDeviceFeatures2) {
        VkPhysicalDeviceVertexAttributeDivisorFeaturesEXT featDiv = {
                .sType = VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_VERTEX_ATTRIBUTE_DIVISOR_FEATURES_EXT
        };
        VkPhysicalDeviceDynamicRenderingFeaturesKHR featDyn = {
                .sType = VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_DYNAMIC_RENDERING_FEATURES_KHR,
                .pNext = &featDiv
        };
        VkPhysicalDeviceSynchronization2FeaturesKHR featSync2 = {
                .sType = VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_SYNCHRONIZATION_2_FEATURES_KHR,
                .pNext = &featDyn
        };
        VkPhysicalDeviceHostQueryResetFeatures featHostQuery = {
                .sType = VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_HOST_QUERY_RESET_FEATURES,
                .pNext = &featSync2
        };
        VkPhysicalDeviceTimelineSemaphoreFeatures featTimeline = {
                .sType = VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_TIMELINE_SEMAPHORE_FEATURES,
                .pNext = &featHostQuery
        };
        VkPhysicalDeviceShaderDrawParametersFeatures featShaderDraw = {
                .sType = VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_SHADER_DRAW_PARAMETERS_FEATURES,
                .pNext = &featTimeline
        };
        VkPhysicalDeviceFeatures2 feat2 = {
                .sType = VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_FEATURES_2,
                .pNext = &featShaderDraw
        };

        pvkGetPhysicalDeviceFeatures2(physicalDevice, &feat2);

        multiDrawIndirect = feat2.features.multiDrawIndirect;
        fillModeNonSolid = feat2.features.fillModeNonSolid;
        drawIndirectFirstInstance = feat2.features.drawIndirectFirstInstance;
        samplerAnisotropy = feat2.features.samplerAnisotropy;
        shaderDrawParameters = featShaderDraw.shaderDrawParameters;
        timelineSemaphore = featTimeline.timelineSemaphore;
        hostQueryReset = featHostQuery.hostQueryReset;
        synchronization2 = featSync2.synchronization2;
        dynamicRendering = featDyn.dynamicRendering;
        vertexAttributeInstanceRateDivisor = featDiv.vertexAttributeInstanceRateDivisor;

        LOG_I("Queried features via vkGetPhysicalDeviceFeatures2");
    } else if (pvkGetPhysicalDeviceFeatures) {
        // Vulkan 1.0 降级路径：仅基础特性
        VkPhysicalDeviceFeatures feat;
        pvkGetPhysicalDeviceFeatures(physicalDevice, &feat);
        multiDrawIndirect = feat.multiDrawIndirect;
        fillModeNonSolid = feat.fillModeNonSolid;
        drawIndirectFirstInstance = feat.drawIndirectFirstInstance;
        samplerAnisotropy = feat.samplerAnisotropy;
        LOG_W("vkGetPhysicalDeviceFeatures2 unavailable; only basic features queried.");
    } else {
        LOG_E("Neither vkGetPhysicalDeviceFeatures2 nor vkGetPhysicalDeviceFeatures available.");
    }

#define PUT_FEAT(key, val) do { \
        jstring _k = (*env)->NewStringUTF(env, key); \
        jobject _v = (*env)->CallStaticObjectMethod(env, boolClass, boolValueOf, (jboolean)(val)); \
        (*env)->CallObjectMethod(env, featuresMap, mapPut, _k, _v); \
        (*env)->DeleteLocalRef(env, _k); \
        (*env)->DeleteLocalRef(env, _v); \
    } while (0)

    PUT_FEAT("multiDrawIndirect", multiDrawIndirect);
    PUT_FEAT("fillModeNonSolid", fillModeNonSolid);
    PUT_FEAT("drawIndirectFirstInstance", drawIndirectFirstInstance);
    PUT_FEAT("samplerAnisotropy", samplerAnisotropy);
    PUT_FEAT("shaderDrawParameters", shaderDrawParameters);
    PUT_FEAT("timelineSemaphore", timelineSemaphore);
    PUT_FEAT("hostQueryReset", hostQueryReset);
    PUT_FEAT("synchronization2", synchronization2);
    PUT_FEAT("dynamicRendering", dynamicRendering);
    PUT_FEAT("vertexAttributeInstanceRateDivisor", vertexAttributeInstanceRateDivisor);
#undef PUT_FEAT

    jclass capClass = (*env)->FindClass(env, "com/mio/device/VulkanCapabilities");
    jmethodID capInit = (*env)->GetMethodID(env, capClass, "<init>", "(IIILjava/util/List;Ljava/util/Map;)V");

    jint major = (jint) VK_API_VERSION_MAJOR(deviceApiVersion);
    jint minor = (jint) VK_API_VERSION_MINOR(deviceApiVersion);
    jint patch = (jint) VK_API_VERSION_PATCH(deviceApiVersion);

    LOG_I("Final device Vulkan version: %d.%d.%d", major, minor, patch);

    jobject result = (*env)->NewObject(env, capClass, capInit,
                                       major, minor, patch,
                                       extensionsList, featuresMap);

    (*env)->DeleteLocalRef(env, listClass);
    (*env)->DeleteLocalRef(env, mapClass);
    (*env)->DeleteLocalRef(env, boolClass);
    (*env)->DeleteLocalRef(env, capClass);

    pvkDestroyInstance(instance, NULL);
    dlclose(vulkan_handle);

    LOG_I("Check finished. Vulkan %d.%d.%d", major, minor, patch);
    return result;
}

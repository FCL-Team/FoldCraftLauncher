#include "egl_bridge.h"
#include <jni.h>
#include <assert.h>
#include <dlfcn.h>
#include <limits.h>

#include <stdbool.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>

#include <EGL/egl.h>
#include "GL/osmesa.h"
#include "GL/gl.h"
#include "ctxbridges/osmesa_loader.h"
#include "ctxbridges/egl_loader.h"
#include "virgl/virgl.h"

#ifdef GLES_TEST
#include <GLES2/gl2.h>
#endif

#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <android/rect.h>
#include <string.h>
#include <inttypes.h>
#include "environ/environ.h"
#include <android/dlext.h>
#include "ctxbridges/bridge_tbl.h"
#include "ctxbridges/osm_bridge.h"
#include "driver_helper/nsbypass.h"
#include "fcl/include/fcl_internal.h"
#include <stdatomic.h>

// 由 input_bridge_v3.c 提供，上报 monitor size 到 Java 侧 GLFW
extern void updateMonitorSize(int width, int height);

#define GLFW_CLIENT_API 0x22001
/* Consider GLFW_NO_API as Vulkan API */
#define GLFW_NO_API 0
#define GLFW_OPENGL_API 0x30001

// This means that the function is an external API and that it will be used
#define EXTERNAL_API __attribute__((used))
// This means that you are forced to have this function/variable for ABI compatibility
#define ABI_COMPAT __attribute__((unused))

EGLConfig config;
struct PotatoBridge potatoBridge;

#define RENDERER_GL4ES 1
#define RENDERER_VK_ZINK 2
#define RENDERER_VULKAN 4

static atomic_uint fps = 0;

EXTERNAL_API void pojavTerminate() {
    printf("EGLBridge: Terminating\n");

    switch (pojav_environ->config_renderer) {
        case RENDERER_GL4ES: {
            eglMakeCurrent_p(potatoBridge.eglDisplay, EGL_NO_SURFACE, EGL_NO_SURFACE,
                             EGL_NO_CONTEXT);
            eglDestroySurface_p(potatoBridge.eglDisplay, potatoBridge.eglSurface);
            eglDestroyContext_p(potatoBridge.eglDisplay, potatoBridge.eglContext);
            eglTerminate_p(potatoBridge.eglDisplay);
            eglReleaseThread_p();

            potatoBridge.eglContext = EGL_NO_CONTEXT;
            potatoBridge.eglDisplay = EGL_NO_DISPLAY;
            potatoBridge.eglSurface = EGL_NO_SURFACE;
        }
            break;

            //case RENDERER_VIRGL:
        case RENDERER_VK_ZINK: {
            // Nothing to do here
        }
            break;
    }
}

JNIEXPORT void JNICALL
Java_org_lwjgl_glfw_CallbackBridge_setupBridgeWindow(JNIEnv *env, ABI_COMPAT jclass clazz,
                                                     jobject surface) {
    pojav_environ->pojavWindow = ANativeWindow_fromSurface(env, surface);
    if (br_setup_window != NULL) br_setup_window();
}


JNIEXPORT void JNICALL
Java_net_kdt_pojavlaunch_utils_JREUtils_releaseBridgeWindow(ABI_COMPAT JNIEnv *env,
                                                            ABI_COMPAT jclass clazz) {
    ANativeWindow_release(pojav_environ->pojavWindow);
}

EXTERNAL_API void *pojavGetCurrentContext() {
    if (pojav_environ->config_renderer == RENDERER_VIRGL) {
        return virglGetCurrentContext();
    }
    return br_get_current();
}

#ifdef ADRENO_POSSIBLE

bool checkAdrenoGraphics() {
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

    const EGLint egl_context_attributes[] = { EGL_CONTEXT_CLIENT_VERSION, 3, EGL_NONE };
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

    const char* vendor = (const char*)glGetString(GL_VENDOR);
    const char* renderer = (const char*)glGetString(GL_RENDERER);

    bool is_adreno = (vendor && renderer && strcmp(vendor, "Qualcomm") == 0 && strstr(renderer, "Adreno") != NULL);

    eglMakeCurrent(eglDisplay, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT);
    eglDestroyContext(eglDisplay, context);
    eglTerminate(eglDisplay);

    return is_adreno;
}

void* loadTurnipVulkan() {
    if (!checkAdrenoGraphics())
        return NULL;

    const char* native_dir = getenv("DRIVER_PATH");
    const char* cache_dir = getenv("TMPDIR");

    if (!native_dir)
        return NULL;

    if (!linker_ns_load(native_dir))
        return NULL;

    void* linkerhook = linker_ns_dlopen("liblinkerhook.so", RTLD_LOCAL | RTLD_NOW);
    if (!linkerhook)
        return NULL;

    void* turnip_driver_handle = linker_ns_dlopen("libvulkan_freedreno.so", RTLD_LOCAL | RTLD_NOW);
    if (!turnip_driver_handle) {
        dlclose(linkerhook);
        return NULL;
    }

    void* dl_android = linker_ns_dlopen("libdl_android.so", RTLD_LOCAL | RTLD_LAZY);
    if (!dl_android) {
        dlclose(linkerhook);
        dlclose(turnip_driver_handle);
        return NULL;
    }

    void* android_get_exported_namespace = dlsym(dl_android, "android_get_exported_namespace");
    void (*linkerhookPassHandles)(void*, void*, void*) = dlsym(linkerhook, "app__pojav_linkerhook_pass_handles");

    if (!linkerhookPassHandles || !android_get_exported_namespace) {
        dlclose(dl_android);
        dlclose(linkerhook);
        dlclose(turnip_driver_handle);
        return NULL;
    }

    linkerhookPassHandles(turnip_driver_handle, android_dlopen_ext, android_get_exported_namespace);

    void* libvulkan = linker_ns_dlopen_unique(cache_dir, "libvulkan.so", RTLD_LOCAL | RTLD_NOW);
    if (!libvulkan) {
        dlclose(dl_android);
        dlclose(linkerhook);
        dlclose(turnip_driver_handle);
        return NULL;
    }

    return libvulkan;
}

#endif

static void set_vulkan_ptr(void* ptr) {
    char envval[64];
    sprintf(envval, "%"PRIxPTR, (uintptr_t)ptr);
    setenv("VULKAN_PTR", envval, 1);
}

void load_vulkan() {
    if(getenv("VULKAN_DRIVER_SYSTEM") == NULL && android_get_device_api_level() >= 28) {
#ifdef ADRENO_POSSIBLE
        void* result = loadTurnipVulkan();
        if(result != NULL) {
            FCL_LOG("AdrenoSupp: Loaded Turnip, loader address: %p", result);
            set_vulkan_ptr(result);
            return;
        }
#endif
    }
    FCL_LOG("OSMDroid: loading vulkan regularly...");
    void* vulkan_ptr = dlopen("libvulkan.so", RTLD_LAZY | RTLD_LOCAL);
    FCL_LOG("OSMDroid: loaded vulkan, ptr=%p", vulkan_ptr);
    set_vulkan_ptr(vulkan_ptr);
}

int pojavInitOpenGL() {
    // Only affects GL4ES as of now
    const char *forceVsync = getenv("FORCE_VSYNC");
    if (!strcmp(forceVsync, "true"))
        pojav_environ->force_vsync = true;

    // NOTE: Override for now.
    const char *renderer = getenv("POJAV_RENDERER");
    if (!strncmp("opengles", renderer, 8)) {
        pojav_environ->config_renderer = RENDERER_GL4ES;
        if (!strcmp(renderer, "opengles3_desktopgl_zink_kopper")) {
            load_vulkan();
            setenv("GALLIUM_DRIVER", "zink", 1);
            setenv("MESA_ANDROID_NO_KMS_SWRAST", "1", 1);
        }
        set_gl_bridge_tbl();
    }

    if (!strcmp(renderer, "gallium_virgl")) {
        pojav_environ->config_renderer = RENDERER_VIRGL;
        setenv("GALLIUM_DRIVER", "virpipe", 1);
        loadSymbolsVirGL();
        virglInit();
        return 0;
    }

    if (!strcmp(renderer, "vulkan_zink")) {
        pojav_environ->config_renderer = RENDERER_VK_ZINK;
        load_vulkan();
        setenv("GALLIUM_DRIVER", "zink", 1);
        set_osm_bridge_tbl();
    }

    if (!strcmp(renderer, "gallium_freedreno")) {
        pojav_environ->config_renderer = RENDERER_VK_ZINK;
        load_vulkan();
        setenv("GALLIUM_DRIVER", "freedreno", 1);
        setenv("MESA_LOADER_DRIVER_OVERRIDE", "kgsl", 1);
        set_osm_bridge_tbl();
    }

    if (!strcmp(renderer, "custom_gallium")) {
        pojav_environ->config_renderer = RENDERER_VK_ZINK;
        load_vulkan();
        set_osm_bridge_tbl();
    }

    if (br_init()) br_setup_window();

    return 0;
}

// 获取当前线程的 JNIEnv（未附着则先 Attach，不 Detach，保留渲染线程的附着状态）
static JNIEnv *get_attached_env(JavaVM *jvm) {
    JNIEnv *jvm_env = NULL;
    jint env_result = (*jvm)->GetEnv(jvm, (void **) &jvm_env, JNI_VERSION_1_4);
    if (env_result == JNI_EDETACHED) {
        env_result = (*jvm)->AttachCurrentThread(jvm, &jvm_env, NULL);
    }
    if (env_result != JNI_OK) {
        printf("get_attached_env failed: %i\n", env_result);
        return NULL;
    }
    return jvm_env;
}

EXTERNAL_API int pojavInit() {
    pojav_environ->glfwThreadVmEnv = get_attached_env(pojav_environ->runtimeJavaVMPtr);
    if (pojav_environ->glfwThreadVmEnv == NULL) {
        printf("Failed to attach Java-side JNIEnv to GLFW thread\n");
        return 0;
    }
    ANativeWindow_acquire(pojav_environ->pojavWindow);
    pojav_environ->savedWidth = ANativeWindow_getWidth(pojav_environ->pojavWindow);
    pojav_environ->savedHeight = ANativeWindow_getHeight(pojav_environ->pojavWindow);
    ANativeWindow_setBuffersGeometry(pojav_environ->pojavWindow, pojav_environ->savedWidth,
                                     pojav_environ->savedHeight,
                                     AHARDWAREBUFFER_FORMAT_R8G8B8X8_UNORM);
    updateMonitorSize(pojav_environ->savedWidth, pojav_environ->savedHeight);
    pojavInitOpenGL();
    return 1;
}

EXTERNAL_API void pojavSetWindowHint(int hint, int value) {
    if (hint != GLFW_CLIENT_API) return;
    switch (value) {
        case GLFW_NO_API:
            pojav_environ->config_renderer = RENDERER_VULKAN;
            /* Nothing to do: initialization is handled in Java-side */
            // pojavInitVulkan();
            break;
        case GLFW_OPENGL_API:
            const char *renderer = getenv("POJAV_RENDERER");
            if (!strncmp("opengles", renderer, 8)) {
                pojav_environ->config_renderer = RENDERER_GL4ES;
            } else if (!strcmp(renderer, "vulkan_zink")) {
                pojav_environ->config_renderer = RENDERER_VK_ZINK;
            }
            /* Nothing to do: initialization is called in pojavCreateContext */
            // pojavInitOpenGL();
            break;
        default:
            printf("GLFW: Unimplemented API 0x%x\n", value);
            abort();
    }
}

EXTERNAL_API void pojavSwapBuffers() {
    atomic_fetch_add(&fps, 1);
    if (pojav_environ->config_renderer == RENDERER_VIRGL)
        virglSwapBuffers();
    else br_swap_buffers();
}


EXTERNAL_API void pojavMakeCurrent(void *window) {
    if (pojav_environ->config_renderer == RENDERER_VIRGL)
        virglMakeCurrent(window);
    else br_make_current((basic_render_window_t *) window);
}

EXTERNAL_API void *pojavCreateContext(void *contextSrc) {
    if (pojav_environ->config_renderer == RENDERER_VULKAN)
        return (void *) pojav_environ->pojavWindow;

    if (pojav_environ->config_renderer == RENDERER_VIRGL)
        return virglCreateContext(contextSrc);

    return br_init_context((basic_render_window_t *) contextSrc);
}

void *maybe_load_vulkan() {
    // We use the env var because
    // 1. it's easier to do that
    // 2. it won't break if something will try to load vulkan and osmesa simultaneously
    if (getenv("VULKAN_PTR") == NULL) load_vulkan();
    return (void *) strtoul(getenv("VULKAN_PTR"), NULL, 0x10);
}

EXTERNAL_API JNIEXPORT jlong JNICALL
Java_org_lwjgl_vulkan_VK_getVulkanDriverHandle(ABI_COMPAT JNIEnv *env, ABI_COMPAT jclass thiz) {
    printf("EGLBridge: LWJGL-side Vulkan loader requested the Vulkan handle\n");
    return (jlong) maybe_load_vulkan();
}

EXTERNAL_API void pojavSwapInterval(int interval) {
    if (pojav_environ->config_renderer == RENDERER_VIRGL)
        virglSwapInterval(interval);
    else br_swap_interval(interval);
}

JNIEXPORT jint JNICALL
Java_org_lwjgl_glfw_CallbackBridge_getFps(JNIEnv *env, jclass clazz) {
    return atomic_exchange(&fps, 0);
}

EXTERNAL_API JNIEXPORT void JNICALL
Java_org_lwjgl_vulkan_VK_updateFps(ABI_COMPAT JNIEnv *env, ABI_COMPAT jclass thiz) {
    atomic_fetch_add(&fps, 1);
}
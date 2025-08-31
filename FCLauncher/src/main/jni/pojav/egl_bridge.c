#include "pojav/egl_bridge.h"
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
#include "pojav/GL/osmesa.h"
#include "ctxbridges/osmesa_loader.h"
#include "ctxbridges/egl_loader.h"
#include "pojav/virgl/virgl.h"
#include "fcl/include/fcl_bridge.h"

#ifdef GLES_TEST
#include <GLES2/gl2.h>
#endif

#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <android/rect.h>
#include <string.h>
#include "pojav/environ/environ.h"
#include <android/dlext.h>
#include "ctxbridges/bridge_tbl.h"
#include "ctxbridges/osm_bridge.h"
#include "fcl/include/utils.h"
#include "driver_helper/driver_helper.h"

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

void bigcore_set_affinity();

#define RENDERER_GL4ES 1
#define RENDERER_VK_ZINK 2
#define RENDERER_VULKAN 4

static int fps = 0;

EXTERNAL_API void pojavTerminate() {
    printf("EGLBridge: Terminating\n");

    switch (pojav_environ->config_renderer) {
        case RENDERER_GL4ES: {
            eglMakeCurrent_p(potatoBridge.eglDisplay, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT);
            eglDestroySurface_p(potatoBridge.eglDisplay, potatoBridge.eglSurface);
            eglDestroyContext_p(potatoBridge.eglDisplay, potatoBridge.eglContext);
            eglTerminate_p(potatoBridge.eglDisplay);
            eglReleaseThread_p();

            potatoBridge.eglContext = EGL_NO_CONTEXT;
            potatoBridge.eglDisplay = EGL_NO_DISPLAY;
            potatoBridge.eglSurface = EGL_NO_SURFACE;
        } break;

            //case RENDERER_VIRGL:
        case RENDERER_VK_ZINK: {
            // Nothing to do here
        } break;
    }
}

JNIEXPORT void JNICALL
Java_org_lwjgl_glfw_CallbackBridge_setupBridgeWindow(JNIEnv *env, ABI_COMPAT jclass clazz, jobject surface) {
    pojav_environ->pojavWindow = ANativeWindow_fromSurface(env, surface);
    if(br_setup_window != NULL) br_setup_window();
}


JNIEXPORT void JNICALL
Java_net_kdt_pojavlaunch_utils_JREUtils_releaseBridgeWindow(ABI_COMPAT JNIEnv *env, ABI_COMPAT jclass clazz) {
    ANativeWindow_release(pojav_environ->pojavWindow);
}

EXTERNAL_API void* pojavGetCurrentContext() {
    if (pojav_environ->config_renderer == RENDERER_VIRGL) {
        return virglGetCurrentContext();
    }
    return br_get_current();
}

int pojavInitOpenGL() {
    load_vulkan();
    // Only affects GL4ES as of now
    const char *forceVsync = getenv("FORCE_VSYNC");
    if (!strcmp(forceVsync, "true"))
        pojav_environ->force_vsync = true;

    // NOTE: Override for now.
    const char *renderer = getenv("POJAV_RENDERER");
    if (!strncmp("opengles", renderer, 8))
    {
        pojav_environ->config_renderer = RENDERER_GL4ES;
        set_gl_bridge_tbl();
    }

    if (!strcmp(renderer, "gallium_virgl"))
    {
        pojav_environ->config_renderer = RENDERER_VIRGL;
        setenv("GALLIUM_DRIVER", "virpipe", 1);
        loadSymbolsVirGL();
        virglInit();
        return 0;
    }

    if (!strcmp(renderer, "vulkan_zink"))
    {
        pojav_environ->config_renderer = RENDERER_VK_ZINK;
        setenv("GALLIUM_DRIVER", "zink", 1);
        set_osm_bridge_tbl();
    }

    if (!strcmp(renderer, "gallium_freedreno"))
    {
        pojav_environ->config_renderer = RENDERER_VK_ZINK;
        setenv("GALLIUM_DRIVER", "freedreno", 1);
        setenv("MESA_LOADER_DRIVER_OVERRIDE", "kgsl", 1);
        set_osm_bridge_tbl();
    }

    if (!strcmp(renderer, "custom_gallium"))
    {
        pojav_environ->config_renderer = RENDERER_VK_ZINK;
        set_osm_bridge_tbl();
    }

    if(br_init()) br_setup_window();

    return 0;
}

EXTERNAL_API int pojavInit() {
    ANativeWindow_acquire(pojav_environ->pojavWindow);
    pojav_environ->savedWidth = ANativeWindow_getWidth(pojav_environ->pojavWindow);
    pojav_environ->savedHeight = ANativeWindow_getHeight(pojav_environ->pojavWindow);
    ANativeWindow_setBuffersGeometry(pojav_environ->pojavWindow,pojav_environ->savedWidth,pojav_environ->savedHeight,AHARDWAREBUFFER_FORMAT_R8G8B8X8_UNORM);
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
            /* Nothing to do: initialization is called in pojavCreateContext */
            // pojavInitOpenGL();
            break;
        default:
            printf("GLFW: Unimplemented API 0x%x\n", value);
            abort();
    }
}

EXTERNAL_API void pojavSwapBuffers() {
    if (fps >= INT_MAX - 1) fps = 0;
    fps++;
    if (pojav_environ->config_renderer == RENDERER_VIRGL)
        virglSwapBuffers();
    else br_swap_buffers();
}


EXTERNAL_API void pojavMakeCurrent(void* window) {
    if (getenv("POJAV_BIG_CORE_AFFINITY") != NULL) bigcore_set_affinity();
    if (pojav_environ->config_renderer == RENDERER_VIRGL)
        virglMakeCurrent(window);
    else br_make_current((basic_render_window_t*)window);
}

EXTERNAL_API void* pojavCreateContext(void* contextSrc) {
    if (pojav_environ->config_renderer == RENDERER_VULKAN)
        return (void *) pojav_environ->pojavWindow;

    if (pojav_environ->config_renderer == RENDERER_VIRGL)
        return virglCreateContext(contextSrc);

    return br_init_context((basic_render_window_t*)contextSrc);
}

void* maybe_load_vulkan() {
    // We use the env var because
    // 1. it's easier to do that
    // 2. it won't break if something will try to load vulkan and osmesa simultaneously
    if(getenv("VULKAN_PTR") == NULL) load_vulkan();
    return (void*) strtoul(getenv("VULKAN_PTR"), NULL, 0x10);
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

EXTERNAL_API void pojavSetInjectorCallback(FCLinjectorfun callback) {
    fclSetInjectorCallback(callback);
}

EXTERNAL_API void pojavSetHitResultType(int type) {
    fclSetHitResultType(type);
}

JNIEXPORT jint JNICALL
Java_org_lwjgl_glfw_CallbackBridge_getFps(JNIEnv *env, jclass clazz) {
    int f = fps;
    fps = 0;
    return f;
}
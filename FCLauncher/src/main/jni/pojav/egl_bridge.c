#include <jni.h>
#include <assert.h>
#include <dlfcn.h>

#include <stdbool.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>

#include <EGL/egl.h>
#include "GL/osmesa.h"
#include "ctxbridges/osmesa_loader.h"
#include "driver_helper/nsbypass.h"

#ifdef GLES_TEST
#include <GLES2/gl2.h>
#endif

#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <android/rect.h>
#include <string.h>
#include <environ/environ.h>
#include <android/dlext.h>
#include <pthread.h>
#include "utils.h"
#include "ctxbridges/bridge_tbl.h"
#include "ctxbridges/osm_bridge.h"
#include "fcl/include/fcl_internal.h"

#define GLFW_CLIENT_API 0x22001
/* Consider GLFW_NO_API as Vulkan API */
#define GLFW_NO_API 0
#define GLFW_OPENGL_API 0x30001

// This means that the function is an external API and that it will be used
#define EXTERNAL_API __attribute__((used))
// This means that you are forced to have this function/variable for ABI compatibility
#define ABI_COMPAT __attribute__((unused))


struct PotatoBridge {

    /* EGLContext */ void* eglContext;
    /* EGLDisplay */ void* eglDisplay;
    /* EGLSurface */ void* eglSurface;
/*
    void* eglSurfaceRead;
    void* eglSurfaceDraw;
*/
};
EGLConfig config;
struct PotatoBridge potatoBridge;

#include "ctxbridges/egl_loader.h"
#include "ctxbridges/osmesa_loader.h"

#define RENDERER_GL4ES 1
#define RENDERER_VK_ZINK 2
#define RENDERER_VIRGL 3
#define RENDERER_VULKAN 4

int (*vtest_main_p) (int argc, char** argv);
void (*vtest_swap_buffers_p) (void);
int initVirgl();

EXTERNAL_API void pojavTerminate() {
    FCL_INTERNAL_LOG("EGLBridge: Terminating");

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

JNIEXPORT void JNICALL Java_org_lwjgl_glfw_CallbackBridge_setupBridgeWindow(JNIEnv* env, ABI_COMPAT jclass clazz, jobject surface) {
    pojav_environ->pojavWindow = ANativeWindow_fromSurface(env, surface);
    if(br_setup_window != NULL) br_setup_window();
}


JNIEXPORT void JNICALL
Java_net_kdt_pojavlaunch_utils_JREUtils_releaseBridgeWindow(ABI_COMPAT JNIEnv *env, ABI_COMPAT jclass clazz) {
    ANativeWindow_release(pojav_environ->pojavWindow);
}

EXTERNAL_API void* pojavGetCurrentContext() {
    if(pojav_environ->config_renderer == RENDERER_VIRGL) {
        return (void *)OSMesaGetCurrentContext_p();
    }
    return br_get_current();
}

//#define ADRENO_POSSIBLE
#ifdef ADRENO_POSSIBLE
//Checks if your graphics are Adreno. Returns true if your graphics are Adreno, false otherwise or if there was an error
bool checkAdrenoGraphics() {
    EGLDisplay eglDisplay = eglGetDisplay(EGL_DEFAULT_DISPLAY);
    if(eglDisplay == EGL_NO_DISPLAY || eglInitialize(eglDisplay, NULL, NULL) != EGL_TRUE) return false;
    EGLint egl_attributes[] = { EGL_BLUE_SIZE, 8, EGL_GREEN_SIZE, 8, EGL_RED_SIZE, 8, EGL_ALPHA_SIZE, 8, EGL_DEPTH_SIZE, 24, EGL_SURFACE_TYPE, EGL_PBUFFER_BIT, EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT, EGL_NONE };
    EGLint num_configs = 0;
    if(eglChooseConfig(eglDisplay, egl_attributes, NULL, 0, &num_configs) != EGL_TRUE || num_configs == 0) {
        eglTerminate(eglDisplay);
        return false;
    }
    EGLConfig eglConfig;
    eglChooseConfig(eglDisplay, egl_attributes, &eglConfig, 1, &num_configs);
    const EGLint egl_context_attributes[] = { EGL_CONTEXT_CLIENT_VERSION, 3, EGL_NONE };
    EGLContext context = eglCreateContext(eglDisplay, eglConfig, EGL_NO_CONTEXT, egl_context_attributes);
    if(context == EGL_NO_CONTEXT) {
        eglTerminate(eglDisplay);
        return false;
    }
    if(eglMakeCurrent(eglDisplay, EGL_NO_SURFACE, EGL_NO_SURFACE, context) != EGL_TRUE) {
        eglDestroyContext(eglDisplay, context);
        eglTerminate(eglDisplay);
    }
    const char* vendor = glGetString(GL_VENDOR);
    const char* renderer = glGetString(GL_RENDERER);
    bool is_adreno = false;
    if(strcmp(vendor, "Qualcomm") == 0 && strstr(renderer, "Adreno") != NULL) {
        is_adreno = true; // TODO: check for Turnip support
    }
    eglMakeCurrent(eglDisplay, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT);
    eglDestroyContext(eglDisplay, context);
    eglTerminate(eglDisplay);
    return is_adreno;
}
void* load_turnip_vulkan() {
    if(!checkAdrenoGraphics()) return NULL;
    const char* native_dir = getenv("POJAV_NATIVEDIR");
    const char* cache_dir = getenv("TMPDIR");
    if(!linker_ns_load(native_dir)) return NULL;
    void* linkerhook = linker_ns_dlopen("liblinkerhook.so", RTLD_LOCAL | RTLD_NOW);
    if(linkerhook == NULL) return NULL;
    void* turnip_driver_handle = linker_ns_dlopen("libvulkan_freedreno.so", RTLD_LOCAL | RTLD_NOW);
    if(turnip_driver_handle == NULL) {
        FCL_INTERNAL_LOG("AdrenoSupp: Failed to load Turnip!\n%s", dlerror());
        dlclose(linkerhook);
        return NULL;
    }
    void* dl_android = linker_ns_dlopen("libdl_android.so", RTLD_LOCAL | RTLD_LAZY);
    if(dl_android == NULL) {
        dlclose(linkerhook);
        dlclose(turnip_driver_handle);
        return NULL;
    }
    void* android_get_exported_namespace = dlsym(dl_android, "android_get_exported_namespace");
    void (*linkerhook_pass_handles)(void*, void*, void*) = dlsym(linkerhook, "app__pojav_linkerhook_pass_handles");
    if(linkerhook_pass_handles == NULL || android_get_exported_namespace == NULL) {
        dlclose(dl_android);
        dlclose(linkerhook);
        dlclose(turnip_driver_handle);
        return NULL;
    }
    linkerhook_pass_handles(turnip_driver_handle, android_dlopen_ext, android_get_exported_namespace);
    void* libvulkan = linker_ns_dlopen_unique(cache_dir, "libvulkan.so", RTLD_LOCAL | RTLD_NOW);
    return libvulkan;
}
#endif

static void set_vulkan_ptr(void* ptr) {
    char envval[64];
    sprintf(envval, "%"PRIxPTR, (uintptr_t)ptr);
    setenv("VULKAN_PTR", envval, 1);
}

void load_vulkan() {
    if(getenv("POJAV_ZINK_PREFER_SYSTEM_DRIVER") == NULL &&
        android_get_device_api_level() >= 28) { // the loader does not support below that
#ifdef ADRENO_POSSIBLE
        void* result = load_turnip_vulkan();
        if(result != NULL) {
            FCL_INTERNAL_LOG("AdrenoSupp: Loaded Turnip, loader address: %p", result);
            set_vulkan_ptr(result);
            return;
        }
#endif
    }
    FCL_INTERNAL_LOG("OSMDroid: loading vulkan regularly...");
    void* vulkan_ptr = dlopen("libvulkan.so", RTLD_LAZY | RTLD_LOCAL);
    FCL_INTERNAL_LOG("OSMDroid: loaded vulkan, ptr=%p", vulkan_ptr);
    set_vulkan_ptr(vulkan_ptr);
}

bool loadSymbolsVirGL() {
    pojav_environ->config_renderer = RENDERER_VIRGL;
    dlsym_OSMesa();
    dlsym_EGL();

    char* fileName = calloc(1, 1024);

    sprintf(fileName, "%s/libvirgl_test_server.so", getenv("POJAV_NATIVEDIR"));
    void *handle = dlopen(fileName, RTLD_LAZY);
    FCL_INTERNAL_LOG("VirGL: libvirgl_test_server = %p", handle);
    if (!handle) {
        FCL_INTERNAL_LOG("VirGL: %s", dlerror());
    }
    vtest_main_p = dlsym(handle, "vtest_main");
    vtest_swap_buffers_p = dlsym(handle, "vtest_swap_buffers");

    free(fileName);
}

int pojavInitOpenGL() {
    // Only affects GL4ES as of now
    const char *forceVsync = getenv("FORCE_VSYNC");
    if (strcmp(forceVsync, "true") == 0)
        pojav_environ->force_vsync = true;

    // NOTE: Override for now.
    const char *renderer = getenv("POJAV_RENDERER");
    if (strncmp("opengles", renderer, 8) == 0) {
        if (strncmp("opengles3_virgl", renderer, 15) == 0) {
            loadSymbolsVirGL();
        } else {
            pojav_environ->config_renderer = RENDERER_GL4ES;
            set_gl_bridge_tbl();
        }
    } else if (strcmp(renderer, "vulkan_zink") == 0) {
        pojav_environ->config_renderer = RENDERER_VK_ZINK;
        load_vulkan();
        setenv("GALLIUM_DRIVER","zink",1);
        set_osm_bridge_tbl();
    }
    if(pojav_environ->config_renderer == RENDERER_VK_ZINK || pojav_environ->config_renderer == RENDERER_GL4ES) {
        if(br_init()) {
            br_setup_window();
        }
    } else if (pojav_environ->config_renderer == RENDERER_VIRGL){
        return initVirgl();
    }
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
            FCL_INTERNAL_LOG("GLFW: Unimplemented API 0x%x", value);
            abort();
    }
}

EXTERNAL_API void pojavSwapBuffers() {
    if (pojav_environ->config_renderer == RENDERER_VIRGL) {
        glFinish_p();
        vtest_swap_buffers_p();
    } else {
        br_swap_buffers();
    }
}


EXTERNAL_API void pojavMakeCurrent(void *window) {
    if (pojav_environ->config_renderer == RENDERER_VIRGL) {
        FCL_INTERNAL_LOG("OSMDroid: making current");
        OSMesaMakeCurrent_p((OSMesaContext) window, setbuffer, GL_UNSIGNED_BYTE,
                            pojav_environ->savedWidth, pojav_environ->savedHeight);
        FCL_INTERNAL_LOG("OSMDroid: vendor: %s", glGetString_p(GL_VENDOR));
        FCL_INTERNAL_LOG("OSMDroid: renderer: %s", glGetString_p(GL_RENDERER));
        glClear_p(GL_COLOR_BUFFER_BIT);
        glClearColor_p(0.4f, 0.4f, 0.4f, 1.0f);
        int pixelsArr[4];
        glReadPixels_p(0, 0, 1, 1, GL_RGB, GL_INT, &pixelsArr);
        pojavSwapBuffers();
    } else {
        br_make_current((basic_render_window_t *) window);
    }
}

EXTERNAL_API void *pojavCreateContext(void *contextSrc) {
    void *ptr;
    if (pojav_environ->config_renderer == RENDERER_VULKAN) {
        ptr = pojav_environ->pojavWindow;
    } else if (pojav_environ->config_renderer == RENDERER_VIRGL) {
        pojavInitOpenGL();
        FCL_INTERNAL_LOG("OSMDroid: generating context");
        ptr = OSMesaCreateContext_p(OSMESA_RGBA, contextSrc);
        FCL_INTERNAL_LOG("OSMDroid: context=%p", ptr);
    } else {
        ptr = br_init_context((basic_render_window_t *) contextSrc);
    }
    //to prevent internalWindowSizeChanged cause too much error
    pojav_environ->showingWindow = (long) ptr;
    return ptr;
}

EXTERNAL_API JNIEXPORT jlong JNICALL
Java_org_lwjgl_vulkan_VK_getVulkanDriverHandle(ABI_COMPAT JNIEnv *env, ABI_COMPAT jclass thiz) {
    FCL_INTERNAL_LOG("EGLBridge: LWJGL-side Vulkan loader requested the Vulkan handle");
    // The code below still uses the env var because
    // 1. it's easier to do that
    // 2. it won't break if something will try to load vulkan and osmesa simultaneously
    if(getenv("VULKAN_PTR") == NULL) load_vulkan();
    return strtoul(getenv("VULKAN_PTR"), NULL, 0x10);
}

EXTERNAL_API void pojavSwapInterval(int interval) {
    if (pojav_environ->config_renderer == RENDERER_VIRGL) {
        eglSwapInterval_p(potatoBridge.eglDisplay, interval);
    } else {
        br_swap_interval(interval);
    }
}

void *egl_make_current(void *window) {
    EGLBoolean success = eglMakeCurrent_p(
            potatoBridge.eglDisplay,
            window == 0 ? (EGLSurface *) 0 : potatoBridge.eglSurface,
            window == 0 ? (EGLSurface *) 0 : potatoBridge.eglSurface,
            /* window==0 ? EGL_NO_CONTEXT : */ (EGLContext *) window
    );

    if (success == EGL_FALSE) {
        FCL_INTERNAL_LOG("EGLBridge: Error: eglMakeCurrent() failed: %p", eglGetError_p());
    } else {
        FCL_INTERNAL_LOG("EGLBridge: eglMakeCurrent() succeed!");
    }

    if (pojav_environ->config_renderer == RENDERER_VIRGL) {
        FCL_INTERNAL_LOG("VirGL: vtest_main = %p", vtest_main_p);
        FCL_INTERNAL_LOG("VirGL: Calling VTest server's main function");
        vtest_main_p(3, (const char *[]) {"vtest", "--no-loop-or-fork", "--use-gles", NULL, NULL});
    }
}

int initVirgl() {
    if (potatoBridge.eglDisplay == NULL || potatoBridge.eglDisplay == EGL_NO_DISPLAY) {
        potatoBridge.eglDisplay = eglGetDisplay_p(EGL_DEFAULT_DISPLAY);
        if (potatoBridge.eglDisplay == EGL_NO_DISPLAY) {
            FCL_INTERNAL_LOG("EGLBridge: Error eglGetDefaultDisplay() failed: %p",
                             eglGetError_p());
            return 0;
        }
    }
    FCL_INTERNAL_LOG("EGLBridge: Initializing");
    if (!eglInitialize_p(potatoBridge.eglDisplay, NULL, NULL)) {
        FCL_INTERNAL_LOG("EGLBridge: Error eglInitialize() failed: %s", eglGetError_p());
        return 0;
    }

    static const EGLint attribs[] = {
            EGL_RED_SIZE, 8,
            EGL_GREEN_SIZE, 8,
            EGL_BLUE_SIZE, 8,
            EGL_ALPHA_SIZE, 8,
            // Minecraft required on initial 24
            EGL_DEPTH_SIZE, 24,
            EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
            EGL_NONE
    };

    EGLint num_configs;
    EGLint vid;

    if (!eglChooseConfig_p(potatoBridge.eglDisplay, attribs, &config, 1, &num_configs)) {
        FCL_INTERNAL_LOG("EGLBridge: Error couldn't get an EGL visual config: %s",
                         eglGetError_p());
        return 0;
    }

    assert(config);
    assert(num_configs > 0);

    if (!eglGetConfigAttrib_p(potatoBridge.eglDisplay, config, EGL_NATIVE_VISUAL_ID, &vid)) {
        FCL_INTERNAL_LOG("EGLBridge: Error eglGetConfigAttrib() failed: %s", eglGetError_p());
        return 0;
    }

    ANativeWindow_setBuffersGeometry(pojav_environ->pojavWindow, 0, 0, vid);

    eglBindAPI_p(EGL_OPENGL_ES_API);

    potatoBridge.eglSurface = eglCreateWindowSurface_p(potatoBridge.eglDisplay, config,
                                                       pojav_environ->pojavWindow, NULL);

    if (!potatoBridge.eglSurface) {
        FCL_INTERNAL_LOG("EGLBridge: Error eglCreateWindowSurface failed: %p", eglGetError_p());
        return 0;
    }

    // sanity checks
    {
        EGLint val;
        assert(eglGetConfigAttrib_p(potatoBridge.eglDisplay, config, EGL_SURFACE_TYPE, &val));
        assert(val & EGL_WINDOW_BIT);
    }

    FCL_INTERNAL_LOG("EGLBridge: Initialized!");
    FCL_INTERNAL_LOG("EGLBridge: ThreadID=%d", gettid());
    FCL_INTERNAL_LOG("EGLBridge: EGLDisplay=%p, EGLSurface=%p",
                     potatoBridge.eglDisplay,
                     potatoBridge.eglSurface
    );

    // Init EGL context and vtest server
    const EGLint ctx_attribs[] = {
            EGL_CONTEXT_CLIENT_VERSION, 3,
            EGL_NONE
    };
    EGLContext *ctx = eglCreateContext_p(potatoBridge.eglDisplay, config, NULL, ctx_attribs);
    FCL_INTERNAL_LOG("VirGL: created EGL context %p", ctx);

    pthread_t t;
    pthread_create(&t, NULL, egl_make_current, (void *) ctx);
    usleep(100 * 1000); // need enough time for the server to init

    if (OSMesaCreateContext_p == NULL) {
        FCL_INTERNAL_LOG("OSMDroid: %s", dlerror());
    }
    return 0;
}

#include "quest_vr.h"
#include "presentation.h"
#include "ctxbridges/egl_loader.h"
#include <EGL/eglext.h>
#include <GLES3/gl3.h>
#include <android/log.h>
#include <android/native_window.h>
#include <stdlib.h>
#include <string.h>

#define VR_LOG(...) __android_log_print(ANDROID_LOG_INFO, "FCL-QuestVR", __VA_ARGS__)
#define VR_ERROR(...) __android_log_print(ANDROID_LOG_ERROR, "FCL-QuestVR", __VA_ARGS__)

static EGLDisplay vr_display = EGL_NO_DISPLAY;
static __thread gl_render_window_t *vr_current;
static gl_render_window_t *vr_primary;
static int flat_swap_interval = 1;

typedef struct {
    gl_render_window_t base;
    EGLSurface pbuffer;
    EGLSurface window_surface;
    uint64_t generation;
    bool window_failed;
    bool flat_frame_notified;
} quest_vr_window;

static bool update_surface(gl_render_window_t *bundle) {
    if (bundle != vr_primary) return false;
    quest_vr_window *window = (quest_vr_window *) bundle;
    quest_vr_presentation desired;
    quest_vr_get_presentation(&desired);
    bool want_window = !desired.immersive && desired.window;
    bool changed = false;
    if (window->window_surface != EGL_NO_SURFACE &&
        (!want_window || desired.generation != window->generation)) {
        if (!eglMakeCurrent_p(vr_display, window->pbuffer, window->pbuffer, bundle->context)) {
            VR_ERROR("Cannot detach the flat panel: 0x%x", eglGetError_p());
            if (desired.window) ANativeWindow_release(desired.window);
            return false;
        }
        bundle->surface = window->pbuffer;
        eglDestroySurface_p(vr_display, window->window_surface);
        window->window_surface = EGL_NO_SURFACE;
        ANativeWindow_release(bundle->nativeSurface);
        bundle->nativeSurface = NULL;
        window->flat_frame_notified = false;
        changed = true;
        VR_LOG("Minecraft context retained; presenting to the XR pbuffer");
    }
    if (desired.generation != window->generation || !want_window)
        window->window_failed = false;
    window->generation = desired.generation;
    if (want_window && window->window_surface == EGL_NO_SURFACE && !window->window_failed) {
        ANativeWindow_setBuffersGeometry(desired.window, 0, 0, bundle->format);
        EGLSurface surface = eglCreateWindowSurface_p(vr_display, bundle->config, desired.window, NULL);
        if (surface != EGL_NO_SURFACE &&
            eglMakeCurrent_p(vr_display, surface, surface, bundle->context)) {
            window->window_surface = surface;
            bundle->surface = surface;
            bundle->nativeSurface = desired.window;
            desired.window = NULL;
            eglSwapInterval_p(vr_display, flat_swap_interval);
            window->flat_frame_notified = false;
            changed = true;
            VR_LOG("Minecraft context retained; presenting to the flat game panel");
        } else {
            VR_ERROR("Cannot bind the flat game panel: 0x%x", eglGetError_p());
            if (surface != EGL_NO_SURFACE) eglDestroySurface_p(vr_display, surface);
            window->window_failed = true;
        }
    }
    if (desired.window) ANativeWindow_release(desired.window);
    return changed;
}

bool quest_vr_enabled(void) {
    const char *enabled = getenv("FCL_VR");
    const char *renderer = getenv("FCL_VR_RENDERER");
    if (!renderer) renderer = getenv("POJAV_RENDERER");
    return enabled && !strcmp(enabled, "1") && renderer &&
           !strcmp(renderer, "quest_mobileglues");
}

bool quest_vr_init(void) {
    setenv("POJAVEXEC_EGL", "libmobileglues.so", 1);
    unsetenv("SFPEW_LAYER");
    unsetenv("LIBGL_GLES");
    setenv("LIBGL_ES", "3", 1);
    if (!dlsym_EGL() || !eglCreatePbufferSurface_p || !eglCreateContext_p ||
        !eglMakeCurrent_p || !eglChooseConfig_p || !eglCreateWindowSurface_p ||
        !eglGetConfigAttrib_p || !eglSwapBuffers_p) {
        VR_ERROR("Renderer does not expose the EGL functions required by OpenXR");
        return false;
    }
    vr_display = eglGetDisplay_p(EGL_DEFAULT_DISPLAY);
    if (vr_display == EGL_NO_DISPLAY || !eglInitialize_p(vr_display, NULL, NULL)) {
        VR_ERROR("Native GLES display initialization failed: 0x%x", eglGetError_p());
        return false;
    }
    VR_LOG("Initialized MobileGlues VR; Minecraft will render to OpenXR eye swapchains");
    return true;
}

gl_render_window_t *quest_vr_init_context(gl_render_window_t *share) {
    if (vr_display == EGL_NO_DISPLAY) return NULL;
    quest_vr_window *window = calloc(1, sizeof(*window));
    if (!window) return NULL;
    gl_render_window_t *bundle = &window->base;
    const EGLint config_attributes[] = {
        EGL_RED_SIZE, 8, EGL_GREEN_SIZE, 8, EGL_BLUE_SIZE, 8, EGL_ALPHA_SIZE, 8,
        EGL_DEPTH_SIZE, 24, EGL_SURFACE_TYPE, EGL_WINDOW_BIT | EGL_PBUFFER_BIT,
        EGL_RENDERABLE_TYPE, EGL_OPENGL_ES3_BIT_KHR, EGL_NONE
    };
    EGLint count = 0;
    if (!eglChooseConfig_p(vr_display, config_attributes, &bundle->config, 1, &count) ||
        count != 1 || !eglBindAPI_p(EGL_OPENGL_ES_API)) {
        VR_ERROR("No GLES 3 window/pbuffer configuration: 0x%x", eglGetError_p());
        free(bundle);
        return NULL;
    }
    if (!eglGetConfigAttrib_p(vr_display, bundle->config, EGL_NATIVE_VISUAL_ID, &bundle->format)) {
        VR_ERROR("Cannot obtain the Android window format: 0x%x", eglGetError_p());
        free(bundle);
        return NULL;
    }
    const EGLint context_attributes[] = {EGL_CONTEXT_CLIENT_VERSION, 3, EGL_NONE};
    bundle->context = eglCreateContext_p(vr_display, bundle->config,
                                        share ? share->context : EGL_NO_CONTEXT,
                                        context_attributes);
    if (bundle->context == EGL_NO_CONTEXT) {
        VR_ERROR("GLES 3 context creation failed: 0x%x", eglGetError_p());
        free(bundle);
        return NULL;
    }
    const EGLint surface_attributes[] = {EGL_WIDTH, 16, EGL_HEIGHT, 16, EGL_NONE};
    bundle->surface = eglCreatePbufferSurface_p(vr_display, bundle->config, surface_attributes);
    if (bundle->surface == EGL_NO_SURFACE) {
        VR_ERROR("Pbuffer creation failed: 0x%x", eglGetError_p());
        eglDestroyContext_p(vr_display, bundle->context);
        free(bundle);
        return NULL;
    }
    window->pbuffer = bundle->surface;
    if (!share && !vr_primary) vr_primary = bundle;
    VR_LOG("Created EGL context=%p display=%p config=%p", bundle->context, vr_display, bundle->config);
    return bundle;
}

gl_render_window_t *quest_vr_get_current(void) { return vr_current; }

void quest_vr_make_current(gl_render_window_t *bundle) {
    if (!bundle) {
        if (eglMakeCurrent_p(vr_display, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT))
            vr_current = NULL;
        return;
    }
    if (!eglMakeCurrent_p(vr_display, bundle->surface, bundle->surface, bundle->context)) {
        VR_ERROR("eglMakeCurrent failed: 0x%x", eglGetError_p());
        return;
    }
    if (eglGetCurrentDisplay() != vr_display || eglGetCurrentContext() != bundle->context) {
        VR_ERROR("Renderer context is not a native GLES context; OpenXR cannot use it");
        eglMakeCurrent_p(vr_display, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT);
        vr_current = NULL;
        return;
    }
    vr_current = bundle;
    update_surface(bundle);
    eglSwapInterval_p(vr_display, ((quest_vr_window *) bundle)->window_surface != EGL_NO_SURFACE
                     ? flat_swap_interval : 0);
}

void quest_vr_swap_buffers(void) {
    if (!vr_current) return;
    if (vr_current == vr_primary) quest_vr_notify_game_frame();
    if (update_surface(vr_current)) { glFlush(); return; }
    quest_vr_window *window = (quest_vr_window *) vr_current;
    if (window->window_surface != EGL_NO_SURFACE) {
        if (eglSwapBuffers_p(vr_display, window->window_surface)) {
            if (!window->flat_frame_notified) {
                window->flat_frame_notified = true;
                quest_vr_notify_flat_frame();
            }
        } else {
            VR_ERROR("Flat game panel swap failed: 0x%x", eglGetError_p());
            if (eglMakeCurrent_p(vr_display, window->pbuffer, window->pbuffer, vr_current->context)) {
                vr_current->surface = window->pbuffer;
                eglDestroySurface_p(vr_display, window->window_surface);
                window->window_surface = EGL_NO_SURFACE;
                ANativeWindow_release(vr_current->nativeSurface);
                vr_current->nativeSurface = NULL;
                window->window_failed = true;
                window->flat_frame_notified = false;
            }
        }
    } else {
        glFlush();
    }
}

void quest_vr_setup_window(void) {                                                       }
void quest_vr_swap_interval(int interval) {
    flat_swap_interval = interval;
    if (vr_current && ((quest_vr_window *) vr_current)->window_surface != EGL_NO_SURFACE)
        eglSwapInterval_p(vr_display, interval);
}

void quest_vr_terminate(void) {
    if (!vr_current) return;
    gl_render_window_t *bundle = vr_current;
    quest_vr_window *window = (quest_vr_window *) bundle;
    eglMakeCurrent_p(vr_display, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT);
    vr_current = NULL;
    if (window->window_surface != EGL_NO_SURFACE)
        eglDestroySurface_p(vr_display, window->window_surface);
    if (bundle->nativeSurface) ANativeWindow_release(bundle->nativeSurface);
    eglDestroySurface_p(vr_display, window->pbuffer);
    eglDestroyContext_p(vr_display, bundle->context);
    if (vr_primary == bundle) vr_primary = NULL;
    free(bundle);
}

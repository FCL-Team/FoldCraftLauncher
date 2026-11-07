#include <jni.h>
#include <EGL/egl.h>
#include <android/log.h>
#include <pthread.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <inttypes.h>
#include <dlfcn.h>
#include <android/native_window.h>
#include <android/native_window_jni.h>
#include "interop.h"
#include "presentation.h"

static EGLDisplay (*native_get_display)(void);
static EGLContext (*native_get_context)(void);
static EGLBoolean (*native_query_context)(EGLDisplay, EGLContext, EGLint, EGLint *);
static EGLBoolean (*native_choose_config)(EGLDisplay, const EGLint *, EGLConfig *, EGLint, EGLint *);
static pthread_once_t egl_once = PTHREAD_ONCE_INIT;
static ANativeWindow *(*native_window_from_surface)(JNIEnv *, jobject);
static void (*native_window_acquire)(ANativeWindow *);
static void (*native_window_release)(ANativeWindow *);
static pthread_once_t window_once = PTHREAD_ONCE_INIT;

static void load_native_window(void) {
    void *library = dlopen("libandroid.so", RTLD_NOW | RTLD_LOCAL);
    if (!library) return;
    native_window_from_surface = dlsym(library, "ANativeWindow_fromSurface");
    native_window_acquire = dlsym(library, "ANativeWindow_acquire");
    native_window_release = dlsym(library, "ANativeWindow_release");
}

static void load_native_egl(void) {
    void *library = dlopen("libEGL.so", RTLD_NOW | RTLD_LOCAL);
    if (!library) return;
    native_get_display = dlsym(library, "eglGetCurrentDisplay");
    native_get_context = dlsym(library, "eglGetCurrentContext");
    native_query_context = dlsym(library, "eglQueryContext");
    native_choose_config = dlsym(library, "eglChooseConfig");
}

typedef struct image_set {
    uint64_t swapchain;
    size_t count;
    uint32_t *textures;
    struct image_set *next;
} image_set;

typedef struct {
    JavaVM *android_vm;
    jobject android_activity;
    jclass android_bridge;
    jmethodID await_immersive_activity;
    jmethodID on_game_frame;
    jmethodID on_first_flat_frame;
    jmethodID on_openxr_stopped;
    bool activity_exposed;
    unsigned int live_instances;
    bool game_frame_notified;
    bool immersive;
    uint64_t surface_generation;
    ANativeWindow *flat_window;
    pthread_mutex_t mutex;
    image_set *images;
} quest_vr_state;

static quest_vr_state *get_state(void) {
    const char *address = getenv("FCL_QUEST_VR_STATE");
    return address ? (quest_vr_state *) (uintptr_t) strtoull(address, NULL, 16) : NULL;
}

bool quest_vr_is_mobileglues(void) {
    const char *renderer = getenv("FCL_VR_RENDERER");
    return renderer && strcmp(renderer, "quest_mobileglues") == 0;
}

void quest_vr_forget_images(uint64_t swapchain) {
    quest_vr_state *state = get_state();
    if (!state) return;
    pthread_mutex_lock(&state->mutex);
    image_set **slot = &state->images;
    while (*slot) {
        image_set *entry = *slot;
        if (entry->swapchain == swapchain) {
            *slot = entry->next;
            free(entry->textures);
            free(entry);
            break;
        }
        slot = &entry->next;
    }
    pthread_mutex_unlock(&state->mutex);
}

void quest_vr_register_images(uint64_t swapchain, const uint32_t *textures, size_t count) {
    quest_vr_state *state = get_state();
    if (!state) return;
    quest_vr_forget_images(swapchain);
    image_set *entry = calloc(1, sizeof(*entry));
    if (!entry) return;
    entry->textures = malloc(count * sizeof(uint32_t));
    if (!entry->textures) { free(entry); return; }
    memcpy(entry->textures, textures, count * sizeof(uint32_t));
    entry->swapchain = swapchain;
    entry->count = count;
    pthread_mutex_lock(&state->mutex);
    entry->next = state->images;
    state->images = entry;
    pthread_mutex_unlock(&state->mutex);
}

bool quest_vr_is_runtime_texture(uint32_t texture) {
    quest_vr_state *state = get_state();
    if (!state) return false;
    bool result = false;
    pthread_mutex_lock(&state->mutex);
    for (image_set *entry = state->images; entry && !result; entry = entry->next)
        for (size_t i = 0; i < entry->count; ++i)
            if (entry->textures[i] == texture) { result = true; break; }
    pthread_mutex_unlock(&state->mutex);
    return result;
}

static void fail(JNIEnv *env, const char *message) {
    jclass exception = (*env)->FindClass(env, "java/lang/IllegalStateException");
    if (exception) (*env)->ThrowNew(env, exception, message);
}

JNIEXPORT void JNICALL
Java_com_tungsten_fcl_vr_QuestVrBridge_nativeInitialize(JNIEnv *env, jclass cls, jobject activity) {
    quest_vr_state *state = get_state();
    if (!state) {
        state = calloc(1, sizeof(*state));
        if (!state) { fail(env, "Cannot allocate the Android OpenXR bridge state"); return; }
        pthread_mutex_init(&state->mutex, NULL);
        char address[2 * sizeof(uintptr_t) + 1];
        snprintf(address, sizeof(address), "%" PRIxPTR, (uintptr_t) state);
        if (setenv("FCL_QUEST_VR_STATE", address, 1)) {
            pthread_mutex_destroy(&state->mutex);
            free(state);
            fail(env, "Cannot share the Android OpenXR bridge state with the JVM");
            return;
        }
    }
    pthread_mutex_lock(&state->mutex);
    if (!state->android_vm) (*env)->GetJavaVM(env, &state->android_vm);
    if (!state->android_bridge) {
        state->await_immersive_activity = (*env)->GetStaticMethodID(env, cls, "awaitImmersiveActivity", "()Z");
        if (state->await_immersive_activity)
            state->on_game_frame = (*env)->GetStaticMethodID(env, cls, "onGameFrame", "()V");
        if (state->on_game_frame)
            state->on_first_flat_frame = (*env)->GetStaticMethodID(env, cls, "onFirstFlatFrame", "()V");
        if (state->on_first_flat_frame)
            state->on_openxr_stopped = (*env)->GetStaticMethodID(env, cls, "onOpenXrStopped", "()V");
        if (state->on_openxr_stopped)
            state->android_bridge = (*env)->NewGlobalRef(env, cls);
        if (!state->android_bridge) {
            pthread_mutex_unlock(&state->mutex);
            if (!(*env)->ExceptionCheck(env)) fail(env, "Cannot resolve the Android VR activity handoff");
            return;
        }
    }
    if (!state->android_activity) state->android_activity = (*env)->NewGlobalRef(env, activity);
    bool same = state->android_activity && (*env)->IsSameObject(env, state->android_activity, activity);
    if (!same && state->android_activity && !state->activity_exposed && !state->live_instances) {
        jobject immersive = (*env)->NewGlobalRef(env, activity);
        if (immersive) {
            jobject previous = state->android_activity;
            state->android_activity = immersive;
            (*env)->DeleteGlobalRef(env, previous);
            same = true;
        }
    }
    pthread_mutex_unlock(&state->mutex);
    if (!same && !(*env)->ExceptionCheck(env))
        fail(env, "An OpenXR Activity is already active. Restart the game process before replacing it.");
}

void quest_vr_set_flat_surface(JNIEnv *env, jobject surface) {
    quest_vr_state *state = get_state();
    if (!state) { fail(env, "Quest VR bridge is not initialized"); return; }
    pthread_once(&window_once, load_native_window);
    if (!native_window_from_surface || !native_window_acquire || !native_window_release) {
        fail(env, "Android native window entry points are unavailable");
        return;
    }
    ANativeWindow *window = surface ? native_window_from_surface(env, surface) : NULL;
    if (surface && !window) {
        if (!(*env)->ExceptionCheck(env)) fail(env, "Cannot obtain the flat game window");
        return;
    }
    pthread_mutex_lock(&state->mutex);
    ANativeWindow *previous = state->flat_window;
    state->flat_window = window;
    state->surface_generation++;
    pthread_mutex_unlock(&state->mutex);
    if (previous) native_window_release(previous);
}

JNIEXPORT void JNICALL
Java_com_tungsten_fcl_vr_QuestVrBridge_nativeSetFlatSurface(JNIEnv *env, jclass cls, jobject surface) {
    (void) cls;
    quest_vr_set_flat_surface(env, surface);
}

JNIEXPORT void JNICALL
Java_com_tungsten_fcl_vr_QuestVrBridge_nativeSetImmersive(JNIEnv *env, jclass cls, jboolean immersive) {
    (void) cls;
    quest_vr_state *state = get_state();
    if (!state) { fail(env, "Quest VR bridge is not initialized"); return; }
    pthread_mutex_lock(&state->mutex);
    state->immersive = immersive == JNI_TRUE;
    pthread_mutex_unlock(&state->mutex);
}

bool quest_vr_get_presentation(quest_vr_presentation *presentation) {
    memset(presentation, 0, sizeof(*presentation));
    quest_vr_state *state = get_state();
    if (!state) return false;
    pthread_once(&window_once, load_native_window);
    pthread_mutex_lock(&state->mutex);
    presentation->immersive = state->immersive;
    presentation->generation = state->surface_generation;
    if (state->flat_window && native_window_acquire) {
        native_window_acquire(state->flat_window);
        presentation->window = state->flat_window;
    }
    pthread_mutex_unlock(&state->mutex);
    return true;
}

static void notify_android(quest_vr_state *state, jmethodID method) {
    if (!state || !state->android_vm || !state->android_bridge || !method) return;
    JNIEnv *env = NULL;
    bool attached_here = false;
    jint status = (*state->android_vm)->GetEnv(state->android_vm, (void **) &env, JNI_VERSION_1_6);
    if (status == JNI_EDETACHED) {
        if ((*state->android_vm)->AttachCurrentThread(state->android_vm, &env, NULL) != JNI_OK) return;
        attached_here = true;
    } else if (status != JNI_OK) return;
    if (!(*env)->ExceptionCheck(env)) {
        (*env)->CallStaticVoidMethod(env, state->android_bridge, method);
        if ((*env)->ExceptionCheck(env)) {
            (*env)->ExceptionDescribe(env);
            (*env)->ExceptionClear(env);
        }
    }
    if (attached_here) (*state->android_vm)->DetachCurrentThread(state->android_vm);
}

void quest_vr_notify_game_frame(void) {
    quest_vr_state *state = get_state();
    if (!state) return;
    pthread_mutex_lock(&state->mutex);
    bool notify = !state->game_frame_notified;
    state->game_frame_notified = true;
    pthread_mutex_unlock(&state->mutex);
    if (notify) notify_android(state, state->on_game_frame);
}

void quest_vr_notify_flat_frame(void) {
    quest_vr_state *state = get_state();
    if (state) notify_android(state, state->on_first_flat_frame);
}

void quest_vr_notify_openxr_stopped(void) {
    quest_vr_state *state = get_state();
    if (state) notify_android(state, state->on_openxr_stopped);
}

void quest_vr_openxr_instance_created(void) {
    quest_vr_state *state = get_state();
    if (!state) return;
    pthread_mutex_lock(&state->mutex);
    state->live_instances++;
    pthread_mutex_unlock(&state->mutex);
}

void quest_vr_openxr_instance_destroyed(void) {
    quest_vr_state *state = get_state();
    if (!state) return;
    pthread_mutex_lock(&state->mutex);
    if (state->live_instances) state->live_instances--;
    if (!state->live_instances) state->activity_exposed = false;
    pthread_mutex_unlock(&state->mutex);
}

void quest_vr_openxr_create_failed(void) {
    quest_vr_state *state = get_state();
    if (!state) return;
    pthread_mutex_lock(&state->mutex);
    if (!state->live_instances) state->activity_exposed = false;
    pthread_mutex_unlock(&state->mutex);
}

static bool await_immersive_activity(quest_vr_state *state) {
    JNIEnv *android_env = NULL;
    bool attached_here = false;
    jint status = (*state->android_vm)->GetEnv(state->android_vm, (void **) &android_env, JNI_VERSION_1_6);
    if (status == JNI_EDETACHED) {
        if ((*state->android_vm)->AttachCurrentThread(state->android_vm, &android_env, NULL) != JNI_OK)
            return false;
        attached_here = true;
    } else if (status != JNI_OK) {
        return false;
    }
    jboolean resumed = (*android_env)->CallStaticBooleanMethod(android_env, state->android_bridge,
                                                              state->await_immersive_activity);
    if ((*android_env)->ExceptionCheck(android_env)) {
        (*android_env)->ExceptionDescribe(android_env);
        (*android_env)->ExceptionClear(android_env);
        resumed = JNI_FALSE;
    }
    if (attached_here) (*state->android_vm)->DetachCurrentThread(state->android_vm);
    return resumed == JNI_TRUE;
}

JNIEXPORT jlong JNICALL
Java_org_vivecraft_util_VLoader_getDalvikVM(JNIEnv *env, jclass cls) {
    (void) cls;
    quest_vr_state *state = get_state();
    if (!state || !state->android_vm) {
        fail(env, "Quest VR bridge was not initialized by the Android game Activity");
        return 0;
    }
    return (jlong) (uintptr_t) state->android_vm;
}

JNIEXPORT jlong JNICALL
Java_org_vivecraft_util_VLoader_getDalvikActivity(JNIEnv *env, jclass cls) {
    (void) cls;
    quest_vr_state *state = get_state();
    if (!state || !state->android_activity) { fail(env, "Quest VR Activity is unavailable"); return 0; }
    if (!await_immersive_activity(state)) {
        fail(env, "The Quest VR Activity did not become ready: launch was cancelled or the headset activity timed out");
        return 0;
    }
    pthread_mutex_lock(&state->mutex);
    state->activity_exposed = true;
    jobject activity = state->android_activity;
    pthread_mutex_unlock(&state->mutex);
    return (jlong) (uintptr_t) activity;
}

JNIEXPORT jlong JNICALL
Java_org_vivecraft_util_VLoader_getEGLDisplay(JNIEnv *env, jclass cls) {
    (void) cls;
    pthread_once(&egl_once, load_native_egl);
    EGLDisplay display = native_get_display ? native_get_display() : EGL_NO_DISPLAY;
    if (display == EGL_NO_DISPLAY) fail(env, "OpenXR needs a current native GLES display (ANGLE is unsupported)");
    return (jlong) (uintptr_t) display;
}

JNIEXPORT jlong JNICALL
Java_org_vivecraft_util_VLoader_getEGLContext(JNIEnv *env, jclass cls) {
    (void) cls;
    pthread_once(&egl_once, load_native_egl);
    EGLContext context = native_get_context ? native_get_context() : EGL_NO_CONTEXT;
    if (context == EGL_NO_CONTEXT) fail(env, "OpenXR needs the Minecraft native GLES context on its render thread");
    return (jlong) (uintptr_t) context;
}

JNIEXPORT jlong JNICALL
Java_org_vivecraft_util_VLoader_getEGLConfig(JNIEnv *env, jclass cls) {
    (void) cls;
    pthread_once(&egl_once, load_native_egl);
    EGLDisplay display = native_get_display ? native_get_display() : EGL_NO_DISPLAY;
    EGLContext context = native_get_context ? native_get_context() : EGL_NO_CONTEXT;
    EGLint config_id = 0, count = 0;
    EGLConfig config = NULL;
    if (display != EGL_NO_DISPLAY && context != EGL_NO_CONTEXT &&
        native_query_context && native_choose_config &&
        native_query_context(display, context, EGL_CONFIG_ID, &config_id)) {
        const EGLint attributes[] = {EGL_CONFIG_ID, config_id, EGL_NONE};
        if (native_choose_config(display, attributes, &config, 1, &count) && count == 1)
            return (jlong) (uintptr_t) config;
    }
    fail(env, "Could not resolve the EGLConfig that owns Minecraft's GLES context");
    return 0;
}

JNIEXPORT void JNICALL
Java_org_vivecraft_util_VLoader_setupAndroid(JNIEnv *env, jclass cls) {
    (void) cls;
    quest_vr_state *state = get_state();
    if (!state || !state->android_vm || !state->android_activity) {
        fail(env, "Quest VR bridge is not initialized");
        return;
    }
    __android_log_print(ANDROID_LOG_INFO, "FCL-QuestVR", "Vivecraft is initializing native Android OpenXR");
}

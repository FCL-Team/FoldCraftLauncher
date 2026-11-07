#include <jni.h>
#include <EGL/egl.h>
#include <GLES3/gl3.h>
#define XR_USE_PLATFORM_ANDROID
#define XR_USE_GRAPHICS_API_OPENGL_ES
#include "openxr/openxr.h"
#include "openxr/openxr_platform.h"
#include "interop.h"
#include "presentation.h"
#include "openxr_fade.h"
#include <android/log.h>
#include <dlfcn.h>
#include <pthread.h>
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>

static PFN_xrGetInstanceProcAddr real_get_proc;
static PFN_xrCreateInstance real_create_instance;
static PFN_xrCreateSession real_create_session;
static PFN_xrDestroySession real_destroy_session;
static PFN_xrEndFrame real_end_frame;
static PFN_xrDestroyInstance real_destroy_instance;
static PFN_xrCreateSwapchain real_create_swapchain;
static PFN_xrEnumerateSwapchainImages real_enumerate_images;
static PFN_xrDestroySwapchain real_destroy_swapchain;
static PFN_xrReleaseSwapchainImage real_release_image;
static pthread_once_t loader_once = PTHREAD_ONCE_INIT;
static pthread_mutex_t chain_mutex = PTHREAD_MUTEX_INITIALIZER;
static pthread_mutex_t fade_mutex = PTHREAD_MUTEX_INITIALIZER;

typedef struct fade_instance_info {
    XrInstance instance;
    struct fade_instance_info *next;
} fade_instance_info;
typedef struct fade_session_info {
    XrSession session;
    XrInstance instance;
    quest_vr_fade_state state;
    struct fade_session_info *next;
} fade_session_info;
static fade_instance_info *fade_instances;
static fade_session_info *fade_sessions;

typedef struct swapchain_info {
    XrSwapchain handle;
    uint32_t width, height, array_size;
    struct swapchain_info *next;
} swapchain_info;
static swapchain_info *chains;

static XrResult instance_result(XrResult result) {
    if (XR_FAILED(result)) quest_vr_notify_openxr_stopped();
    return result;
}

static XrResult create_instance_result(XrResult result) {
    if (XR_SUCCEEDED(result)) quest_vr_openxr_instance_created();
    else quest_vr_openxr_create_failed();
    return instance_result(result);
}

static void load_openxr(void) {
    void *loader = dlopen("libopenxr_loader.so", RTLD_NOW | RTLD_LOCAL);
    if (loader) real_get_proc = (PFN_xrGetInstanceProcAddr) dlsym(loader, "xrGetInstanceProcAddr");
    if (!real_get_proc)
        __android_log_print(ANDROID_LOG_ERROR, "FCL-QuestVR", "OpenXR loader unavailable: %s", dlerror());
}

static bool has_extension(const XrInstanceCreateInfo *info, const char *extension) {
    if (!info || !info->enabledExtensionNames) return false;
    for (uint32_t i = 0; i < info->enabledExtensionCount; ++i)
        if (info->enabledExtensionNames[i] && !strcmp(info->enabledExtensionNames[i], extension)) return true;
    return false;
}

static bool runtime_has_color_scale(void) {
    PFN_xrVoidFunction address = NULL;
    if (!real_get_proc || XR_FAILED(real_get_proc(XR_NULL_HANDLE,
            "xrEnumerateInstanceExtensionProperties", &address)) || !address) return false;
    PFN_xrEnumerateInstanceExtensionProperties enumerate = (PFN_xrEnumerateInstanceExtensionProperties) address;
    uint32_t count = 0;
    if (XR_FAILED(enumerate(NULL, 0, &count, NULL)) || !count) return false;
    XrExtensionProperties *properties = calloc(count, sizeof(*properties));
    if (!properties) return false;
    for (uint32_t i = 0; i < count; ++i) properties[i].type = XR_TYPE_EXTENSION_PROPERTIES;
    uint32_t returned = 0;
    bool found = false;
    if (XR_SUCCEEDED(enumerate(NULL, count, &returned, properties)) && returned <= count)
        for (uint32_t i = 0; i < returned; ++i)
            if (!strcmp(properties[i].extensionName, XR_KHR_COMPOSITION_LAYER_COLOR_SCALE_BIAS_EXTENSION_NAME))
                found = true;
    free(properties);
    return found;
}

static void remember_fade_instance(XrInstance instance) {
    fade_instance_info *entry = calloc(1, sizeof(*entry));
    if (!entry) return;
    entry->instance = instance;
    pthread_mutex_lock(&fade_mutex);
    entry->next = fade_instances;
    fade_instances = entry;
    pthread_mutex_unlock(&fade_mutex);
}

static XrResult XRAPI_PTR adapted_create_instance(const XrInstanceCreateInfo *info, XrInstance *instance) {
    if (!info) return create_instance_result(real_create_instance(info, instance));
    bool android_info = false;
    for (const XrBaseInStructure *next = info->next; next; next = next->next)
        if (next->type == XR_TYPE_INSTANCE_CREATE_INFO_ANDROID_KHR) android_info = true;
    bool add_android = android_info && !has_extension(info, XR_KHR_ANDROID_CREATE_INSTANCE_EXTENSION_NAME);
    const char *animation = getenv("FCL_VR_TRANSITION_ANIMATION");
    bool requested_fade = animation && !strcmp(animation, "1");
    bool color_enabled = has_extension(info, XR_KHR_COMPOSITION_LAYER_COLOR_SCALE_BIAS_EXTENSION_NAME);
    bool fade_enabled = requested_fade && (color_enabled || runtime_has_color_scale());
    bool add_color = fade_enabled && !color_enabled;
    const char **extensions = NULL;
    XrInstanceCreateInfo adapted = *info;
    if (add_android || add_color) {
        if (info->enabledExtensionCount > UINT32_MAX - 2)
            return create_instance_result(real_create_instance(info, instance));
        extensions = calloc((size_t) info->enabledExtensionCount + 2, sizeof(*extensions));
        if (!extensions && add_android) return create_instance_result(XR_ERROR_OUT_OF_MEMORY);
        if (extensions) {
            if (info->enabledExtensionCount)
                memcpy(extensions, info->enabledExtensionNames, info->enabledExtensionCount * sizeof(*extensions));
            if (add_android) extensions[adapted.enabledExtensionCount++] = XR_KHR_ANDROID_CREATE_INSTANCE_EXTENSION_NAME;
            if (add_color) extensions[adapted.enabledExtensionCount++] = XR_KHR_COMPOSITION_LAYER_COLOR_SCALE_BIAS_EXTENSION_NAME;
            adapted.enabledExtensionNames = extensions;
        } else fade_enabled = false;
    }
    XrResult result = real_create_instance(extensions ? &adapted : info, instance);
    free(extensions);
    if (XR_SUCCEEDED(result)) {
        if (fade_enabled) remember_fade_instance(*instance);
        __android_log_print(ANDROID_LOG_INFO, "FCL-QuestVR", "OpenXR compositor fade: %s",
                fade_enabled ? "enabled (300ms RGB fade-in)" : requested_fade
                    ? "unavailable; runtime has no usable color-scale extension" : "disabled by launcher setting");
    }
    return create_instance_result(result);
}

static void forget_fade_session(XrSession session) {
    pthread_mutex_lock(&fade_mutex);
    fade_session_info **slot = &fade_sessions;
    while (*slot) {
        fade_session_info *entry = *slot;
        if (entry->session == session) { *slot = entry->next; free(entry); break; }
        slot = &entry->next;
    }
    pthread_mutex_unlock(&fade_mutex);
}

static XrResult XRAPI_PTR adapted_destroy_instance(XrInstance instance) {
    XrResult result = real_destroy_instance(instance);
    if (XR_SUCCEEDED(result)) {
        pthread_mutex_lock(&fade_mutex);
        fade_instance_info **instance_slot = &fade_instances;
        while (*instance_slot) {
            fade_instance_info *entry = *instance_slot;
            if (entry->instance == instance) { *instance_slot = entry->next; free(entry); break; }
            instance_slot = &entry->next;
        }
        fade_session_info **session_slot = &fade_sessions;
        while (*session_slot) {
            fade_session_info *entry = *session_slot;
            if (entry->instance == instance) { *session_slot = entry->next; free(entry); }
            else session_slot = &entry->next;
        }
        pthread_mutex_unlock(&fade_mutex);
        quest_vr_openxr_instance_destroyed();
        quest_vr_notify_openxr_stopped();
    }
    return result;
}

static XrResult XRAPI_PTR adapted_create_session(XrInstance instance, const XrSessionCreateInfo *info,
                                                XrSession *session) {
    const XrBaseInStructure *next = info ? info->next : NULL;
    while (next) {
        if (next->type == XR_TYPE_GRAPHICS_BINDING_OPENGL_ES_ANDROID_KHR) {
            const XrGraphicsBindingOpenGLESAndroidKHR *binding = (const void *) next;
            if (binding->context == EGL_NO_CONTEXT || binding->display == EGL_NO_DISPLAY ||
                eglGetCurrentContext() != binding->context || eglGetCurrentDisplay() != binding->display) {
                __android_log_print(ANDROID_LOG_ERROR, "FCL-QuestVR",
                                    "Rejected OpenXR session: renderer does not share the native GLES context");
                return instance_result(XR_ERROR_GRAPHICS_DEVICE_INVALID);
            }
            break;
        }
        next = next->next;
    }
    XrResult result = real_create_session(instance, info, session);
    if (XR_SUCCEEDED(result)) {
        pthread_mutex_lock(&fade_mutex);
        bool enabled = false;
        for (fade_instance_info *entry = fade_instances; entry; entry = entry->next)
            if (entry->instance == instance) { enabled = true; break; }
        if (enabled) {
            fade_session_info *entry = calloc(1, sizeof(*entry));
            if (entry) {
                entry->instance = instance;
                entry->session = *session;
                entry->next = fade_sessions;
                fade_sessions = entry;
            }
        }
        pthread_mutex_unlock(&fade_mutex);
    }
    return instance_result(result);
}

static XrResult XRAPI_PTR adapted_destroy_session(XrSession session) {
    XrResult result = real_destroy_session(session);
    if (XR_SUCCEEDED(result)) forget_fade_session(session);
    return result;
}

static XrResult XRAPI_PTR adapted_end_frame(XrSession session, const XrFrameEndInfo *info) {
    quest_vr_fade_state state = {0};
    bool tracked = false;
    pthread_mutex_lock(&fade_mutex);
    for (fade_session_info *entry = fade_sessions; entry; entry = entry->next)
        if (entry->session == session) { state = entry->state; tracked = true; break; }
    pthread_mutex_unlock(&fade_mutex);
    if (!tracked || state.complete) return real_end_frame(session, info);
    bool started = state.started;
    quest_vr_fade_frame frame;
    bool adapted = quest_vr_fade_prepare(&state, info, &frame);
    XrResult result = real_end_frame(session, adapted ? &frame.info : info);
    quest_vr_fade_release(&frame);
    if (XR_SUCCEEDED(result)) {
        pthread_mutex_lock(&fade_mutex);
        for (fade_session_info *entry = fade_sessions; entry; entry = entry->next)
            if (entry->session == session) { entry->state = state; break; }
        pthread_mutex_unlock(&fade_mutex);
        if (adapted && !started)
            __android_log_print(ANDROID_LOG_INFO, "FCL-QuestVR", "OpenXR 300ms compositor fade-in started");
    }
    return result;
}

static XrResult XRAPI_PTR adapted_create_swapchain(XrSession session, const XrSwapchainCreateInfo *info,
                                                  XrSwapchain *swapchain) {
    XrResult result = real_create_swapchain(session, info, swapchain);
    if (XR_SUCCEEDED(result)) {
        swapchain_info *entry = calloc(1, sizeof(*entry));
        if (entry) {
            entry->handle = *swapchain;
            entry->width = info->width;
            entry->height = info->height;
            entry->array_size = info->arraySize;
            pthread_mutex_lock(&chain_mutex);
            entry->next = chains;
            chains = entry;
            pthread_mutex_unlock(&chain_mutex);
        }
        __android_log_print(ANDROID_LOG_INFO, "FCL-QuestVR", "OpenXR swapchain: %ux%u, layers=%u, format=%lld",
                            info->width, info->height, info->arraySize, (long long) info->format);
    }
    return result;
}

static void register_images(XrSwapchain swapchain, uint32_t count, XrSwapchainImageBaseHeader *base) {
    if (!count || base[0].type != XR_TYPE_SWAPCHAIN_IMAGE_OPENGL_ES_KHR) return;
    XrSwapchainImageOpenGLESKHR *images = (void *) base;
    uint32_t *names = malloc(count * sizeof(*names));
    if (!names) return;
    uint32_t array_size = 2;
    pthread_mutex_lock(&chain_mutex);
    for (swapchain_info *entry = chains; entry; entry = entry->next) {
        if (entry->handle == swapchain) { array_size = entry->array_size; break; }
    }
    pthread_mutex_unlock(&chain_mutex);
    for (uint32_t i = 0; i < count; ++i) names[i] = images[i].image;
    quest_vr_register_images((uint64_t) (uintptr_t) swapchain, names, count);

    if (quest_vr_is_mobileglues()) {
        void *mobileglues = dlopen("libmobileglues.so", RTLD_NOW | RTLD_LOCAL);
        void (*bind_texture)(GLenum, GLuint) = mobileglues ? dlsym(mobileglues, "glBindTexture") : NULL;
        void (*get_integer)(GLenum, GLint *) = mobileglues ? dlsym(mobileglues, "glGetIntegerv") : NULL;
        if (bind_texture && get_integer) {
            GLenum target = array_size > 1 ? GL_TEXTURE_2D_ARRAY : GL_TEXTURE_2D;
            GLenum binding = array_size > 1 ? GL_TEXTURE_BINDING_2D_ARRAY : GL_TEXTURE_BINDING_2D;
            GLint previous = 0;
            get_integer(binding, &previous);
            for (uint32_t i = 0; i < count; ++i) {
                bind_texture(target, names[i]);
            }
            bind_texture(target, (GLuint) previous);
            __android_log_print(ANDROID_LOG_INFO, "FCL-QuestVR", "Imported %u OpenXR textures into MobileGlues", count);
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "FCL-QuestVR", "MobileGlues texture import entry points missing");
        }
    }
    free(names);
}

static XrResult XRAPI_PTR adapted_enumerate_images(XrSwapchain swapchain, uint32_t capacity,
                                                  uint32_t *count, XrSwapchainImageBaseHeader *images) {
    XrResult result = real_enumerate_images(swapchain, capacity, count, images);
    if (XR_SUCCEEDED(result) && capacity && images && count && *count <= capacity)
        register_images(swapchain, *count, images);
    return result;
}

static XrResult XRAPI_PTR adapted_destroy_swapchain(XrSwapchain swapchain) {
    XrResult result = real_destroy_swapchain(swapchain);
    if (XR_SUCCEEDED(result)) {
        quest_vr_forget_images((uint64_t) (uintptr_t) swapchain);
        pthread_mutex_lock(&chain_mutex);
        swapchain_info **slot = &chains;
        while (*slot) {
            swapchain_info *entry = *slot;
            if (entry->handle == swapchain) { *slot = entry->next; free(entry); break; }
            slot = &entry->next;
        }
        pthread_mutex_unlock(&chain_mutex);
    }
    return result;
}

static XrResult XRAPI_PTR adapted_release_image(XrSwapchain swapchain, const XrSwapchainImageReleaseInfo *info) {
    glFlush();
    return real_release_image(swapchain, info);
}

XRAPI_ATTR XrResult XRAPI_CALL xrGetInstanceProcAddr(XrInstance instance, const char *name,
                                                   PFN_xrVoidFunction *function) {
    pthread_once(&loader_once, load_openxr);
    if (!real_get_proc) return instance_result(XR_ERROR_INITIALIZATION_FAILED);
    XrResult result = real_get_proc(instance, name, function);
    if (XR_FAILED(result) || !name || !function || !*function) return result;
#define INTERCEPT(NAME, FIELD, ADAPTER) \
    if (!strcmp(name, #NAME)) { FIELD = (PFN_##NAME) *function; *function = (PFN_xrVoidFunction) ADAPTER; return result; }
    INTERCEPT(xrCreateInstance, real_create_instance, adapted_create_instance)
    INTERCEPT(xrDestroyInstance, real_destroy_instance, adapted_destroy_instance)
    INTERCEPT(xrCreateSession, real_create_session, adapted_create_session)
    INTERCEPT(xrDestroySession, real_destroy_session, adapted_destroy_session)
    INTERCEPT(xrEndFrame, real_end_frame, adapted_end_frame)
    INTERCEPT(xrCreateSwapchain, real_create_swapchain, adapted_create_swapchain)
    INTERCEPT(xrEnumerateSwapchainImages, real_enumerate_images, adapted_enumerate_images)
    INTERCEPT(xrDestroySwapchain, real_destroy_swapchain, adapted_destroy_swapchain)
    INTERCEPT(xrReleaseSwapchainImage, real_release_image, adapted_release_image)
#undef INTERCEPT
    return result;
}

// Reference AAMC: https://github.com/AngelAuraMC/Amethyst-Android/blob/360d708262ff703d9b52782d20cd348410a33df5/app_pojavlauncher/src/main/jni/native_hooks/sdl_hook.c
// 移植自 ZalithLauncher2 feat/sdl3，SDL3 集成核心 hook
#include <stdbool.h>
#include <stdint.h>

#include "environ/environ.h"
#include "utils.h"
#include "native_hooks.h"

#include <bytehook.h>
#include <dlfcn.h>
#include <jni.h>
#include <stdlib.h>
#include <string.h>
#include <android/log.h>

#define LOG_TO_I(...) __android_log_print(ANDROID_LOG_INFO, "FCL", __VA_ARGS__)
#define LOG_TO_W(...) __android_log_print(ANDROID_LOG_WARN, "FCL", __VA_ARGS__)
#define LOG_TO_E(...) __android_log_print(ANDROID_LOG_ERROR, "FCL", __VA_ARGS__)

// --- 最小 SDL3 声明（仅 hook 所需；完整 headers 由 lwjgl-sdl 绑定侧提供） ---
typedef uint32_t SDL_InitFlags;
typedef struct SDL_Window SDL_Window;
typedef struct SDL_Rect { int x, y, w, h; } SDL_Rect;

bool SDL_InitSubSystem(SDL_InitFlags flags);
bool SDL_SetHint(const char *name, const char *value);
bool SDL_SetTextInputArea(SDL_Window *window, const SDL_Rect *rect, int cursor);
void SDL_SetError(const char *fmt, ...);
const char *SDL_GetError(void);
SDL_Window *SDL_GetWindowFromEvent(const void *event);
SDL_Window *SDL_GetWindowFromID(uint32_t id);
bool SDL_GL_SetAttribute(int attr, int value);
void *SDL_LoadObject(const char *path);
void SDL_UnloadObject(void *handle);
void *SDL_LoadFunction(void *handle, const char *name);
SDL_Window *SDL_CreateWindow(const char *title, int w, int h, uint32_t flags);
SDL_Window *SDL_CreateWindowWithProperties(uint32_t props);
void SDL_DestroyWindow(SDL_Window *window);

// egl_bridge.c（libpojavexec.so），SDL 路径下经 EGL 交换代理计帧
void calculateFPS(void);

DECL_DLSYM(SDL_InitSubSystem)
DECL_DLSYM(SDL_SetHint);
DECL_DLSYM(SDL_SetTextInputArea);
DECL_DLSYM(SDL_SetError);
DECL_DLSYM(SDL_GetError);
DECL_DLSYM(SDL_GetWindowFromEvent)
DECL_DLSYM(SDL_GetWindowFromID)
DECL_DLSYM(SDL_GL_SetAttribute)
DECL_DLSYM(SDL_LoadObject)
DECL_DLSYM(SDL_UnloadObject)
DECL_DLSYM(SDL_LoadFunction)
DECL_DLSYM(SDL_CreateWindow)
DECL_DLSYM(SDL_CreateWindowWithProperties)
DECL_DLSYM(SDL_DestroyWindow)

typedef void *EGLDisplay;
typedef void *EGLConfig;
typedef int EGLint;
typedef int EGLBoolean;
typedef EGLBoolean (*eglChooseConfig_t)(EGLDisplay dpy, const EGLint *attrib_list, EGLConfig *configs,
                                        EGLint config_size, EGLint *num_config);
typedef void *(*eglCreateContext_t)(EGLDisplay dpy, EGLConfig config, void *share, const EGLint *attrib_list);
typedef EGLBoolean (*eglSwapBuffers_t)(EGLDisplay dpy, void *surface);

// EGL 常量（EGL/egl.h），避免引入完整 EGL 头
#define EGL_NONE              0x3038
#define EGL_RENDERABLE_TYPE   0x3040
#define EGL_OPENGL_BIT        0x0008
#define EGL_OPENGL_ES2_BIT    0x0004
#define EGL_OPENGL_ES3_BIT    0x0040
#define EGL_CONTEXT_CLIENT_VERSION    0x3098
#define EGL_CONTEXT_MAJOR_VERSION_KHR 0x30FB
#define EGL_CONTEXT_MINOR_VERSION_KHR 0x30FC



// --- SDL 事件窗口解析修正 ---

// SDL 的 Android 后端中，鼠标焦点（mouse->focus）会被 SDL_UpdateMouseFocus 的坐标越界
// 判定意外清除（虚拟鼠标坐标经分辨率缩放后可超过 SDL window 尺寸）
// Android 同一时刻只会有一个窗口，事件解析失败时回落为之前成功解析出的唯一窗口

static SDL_Window *sdlLastEventWindow = NULL;

static SDL_Window *custom_SDL_GetWindowFromEvent_Func(const void *event) {
    SDL_Window *window = BYTEHOOK_CALL_PREV(custom_SDL_GetWindowFromEvent_Func, SDL_GetWindowFromEvent_t, event);
    if (window != NULL) {
        sdlLastEventWindow = window;
    } else if (sdlLastEventWindow != NULL) {
        window = sdlLastEventWindow;
    }
    BYTEHOOK_POP_STACK();
    return window;
}

static SDL_Window *custom_SDL_GetWindowFromID_Func(uint32_t id) {
    SDL_Window *window = BYTEHOOK_CALL_PREV(custom_SDL_GetWindowFromID_Func, SDL_GetWindowFromID_t, id);
    if (window != NULL) {
        sdlLastEventWindow = window;
    } else if (sdlLastEventWindow != NULL) {
        window = sdlLastEventWindow;
    }
    BYTEHOOK_POP_STACK();
    return window;
}

// --- SDL 事件窗口解析修正 ---

// --- 移动渲染器（ES 实现）下 SDL 创建 GL 上下文的宿主 EGL 兼容 ---

// 部分宿主 libEGL 不接受 RENDERABLE_TYPE 携带 ES3_BIT/OPENGL_BIT 的请求。
// 统一归一化为 ES2_BIT 以放宽 config 匹配；实际上下文版本由后续的
// EGL_CONTEXT_CLIENT_VERSION 决定，不受影响。
static EGLBoolean normalizeEglChooseConfigList(const EGLint *attrib_list, EGLint *fixed, int cap) {
    if (attrib_list == NULL) return 0;
    int n = 0;
    for (int i = 0; n < cap - 2; i += 2) {
        EGLint attr = attrib_list[i];
        EGLint val = attrib_list[i + 1];
        if (attr == EGL_NONE) {
            fixed[n] = EGL_NONE;
            fixed[n + 1] = 0;
            n += 2;
            break;
        }
        if (attr == EGL_RENDERABLE_TYPE) {
            if ((val & (EGL_OPENGL_ES3_BIT | EGL_OPENGL_BIT)) != 0 && (val & EGL_OPENGL_ES2_BIT) == 0) {
                val = (val & ~(EGL_OPENGL_ES3_BIT | EGL_OPENGL_BIT)) | EGL_OPENGL_ES2_BIT;
            }
        }
        fixed[n] = attr;
        fixed[n + 1] = val;
        n += 2;
    }
    return n > 0;
}

// 部分宿主 libEGL 不识别 EGL_CONTEXT_MAJOR/MINOR_VERSION_KHR，
// 属性表携带即报 EGL_BAD_ATTRIBUTE。此处将其剔除，仅保留 CLIENT_VERSION：
// 对 "≥3.0" 类请求，驱动本就返回其支持的最高 3.x 版本，语义不变。
// 返回请求的主版本号供降级重试使用（无版本属性时为 0）。
static int normalizeEglContextAttribs(const EGLint *attrib_list, EGLint *fixed, int cap) {
    int version = 0;
    if (attrib_list == NULL) return 0;
    int n = 0;
    for (int i = 0; n < cap - 2; i += 2) {
        EGLint attr = attrib_list[i];
        EGLint val = attrib_list[i + 1];
        if (attr == EGL_NONE) {
            fixed[n] = EGL_NONE;
            fixed[n + 1] = 0;
            n += 2;
            break;
        }
        if (attr == EGL_CONTEXT_MAJOR_VERSION_KHR) {
            // 版本信息并入 CLIENT_VERSION 后丢弃本项
            if (version == 0) version = val;
            continue;
        }
        if (attr == EGL_CONTEXT_MINOR_VERSION_KHR) continue;
        if (attr == EGL_CONTEXT_CLIENT_VERSION && version == 0) version = val;
        fixed[n] = attr;
        fixed[n + 1] = val;
        n += 2;
    }
    return version;
}

// --- SDL 的 EGL 函数解析接管 ---
// SDL 经 SDL_LoadFunction 取得 EGL 函数指针后直接调用（不走 PLT），
// 故在其解析 eglChooseConfig/eglCreateContext 时注入代理，
// 使上述归一化对所有调用路径生效。
static eglChooseConfig_t sOrigEglChooseConfig = NULL;
static eglCreateContext_t sOrigEglCreateContext = NULL;
static eglSwapBuffers_t sOrigEglSwapBuffers = NULL;

static void *proxyEglCreateContext(EGLDisplay dpy, EGLConfig config, void *share, const EGLint *attrib_list) {
    EGLint fixed[64];
    const EGLint *use_list = attrib_list;
    int version = normalizeEglContextAttribs(attrib_list, fixed, 64);
    if (version > 0) use_list = fixed;

    void *ctx = sOrigEglCreateContext(dpy, config, share, use_list);
    if (ctx == NULL && version > 2) {
        // ES3 上下文创建失败时的降级重试
        LOG_TO_W("SDL_Hook: eglCreateContext failed with CV=%d, retrying with CV=2", version);
        EGLint es2[3] = {EGL_CONTEXT_CLIENT_VERSION, 2, EGL_NONE};
        ctx = sOrigEglCreateContext(dpy, config, share, es2);
    }
    return ctx;
}

static EGLBoolean proxyEglChooseConfig(EGLDisplay dpy, const EGLint *attrib_list, EGLConfig *configs,
                                       EGLint config_size, EGLint *num_config) {
    // 归一化 RENDERABLE_TYPE 后转发（见 normalizeEglChooseConfigList）
    EGLint fixed[64];
    const EGLint *use_list = attrib_list;
    if (normalizeEglChooseConfigList(attrib_list, fixed, 64)) {
        use_list = fixed;
    }
    return sOrigEglChooseConfig(dpy, use_list, configs, config_size, num_config);
}

static EGLBoolean proxyEglSwapBuffers(EGLDisplay dpy, void *surface) {
    calculateFPS();
    return sOrigEglSwapBuffers(dpy, surface);
}

// 接管 SDL 的 EGL 函数解析，注入上述代理
static void *custom_SDL_LoadFunction_Func(void *handle, const char *name) {
    void *r = BYTEHOOK_CALL_PREV(custom_SDL_LoadFunction_Func, SDL_LoadFunction_t, handle, name);
    BYTEHOOK_POP_STACK();
    if (name != NULL) {
        if (strcmp(name, "eglChooseConfig") == 0) {
            if (sOrigEglChooseConfig == NULL && r != NULL) {
                sOrigEglChooseConfig = (eglChooseConfig_t) r;
            }
            r = (void *) proxyEglChooseConfig;
        } else if (strcmp(name, "eglCreateContext") == 0) {
            if (sOrigEglCreateContext == NULL && r != NULL) {
                sOrigEglCreateContext = (eglCreateContext_t) r;
            }
            r = (void *) proxyEglCreateContext;
        } else if (strcmp(name, "eglSwapBuffers") == 0) {
            if (sOrigEglSwapBuffers == NULL && r != NULL) {
                sOrigEglSwapBuffers = (eglSwapBuffers_t) r;
            }
            r = (void *) proxyEglSwapBuffers;
        }
    }
    return r;
}

// --- Vulkan 加载器一致性 ---
// 启动器经 EGLBridge 将 LWJGL 的 Vulkan 句柄重定向到私有命名空间中的
// 加载器副本（Turnip 链路，句柄记录于 VULKAN_PTR 环境变量）。
// MC 26.3 起 RenderPearl 要求 SDL 与 LWJGL 使用同一加载器实例
// （校验 vkGetInstanceProcAddr 指针一致），而 SDL 仅能按路径加载，
// 无法触及该私有实例。此处在 SDL 加载 Vulkan loader 时改还 VULKAN_PTR
// 句柄；对应句柄的引用计数由启动器持有，忽略 SDL 侧的卸载。
static void *custom_SDL_LoadObject_Func(const char *path) {
    if (path != NULL && strstr(path, "libvulkan") != NULL) {
        const char *vkptr = getenv("VULKAN_PTR");
        if (vkptr != NULL && vkptr[0] != '\0') {
            void *handle = (void *) (uintptr_t) strtoull(vkptr, NULL, 16);
            if (handle != NULL) return handle;
        }
    }
    void *r = BYTEHOOK_CALL_PREV(custom_SDL_LoadObject_Func, SDL_LoadObject_t, path);
    BYTEHOOK_POP_STACK();
    return r;
}

static void custom_SDL_UnloadObject_Func(void *handle) {
    const char *vkptr = getenv("VULKAN_PTR");
    if (vkptr != NULL && vkptr[0] != '\0') {
        void *vulkan_handle = (void *) (uintptr_t) strtoull(vkptr, NULL, 16);
        if (handle == vulkan_handle) return;
    }
    BYTEHOOK_CALL_PREV(custom_SDL_UnloadObject_Func, SDL_UnloadObject_t, handle);
    BYTEHOOK_POP_STACK();
}

// 首个成功创建的 SDL 窗口，后续创建请求将重定向到它
static SDL_Window *sPrimaryWindow = NULL;

static void custom_SDL_DestroyWindow_Func(SDL_Window *window) {
    // 主窗口销毁后清除记录，后续创建请求恢复正常创建流程
    if (window == sPrimaryWindow) sPrimaryWindow = NULL;
    BYTEHOOK_CALL_PREV(custom_SDL_DestroyWindow_Func, SDL_DestroyWindow_t, window);
    BYTEHOOK_POP_STACK();
}

static bool custom_SDL_InitSubSystem_Func(SDL_InitFlags flags) {
    // Call notifyLauncher on SDL_InitSubSystem, this sets up all the JNI stuff needed by SDL.
    TRY_ATTACH_ENV(dvm_env, pojav_environ->dalvikJavaVMPtr, "SDL_InitSubSystem failed!",
            SET_DLSYM_PTR(dlopen("libSDL3.so", RTLD_NOLOAD), SDL_SetError);
            if (SDL_SetError_p) SDL_SetError_p("Failed to load SDL launcher integration android-side. This is not an SDL bug, please contact the launcher developer.");
            return false;
            );

    // Just in case of bozo
    jint safeFlags;
    if (flags > INT32_MAX) {
        safeFlags = -1;
    } else safeFlags = (jint)flags;

    notifyLauncher(dvm_env, NOTIF_TYPE_SDL, (int[]){ACTION_INIT_LAUNCHER_INTEGRATION, safeFlags}, 2);

    // This is the normal for the launcher, the default in SDL is false.
    SET_DLSYM_PTR(dlopen("libSDL3.so", RTLD_NOLOAD), SDL_SetHint);
    if (SDL_SetHint_p) SDL_SetHint_p("SDL_RETURN_KEY_HIDES_IME", "true");
    // FIXME: MobileGlues has issues with passing in the proper EGL params to make this work
    const char *egl = getenv("POJAVEXEC_EGL");
    if (egl && strcmp(egl, "libmobileglues.so") == 0) {
        SDL_SetHint_p("SDL_OPENGL_FORCE_SRGB_FRAMEBUFFER", "0");
    }
    // MC 按桌面惯例设置 SDL_ENABLE_SCREEN_KEYBOARD=0 来禁用平台软键盘（改用自绘 IME UI），
    // 但移动端依赖 SDL 唤起输入法；MC 在 SDL_Init 之前设置此 hint，
    // 本 hook 于 SDL_Init 时执行，此处覆盖回启用。
    if (SDL_SetHint_p) SDL_SetHint_p("SDL_ENABLE_SCREEN_KEYBOARD", "1");

    // Call original func after doing all the needed setup
    bool r = BYTEHOOK_CALL_PREV(custom_SDL_InitSubSystem_Func, SDL_InitSubSystem_t, flags);
    if (!r){
        SET_DLSYM_PTR(dlopen("libSDL3.so", RTLD_NOLOAD), SDL_GetError);
        LOG_TO_E("SDL_Hook: SDL_InitSubsystem Error: %s", SDL_GetError_p());
    }
    BYTEHOOK_POP_STACK();
    return r;
}

// 移动渲染器均为 OpenGL ES 实现，而游戏按桌面 GL 惯例初始化 SDL，
// 非 ES 的 profile 请求会被宿主拒绝。因此在每次窗口创建前
// 将 GL profile 强制为 ES。
static void forceEglProfileEs(void) {
    SET_DLSYM_PTR(dlopen("libSDL3.so", RTLD_NOLOAD), SDL_GL_SetAttribute);
    if (SDL_GL_SetAttribute_p) {
        SDL_GL_SetAttribute_p(20 /* SDL_GL_CONTEXT_PROFILE_MASK */, 4 /* SDL_GL_CONTEXT_PROFILE_ES */);
    }
}

// --- Android 单窗口约束下的主窗口复用 ---

// SDL Android 后端同一进程内只支持一个窗口，而 MC 26.3 ss9 起 RenderPearl 在设备
// 初始化时会先创建一个隐藏工具窗口（GL 上下文依附其上），随后的主窗口创建将被拒绝
// 销毁工具窗口又会使其上的 GL surface 失效。故将后续创建请求重定向到首个窗口。

// 尺寸与方向均无需额外处理：
// 前者由 Android Surface 决定（创建时即取 Surface 尺寸，与请求值无关）
// 后者由 SDL_ORIENTATIONS hint 统一控制。
static SDL_Window *reusePrimaryWindow(void) {
    LOG_TO_I("SDL_Hook: reusing primary window %p", sPrimaryWindow);
    return sPrimaryWindow;
}

static SDL_Window *custom_SDL_CreateWindow_Func(const char *title, int w, int h, uint32_t flags) {
    forceEglProfileEs();
    if (sPrimaryWindow != NULL) return reusePrimaryWindow();
    SDL_Window *wnd = BYTEHOOK_CALL_PREV(custom_SDL_CreateWindow_Func, SDL_CreateWindow_t, title, w, h, flags);
    if (wnd != NULL) sPrimaryWindow = wnd;
    BYTEHOOK_POP_STACK();
    return wnd;
}

static SDL_Window *custom_SDL_CreateWindowWithProperties_Func(uint32_t props) {
    forceEglProfileEs();
    if (sPrimaryWindow != NULL) return reusePrimaryWindow();
    SDL_Window *wnd = BYTEHOOK_CALL_PREV(custom_SDL_CreateWindowWithProperties_Func, SDL_CreateWindowWithProperties_t, props);
    if (wnd != NULL) sPrimaryWindow = wnd;
    BYTEHOOK_POP_STACK();
    return wnd;
}

void create_sdl_hooks(bytehook_hook_all_t bytehook_hook_all_p) {
    // Don't set callee_path_name to anything besides NULL or else it won't be able to find the symbol
    bytehook_stub_t stub_SDL_InitSubSystem = bytehook_hook_all_p(NULL, "SDL_InitSubSystem", &custom_SDL_InitSubSystem_Func, NULL, NULL);
    bytehook_stub_t stub_SDL_GetWindowFromEvent = bytehook_hook_all_p(NULL, "SDL_GetWindowFromEvent", &custom_SDL_GetWindowFromEvent_Func, NULL, NULL);
    bytehook_stub_t stub_SDL_GetWindowFromID = bytehook_hook_all_p(NULL, "SDL_GetWindowFromID", &custom_SDL_GetWindowFromID_Func, NULL, NULL);
    // 窗口创建前强制 ES profile（覆盖 SDL3 的两种窗口创建入口）
    bytehook_stub_t stub_SDL_CreateWindow = bytehook_hook_all_p(NULL, "SDL_CreateWindow", &custom_SDL_CreateWindow_Func, NULL, NULL);
    bytehook_stub_t stub_SDL_CreateWindowWithProperties = bytehook_hook_all_p(NULL, "SDL_CreateWindowWithProperties", &custom_SDL_CreateWindowWithProperties_Func, NULL, NULL);
    // 接管 SDL 的 EGL 函数解析，注入归一化代理
    bytehook_stub_t stub_SDL_LoadFunction = bytehook_hook_all_p(NULL, "SDL_LoadFunction", &custom_SDL_LoadFunction_Func, NULL, NULL);
    // Vulkan 加载器一致性：SDL 侧改用启动器重定向的加载器句柄
    bytehook_stub_t stub_SDL_LoadObject = bytehook_hook_all_p(NULL, "SDL_LoadObject", &custom_SDL_LoadObject_Func, NULL, NULL);
    bytehook_stub_t stub_SDL_UnloadObject = bytehook_hook_all_p(NULL, "SDL_UnloadObject", &custom_SDL_UnloadObject_Func, NULL, NULL);
    // 主窗口销毁跟踪，配合窗口复用（见 custom_SDL_DestroyWindow_Func）
    bytehook_stub_t stub_SDL_DestroyWindow = bytehook_hook_all_p(NULL, "SDL_DestroyWindow", &custom_SDL_DestroyWindow_Func, NULL, NULL);
    LOG_TO_I("SDL_Hook: Successfully initialized SDL hooks, stubs: InitSubSystem=%p GetWindowFromEvent=%p GetWindowFromID=%p LoadFunction=%p CreateWindow=%p CreateWindowWithProps=%p LoadObject=%p UnloadObject=%p DestroyWindow=%p", stub_SDL_InitSubSystem, stub_SDL_GetWindowFromEvent, stub_SDL_GetWindowFromID, stub_SDL_LoadFunction, stub_SDL_CreateWindow, stub_SDL_CreateWindowWithProperties, stub_SDL_LoadObject, stub_SDL_UnloadObject, stub_SDL_DestroyWindow);
}
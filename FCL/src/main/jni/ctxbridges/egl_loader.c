//
// Created by maks on 21.09.2022.
//
#include <stdlib.h>
#include <dlfcn.h>
#include <string.h>
#include "egl_loader.h"
#include "loader_dlopen.h"

// 逃逸命名空间：定义于此、经 global_state.h 声明，由 loader_dlopen 兜底 dlopen 失败
struct android_namespace_t* app_escapeNs;

EGLBoolean (*eglMakeCurrent_p) (EGLDisplay dpy, EGLSurface draw, EGLSurface read, EGLContext ctx);
EGLBoolean (*eglDestroyContext_p) (EGLDisplay dpy, EGLContext ctx);
EGLBoolean (*eglDestroySurface_p) (EGLDisplay dpy, EGLSurface surface);
EGLBoolean (*eglTerminate_p) (EGLDisplay dpy);
EGLBoolean (*eglReleaseThread_p) (void);
EGLContext (*eglGetCurrentContext_p) (void);
EGLDisplay (*eglGetDisplay_p) (NativeDisplayType display);
EGLBoolean (*eglInitialize_p) (EGLDisplay dpy, EGLint *major, EGLint *minor);
EGLBoolean (*eglChooseConfig_p) (EGLDisplay dpy, const EGLint *attrib_list, EGLConfig *configs, EGLint config_size, EGLint *num_config);
EGLBoolean (*eglGetConfigAttrib_p) (EGLDisplay dpy, EGLConfig config, EGLint attribute, EGLint *value);
EGLBoolean (*eglBindAPI_p) (EGLenum api);
EGLSurface (*eglCreatePbufferSurface_p) (EGLDisplay dpy, EGLConfig config, const EGLint *attrib_list);
EGLSurface (*eglCreateWindowSurface_p) (EGLDisplay dpy, EGLConfig config, NativeWindowType window, const EGLint *attrib_list);
EGLBoolean (*eglSwapBuffers_p) (EGLDisplay dpy, EGLSurface draw);
EGLint (*eglGetError_p) (void);
EGLContext (*eglCreateContext_p) (EGLDisplay dpy, EGLConfig config, EGLContext share_list, const EGLint *attrib_list);
EGLBoolean (*eglSwapInterval_p) (EGLDisplay dpy, EGLint interval);
EGLSurface (*eglGetCurrentSurface_p) (EGLint readdraw);
EGLBoolean (*eglQuerySurface_p)( 	EGLDisplay display,
                                    EGLSurface surface,
                                    EGLint attribute,
                                    EGLint * value);
__eglMustCastToProperFunctionPointerType (*eglGetProcAddress_p) (const char *procname);

// SFPEW 包装生命周期入口；非 SFPEW 渲染器保持 NULL，调用方回落系统 EGL
EGLBoolean (*sfpew_eglMakeCurrent_p) (EGLDisplay dpy, EGLSurface draw, EGLSurface read, EGLContext ctx);
EGLBoolean (*sfpew_eglSwapBuffers_p) (EGLDisplay dpy, EGLSurface draw);

bool dlsym_EGL() {
    char* gles = getenv("LIBGL_GLES");
    char* eglName = (strncmp(gles ? gles : "", "libGLESv2_angle.so", 18) == 0) ? "libEGL_angle.so" : getenv("POJAVEXEC_EGL");
    // 普通 dlopen 失败时 loader_dlopen 内部会改用逃逸命名空间重试（mesa 等随驱动一起打包的库）
    void* dl_handle = loader_dlopen(eglName,"libEGL.so", RTLD_LOCAL|RTLD_LAZY);
    if(dl_handle == NULL) return false;
    eglGetProcAddress_p = dlsym(dl_handle, "eglGetProcAddress");
    if(eglGetProcAddress_p == NULL) {
        printf("%s\n", dlerror());
        return false;
    }
    eglBindAPI_p = (void*) eglGetProcAddress_p("eglBindAPI");
    eglChooseConfig_p = (void*) eglGetProcAddress_p("eglChooseConfig");
    eglCreateContext_p = (void*) eglGetProcAddress_p("eglCreateContext");
    eglCreatePbufferSurface_p = (void*) eglGetProcAddress_p("eglCreatePbufferSurface");
    eglCreateWindowSurface_p = (void*) eglGetProcAddress_p("eglCreateWindowSurface");
    eglDestroyContext_p = (void*) eglGetProcAddress_p("eglDestroyContext");
    eglDestroySurface_p = (void*) eglGetProcAddress_p("eglDestroySurface");
    eglGetConfigAttrib_p = (void*) eglGetProcAddress_p("eglGetConfigAttrib");
    eglGetCurrentContext_p = (void*) eglGetProcAddress_p("eglGetCurrentContext");
    eglGetDisplay_p = (void*) eglGetProcAddress_p("eglGetDisplay");
    eglGetError_p = (void*) eglGetProcAddress_p("eglGetError");
    eglInitialize_p = (void*) eglGetProcAddress_p("eglInitialize");
    eglMakeCurrent_p = (void*) eglGetProcAddress_p("eglMakeCurrent");
    eglSwapBuffers_p = (void*) eglGetProcAddress_p("eglSwapBuffers");
    eglReleaseThread_p = (void*) eglGetProcAddress_p("eglReleaseThread");
    eglSwapInterval_p = (void*) eglGetProcAddress_p("eglSwapInterval");
    eglTerminate_p = (void*) eglGetProcAddress_p("eglTerminate");
    eglGetCurrentSurface_p = (void*) eglGetProcAddress_p("eglGetCurrentSurface");
    eglQuerySurface_p = (void*) eglGetProcAddress_p("eglQuerySurface");

    // SFPEW 兼容层启用时（Java 侧注入 SFPEW_LAYER=1）按文档宿主集成指引解析包装生命周期入口，
    // makeCurrent/swap 经包装层记录上下文切换、swap 前清空立即模式批次；解析失败回落系统入口（strict 模式仍可逐入口对账）
    if (getenv("SFPEW_LAYER") != NULL) {
        void* wrapper = dlopen("libSimpleFPEWrapper.so", RTLD_NOW | RTLD_LOCAL);
        void* (*sfpew_get_proc)(const char*) =
                wrapper != NULL ? (void* (*)(const char*)) dlsym(wrapper, "eglGetProcAddress") : NULL;
        if (sfpew_get_proc != NULL) {
            sfpew_eglMakeCurrent_p = (void*) sfpew_get_proc("eglMakeCurrent");
            sfpew_eglSwapBuffers_p = (void*) sfpew_get_proc("eglSwapBuffers");
        }
        if (sfpew_eglMakeCurrent_p == NULL || sfpew_eglSwapBuffers_p == NULL) {
            printf("EGLLoader: SFPEW lifecycle resolution failed, falling back to system EGL\n");
            sfpew_eglMakeCurrent_p = NULL;
            sfpew_eglSwapBuffers_p = NULL;
        }
    }
    return true;
}

// 供 glxshim 等外部库转发 eglGetProcAddress
__attribute__((visibility("default")))
void *getProcAddress(const char *procname) {
    // SDL 路径不经过 pojavInitOpenGL，首次调用时补齐 EGL 符号解析
    if (eglGetProcAddress_p == NULL && !dlsym_EGL()) return NULL;
    return eglGetProcAddress_p(procname);
}

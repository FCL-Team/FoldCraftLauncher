//
// Created by maks on 21.09.2022.
//
#include <EGL/egl.h>
#include <stdbool.h>
#ifndef POJAVLAUNCHER_EGL_LOADER_H
#define POJAVLAUNCHER_EGL_LOADER_H

extern EGLBoolean (*eglMakeCurrent_p) (EGLDisplay dpy, EGLSurface draw, EGLSurface read, EGLContext ctx);
extern EGLBoolean (*eglDestroyContext_p) (EGLDisplay dpy, EGLContext ctx);
extern EGLBoolean (*eglDestroySurface_p) (EGLDisplay dpy, EGLSurface surface);
extern EGLBoolean (*eglTerminate_p) (EGLDisplay dpy);
extern EGLBoolean (*eglReleaseThread_p) (void);
extern EGLContext (*eglGetCurrentContext_p) (void);
extern EGLDisplay (*eglGetDisplay_p) (NativeDisplayType display);
extern EGLBoolean (*eglInitialize_p) (EGLDisplay dpy, EGLint *major, EGLint *minor);
extern EGLBoolean (*eglChooseConfig_p) (EGLDisplay dpy, const EGLint *attrib_list, EGLConfig *configs, EGLint config_size, EGLint *num_config);
extern EGLBoolean (*eglGetConfigAttrib_p) (EGLDisplay dpy, EGLConfig config, EGLint attribute, EGLint *value);
extern EGLBoolean (*eglBindAPI_p) (EGLenum api);
extern EGLSurface (*eglCreatePbufferSurface_p) (EGLDisplay dpy, EGLConfig config, const EGLint *attrib_list);
extern EGLSurface (*eglCreateWindowSurface_p) (EGLDisplay dpy, EGLConfig config, NativeWindowType window, const EGLint *attrib_list);
extern EGLBoolean (*eglSwapBuffers_p) (EGLDisplay dpy, EGLSurface draw);
extern EGLint (*eglGetError_p) (void);
extern EGLContext (*eglCreateContext_p) (EGLDisplay dpy, EGLConfig config, EGLContext share_list, const EGLint *attrib_list);
extern EGLBoolean (*eglSwapInterval_p) (EGLDisplay dpy, EGLint interval);
extern EGLSurface (*eglGetCurrentSurface_p) (EGLint readdraw);
extern __eglMustCastToProperFunctionPointerType (*eglGetProcAddress_p) (const char *procname);
extern EGLBoolean (*eglQuerySurface_p)( 	EGLDisplay display,
                                           EGLSurface surface,
                                           EGLint attribute,
                                           EGLint * value);

// SFPEW 兼容层（SFPEW_LAYER=1 时启用）：从 libSimpleFPEWrapper.so 解析的包装生命周期入口。
// 包装层在 makeCurrent 时记录线程本地上下文观测、swap 前清空立即模式批次；未启用时为 NULL
extern EGLBoolean (*sfpew_eglMakeCurrent_p) (EGLDisplay dpy, EGLSurface draw, EGLSurface read, EGLContext ctx);
extern EGLBoolean (*sfpew_eglSwapBuffers_p) (EGLDisplay dpy, EGLSurface draw);
// 建/毁上下文同样是包装层入口（经其 eglGetProcAddress 解析）：native-profile 逐上下文
// 自动开关以包装层看到建上下文请求为前提。旧版包装库未提供时保持 NULL，建/毁回落底座 EGL
extern EGLContext (*sfpew_eglCreateContext_p) (EGLDisplay dpy, EGLConfig config, EGLContext share_list, const EGLint *attrib_list);
extern EGLBoolean (*sfpew_eglDestroyContext_p) (EGLDisplay dpy, EGLContext ctx);

bool dlsym_EGL();
void* getProcAddress(const char* procname);

#endif //POJAVLAUNCHER_EGL_LOADER_H

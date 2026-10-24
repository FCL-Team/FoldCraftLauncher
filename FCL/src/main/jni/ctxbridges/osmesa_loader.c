//
// Created by maks on 21.09.2022.
//
#include <stdio.h>
#include <stdlib.h>
#include <dlfcn.h>
#include "loader_dlopen.h"
#include "osmesa_loader.h"
#include "environ/environ.h"
#include "virgl/virgl.h"

GLboolean (*OSMesaMakeCurrent_p) (OSMesaContext ctx, void *buffer, GLenum type,
                                         GLsizei width, GLsizei height);
OSMesaContext (*OSMesaGetCurrentContext_p) (void);
OSMesaContext  (*OSMesaCreateContext_p) (GLenum format, OSMesaContext sharelist);
void (*OSMesaDestroyContext_p) (OSMesaContext ctx);
void (*OSMesaPixelStore_p) ( GLint pname, GLint value );
GLubyte* (*glGetString_p) (GLenum name);
void (*glFinish_p) (void);
void (*glClearColor_p) (GLclampf red, GLclampf green, GLclampf blue, GLclampf alpha);
void (*glClear_p) (GLbitfield mask);
void (*glReadPixels_p) (GLint x, GLint y, GLsizei width, GLsizei height, GLenum format, GLenum type, void * data);
void* (*OSMesaGetProcAddress_p)(const char* funcName);

bool dlsym_OSMesa() {
    void *dl_handle = NULL;
    // 插件渲染器经 LIB_MESA_NAME 提供插件目录内的绝对路径；裸库名只有库位于
    // 主 APK 目录（旧内置布局）时才能被类加载器命名空间解析到
    char *lib_name = getenv("LIB_MESA_NAME");
    if (lib_name == NULL || *lib_name == '\0') {
        lib_name = (pojav_environ->config_renderer == RENDERER_VIRGL)
                   ? "libOSMesa_81.so" : "libOSMesa_8.so";
    }
    dl_handle = dlopen(lib_name, RTLD_LOCAL | RTLD_LAZY);
    if (dl_handle == NULL) return false;
    OSMesaGetProcAddress_p = dlsym(dl_handle, "OSMesaGetProcAddress");
    if (OSMesaGetProcAddress_p == NULL) {
        printf("%s\n", dlerror());
        return false;
    }
    OSMesaMakeCurrent_p = OSMesaGetProcAddress_p("OSMesaMakeCurrent");
    OSMesaGetCurrentContext_p = OSMesaGetProcAddress_p("OSMesaGetCurrentContext");
    OSMesaCreateContext_p = OSMesaGetProcAddress_p("OSMesaCreateContext");
    OSMesaDestroyContext_p = OSMesaGetProcAddress_p("OSMesaDestroyContext");
    OSMesaPixelStore_p = OSMesaGetProcAddress_p("OSMesaPixelStore");
    glGetString_p = OSMesaGetProcAddress_p("glGetString");
    glClearColor_p = OSMesaGetProcAddress_p("glClearColor");
    glClear_p = OSMesaGetProcAddress_p("glClear");
    glFinish_p = OSMesaGetProcAddress_p("glFinish");
    glReadPixels_p = OSMesaGetProcAddress_p("glReadPixels");
    return true;
}
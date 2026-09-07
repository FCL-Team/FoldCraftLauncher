//
// glxshim：为只认 glXGetProcAddress 的旧版 OpenGL 调用方提供入口
//

#include "../../ctxbridges/egl_loader.h"

// LWJGL 3.3.5 之前的版本使用 glXGetProcAddress，且不会回退到 eglGetProcAddress
// 参考 LWJGL（https://github.com/LWJGL/lwjgl3/commit/05ef6288b187862897338a1ccddc9e4f854eb1a0）
__attribute__((used)) void *glXGetProcAddress(const char *name) {
    return getProcAddress(name);
}
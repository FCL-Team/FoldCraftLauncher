#include <GLES3/gl31.h>
#include <android/log.h>
#include <dlfcn.h>
#include <pthread.h>
#include <stdlib.h>
#include <string.h>
#include "interop.h"

static void *mg_handle;
static void *gles_handle;
static void *(*mg_get_proc)(const unsigned char *);
static pthread_once_t adapter_once = PTHREAD_ONCE_INIT;

static void load_mobileglues(void) {
    mg_handle = dlopen("libmobileglues.so", RTLD_NOW | RTLD_LOCAL);
    gles_handle = dlopen("libGLESv3.so", RTLD_NOW | RTLD_LOCAL);
    if (mg_handle) {
        mg_get_proc = dlsym(mg_handle, "glXGetProcAddress");
        if (!mg_get_proc) mg_get_proc = dlsym(mg_handle, "glXGetProcAddressARB");
    }
    __android_log_print(mg_handle ? ANDROID_LOG_INFO : ANDROID_LOG_ERROR, "FCL-QuestVR",
                        "MobileGlues/OpenXR GL adapter: %s", mg_handle ? "ready" : "library unavailable");
}

static void *mg_proc(const char *name) {
    pthread_once(&adapter_once, load_mobileglues);
    if (!mg_handle) return NULL;
    void *address = dlsym(mg_handle, name);
    return address ? address : (mg_get_proc ? mg_get_proc((const unsigned char *) name) : NULL);
}

static GLuint bound_texture(GLenum target) {
    pthread_once(&adapter_once, load_mobileglues);
    if (!gles_handle) return 0;
    GLenum binding;
    switch (target) {
        case GL_TEXTURE_2D: binding = GL_TEXTURE_BINDING_2D; break;
        case GL_TEXTURE_2D_ARRAY: binding = GL_TEXTURE_BINDING_2D_ARRAY; break;
        case GL_TEXTURE_3D: binding = GL_TEXTURE_BINDING_3D; break;
        case GL_TEXTURE_CUBE_MAP: binding = GL_TEXTURE_BINDING_CUBE_MAP; break;
        default: return 0;
    }
    void (*get_integer)(GLenum, GLint *) = dlsym(gles_handle, "glGetIntegerv");
    GLint texture = 0;
    if (get_integer) get_integer(binding, &texture);
    return (GLuint) texture;
}

static void adapter_delete_textures(GLsizei count, const GLuint *textures) {
    void (*delete_textures)(GLsizei, const GLuint *) = mg_proc("glDeleteTextures");
    if (!delete_textures) return;
    if (count <= 0 || !textures) { delete_textures(count, textures); return; }
    for (GLsizei i = 0; i < count; ++i) {
        if (!quest_vr_is_runtime_texture(textures[i])) delete_textures(1, &textures[i]);
    }
}

static void adapter_get_tex_level_iv(GLenum target, GLint level, GLenum pname, GLint *params) {
    void (*query)(GLenum, GLint, GLenum, GLint *) = NULL;
    if (quest_vr_is_runtime_texture(bound_texture(target)))
        query = dlsym(gles_handle, "glGetTexLevelParameteriv");
    if (!query) query = mg_proc("glGetTexLevelParameteriv");
    if (query) query(target, level, pname, params);
}

static void adapter_get_tex_level_fv(GLenum target, GLint level, GLenum pname, GLfloat *params) {
    void (*query)(GLenum, GLint, GLenum, GLfloat *) = NULL;
    if (quest_vr_is_runtime_texture(bound_texture(target)))
        query = dlsym(gles_handle, "glGetTexLevelParameterfv");
    if (!query) query = mg_proc("glGetTexLevelParameterfv");
    if (query) query(target, level, pname, params);
}

static void *resolve_proc(const unsigned char *name) {
    if (!name) return NULL;
    pthread_once(&adapter_once, load_mobileglues);
    if (!strcmp((const char *) name, "glDeleteTextures")) return adapter_delete_textures;
    if (!strcmp((const char *) name, "glGetTexLevelParameteriv")) return adapter_get_tex_level_iv;
    if (!strcmp((const char *) name, "glGetTexLevelParameterfv")) return adapter_get_tex_level_fv;
    return mg_proc((const char *) name);
}

__attribute__((visibility("default")))
void *glXGetProcAddress(const unsigned char *name) { return resolve_proc(name); }

__attribute__((visibility("default")))
void *glXGetProcAddressARB(const unsigned char *name) { return resolve_proc(name); }

__attribute__((visibility("default")))
void *eglGetProcAddress(const char *name) { return resolve_proc((const unsigned char *) name); }

__attribute__((visibility("default")))
void glDeleteTextures(GLsizei count, const GLuint *textures) {
    adapter_delete_textures(count, textures);
}

__attribute__((visibility("default")))
void glGetTexLevelParameteriv(GLenum target, GLint level, GLenum pname, GLint *params) {
    adapter_get_tex_level_iv(target, level, pname, params);
}

__attribute__((visibility("default")))
void glGetTexLevelParameterfv(GLenum target, GLint level, GLenum pname, GLfloat *params) {
    adapter_get_tex_level_fv(target, level, pname, params);
}

/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glTextureStorage1DEXTPROC) (GLuint texture, GLenum target, GLsizei levels, GLenum internalformat, GLsizei width);
typedef void (APIENTRY *glTextureStorage2DEXTPROC) (GLuint texture, GLenum target, GLsizei levels, GLenum internalformat, GLsizei width, GLsizei height);
typedef void (APIENTRY *glTextureStorage3DEXTPROC) (GLuint texture, GLenum target, GLsizei levels, GLenum internalformat, GLsizei width, GLsizei height, GLsizei depth);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTextureStorage_nglTextureStorage1DEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint levels, jint internalformat, jint width, jlong function_pointer) {
	glTextureStorage1DEXTPROC glTextureStorage1DEXT = (glTextureStorage1DEXTPROC)((intptr_t)function_pointer);
	glTextureStorage1DEXT(texture, target, levels, internalformat, width);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTextureStorage_nglTextureStorage2DEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint levels, jint internalformat, jint width, jint height, jlong function_pointer) {
	glTextureStorage2DEXTPROC glTextureStorage2DEXT = (glTextureStorage2DEXTPROC)((intptr_t)function_pointer);
	glTextureStorage2DEXT(texture, target, levels, internalformat, width, height);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTextureStorage_nglTextureStorage3DEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint levels, jint internalformat, jint width, jint height, jint depth, jlong function_pointer) {
	glTextureStorage3DEXTPROC glTextureStorage3DEXT = (glTextureStorage3DEXTPROC)((intptr_t)function_pointer);
	glTextureStorage3DEXT(texture, target, levels, internalformat, width, height, depth);
}


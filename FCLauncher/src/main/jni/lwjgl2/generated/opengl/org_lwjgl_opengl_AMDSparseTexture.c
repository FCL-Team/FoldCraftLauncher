/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glTexStorageSparseAMDPROC) (GLenum target, GLenum internalFormat, GLsizei width, GLsizei height, GLsizei depth, GLsizei layers, GLbitfield flags);
typedef void (APIENTRY *glTextureStorageSparseAMDPROC) (GLuint texture, GLenum target, GLenum internalFormat, GLsizei width, GLsizei height, GLsizei depth, GLsizei layers, GLbitfield flags);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDSparseTexture_nglTexStorageSparseAMD(JNIEnv *env, jclass clazz, jint target, jint internalFormat, jint width, jint height, jint depth, jint layers, jint flags, jlong function_pointer) {
	glTexStorageSparseAMDPROC glTexStorageSparseAMD = (glTexStorageSparseAMDPROC)((intptr_t)function_pointer);
	glTexStorageSparseAMD(target, internalFormat, width, height, depth, layers, flags);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDSparseTexture_nglTextureStorageSparseAMD(JNIEnv *env, jclass clazz, jint texture, jint target, jint internalFormat, jint width, jint height, jint depth, jint layers, jint flags, jlong function_pointer) {
	glTextureStorageSparseAMDPROC glTextureStorageSparseAMD = (glTextureStorageSparseAMDPROC)((intptr_t)function_pointer);
	glTextureStorageSparseAMD(texture, target, internalFormat, width, height, depth, layers, flags);
}


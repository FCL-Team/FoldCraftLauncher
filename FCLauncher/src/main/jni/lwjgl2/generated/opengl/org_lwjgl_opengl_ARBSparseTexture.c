/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glTexPageCommitmentARBPROC) (GLenum target, GLint level, GLint xoffset, GLint yoffset, GLint zoffset, GLsizei width, GLsizei height, GLsizei depth, GLboolean commit);
typedef void (APIENTRY *glTexturePageCommitmentEXTPROC) (GLuint texture, GLenum target, GLint level, GLint xoffset, GLint yoffset, GLint zoffset, GLsizei width, GLsizei height, GLsizei depth, GLboolean commit);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBSparseTexture_nglTexPageCommitmentARB(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jboolean commit, jlong function_pointer) {
	glTexPageCommitmentARBPROC glTexPageCommitmentARB = (glTexPageCommitmentARBPROC)((intptr_t)function_pointer);
	glTexPageCommitmentARB(target, level, xoffset, yoffset, zoffset, width, height, depth, commit);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBSparseTexture_nglTexturePageCommitmentEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint level, jint xoffset, jint yoffset, jint zoffset, jint width, jint height, jint depth, jboolean commit, jlong function_pointer) {
	glTexturePageCommitmentEXTPROC glTexturePageCommitmentEXT = (glTexturePageCommitmentEXTPROC)((intptr_t)function_pointer);
	glTexturePageCommitmentEXT(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, commit);
}


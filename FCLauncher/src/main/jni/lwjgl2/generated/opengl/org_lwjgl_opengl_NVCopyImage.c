/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glCopyImageSubDataNVPROC) (GLuint srcName, GLenum srcTarget, GLint srcLevel, GLint srcX, GLint srcY, GLint srcZ, GLuint dstName, GLenum dstTarget, GLint dstLevel, GLint dstX, GLint dstY, GLint dstZ, GLsizei width, GLsizei height, GLsizei depth);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVCopyImage_nglCopyImageSubDataNV(JNIEnv *env, jclass clazz, jint srcName, jint srcTarget, jint srcLevel, jint srcX, jint srcY, jint srcZ, jint dstName, jint dstTarget, jint dstLevel, jint dstX, jint dstY, jint dstZ, jint width, jint height, jint depth, jlong function_pointer) {
	glCopyImageSubDataNVPROC glCopyImageSubDataNV = (glCopyImageSubDataNVPROC)((intptr_t)function_pointer);
	glCopyImageSubDataNV(srcName, srcTarget, srcLevel, srcX, srcY, srcZ, dstName, dstTarget, dstLevel, dstX, dstY, dstZ, width, height, depth);
}


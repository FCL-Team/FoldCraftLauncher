/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBlitFramebufferEXTPROC) (GLint srcX0, GLint srcY0, GLint srcX1, GLint srcY1, GLint dstX0, GLint dstY0, GLint dstX1, GLint dstY1, GLbitfield mask, GLenum filter);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTFramebufferBlit_nglBlitFramebufferEXT(JNIEnv *env, jclass clazz, jint srcX0, jint srcY0, jint srcX1, jint srcY1, jint dstX0, jint dstY0, jint dstX1, jint dstY1, jint mask, jint filter, jlong function_pointer) {
	glBlitFramebufferEXTPROC glBlitFramebufferEXT = (glBlitFramebufferEXTPROC)((intptr_t)function_pointer);
	glBlitFramebufferEXT(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
}


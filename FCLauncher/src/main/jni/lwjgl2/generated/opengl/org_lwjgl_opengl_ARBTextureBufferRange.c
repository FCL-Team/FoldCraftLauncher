/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glTextureBufferRangeEXTPROC) (GLuint texture, GLenum target, GLenum internalformat, GLuint buffer, GLintptr offset, GLsizeiptr size);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTextureBufferRange_nglTextureBufferRangeEXT(JNIEnv *env, jclass clazz, jint texture, jint target, jint internalformat, jint buffer, jlong offset, jlong size, jlong function_pointer) {
	glTextureBufferRangeEXTPROC glTextureBufferRangeEXT = (glTextureBufferRangeEXTPROC)((intptr_t)function_pointer);
	glTextureBufferRangeEXT(texture, target, internalformat, buffer, offset, size);
}


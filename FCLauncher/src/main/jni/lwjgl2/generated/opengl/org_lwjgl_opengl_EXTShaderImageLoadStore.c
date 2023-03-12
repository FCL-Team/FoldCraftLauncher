/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBindImageTextureEXTPROC) (GLuint index, GLuint texture, GLint level, GLboolean layered, GLint layer, GLenum access, GLint format);
typedef void (APIENTRY *glMemoryBarrierEXTPROC) (GLbitfield barriers);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTShaderImageLoadStore_nglBindImageTextureEXT(JNIEnv *env, jclass clazz, jint index, jint texture, jint level, jboolean layered, jint layer, jint access, jint format, jlong function_pointer) {
	glBindImageTextureEXTPROC glBindImageTextureEXT = (glBindImageTextureEXTPROC)((intptr_t)function_pointer);
	glBindImageTextureEXT(index, texture, level, layered, layer, access, format);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTShaderImageLoadStore_nglMemoryBarrierEXT(JNIEnv *env, jclass clazz, jint barriers, jlong function_pointer) {
	glMemoryBarrierEXTPROC glMemoryBarrierEXT = (glMemoryBarrierEXTPROC)((intptr_t)function_pointer);
	glMemoryBarrierEXT(barriers);
}


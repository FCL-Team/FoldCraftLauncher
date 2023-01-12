/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef GLvoid * (APIENTRY *glMapTexture2DINTELPROC) (GLuint texture, GLint level, GLbitfield access, GLint * stride, GLenum * layout);
typedef void (APIENTRY *glUnmapTexture2DINTELPROC) (GLuint texture, GLint level);
typedef void (APIENTRY *glSyncTextureINTELPROC) (GLuint texture);

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_INTELMapTexture_nglMapTexture2DINTEL(JNIEnv *env, jclass clazz, jint texture, jint level, jlong length, jint access, jlong stride, jlong layout, jobject old_buffer, jlong function_pointer) {
	GLint *stride_address = (GLint *)(intptr_t)stride;
	GLenum *layout_address = (GLenum *)(intptr_t)layout;
	glMapTexture2DINTELPROC glMapTexture2DINTEL = (glMapTexture2DINTELPROC)((intptr_t)function_pointer);
	GLvoid * __result = glMapTexture2DINTEL(texture, level, access, stride_address, layout_address);
	return safeNewBufferCached(env, __result, length, old_buffer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_INTELMapTexture_nglUnmapTexture2DINTEL(JNIEnv *env, jclass clazz, jint texture, jint level, jlong function_pointer) {
	glUnmapTexture2DINTELPROC glUnmapTexture2DINTEL = (glUnmapTexture2DINTELPROC)((intptr_t)function_pointer);
	glUnmapTexture2DINTEL(texture, level);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_INTELMapTexture_nglSyncTextureINTEL(JNIEnv *env, jclass clazz, jint texture, jlong function_pointer) {
	glSyncTextureINTELPROC glSyncTextureINTEL = (glSyncTextureINTELPROC)((intptr_t)function_pointer);
	glSyncTextureINTEL(texture);
}


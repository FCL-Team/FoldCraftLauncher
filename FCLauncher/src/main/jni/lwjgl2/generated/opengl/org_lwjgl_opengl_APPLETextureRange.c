/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glTextureRangeAPPLEPROC) (GLenum target, GLsizei length, GLvoid * pointer);
typedef void (APIENTRY *glGetTexParameterPointervAPPLEPROC) (GLenum target, GLenum pname, GLvoid ** params);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_APPLETextureRange_nglTextureRangeAPPLE(JNIEnv *env, jclass clazz, jint target, jint length, jlong pointer, jlong function_pointer) {
	GLvoid *pointer_address = (GLvoid *)(intptr_t)pointer;
	glTextureRangeAPPLEPROC glTextureRangeAPPLE = (glTextureRangeAPPLEPROC)((intptr_t)function_pointer);
	glTextureRangeAPPLE(target, length, pointer_address);
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_APPLETextureRange_nglGetTexParameterPointervAPPLE(JNIEnv *env, jclass clazz, jint target, jint pname, jlong result_size, jlong function_pointer) {
	glGetTexParameterPointervAPPLEPROC glGetTexParameterPointervAPPLE = (glGetTexParameterPointervAPPLEPROC)((intptr_t)function_pointer);
	GLvoid * __result;
	glGetTexParameterPointervAPPLE(target, pname, &__result);
	return safeNewBuffer(env, __result, result_size);
}


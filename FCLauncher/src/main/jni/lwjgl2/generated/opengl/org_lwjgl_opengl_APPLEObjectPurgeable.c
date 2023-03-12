/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef GLenum (APIENTRY *glObjectPurgeableAPPLEPROC) (GLenum objectType, GLuint name, GLenum option);
typedef GLenum (APIENTRY *glObjectUnpurgeableAPPLEPROC) (GLenum objectType, GLuint name, GLenum option);
typedef void (APIENTRY *glGetObjectParameterivAPPLEPROC) (GLenum objectType, GLuint name, GLenum pname, GLint * params);

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_APPLEObjectPurgeable_nglObjectPurgeableAPPLE(JNIEnv *env, jclass clazz, jint objectType, jint name, jint option, jlong function_pointer) {
	glObjectPurgeableAPPLEPROC glObjectPurgeableAPPLE = (glObjectPurgeableAPPLEPROC)((intptr_t)function_pointer);
	GLenum __result = glObjectPurgeableAPPLE(objectType, name, option);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_APPLEObjectPurgeable_nglObjectUnpurgeableAPPLE(JNIEnv *env, jclass clazz, jint objectType, jint name, jint option, jlong function_pointer) {
	glObjectUnpurgeableAPPLEPROC glObjectUnpurgeableAPPLE = (glObjectUnpurgeableAPPLEPROC)((intptr_t)function_pointer);
	GLenum __result = glObjectUnpurgeableAPPLE(objectType, name, option);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_APPLEObjectPurgeable_nglGetObjectParameterivAPPLE(JNIEnv *env, jclass clazz, jint objectType, jint name, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetObjectParameterivAPPLEPROC glGetObjectParameterivAPPLE = (glGetObjectParameterivAPPLEPROC)((intptr_t)function_pointer);
	glGetObjectParameterivAPPLE(objectType, name, pname, params_address);
}


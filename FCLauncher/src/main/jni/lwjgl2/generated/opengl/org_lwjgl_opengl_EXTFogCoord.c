/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glFogCoordfEXTPROC) (GLfloat coord);
typedef void (APIENTRY *glFogCoorddEXTPROC) (GLdouble coord);
typedef void (APIENTRY *glFogCoordPointerEXTPROC) (GLenum type, GLsizei stride, const GLvoid * data);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTFogCoord_nglFogCoordfEXT(JNIEnv *env, jclass clazz, jfloat coord, jlong function_pointer) {
	glFogCoordfEXTPROC glFogCoordfEXT = (glFogCoordfEXTPROC)((intptr_t)function_pointer);
	glFogCoordfEXT(coord);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTFogCoord_nglFogCoorddEXT(JNIEnv *env, jclass clazz, jdouble coord, jlong function_pointer) {
	glFogCoorddEXTPROC glFogCoorddEXT = (glFogCoorddEXTPROC)((intptr_t)function_pointer);
	glFogCoorddEXT(coord);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTFogCoord_nglFogCoordPointerEXT(JNIEnv *env, jclass clazz, jint type, jint stride, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glFogCoordPointerEXTPROC glFogCoordPointerEXT = (glFogCoordPointerEXTPROC)((intptr_t)function_pointer);
	glFogCoordPointerEXT(type, stride, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTFogCoord_nglFogCoordPointerEXTBO(JNIEnv *env, jclass clazz, jint type, jint stride, jlong data_buffer_offset, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)offsetToPointer(data_buffer_offset);
	glFogCoordPointerEXTPROC glFogCoordPointerEXT = (glFogCoordPointerEXTPROC)((intptr_t)function_pointer);
	glFogCoordPointerEXT(type, stride, data_address);
}


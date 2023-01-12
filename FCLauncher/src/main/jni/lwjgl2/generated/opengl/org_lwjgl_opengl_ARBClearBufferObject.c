/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glClearNamedBufferDataEXTPROC) (GLuint buffer, GLenum internalformat, GLenum format, GLenum type, const GLvoid * data);
typedef void (APIENTRY *glClearNamedBufferSubDataEXTPROC) (GLuint buffer, GLenum internalformat, GLintptr offset, GLsizeiptr size, GLenum format, GLenum type, GLvoid * data);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBClearBufferObject_nglClearNamedBufferDataEXT(JNIEnv *env, jclass clazz, jint buffer, jint internalformat, jint format, jint type, jlong data, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glClearNamedBufferDataEXTPROC glClearNamedBufferDataEXT = (glClearNamedBufferDataEXTPROC)((intptr_t)function_pointer);
	glClearNamedBufferDataEXT(buffer, internalformat, format, type, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBClearBufferObject_nglClearNamedBufferSubDataEXT(JNIEnv *env, jclass clazz, jint buffer, jint internalformat, jlong offset, jlong size, jint format, jint type, jlong data, jlong function_pointer) {
	GLvoid *data_address = (GLvoid *)(intptr_t)data;
	glClearNamedBufferSubDataEXTPROC glClearNamedBufferSubDataEXT = (glClearNamedBufferSubDataEXTPROC)((intptr_t)function_pointer);
	glClearNamedBufferSubDataEXT(buffer, internalformat, offset, size, format, type, data_address);
}


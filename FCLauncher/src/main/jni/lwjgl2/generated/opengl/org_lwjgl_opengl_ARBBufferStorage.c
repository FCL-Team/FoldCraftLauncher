/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glNamedBufferStorageEXTPROC) (GLuint buffer, GLsizeiptr size, const GLvoid * data, GLbitfield flags);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBufferStorage_nglNamedBufferStorageEXT(JNIEnv *env, jclass clazz, jint buffer, jlong size, jlong data, jint flags, jlong function_pointer) {
	const GLvoid *data_address = (const GLvoid *)(intptr_t)data;
	glNamedBufferStorageEXTPROC glNamedBufferStorageEXT = (glNamedBufferStorageEXTPROC)((intptr_t)function_pointer);
	glNamedBufferStorageEXT(buffer, size, data_address, flags);
}


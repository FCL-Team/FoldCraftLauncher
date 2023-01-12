/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glDrawBuffersATIPROC) (GLsizei size, const GLenum * buffers);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIDrawBuffers_nglDrawBuffersATI(JNIEnv *env, jclass clazz, jint size, jlong buffers, jlong function_pointer) {
	const GLenum *buffers_address = (const GLenum *)(intptr_t)buffers;
	glDrawBuffersATIPROC glDrawBuffersATI = (glDrawBuffersATIPROC)((intptr_t)function_pointer);
	glDrawBuffersATI(size, buffers_address);
}


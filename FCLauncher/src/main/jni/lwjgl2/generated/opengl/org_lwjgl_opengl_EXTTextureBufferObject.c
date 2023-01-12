/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glTexBufferEXTPROC) (GLenum target, GLenum internalformat, GLuint buffer);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTTextureBufferObject_nglTexBufferEXT(JNIEnv *env, jclass clazz, jint target, jint internalformat, jint buffer, jlong function_pointer) {
	glTexBufferEXTPROC glTexBufferEXT = (glTexBufferEXTPROC)((intptr_t)function_pointer);
	glTexBufferEXT(target, internalformat, buffer);
}


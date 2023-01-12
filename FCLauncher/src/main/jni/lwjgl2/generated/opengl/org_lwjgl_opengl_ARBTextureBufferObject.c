/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glTexBufferARBPROC) (GLenum target, GLenum internalformat, GLuint buffer);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTextureBufferObject_nglTexBufferARB(JNIEnv *env, jclass clazz, jint target, jint internalformat, jint buffer, jlong function_pointer) {
	glTexBufferARBPROC glTexBufferARB = (glTexBufferARBPROC)((intptr_t)function_pointer);
	glTexBufferARB(target, internalformat, buffer);
}


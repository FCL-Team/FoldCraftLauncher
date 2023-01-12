/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glRenderbufferStorageMultisampleEXTPROC) (GLenum target, GLsizei samples, GLenum internalformat, GLsizei width, GLsizei height);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTFramebufferMultisample_nglRenderbufferStorageMultisampleEXT(JNIEnv *env, jclass clazz, jint target, jint samples, jint internalformat, jint width, jint height, jlong function_pointer) {
	glRenderbufferStorageMultisampleEXTPROC glRenderbufferStorageMultisampleEXT = (glRenderbufferStorageMultisampleEXTPROC)((intptr_t)function_pointer);
	glRenderbufferStorageMultisampleEXT(target, samples, internalformat, width, height);
}


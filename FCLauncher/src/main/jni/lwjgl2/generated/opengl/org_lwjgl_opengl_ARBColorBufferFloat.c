/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glClampColorARBPROC) (GLenum target, GLenum clamp);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBColorBufferFloat_nglClampColorARB(JNIEnv *env, jclass clazz, jint target, jint clamp, jlong function_pointer) {
	glClampColorARBPROC glClampColorARB = (glClampColorARBPROC)((intptr_t)function_pointer);
	glClampColorARB(target, clamp);
}


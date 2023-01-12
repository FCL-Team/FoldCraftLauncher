/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBlendColorEXTPROC) (GLclampf red, GLclampf green, GLclampf blue, GLclampf alpha);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTBlendColor_nglBlendColorEXT(JNIEnv *env, jclass clazz, jfloat red, jfloat green, jfloat blue, jfloat alpha, jlong function_pointer) {
	glBlendColorEXTPROC glBlendColorEXT = (glBlendColorEXTPROC)((intptr_t)function_pointer);
	glBlendColorEXT(red, green, blue, alpha);
}


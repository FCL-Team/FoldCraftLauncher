/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBlendEquationSeparateEXTPROC) (GLenum modeRGB, GLenum modeAlpha);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTBlendEquationSeparate_nglBlendEquationSeparateEXT(JNIEnv *env, jclass clazz, jint modeRGB, jint modeAlpha, jlong function_pointer) {
	glBlendEquationSeparateEXTPROC glBlendEquationSeparateEXT = (glBlendEquationSeparateEXTPROC)((intptr_t)function_pointer);
	glBlendEquationSeparateEXT(modeRGB, modeAlpha);
}


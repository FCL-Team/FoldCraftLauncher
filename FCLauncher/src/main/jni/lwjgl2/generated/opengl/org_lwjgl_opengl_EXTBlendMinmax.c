/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBlendEquationEXTPROC) (GLenum mode);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTBlendMinmax_nglBlendEquationEXT(JNIEnv *env, jclass clazz, jint mode, jlong function_pointer) {
	glBlendEquationEXTPROC glBlendEquationEXT = (glBlendEquationEXTPROC)((intptr_t)function_pointer);
	glBlendEquationEXT(mode);
}


/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBlendFuncSeparateEXTPROC) (GLenum sfactorRGB, GLenum dfactorRGB, GLenum sfactorAlpha, GLenum dfactorAlpha);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTBlendFuncSeparate_nglBlendFuncSeparateEXT(JNIEnv *env, jclass clazz, jint sfactorRGB, jint dfactorRGB, jint sfactorAlpha, jint dfactorAlpha, jlong function_pointer) {
	glBlendFuncSeparateEXTPROC glBlendFuncSeparateEXT = (glBlendFuncSeparateEXTPROC)((intptr_t)function_pointer);
	glBlendFuncSeparateEXT(sfactorRGB, dfactorRGB, sfactorAlpha, dfactorAlpha);
}


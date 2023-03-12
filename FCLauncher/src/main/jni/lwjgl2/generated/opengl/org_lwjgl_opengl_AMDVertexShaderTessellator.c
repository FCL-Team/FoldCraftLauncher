/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glTessellationFactorAMDPROC) (GLfloat factor);
typedef void (APIENTRY *glTessellationModeAMDPROC) (GLenum mode);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDVertexShaderTessellator_nglTessellationFactorAMD(JNIEnv *env, jclass clazz, jfloat factor, jlong function_pointer) {
	glTessellationFactorAMDPROC glTessellationFactorAMD = (glTessellationFactorAMDPROC)((intptr_t)function_pointer);
	glTessellationFactorAMD(factor);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDVertexShaderTessellator_nglTessellationModeAMD(JNIEnv *env, jclass clazz, jint mode, jlong function_pointer) {
	glTessellationModeAMDPROC glTessellationModeAMD = (glTessellationModeAMDPROC)((intptr_t)function_pointer);
	glTessellationModeAMD(mode);
}


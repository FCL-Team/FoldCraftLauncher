/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glLoadTransposeMatrixfARBPROC) (const GLfloat * pfMtx);
typedef void (APIENTRY *glMultTransposeMatrixfARBPROC) (const GLfloat * pfMtx);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTransposeMatrix_nglLoadTransposeMatrixfARB(JNIEnv *env, jclass clazz, jlong pfMtx, jlong function_pointer) {
	const GLfloat *pfMtx_address = (const GLfloat *)(intptr_t)pfMtx;
	glLoadTransposeMatrixfARBPROC glLoadTransposeMatrixfARB = (glLoadTransposeMatrixfARBPROC)((intptr_t)function_pointer);
	glLoadTransposeMatrixfARB(pfMtx_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTransposeMatrix_nglMultTransposeMatrixfARB(JNIEnv *env, jclass clazz, jlong pfMtx, jlong function_pointer) {
	const GLfloat *pfMtx_address = (const GLfloat *)(intptr_t)pfMtx;
	glMultTransposeMatrixfARBPROC glMultTransposeMatrixfARB = (glMultTransposeMatrixfARBPROC)((intptr_t)function_pointer);
	glMultTransposeMatrixfARB(pfMtx_address);
}


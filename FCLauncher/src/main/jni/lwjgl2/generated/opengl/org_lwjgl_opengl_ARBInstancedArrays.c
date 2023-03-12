/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glVertexAttribDivisorARBPROC) (GLuint index, GLuint divisor);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBInstancedArrays_nglVertexAttribDivisorARB(JNIEnv *env, jclass clazz, jint index, jint divisor, jlong function_pointer) {
	glVertexAttribDivisorARBPROC glVertexAttribDivisorARB = (glVertexAttribDivisorARBPROC)((intptr_t)function_pointer);
	glVertexAttribDivisorARB(index, divisor);
}


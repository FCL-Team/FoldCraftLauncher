/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBufferPageCommitmentARBPROC) (GLenum target, GLintptr offset, GLsizeiptr size, GLboolean commit);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBSparseBuffer_nglBufferPageCommitmentARB(JNIEnv *env, jclass clazz, jint target, jlong offset, jlong size, jboolean commit, jlong function_pointer) {
	glBufferPageCommitmentARBPROC glBufferPageCommitmentARB = (glBufferPageCommitmentARBPROC)((intptr_t)function_pointer);
	glBufferPageCommitmentARB(target, offset, size, commit);
}


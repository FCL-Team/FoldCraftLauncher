/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBindTransformFeedbackNVPROC) (GLenum target, GLuint id);
typedef void (APIENTRY *glDeleteTransformFeedbacksNVPROC) (GLsizei n, const GLuint * ids);
typedef void (APIENTRY *glGenTransformFeedbacksNVPROC) (GLsizei n, GLuint * ids);
typedef GLboolean (APIENTRY *glIsTransformFeedbackNVPROC) (GLuint id);
typedef void (APIENTRY *glPauseTransformFeedbackNVPROC) ();
typedef void (APIENTRY *glResumeTransformFeedbackNVPROC) ();
typedef void (APIENTRY *glDrawTransformFeedbackNVPROC) (GLenum mode, GLuint id);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTransformFeedback2_nglBindTransformFeedbackNV(JNIEnv *env, jclass clazz, jint target, jint id, jlong function_pointer) {
	glBindTransformFeedbackNVPROC glBindTransformFeedbackNV = (glBindTransformFeedbackNVPROC)((intptr_t)function_pointer);
	glBindTransformFeedbackNV(target, id);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTransformFeedback2_nglDeleteTransformFeedbacksNV(JNIEnv *env, jclass clazz, jint n, jlong ids, jlong function_pointer) {
	const GLuint *ids_address = (const GLuint *)(intptr_t)ids;
	glDeleteTransformFeedbacksNVPROC glDeleteTransformFeedbacksNV = (glDeleteTransformFeedbacksNVPROC)((intptr_t)function_pointer);
	glDeleteTransformFeedbacksNV(n, ids_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTransformFeedback2_nglGenTransformFeedbacksNV(JNIEnv *env, jclass clazz, jint n, jlong ids, jlong function_pointer) {
	GLuint *ids_address = (GLuint *)(intptr_t)ids;
	glGenTransformFeedbacksNVPROC glGenTransformFeedbacksNV = (glGenTransformFeedbacksNVPROC)((intptr_t)function_pointer);
	glGenTransformFeedbacksNV(n, ids_address);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_NVTransformFeedback2_nglIsTransformFeedbackNV(JNIEnv *env, jclass clazz, jint id, jlong function_pointer) {
	glIsTransformFeedbackNVPROC glIsTransformFeedbackNV = (glIsTransformFeedbackNVPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsTransformFeedbackNV(id);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTransformFeedback2_nglPauseTransformFeedbackNV(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glPauseTransformFeedbackNVPROC glPauseTransformFeedbackNV = (glPauseTransformFeedbackNVPROC)((intptr_t)function_pointer);
	glPauseTransformFeedbackNV();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTransformFeedback2_nglResumeTransformFeedbackNV(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glResumeTransformFeedbackNVPROC glResumeTransformFeedbackNV = (glResumeTransformFeedbackNVPROC)((intptr_t)function_pointer);
	glResumeTransformFeedbackNV();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVTransformFeedback2_nglDrawTransformFeedbackNV(JNIEnv *env, jclass clazz, jint mode, jint id, jlong function_pointer) {
	glDrawTransformFeedbackNVPROC glDrawTransformFeedbackNV = (glDrawTransformFeedbackNVPROC)((intptr_t)function_pointer);
	glDrawTransformFeedbackNV(mode, id);
}


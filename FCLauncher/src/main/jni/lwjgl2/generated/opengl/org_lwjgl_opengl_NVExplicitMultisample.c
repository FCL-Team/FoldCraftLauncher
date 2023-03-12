/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glGetMultisamplefvNVPROC) (GLenum pname, GLuint index, GLfloat * val);
typedef void (APIENTRY *glSampleMaskIndexedNVPROC) (GLuint index, GLbitfield mask);
typedef void (APIENTRY *glTexRenderbufferNVPROC) (GLenum target, GLuint renderbuffer);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVExplicitMultisample_nglGetMultisamplefvNV(JNIEnv *env, jclass clazz, jint pname, jint index, jlong val, jlong function_pointer) {
	GLfloat *val_address = (GLfloat *)(intptr_t)val;
	glGetMultisamplefvNVPROC glGetMultisamplefvNV = (glGetMultisamplefvNVPROC)((intptr_t)function_pointer);
	glGetMultisamplefvNV(pname, index, val_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVExplicitMultisample_nglSampleMaskIndexedNV(JNIEnv *env, jclass clazz, jint index, jint mask, jlong function_pointer) {
	glSampleMaskIndexedNVPROC glSampleMaskIndexedNV = (glSampleMaskIndexedNVPROC)((intptr_t)function_pointer);
	glSampleMaskIndexedNV(index, mask);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVExplicitMultisample_nglTexRenderbufferNV(JNIEnv *env, jclass clazz, jint target, jint renderbuffer, jlong function_pointer) {
	glTexRenderbufferNVPROC glTexRenderbufferNV = (glTexRenderbufferNVPROC)((intptr_t)function_pointer);
	glTexRenderbufferNV(target, renderbuffer);
}


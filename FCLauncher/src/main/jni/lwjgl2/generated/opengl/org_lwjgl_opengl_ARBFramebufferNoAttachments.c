/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glNamedFramebufferParameteriEXTPROC) (GLuint framebuffer, GLenum pname, GLint param);
typedef void (APIENTRY *glGetNamedFramebufferParameterivEXTPROC) (GLuint framebuffer, GLenum pname, GLint * params);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBFramebufferNoAttachments_nglNamedFramebufferParameteriEXT(JNIEnv *env, jclass clazz, jint framebuffer, jint pname, jint param, jlong function_pointer) {
	glNamedFramebufferParameteriEXTPROC glNamedFramebufferParameteriEXT = (glNamedFramebufferParameteriEXTPROC)((intptr_t)function_pointer);
	glNamedFramebufferParameteriEXT(framebuffer, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBFramebufferNoAttachments_nglGetNamedFramebufferParameterivEXT(JNIEnv *env, jclass clazz, jint framebuffer, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetNamedFramebufferParameterivEXTPROC glGetNamedFramebufferParameterivEXT = (glGetNamedFramebufferParameterivEXTPROC)((intptr_t)function_pointer);
	glGetNamedFramebufferParameterivEXT(framebuffer, pname, params_address);
}


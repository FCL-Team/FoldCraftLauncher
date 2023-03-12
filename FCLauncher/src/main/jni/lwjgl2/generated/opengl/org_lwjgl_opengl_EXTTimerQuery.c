/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glGetQueryObjecti64vEXTPROC) (GLuint id, GLenum pname, GLint64EXT * params);
typedef void (APIENTRY *glGetQueryObjectui64vEXTPROC) (GLuint id, GLenum pname, GLuint64EXT * params);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTTimerQuery_nglGetQueryObjecti64vEXT(JNIEnv *env, jclass clazz, jint id, jint pname, jlong params, jlong function_pointer) {
	GLint64EXT *params_address = (GLint64EXT *)(intptr_t)params;
	glGetQueryObjecti64vEXTPROC glGetQueryObjecti64vEXT = (glGetQueryObjecti64vEXTPROC)((intptr_t)function_pointer);
	glGetQueryObjecti64vEXT(id, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTTimerQuery_nglGetQueryObjectui64vEXT(JNIEnv *env, jclass clazz, jint id, jint pname, jlong params, jlong function_pointer) {
	GLuint64EXT *params_address = (GLuint64EXT *)(intptr_t)params;
	glGetQueryObjectui64vEXTPROC glGetQueryObjectui64vEXT = (glGetQueryObjectui64vEXTPROC)((intptr_t)function_pointer);
	glGetQueryObjectui64vEXT(id, pname, params_address);
}


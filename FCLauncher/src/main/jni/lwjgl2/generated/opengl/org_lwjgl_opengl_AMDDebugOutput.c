/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glDebugMessageEnableAMDPROC) (GLenum category, GLenum severity, GLsizei count, const GLuint * ids, GLboolean enabled);
typedef void (APIENTRY *glDebugMessageInsertAMDPROC) (GLenum category, GLenum severity, GLuint id, GLsizei length, const GLchar * buf);
typedef void (APIENTRY *glDebugMessageCallbackAMDPROC) (GLDEBUGPROCAMD callback, GLvoid * userParam);
typedef GLuint (APIENTRY *glGetDebugMessageLogAMDPROC) (GLuint count, GLsizei logSize, GLenum * categories, GLuint * severities, GLuint * ids, GLsizei * lengths, GLchar * messageLog);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDDebugOutput_nglDebugMessageEnableAMD(JNIEnv *env, jclass clazz, jint category, jint severity, jint count, jlong ids, jboolean enabled, jlong function_pointer) {
	const GLuint *ids_address = (const GLuint *)(intptr_t)ids;
	glDebugMessageEnableAMDPROC glDebugMessageEnableAMD = (glDebugMessageEnableAMDPROC)((intptr_t)function_pointer);
	glDebugMessageEnableAMD(category, severity, count, ids_address, enabled);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDDebugOutput_nglDebugMessageInsertAMD(JNIEnv *env, jclass clazz, jint category, jint severity, jint id, jint length, jlong buf, jlong function_pointer) {
	const GLchar *buf_address = (const GLchar *)(intptr_t)buf;
	glDebugMessageInsertAMDPROC glDebugMessageInsertAMD = (glDebugMessageInsertAMDPROC)((intptr_t)function_pointer);
	glDebugMessageInsertAMD(category, severity, id, length, buf_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDDebugOutput_nglDebugMessageCallbackAMD(JNIEnv *env, jclass clazz, jlong callback, jlong userParam, jlong function_pointer) {
	glDebugMessageCallbackAMDPROC glDebugMessageCallbackAMD = (glDebugMessageCallbackAMDPROC)((intptr_t)function_pointer);
	glDebugMessageCallbackAMD((GLDEBUGPROCAMD)(intptr_t)callback, (GLvoid *)(intptr_t)userParam);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_AMDDebugOutput_nglGetDebugMessageLogAMD(JNIEnv *env, jclass clazz, jint count, jint logSize, jlong categories, jlong severities, jlong ids, jlong lengths, jlong messageLog, jlong function_pointer) {
	GLenum *categories_address = (GLenum *)(intptr_t)categories;
	GLuint *severities_address = (GLuint *)(intptr_t)severities;
	GLuint *ids_address = (GLuint *)(intptr_t)ids;
	GLsizei *lengths_address = (GLsizei *)(intptr_t)lengths;
	GLchar *messageLog_address = (GLchar *)(intptr_t)messageLog;
	glGetDebugMessageLogAMDPROC glGetDebugMessageLogAMD = (glGetDebugMessageLogAMDPROC)((intptr_t)function_pointer);
	GLuint __result = glGetDebugMessageLogAMD(count, logSize, categories_address, severities_address, ids_address, lengths_address, messageLog_address);
	return __result;
}


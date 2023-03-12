/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glDebugMessageControlARBPROC) (GLenum source, GLenum type, GLenum severity, GLsizei count, const GLuint * ids, GLboolean enabled);
typedef void (APIENTRY *glDebugMessageInsertARBPROC) (GLenum source, GLenum type, GLuint id, GLenum severity, GLsizei length, const GLchar * buf);
typedef void (APIENTRY *glDebugMessageCallbackARBPROC) (GLDEBUGPROCARB callback, GLvoid * userParam);
typedef GLuint (APIENTRY *glGetDebugMessageLogARBPROC) (GLuint count, GLsizei logSize, GLenum * sources, GLenum * types, GLuint * ids, GLenum * severities, GLsizei * lengths, GLchar * messageLog);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBDebugOutput_nglDebugMessageControlARB(JNIEnv *env, jclass clazz, jint source, jint type, jint severity, jint count, jlong ids, jboolean enabled, jlong function_pointer) {
	const GLuint *ids_address = (const GLuint *)(intptr_t)ids;
	glDebugMessageControlARBPROC glDebugMessageControlARB = (glDebugMessageControlARBPROC)((intptr_t)function_pointer);
	glDebugMessageControlARB(source, type, severity, count, ids_address, enabled);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBDebugOutput_nglDebugMessageInsertARB(JNIEnv *env, jclass clazz, jint source, jint type, jint id, jint severity, jint length, jlong buf, jlong function_pointer) {
	const GLchar *buf_address = (const GLchar *)(intptr_t)buf;
	glDebugMessageInsertARBPROC glDebugMessageInsertARB = (glDebugMessageInsertARBPROC)((intptr_t)function_pointer);
	glDebugMessageInsertARB(source, type, id, severity, length, buf_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBDebugOutput_nglDebugMessageCallbackARB(JNIEnv *env, jclass clazz, jlong callback, jlong userParam, jlong function_pointer) {
	glDebugMessageCallbackARBPROC glDebugMessageCallbackARB = (glDebugMessageCallbackARBPROC)((intptr_t)function_pointer);
	glDebugMessageCallbackARB((GLDEBUGPROCARB)(intptr_t)callback, (GLvoid *)(intptr_t)userParam);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_ARBDebugOutput_nglGetDebugMessageLogARB(JNIEnv *env, jclass clazz, jint count, jint logSize, jlong sources, jlong types, jlong ids, jlong severities, jlong lengths, jlong messageLog, jlong function_pointer) {
	GLenum *sources_address = (GLenum *)(intptr_t)sources;
	GLenum *types_address = (GLenum *)(intptr_t)types;
	GLuint *ids_address = (GLuint *)(intptr_t)ids;
	GLenum *severities_address = (GLenum *)(intptr_t)severities;
	GLsizei *lengths_address = (GLsizei *)(intptr_t)lengths;
	GLchar *messageLog_address = (GLchar *)(intptr_t)messageLog;
	glGetDebugMessageLogARBPROC glGetDebugMessageLogARB = (glGetDebugMessageLogARBPROC)((intptr_t)function_pointer);
	GLuint __result = glGetDebugMessageLogARB(count, logSize, sources_address, types_address, ids_address, severities_address, lengths_address, messageLog_address);
	return __result;
}


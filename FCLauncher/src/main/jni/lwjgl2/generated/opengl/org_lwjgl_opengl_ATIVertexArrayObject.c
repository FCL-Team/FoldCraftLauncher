/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef GLuint (APIENTRY *glNewObjectBufferATIPROC) (GLsizei size, const GLvoid * pPointer, GLenum usage);
typedef GLboolean (APIENTRY *glIsObjectBufferATIPROC) (GLuint buffer);
typedef void (APIENTRY *glUpdateObjectBufferATIPROC) (GLuint buffer, GLuint offset, GLsizei size, const GLvoid * pPointer, GLenum preserve);
typedef void (APIENTRY *glGetObjectBufferfvATIPROC) (GLuint buffer, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetObjectBufferivATIPROC) (GLuint buffer, GLenum pname, GLint * params);
typedef void (APIENTRY *glFreeObjectBufferATIPROC) (GLuint buffer);
typedef void (APIENTRY *glArrayObjectATIPROC) (GLenum array, GLint size, GLenum type, GLsizei stride, GLuint buffer, GLuint offset);
typedef void (APIENTRY *glGetArrayObjectfvATIPROC) (GLenum array, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetArrayObjectivATIPROC) (GLenum array, GLenum pname, GLint * params);
typedef void (APIENTRY *glVariantArrayObjectATIPROC) (GLuint id, GLenum type, GLsizei stride, GLuint buffer, GLuint offset);
typedef void (APIENTRY *glGetVariantArrayObjectfvATIPROC) (GLuint id, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetVariantArrayObjectivATIPROC) (GLuint id, GLenum pname, GLint * params);

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_ATIVertexArrayObject_nglNewObjectBufferATI(JNIEnv *env, jclass clazz, jint size, jlong pPointer, jint usage, jlong function_pointer) {
	const GLvoid *pPointer_address = (const GLvoid *)(intptr_t)pPointer;
	glNewObjectBufferATIPROC glNewObjectBufferATI = (glNewObjectBufferATIPROC)((intptr_t)function_pointer);
	GLuint __result = glNewObjectBufferATI(size, pPointer_address, usage);
	return __result;
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_ATIVertexArrayObject_nglIsObjectBufferATI(JNIEnv *env, jclass clazz, jint buffer, jlong function_pointer) {
	glIsObjectBufferATIPROC glIsObjectBufferATI = (glIsObjectBufferATIPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsObjectBufferATI(buffer);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexArrayObject_nglUpdateObjectBufferATI(JNIEnv *env, jclass clazz, jint buffer, jint offset, jint size, jlong pPointer, jint preserve, jlong function_pointer) {
	const GLvoid *pPointer_address = (const GLvoid *)(intptr_t)pPointer;
	glUpdateObjectBufferATIPROC glUpdateObjectBufferATI = (glUpdateObjectBufferATIPROC)((intptr_t)function_pointer);
	glUpdateObjectBufferATI(buffer, offset, size, pPointer_address, preserve);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexArrayObject_nglGetObjectBufferfvATI(JNIEnv *env, jclass clazz, jint buffer, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetObjectBufferfvATIPROC glGetObjectBufferfvATI = (glGetObjectBufferfvATIPROC)((intptr_t)function_pointer);
	glGetObjectBufferfvATI(buffer, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexArrayObject_nglGetObjectBufferivATI(JNIEnv *env, jclass clazz, jint buffer, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetObjectBufferivATIPROC glGetObjectBufferivATI = (glGetObjectBufferivATIPROC)((intptr_t)function_pointer);
	glGetObjectBufferivATI(buffer, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexArrayObject_nglFreeObjectBufferATI(JNIEnv *env, jclass clazz, jint buffer, jlong function_pointer) {
	glFreeObjectBufferATIPROC glFreeObjectBufferATI = (glFreeObjectBufferATIPROC)((intptr_t)function_pointer);
	glFreeObjectBufferATI(buffer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexArrayObject_nglArrayObjectATI(JNIEnv *env, jclass clazz, jint array, jint size, jint type, jint stride, jint buffer, jint offset, jlong function_pointer) {
	glArrayObjectATIPROC glArrayObjectATI = (glArrayObjectATIPROC)((intptr_t)function_pointer);
	glArrayObjectATI(array, size, type, stride, buffer, offset);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexArrayObject_nglGetArrayObjectfvATI(JNIEnv *env, jclass clazz, jint array, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetArrayObjectfvATIPROC glGetArrayObjectfvATI = (glGetArrayObjectfvATIPROC)((intptr_t)function_pointer);
	glGetArrayObjectfvATI(array, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexArrayObject_nglGetArrayObjectivATI(JNIEnv *env, jclass clazz, jint array, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetArrayObjectivATIPROC glGetArrayObjectivATI = (glGetArrayObjectivATIPROC)((intptr_t)function_pointer);
	glGetArrayObjectivATI(array, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexArrayObject_nglVariantArrayObjectATI(JNIEnv *env, jclass clazz, jint id, jint type, jint stride, jint buffer, jint offset, jlong function_pointer) {
	glVariantArrayObjectATIPROC glVariantArrayObjectATI = (glVariantArrayObjectATIPROC)((intptr_t)function_pointer);
	glVariantArrayObjectATI(id, type, stride, buffer, offset);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexArrayObject_nglGetVariantArrayObjectfvATI(JNIEnv *env, jclass clazz, jint id, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetVariantArrayObjectfvATIPROC glGetVariantArrayObjectfvATI = (glGetVariantArrayObjectfvATIPROC)((intptr_t)function_pointer);
	glGetVariantArrayObjectfvATI(id, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexArrayObject_nglGetVariantArrayObjectivATI(JNIEnv *env, jclass clazz, jint id, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetVariantArrayObjectivATIPROC glGetVariantArrayObjectivATI = (glGetVariantArrayObjectivATIPROC)((intptr_t)function_pointer);
	glGetVariantArrayObjectivATI(id, pname, params_address);
}


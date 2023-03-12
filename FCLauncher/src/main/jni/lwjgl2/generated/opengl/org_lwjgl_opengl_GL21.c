/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glUniformMatrix2x3fvPROC) (GLint location, GLsizei count, GLboolean transpose, GLfloat * matrices);
typedef void (APIENTRY *glUniformMatrix3x2fvPROC) (GLint location, GLsizei count, GLboolean transpose, GLfloat * matrices);
typedef void (APIENTRY *glUniformMatrix2x4fvPROC) (GLint location, GLsizei count, GLboolean transpose, GLfloat * matrices);
typedef void (APIENTRY *glUniformMatrix4x2fvPROC) (GLint location, GLsizei count, GLboolean transpose, GLfloat * matrices);
typedef void (APIENTRY *glUniformMatrix3x4fvPROC) (GLint location, GLsizei count, GLboolean transpose, GLfloat * matrices);
typedef void (APIENTRY *glUniformMatrix4x3fvPROC) (GLint location, GLsizei count, GLboolean transpose, GLfloat * matrices);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL21_nglUniformMatrix2x3fv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong matrices, jlong function_pointer) {
	GLfloat *matrices_address = (GLfloat *)(intptr_t)matrices;
	glUniformMatrix2x3fvPROC glUniformMatrix2x3fv = (glUniformMatrix2x3fvPROC)((intptr_t)function_pointer);
	glUniformMatrix2x3fv(location, count, transpose, matrices_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL21_nglUniformMatrix3x2fv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong matrices, jlong function_pointer) {
	GLfloat *matrices_address = (GLfloat *)(intptr_t)matrices;
	glUniformMatrix3x2fvPROC glUniformMatrix3x2fv = (glUniformMatrix3x2fvPROC)((intptr_t)function_pointer);
	glUniformMatrix3x2fv(location, count, transpose, matrices_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL21_nglUniformMatrix2x4fv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong matrices, jlong function_pointer) {
	GLfloat *matrices_address = (GLfloat *)(intptr_t)matrices;
	glUniformMatrix2x4fvPROC glUniformMatrix2x4fv = (glUniformMatrix2x4fvPROC)((intptr_t)function_pointer);
	glUniformMatrix2x4fv(location, count, transpose, matrices_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL21_nglUniformMatrix4x2fv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong matrices, jlong function_pointer) {
	GLfloat *matrices_address = (GLfloat *)(intptr_t)matrices;
	glUniformMatrix4x2fvPROC glUniformMatrix4x2fv = (glUniformMatrix4x2fvPROC)((intptr_t)function_pointer);
	glUniformMatrix4x2fv(location, count, transpose, matrices_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL21_nglUniformMatrix3x4fv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong matrices, jlong function_pointer) {
	GLfloat *matrices_address = (GLfloat *)(intptr_t)matrices;
	glUniformMatrix3x4fvPROC glUniformMatrix3x4fv = (glUniformMatrix3x4fvPROC)((intptr_t)function_pointer);
	glUniformMatrix3x4fv(location, count, transpose, matrices_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL21_nglUniformMatrix4x3fv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong matrices, jlong function_pointer) {
	GLfloat *matrices_address = (GLfloat *)(intptr_t)matrices;
	glUniformMatrix4x3fvPROC glUniformMatrix4x3fv = (glUniformMatrix4x3fvPROC)((intptr_t)function_pointer);
	glUniformMatrix4x3fv(location, count, transpose, matrices_address);
}


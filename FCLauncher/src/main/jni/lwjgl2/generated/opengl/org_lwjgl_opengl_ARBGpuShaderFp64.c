/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glProgramUniform1dEXTPROC) (GLuint program, GLint location, GLdouble x);
typedef void (APIENTRY *glProgramUniform2dEXTPROC) (GLuint program, GLint location, GLdouble x, GLdouble y);
typedef void (APIENTRY *glProgramUniform3dEXTPROC) (GLuint program, GLint location, GLdouble x, GLdouble y, GLdouble z);
typedef void (APIENTRY *glProgramUniform4dEXTPROC) (GLuint program, GLint location, GLdouble x, GLdouble y, GLdouble z, GLdouble w);
typedef void (APIENTRY *glProgramUniform1dvEXTPROC) (GLuint program, GLint location, GLsizei count, const GLdouble * value);
typedef void (APIENTRY *glProgramUniform2dvEXTPROC) (GLuint program, GLint location, GLsizei count, const GLdouble * value);
typedef void (APIENTRY *glProgramUniform3dvEXTPROC) (GLuint program, GLint location, GLsizei count, const GLdouble * value);
typedef void (APIENTRY *glProgramUniform4dvEXTPROC) (GLuint program, GLint location, GLsizei count, const GLdouble * value);
typedef void (APIENTRY *glProgramUniformMatrix2dvEXTPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glProgramUniformMatrix3dvEXTPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glProgramUniformMatrix4dvEXTPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glProgramUniformMatrix2x3dvEXTPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glProgramUniformMatrix2x4dvEXTPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glProgramUniformMatrix3x2dvEXTPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glProgramUniformMatrix3x4dvEXTPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glProgramUniformMatrix4x2dvEXTPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glProgramUniformMatrix4x3dvEXTPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBGpuShaderFp64_nglProgramUniform1dEXT(JNIEnv *env, jclass clazz, jint program, jint location, jdouble x, jlong function_pointer) {
	glProgramUniform1dEXTPROC glProgramUniform1dEXT = (glProgramUniform1dEXTPROC)((intptr_t)function_pointer);
	glProgramUniform1dEXT(program, location, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBGpuShaderFp64_nglProgramUniform2dEXT(JNIEnv *env, jclass clazz, jint program, jint location, jdouble x, jdouble y, jlong function_pointer) {
	glProgramUniform2dEXTPROC glProgramUniform2dEXT = (glProgramUniform2dEXTPROC)((intptr_t)function_pointer);
	glProgramUniform2dEXT(program, location, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBGpuShaderFp64_nglProgramUniform3dEXT(JNIEnv *env, jclass clazz, jint program, jint location, jdouble x, jdouble y, jdouble z, jlong function_pointer) {
	glProgramUniform3dEXTPROC glProgramUniform3dEXT = (glProgramUniform3dEXTPROC)((intptr_t)function_pointer);
	glProgramUniform3dEXT(program, location, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBGpuShaderFp64_nglProgramUniform4dEXT(JNIEnv *env, jclass clazz, jint program, jint location, jdouble x, jdouble y, jdouble z, jdouble w, jlong function_pointer) {
	glProgramUniform4dEXTPROC glProgramUniform4dEXT = (glProgramUniform4dEXTPROC)((intptr_t)function_pointer);
	glProgramUniform4dEXT(program, location, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBGpuShaderFp64_nglProgramUniform1dvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniform1dvEXTPROC glProgramUniform1dvEXT = (glProgramUniform1dvEXTPROC)((intptr_t)function_pointer);
	glProgramUniform1dvEXT(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBGpuShaderFp64_nglProgramUniform2dvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniform2dvEXTPROC glProgramUniform2dvEXT = (glProgramUniform2dvEXTPROC)((intptr_t)function_pointer);
	glProgramUniform2dvEXT(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBGpuShaderFp64_nglProgramUniform3dvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniform3dvEXTPROC glProgramUniform3dvEXT = (glProgramUniform3dvEXTPROC)((intptr_t)function_pointer);
	glProgramUniform3dvEXT(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBGpuShaderFp64_nglProgramUniform4dvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniform4dvEXTPROC glProgramUniform4dvEXT = (glProgramUniform4dvEXTPROC)((intptr_t)function_pointer);
	glProgramUniform4dvEXT(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBGpuShaderFp64_nglProgramUniformMatrix2dvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniformMatrix2dvEXTPROC glProgramUniformMatrix2dvEXT = (glProgramUniformMatrix2dvEXTPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix2dvEXT(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBGpuShaderFp64_nglProgramUniformMatrix3dvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniformMatrix3dvEXTPROC glProgramUniformMatrix3dvEXT = (glProgramUniformMatrix3dvEXTPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix3dvEXT(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBGpuShaderFp64_nglProgramUniformMatrix4dvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniformMatrix4dvEXTPROC glProgramUniformMatrix4dvEXT = (glProgramUniformMatrix4dvEXTPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix4dvEXT(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBGpuShaderFp64_nglProgramUniformMatrix2x3dvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniformMatrix2x3dvEXTPROC glProgramUniformMatrix2x3dvEXT = (glProgramUniformMatrix2x3dvEXTPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix2x3dvEXT(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBGpuShaderFp64_nglProgramUniformMatrix2x4dvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniformMatrix2x4dvEXTPROC glProgramUniformMatrix2x4dvEXT = (glProgramUniformMatrix2x4dvEXTPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix2x4dvEXT(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBGpuShaderFp64_nglProgramUniformMatrix3x2dvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniformMatrix3x2dvEXTPROC glProgramUniformMatrix3x2dvEXT = (glProgramUniformMatrix3x2dvEXTPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix3x2dvEXT(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBGpuShaderFp64_nglProgramUniformMatrix3x4dvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniformMatrix3x4dvEXTPROC glProgramUniformMatrix3x4dvEXT = (glProgramUniformMatrix3x4dvEXTPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix3x4dvEXT(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBGpuShaderFp64_nglProgramUniformMatrix4x2dvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniformMatrix4x2dvEXTPROC glProgramUniformMatrix4x2dvEXT = (glProgramUniformMatrix4x2dvEXTPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix4x2dvEXT(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBGpuShaderFp64_nglProgramUniformMatrix4x3dvEXT(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniformMatrix4x3dvEXTPROC glProgramUniformMatrix4x3dvEXT = (glProgramUniformMatrix4x3dvEXTPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix4x3dvEXT(program, location, count, transpose, value_address);
}


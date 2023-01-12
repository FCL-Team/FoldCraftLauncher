/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glVertexAttribI1iEXTPROC) (GLuint index, GLint x);
typedef void (APIENTRY *glVertexAttribI2iEXTPROC) (GLuint index, GLint x, GLint y);
typedef void (APIENTRY *glVertexAttribI3iEXTPROC) (GLuint index, GLint x, GLint y, GLint z);
typedef void (APIENTRY *glVertexAttribI4iEXTPROC) (GLuint index, GLint x, GLint y, GLint z, GLint w);
typedef void (APIENTRY *glVertexAttribI1uiEXTPROC) (GLuint index, GLuint x);
typedef void (APIENTRY *glVertexAttribI2uiEXTPROC) (GLuint index, GLuint x, GLuint y);
typedef void (APIENTRY *glVertexAttribI3uiEXTPROC) (GLuint index, GLuint x, GLuint y, GLuint z);
typedef void (APIENTRY *glVertexAttribI4uiEXTPROC) (GLuint index, GLuint x, GLuint y, GLuint z, GLuint w);
typedef void (APIENTRY *glVertexAttribI1ivEXTPROC) (GLuint index, const GLint * v);
typedef void (APIENTRY *glVertexAttribI2ivEXTPROC) (GLuint index, const GLint * v);
typedef void (APIENTRY *glVertexAttribI3ivEXTPROC) (GLuint index, const GLint * v);
typedef void (APIENTRY *glVertexAttribI4ivEXTPROC) (GLuint index, const GLint * v);
typedef void (APIENTRY *glVertexAttribI1uivEXTPROC) (GLuint index, const GLuint * v);
typedef void (APIENTRY *glVertexAttribI2uivEXTPROC) (GLuint index, const GLuint * v);
typedef void (APIENTRY *glVertexAttribI3uivEXTPROC) (GLuint index, const GLuint * v);
typedef void (APIENTRY *glVertexAttribI4uivEXTPROC) (GLuint index, const GLuint * v);
typedef void (APIENTRY *glVertexAttribI4bvEXTPROC) (GLuint index, const GLbyte * v);
typedef void (APIENTRY *glVertexAttribI4svEXTPROC) (GLuint index, const GLshort * v);
typedef void (APIENTRY *glVertexAttribI4ubvEXTPROC) (GLuint index, const GLubyte * v);
typedef void (APIENTRY *glVertexAttribI4usvEXTPROC) (GLuint index, const GLushort * v);
typedef void (APIENTRY *glVertexAttribIPointerEXTPROC) (GLuint index, GLint size, GLenum type, GLsizei stride, const GLvoid * buffer);
typedef void (APIENTRY *glGetVertexAttribIivEXTPROC) (GLuint index, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetVertexAttribIuivEXTPROC) (GLuint index, GLenum pname, GLuint * params);
typedef void (APIENTRY *glUniform1uiEXTPROC) (GLint location, GLuint v0);
typedef void (APIENTRY *glUniform2uiEXTPROC) (GLint location, GLuint v0, GLuint v1);
typedef void (APIENTRY *glUniform3uiEXTPROC) (GLint location, GLuint v0, GLuint v1, GLuint v2);
typedef void (APIENTRY *glUniform4uiEXTPROC) (GLint location, GLuint v0, GLuint v1, GLuint v2, GLuint v3);
typedef void (APIENTRY *glUniform1uivEXTPROC) (GLint location, GLsizei count, const GLuint * value);
typedef void (APIENTRY *glUniform2uivEXTPROC) (GLint location, GLsizei count, const GLuint * value);
typedef void (APIENTRY *glUniform3uivEXTPROC) (GLint location, GLsizei count, const GLuint * value);
typedef void (APIENTRY *glUniform4uivEXTPROC) (GLint location, GLsizei count, const GLuint * value);
typedef void (APIENTRY *glGetUniformuivEXTPROC) (GLuint program, GLint location, GLuint * params);
typedef void (APIENTRY *glBindFragDataLocationEXTPROC) (GLuint program, GLuint colorNumber, const GLchar * name);
typedef GLint (APIENTRY *glGetFragDataLocationEXTPROC) (GLuint program, const GLchar * name);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglVertexAttribI1iEXT(JNIEnv *env, jclass clazz, jint index, jint x, jlong function_pointer) {
	glVertexAttribI1iEXTPROC glVertexAttribI1iEXT = (glVertexAttribI1iEXTPROC)((intptr_t)function_pointer);
	glVertexAttribI1iEXT(index, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglVertexAttribI2iEXT(JNIEnv *env, jclass clazz, jint index, jint x, jint y, jlong function_pointer) {
	glVertexAttribI2iEXTPROC glVertexAttribI2iEXT = (glVertexAttribI2iEXTPROC)((intptr_t)function_pointer);
	glVertexAttribI2iEXT(index, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglVertexAttribI3iEXT(JNIEnv *env, jclass clazz, jint index, jint x, jint y, jint z, jlong function_pointer) {
	glVertexAttribI3iEXTPROC glVertexAttribI3iEXT = (glVertexAttribI3iEXTPROC)((intptr_t)function_pointer);
	glVertexAttribI3iEXT(index, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglVertexAttribI4iEXT(JNIEnv *env, jclass clazz, jint index, jint x, jint y, jint z, jint w, jlong function_pointer) {
	glVertexAttribI4iEXTPROC glVertexAttribI4iEXT = (glVertexAttribI4iEXTPROC)((intptr_t)function_pointer);
	glVertexAttribI4iEXT(index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglVertexAttribI1uiEXT(JNIEnv *env, jclass clazz, jint index, jint x, jlong function_pointer) {
	glVertexAttribI1uiEXTPROC glVertexAttribI1uiEXT = (glVertexAttribI1uiEXTPROC)((intptr_t)function_pointer);
	glVertexAttribI1uiEXT(index, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglVertexAttribI2uiEXT(JNIEnv *env, jclass clazz, jint index, jint x, jint y, jlong function_pointer) {
	glVertexAttribI2uiEXTPROC glVertexAttribI2uiEXT = (glVertexAttribI2uiEXTPROC)((intptr_t)function_pointer);
	glVertexAttribI2uiEXT(index, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglVertexAttribI3uiEXT(JNIEnv *env, jclass clazz, jint index, jint x, jint y, jint z, jlong function_pointer) {
	glVertexAttribI3uiEXTPROC glVertexAttribI3uiEXT = (glVertexAttribI3uiEXTPROC)((intptr_t)function_pointer);
	glVertexAttribI3uiEXT(index, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglVertexAttribI4uiEXT(JNIEnv *env, jclass clazz, jint index, jint x, jint y, jint z, jint w, jlong function_pointer) {
	glVertexAttribI4uiEXTPROC glVertexAttribI4uiEXT = (glVertexAttribI4uiEXTPROC)((intptr_t)function_pointer);
	glVertexAttribI4uiEXT(index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglVertexAttribI1ivEXT(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLint *v_address = (const GLint *)(intptr_t)v;
	glVertexAttribI1ivEXTPROC glVertexAttribI1ivEXT = (glVertexAttribI1ivEXTPROC)((intptr_t)function_pointer);
	glVertexAttribI1ivEXT(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglVertexAttribI2ivEXT(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLint *v_address = (const GLint *)(intptr_t)v;
	glVertexAttribI2ivEXTPROC glVertexAttribI2ivEXT = (glVertexAttribI2ivEXTPROC)((intptr_t)function_pointer);
	glVertexAttribI2ivEXT(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglVertexAttribI3ivEXT(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLint *v_address = (const GLint *)(intptr_t)v;
	glVertexAttribI3ivEXTPROC glVertexAttribI3ivEXT = (glVertexAttribI3ivEXTPROC)((intptr_t)function_pointer);
	glVertexAttribI3ivEXT(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglVertexAttribI4ivEXT(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLint *v_address = (const GLint *)(intptr_t)v;
	glVertexAttribI4ivEXTPROC glVertexAttribI4ivEXT = (glVertexAttribI4ivEXTPROC)((intptr_t)function_pointer);
	glVertexAttribI4ivEXT(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglVertexAttribI1uivEXT(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLuint *v_address = (const GLuint *)(intptr_t)v;
	glVertexAttribI1uivEXTPROC glVertexAttribI1uivEXT = (glVertexAttribI1uivEXTPROC)((intptr_t)function_pointer);
	glVertexAttribI1uivEXT(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglVertexAttribI2uivEXT(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLuint *v_address = (const GLuint *)(intptr_t)v;
	glVertexAttribI2uivEXTPROC glVertexAttribI2uivEXT = (glVertexAttribI2uivEXTPROC)((intptr_t)function_pointer);
	glVertexAttribI2uivEXT(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglVertexAttribI3uivEXT(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLuint *v_address = (const GLuint *)(intptr_t)v;
	glVertexAttribI3uivEXTPROC glVertexAttribI3uivEXT = (glVertexAttribI3uivEXTPROC)((intptr_t)function_pointer);
	glVertexAttribI3uivEXT(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglVertexAttribI4uivEXT(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLuint *v_address = (const GLuint *)(intptr_t)v;
	glVertexAttribI4uivEXTPROC glVertexAttribI4uivEXT = (glVertexAttribI4uivEXTPROC)((intptr_t)function_pointer);
	glVertexAttribI4uivEXT(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglVertexAttribI4bvEXT(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLbyte *v_address = (const GLbyte *)(intptr_t)v;
	glVertexAttribI4bvEXTPROC glVertexAttribI4bvEXT = (glVertexAttribI4bvEXTPROC)((intptr_t)function_pointer);
	glVertexAttribI4bvEXT(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglVertexAttribI4svEXT(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLshort *v_address = (const GLshort *)(intptr_t)v;
	glVertexAttribI4svEXTPROC glVertexAttribI4svEXT = (glVertexAttribI4svEXTPROC)((intptr_t)function_pointer);
	glVertexAttribI4svEXT(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglVertexAttribI4ubvEXT(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLubyte *v_address = (const GLubyte *)(intptr_t)v;
	glVertexAttribI4ubvEXTPROC glVertexAttribI4ubvEXT = (glVertexAttribI4ubvEXTPROC)((intptr_t)function_pointer);
	glVertexAttribI4ubvEXT(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglVertexAttribI4usvEXT(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLushort *v_address = (const GLushort *)(intptr_t)v;
	glVertexAttribI4usvEXTPROC glVertexAttribI4usvEXT = (glVertexAttribI4usvEXTPROC)((intptr_t)function_pointer);
	glVertexAttribI4usvEXT(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglVertexAttribIPointerEXT(JNIEnv *env, jclass clazz, jint index, jint size, jint type, jint stride, jlong buffer, jlong function_pointer) {
	const GLvoid *buffer_address = (const GLvoid *)(intptr_t)buffer;
	glVertexAttribIPointerEXTPROC glVertexAttribIPointerEXT = (glVertexAttribIPointerEXTPROC)((intptr_t)function_pointer);
	glVertexAttribIPointerEXT(index, size, type, stride, buffer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglVertexAttribIPointerEXTBO(JNIEnv *env, jclass clazz, jint index, jint size, jint type, jint stride, jlong buffer_buffer_offset, jlong function_pointer) {
	const GLvoid *buffer_address = (const GLvoid *)(intptr_t)offsetToPointer(buffer_buffer_offset);
	glVertexAttribIPointerEXTPROC glVertexAttribIPointerEXT = (glVertexAttribIPointerEXTPROC)((intptr_t)function_pointer);
	glVertexAttribIPointerEXT(index, size, type, stride, buffer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglGetVertexAttribIivEXT(JNIEnv *env, jclass clazz, jint index, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetVertexAttribIivEXTPROC glGetVertexAttribIivEXT = (glGetVertexAttribIivEXTPROC)((intptr_t)function_pointer);
	glGetVertexAttribIivEXT(index, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglGetVertexAttribIuivEXT(JNIEnv *env, jclass clazz, jint index, jint pname, jlong params, jlong function_pointer) {
	GLuint *params_address = (GLuint *)(intptr_t)params;
	glGetVertexAttribIuivEXTPROC glGetVertexAttribIuivEXT = (glGetVertexAttribIuivEXTPROC)((intptr_t)function_pointer);
	glGetVertexAttribIuivEXT(index, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglUniform1uiEXT(JNIEnv *env, jclass clazz, jint location, jint v0, jlong function_pointer) {
	glUniform1uiEXTPROC glUniform1uiEXT = (glUniform1uiEXTPROC)((intptr_t)function_pointer);
	glUniform1uiEXT(location, v0);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglUniform2uiEXT(JNIEnv *env, jclass clazz, jint location, jint v0, jint v1, jlong function_pointer) {
	glUniform2uiEXTPROC glUniform2uiEXT = (glUniform2uiEXTPROC)((intptr_t)function_pointer);
	glUniform2uiEXT(location, v0, v1);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglUniform3uiEXT(JNIEnv *env, jclass clazz, jint location, jint v0, jint v1, jint v2, jlong function_pointer) {
	glUniform3uiEXTPROC glUniform3uiEXT = (glUniform3uiEXTPROC)((intptr_t)function_pointer);
	glUniform3uiEXT(location, v0, v1, v2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglUniform4uiEXT(JNIEnv *env, jclass clazz, jint location, jint v0, jint v1, jint v2, jint v3, jlong function_pointer) {
	glUniform4uiEXTPROC glUniform4uiEXT = (glUniform4uiEXTPROC)((intptr_t)function_pointer);
	glUniform4uiEXT(location, v0, v1, v2, v3);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglUniform1uivEXT(JNIEnv *env, jclass clazz, jint location, jint count, jlong value, jlong function_pointer) {
	const GLuint *value_address = (const GLuint *)(intptr_t)value;
	glUniform1uivEXTPROC glUniform1uivEXT = (glUniform1uivEXTPROC)((intptr_t)function_pointer);
	glUniform1uivEXT(location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglUniform2uivEXT(JNIEnv *env, jclass clazz, jint location, jint count, jlong value, jlong function_pointer) {
	const GLuint *value_address = (const GLuint *)(intptr_t)value;
	glUniform2uivEXTPROC glUniform2uivEXT = (glUniform2uivEXTPROC)((intptr_t)function_pointer);
	glUniform2uivEXT(location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglUniform3uivEXT(JNIEnv *env, jclass clazz, jint location, jint count, jlong value, jlong function_pointer) {
	const GLuint *value_address = (const GLuint *)(intptr_t)value;
	glUniform3uivEXTPROC glUniform3uivEXT = (glUniform3uivEXTPROC)((intptr_t)function_pointer);
	glUniform3uivEXT(location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglUniform4uivEXT(JNIEnv *env, jclass clazz, jint location, jint count, jlong value, jlong function_pointer) {
	const GLuint *value_address = (const GLuint *)(intptr_t)value;
	glUniform4uivEXTPROC glUniform4uivEXT = (glUniform4uivEXTPROC)((intptr_t)function_pointer);
	glUniform4uivEXT(location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglGetUniformuivEXT(JNIEnv *env, jclass clazz, jint program, jint location, jlong params, jlong function_pointer) {
	GLuint *params_address = (GLuint *)(intptr_t)params;
	glGetUniformuivEXTPROC glGetUniformuivEXT = (glGetUniformuivEXTPROC)((intptr_t)function_pointer);
	glGetUniformuivEXT(program, location, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglBindFragDataLocationEXT(JNIEnv *env, jclass clazz, jint program, jint colorNumber, jlong name, jlong function_pointer) {
	const GLchar *name_address = (const GLchar *)(intptr_t)name;
	glBindFragDataLocationEXTPROC glBindFragDataLocationEXT = (glBindFragDataLocationEXTPROC)((intptr_t)function_pointer);
	glBindFragDataLocationEXT(program, colorNumber, name_address);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_EXTGpuShader4_nglGetFragDataLocationEXT(JNIEnv *env, jclass clazz, jint program, jlong name, jlong function_pointer) {
	const GLchar *name_address = (const GLchar *)(intptr_t)name;
	glGetFragDataLocationEXTPROC glGetFragDataLocationEXT = (glGetFragDataLocationEXTPROC)((intptr_t)function_pointer);
	GLint __result = glGetFragDataLocationEXT(program, name_address);
	return __result;
}


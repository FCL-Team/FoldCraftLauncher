/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glExecuteProgramNVPROC) (GLenum target, GLuint id, const GLfloat * params);
typedef void (APIENTRY *glGetProgramParameterfvNVPROC) (GLenum target, GLuint index, GLenum parameterName, GLfloat * params);
typedef void (APIENTRY *glGetProgramParameterdvNVPROC) (GLenum target, GLuint index, GLenum parameterName, GLdouble * params);
typedef void (APIENTRY *glGetTrackMatrixivNVPROC) (GLenum target, GLuint address, GLenum parameterName, GLint * params);
typedef void (APIENTRY *glGetVertexAttribfvNVPROC) (GLuint index, GLenum parameterName, GLfloat * params);
typedef void (APIENTRY *glGetVertexAttribdvNVPROC) (GLuint index, GLenum parameterName, GLdouble * params);
typedef void (APIENTRY *glGetVertexAttribivNVPROC) (GLuint index, GLenum parameterName, GLint * params);
typedef void (APIENTRY *glGetVertexAttribPointervNVPROC) (GLuint index, GLenum parameterName, GLvoid ** pointer);
typedef void (APIENTRY *glProgramParameter4fNVPROC) (GLenum target, GLuint index, GLfloat x, GLfloat y, GLfloat z, GLfloat w);
typedef void (APIENTRY *glProgramParameter4dNVPROC) (GLenum target, GLuint index, GLdouble x, GLdouble y, GLdouble z, GLdouble w);
typedef void (APIENTRY *glProgramParameters4fvNVPROC) (GLenum target, GLuint index, GLuint count, const GLfloat * params);
typedef void (APIENTRY *glProgramParameters4dvNVPROC) (GLenum target, GLuint index, GLuint count, const GLdouble * params);
typedef void (APIENTRY *glTrackMatrixNVPROC) (GLenum target, GLuint address, GLenum matrix, GLenum transform);
typedef void (APIENTRY *glVertexAttribPointerNVPROC) (GLuint index, GLint size, GLenum type, GLsizei stride, const GLvoid * buffer);
typedef void (APIENTRY *glVertexAttrib1sNVPROC) (GLuint index, GLshort x);
typedef void (APIENTRY *glVertexAttrib1fNVPROC) (GLuint index, GLfloat x);
typedef void (APIENTRY *glVertexAttrib1dNVPROC) (GLuint index, GLdouble x);
typedef void (APIENTRY *glVertexAttrib2sNVPROC) (GLuint index, GLshort x, GLshort y);
typedef void (APIENTRY *glVertexAttrib2fNVPROC) (GLuint index, GLfloat x, GLfloat y);
typedef void (APIENTRY *glVertexAttrib2dNVPROC) (GLuint index, GLdouble x, GLdouble y);
typedef void (APIENTRY *glVertexAttrib3sNVPROC) (GLuint index, GLshort x, GLshort y, GLshort z);
typedef void (APIENTRY *glVertexAttrib3fNVPROC) (GLuint index, GLfloat x, GLfloat y, GLfloat z);
typedef void (APIENTRY *glVertexAttrib3dNVPROC) (GLuint index, GLdouble x, GLdouble y, GLdouble z);
typedef void (APIENTRY *glVertexAttrib4sNVPROC) (GLuint index, GLshort x, GLshort y, GLshort z, GLshort w);
typedef void (APIENTRY *glVertexAttrib4fNVPROC) (GLuint index, GLfloat x, GLfloat y, GLfloat z, GLfloat w);
typedef void (APIENTRY *glVertexAttrib4dNVPROC) (GLuint index, GLdouble x, GLdouble y, GLdouble z, GLdouble w);
typedef void (APIENTRY *glVertexAttrib4ubNVPROC) (GLuint index, GLubyte x, GLubyte y, GLubyte z, GLubyte w);
typedef void (APIENTRY *glVertexAttribs1svNVPROC) (GLuint index, GLsizei n, const GLshort * v);
typedef void (APIENTRY *glVertexAttribs1fvNVPROC) (GLuint index, GLsizei n, const GLfloat * v);
typedef void (APIENTRY *glVertexAttribs1dvNVPROC) (GLuint index, GLsizei n, const GLdouble * v);
typedef void (APIENTRY *glVertexAttribs2svNVPROC) (GLuint index, GLsizei n, const GLshort * v);
typedef void (APIENTRY *glVertexAttribs2fvNVPROC) (GLuint index, GLsizei n, const GLfloat * v);
typedef void (APIENTRY *glVertexAttribs2dvNVPROC) (GLuint index, GLsizei n, const GLdouble * v);
typedef void (APIENTRY *glVertexAttribs3svNVPROC) (GLuint index, GLsizei n, const GLshort * v);
typedef void (APIENTRY *glVertexAttribs3fvNVPROC) (GLuint index, GLsizei n, const GLfloat * v);
typedef void (APIENTRY *glVertexAttribs3dvNVPROC) (GLuint index, GLsizei n, const GLdouble * v);
typedef void (APIENTRY *glVertexAttribs4svNVPROC) (GLuint index, GLsizei n, const GLshort * v);
typedef void (APIENTRY *glVertexAttribs4fvNVPROC) (GLuint index, GLsizei n, const GLfloat * v);
typedef void (APIENTRY *glVertexAttribs4dvNVPROC) (GLuint index, GLsizei n, const GLdouble * v);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglExecuteProgramNV(JNIEnv *env, jclass clazz, jint target, jint id, jlong params, jlong function_pointer) {
	const GLfloat *params_address = (const GLfloat *)(intptr_t)params;
	glExecuteProgramNVPROC glExecuteProgramNV = (glExecuteProgramNVPROC)((intptr_t)function_pointer);
	glExecuteProgramNV(target, id, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglGetProgramParameterfvNV(JNIEnv *env, jclass clazz, jint target, jint index, jint parameterName, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetProgramParameterfvNVPROC glGetProgramParameterfvNV = (glGetProgramParameterfvNVPROC)((intptr_t)function_pointer);
	glGetProgramParameterfvNV(target, index, parameterName, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglGetProgramParameterdvNV(JNIEnv *env, jclass clazz, jint target, jint index, jint parameterName, jlong params, jlong function_pointer) {
	GLdouble *params_address = (GLdouble *)(intptr_t)params;
	glGetProgramParameterdvNVPROC glGetProgramParameterdvNV = (glGetProgramParameterdvNVPROC)((intptr_t)function_pointer);
	glGetProgramParameterdvNV(target, index, parameterName, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglGetTrackMatrixivNV(JNIEnv *env, jclass clazz, jint target, jint address, jint parameterName, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetTrackMatrixivNVPROC glGetTrackMatrixivNV = (glGetTrackMatrixivNVPROC)((intptr_t)function_pointer);
	glGetTrackMatrixivNV(target, address, parameterName, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglGetVertexAttribfvNV(JNIEnv *env, jclass clazz, jint index, jint parameterName, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetVertexAttribfvNVPROC glGetVertexAttribfvNV = (glGetVertexAttribfvNVPROC)((intptr_t)function_pointer);
	glGetVertexAttribfvNV(index, parameterName, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglGetVertexAttribdvNV(JNIEnv *env, jclass clazz, jint index, jint parameterName, jlong params, jlong function_pointer) {
	GLdouble *params_address = (GLdouble *)(intptr_t)params;
	glGetVertexAttribdvNVPROC glGetVertexAttribdvNV = (glGetVertexAttribdvNVPROC)((intptr_t)function_pointer);
	glGetVertexAttribdvNV(index, parameterName, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglGetVertexAttribivNV(JNIEnv *env, jclass clazz, jint index, jint parameterName, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetVertexAttribivNVPROC glGetVertexAttribivNV = (glGetVertexAttribivNVPROC)((intptr_t)function_pointer);
	glGetVertexAttribivNV(index, parameterName, params_address);
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglGetVertexAttribPointervNV(JNIEnv *env, jclass clazz, jint index, jint parameterName, jlong result_size, jlong function_pointer) {
	glGetVertexAttribPointervNVPROC glGetVertexAttribPointervNV = (glGetVertexAttribPointervNVPROC)((intptr_t)function_pointer);
	GLvoid * __result;
	glGetVertexAttribPointervNV(index, parameterName, &__result);
	return safeNewBuffer(env, __result, result_size);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglProgramParameter4fNV(JNIEnv *env, jclass clazz, jint target, jint index, jfloat x, jfloat y, jfloat z, jfloat w, jlong function_pointer) {
	glProgramParameter4fNVPROC glProgramParameter4fNV = (glProgramParameter4fNVPROC)((intptr_t)function_pointer);
	glProgramParameter4fNV(target, index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglProgramParameter4dNV(JNIEnv *env, jclass clazz, jint target, jint index, jdouble x, jdouble y, jdouble z, jdouble w, jlong function_pointer) {
	glProgramParameter4dNVPROC glProgramParameter4dNV = (glProgramParameter4dNVPROC)((intptr_t)function_pointer);
	glProgramParameter4dNV(target, index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglProgramParameters4fvNV(JNIEnv *env, jclass clazz, jint target, jint index, jint count, jlong params, jlong function_pointer) {
	const GLfloat *params_address = (const GLfloat *)(intptr_t)params;
	glProgramParameters4fvNVPROC glProgramParameters4fvNV = (glProgramParameters4fvNVPROC)((intptr_t)function_pointer);
	glProgramParameters4fvNV(target, index, count, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglProgramParameters4dvNV(JNIEnv *env, jclass clazz, jint target, jint index, jint count, jlong params, jlong function_pointer) {
	const GLdouble *params_address = (const GLdouble *)(intptr_t)params;
	glProgramParameters4dvNVPROC glProgramParameters4dvNV = (glProgramParameters4dvNVPROC)((intptr_t)function_pointer);
	glProgramParameters4dvNV(target, index, count, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglTrackMatrixNV(JNIEnv *env, jclass clazz, jint target, jint address, jint matrix, jint transform, jlong function_pointer) {
	glTrackMatrixNVPROC glTrackMatrixNV = (glTrackMatrixNVPROC)((intptr_t)function_pointer);
	glTrackMatrixNV(target, address, matrix, transform);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttribPointerNV(JNIEnv *env, jclass clazz, jint index, jint size, jint type, jint stride, jlong buffer, jlong function_pointer) {
	const GLvoid *buffer_address = (const GLvoid *)(intptr_t)buffer;
	glVertexAttribPointerNVPROC glVertexAttribPointerNV = (glVertexAttribPointerNVPROC)((intptr_t)function_pointer);
	glVertexAttribPointerNV(index, size, type, stride, buffer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttribPointerNVBO(JNIEnv *env, jclass clazz, jint index, jint size, jint type, jint stride, jlong buffer_buffer_offset, jlong function_pointer) {
	const GLvoid *buffer_address = (const GLvoid *)(intptr_t)offsetToPointer(buffer_buffer_offset);
	glVertexAttribPointerNVPROC glVertexAttribPointerNV = (glVertexAttribPointerNVPROC)((intptr_t)function_pointer);
	glVertexAttribPointerNV(index, size, type, stride, buffer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttrib1sNV(JNIEnv *env, jclass clazz, jint index, jshort x, jlong function_pointer) {
	glVertexAttrib1sNVPROC glVertexAttrib1sNV = (glVertexAttrib1sNVPROC)((intptr_t)function_pointer);
	glVertexAttrib1sNV(index, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttrib1fNV(JNIEnv *env, jclass clazz, jint index, jfloat x, jlong function_pointer) {
	glVertexAttrib1fNVPROC glVertexAttrib1fNV = (glVertexAttrib1fNVPROC)((intptr_t)function_pointer);
	glVertexAttrib1fNV(index, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttrib1dNV(JNIEnv *env, jclass clazz, jint index, jdouble x, jlong function_pointer) {
	glVertexAttrib1dNVPROC glVertexAttrib1dNV = (glVertexAttrib1dNVPROC)((intptr_t)function_pointer);
	glVertexAttrib1dNV(index, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttrib2sNV(JNIEnv *env, jclass clazz, jint index, jshort x, jshort y, jlong function_pointer) {
	glVertexAttrib2sNVPROC glVertexAttrib2sNV = (glVertexAttrib2sNVPROC)((intptr_t)function_pointer);
	glVertexAttrib2sNV(index, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttrib2fNV(JNIEnv *env, jclass clazz, jint index, jfloat x, jfloat y, jlong function_pointer) {
	glVertexAttrib2fNVPROC glVertexAttrib2fNV = (glVertexAttrib2fNVPROC)((intptr_t)function_pointer);
	glVertexAttrib2fNV(index, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttrib2dNV(JNIEnv *env, jclass clazz, jint index, jdouble x, jdouble y, jlong function_pointer) {
	glVertexAttrib2dNVPROC glVertexAttrib2dNV = (glVertexAttrib2dNVPROC)((intptr_t)function_pointer);
	glVertexAttrib2dNV(index, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttrib3sNV(JNIEnv *env, jclass clazz, jint index, jshort x, jshort y, jshort z, jlong function_pointer) {
	glVertexAttrib3sNVPROC glVertexAttrib3sNV = (glVertexAttrib3sNVPROC)((intptr_t)function_pointer);
	glVertexAttrib3sNV(index, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttrib3fNV(JNIEnv *env, jclass clazz, jint index, jfloat x, jfloat y, jfloat z, jlong function_pointer) {
	glVertexAttrib3fNVPROC glVertexAttrib3fNV = (glVertexAttrib3fNVPROC)((intptr_t)function_pointer);
	glVertexAttrib3fNV(index, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttrib3dNV(JNIEnv *env, jclass clazz, jint index, jdouble x, jdouble y, jdouble z, jlong function_pointer) {
	glVertexAttrib3dNVPROC glVertexAttrib3dNV = (glVertexAttrib3dNVPROC)((intptr_t)function_pointer);
	glVertexAttrib3dNV(index, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttrib4sNV(JNIEnv *env, jclass clazz, jint index, jshort x, jshort y, jshort z, jshort w, jlong function_pointer) {
	glVertexAttrib4sNVPROC glVertexAttrib4sNV = (glVertexAttrib4sNVPROC)((intptr_t)function_pointer);
	glVertexAttrib4sNV(index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttrib4fNV(JNIEnv *env, jclass clazz, jint index, jfloat x, jfloat y, jfloat z, jfloat w, jlong function_pointer) {
	glVertexAttrib4fNVPROC glVertexAttrib4fNV = (glVertexAttrib4fNVPROC)((intptr_t)function_pointer);
	glVertexAttrib4fNV(index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttrib4dNV(JNIEnv *env, jclass clazz, jint index, jdouble x, jdouble y, jdouble z, jdouble w, jlong function_pointer) {
	glVertexAttrib4dNVPROC glVertexAttrib4dNV = (glVertexAttrib4dNVPROC)((intptr_t)function_pointer);
	glVertexAttrib4dNV(index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttrib4ubNV(JNIEnv *env, jclass clazz, jint index, jbyte x, jbyte y, jbyte z, jbyte w, jlong function_pointer) {
	glVertexAttrib4ubNVPROC glVertexAttrib4ubNV = (glVertexAttrib4ubNVPROC)((intptr_t)function_pointer);
	glVertexAttrib4ubNV(index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttribs1svNV(JNIEnv *env, jclass clazz, jint index, jint n, jlong v, jlong function_pointer) {
	const GLshort *v_address = (const GLshort *)(intptr_t)v;
	glVertexAttribs1svNVPROC glVertexAttribs1svNV = (glVertexAttribs1svNVPROC)((intptr_t)function_pointer);
	glVertexAttribs1svNV(index, n, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttribs1fvNV(JNIEnv *env, jclass clazz, jint index, jint n, jlong v, jlong function_pointer) {
	const GLfloat *v_address = (const GLfloat *)(intptr_t)v;
	glVertexAttribs1fvNVPROC glVertexAttribs1fvNV = (glVertexAttribs1fvNVPROC)((intptr_t)function_pointer);
	glVertexAttribs1fvNV(index, n, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttribs1dvNV(JNIEnv *env, jclass clazz, jint index, jint n, jlong v, jlong function_pointer) {
	const GLdouble *v_address = (const GLdouble *)(intptr_t)v;
	glVertexAttribs1dvNVPROC glVertexAttribs1dvNV = (glVertexAttribs1dvNVPROC)((intptr_t)function_pointer);
	glVertexAttribs1dvNV(index, n, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttribs2svNV(JNIEnv *env, jclass clazz, jint index, jint n, jlong v, jlong function_pointer) {
	const GLshort *v_address = (const GLshort *)(intptr_t)v;
	glVertexAttribs2svNVPROC glVertexAttribs2svNV = (glVertexAttribs2svNVPROC)((intptr_t)function_pointer);
	glVertexAttribs2svNV(index, n, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttribs2fvNV(JNIEnv *env, jclass clazz, jint index, jint n, jlong v, jlong function_pointer) {
	const GLfloat *v_address = (const GLfloat *)(intptr_t)v;
	glVertexAttribs2fvNVPROC glVertexAttribs2fvNV = (glVertexAttribs2fvNVPROC)((intptr_t)function_pointer);
	glVertexAttribs2fvNV(index, n, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttribs2dvNV(JNIEnv *env, jclass clazz, jint index, jint n, jlong v, jlong function_pointer) {
	const GLdouble *v_address = (const GLdouble *)(intptr_t)v;
	glVertexAttribs2dvNVPROC glVertexAttribs2dvNV = (glVertexAttribs2dvNVPROC)((intptr_t)function_pointer);
	glVertexAttribs2dvNV(index, n, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttribs3svNV(JNIEnv *env, jclass clazz, jint index, jint n, jlong v, jlong function_pointer) {
	const GLshort *v_address = (const GLshort *)(intptr_t)v;
	glVertexAttribs3svNVPROC glVertexAttribs3svNV = (glVertexAttribs3svNVPROC)((intptr_t)function_pointer);
	glVertexAttribs3svNV(index, n, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttribs3fvNV(JNIEnv *env, jclass clazz, jint index, jint n, jlong v, jlong function_pointer) {
	const GLfloat *v_address = (const GLfloat *)(intptr_t)v;
	glVertexAttribs3fvNVPROC glVertexAttribs3fvNV = (glVertexAttribs3fvNVPROC)((intptr_t)function_pointer);
	glVertexAttribs3fvNV(index, n, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttribs3dvNV(JNIEnv *env, jclass clazz, jint index, jint n, jlong v, jlong function_pointer) {
	const GLdouble *v_address = (const GLdouble *)(intptr_t)v;
	glVertexAttribs3dvNVPROC glVertexAttribs3dvNV = (glVertexAttribs3dvNVPROC)((intptr_t)function_pointer);
	glVertexAttribs3dvNV(index, n, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttribs4svNV(JNIEnv *env, jclass clazz, jint index, jint n, jlong v, jlong function_pointer) {
	const GLshort *v_address = (const GLshort *)(intptr_t)v;
	glVertexAttribs4svNVPROC glVertexAttribs4svNV = (glVertexAttribs4svNVPROC)((intptr_t)function_pointer);
	glVertexAttribs4svNV(index, n, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttribs4fvNV(JNIEnv *env, jclass clazz, jint index, jint n, jlong v, jlong function_pointer) {
	const GLfloat *v_address = (const GLfloat *)(intptr_t)v;
	glVertexAttribs4fvNVPROC glVertexAttribs4fvNV = (glVertexAttribs4fvNVPROC)((intptr_t)function_pointer);
	glVertexAttribs4fvNV(index, n, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexProgram_nglVertexAttribs4dvNV(JNIEnv *env, jclass clazz, jint index, jint n, jlong v, jlong function_pointer) {
	const GLdouble *v_address = (const GLdouble *)(intptr_t)v;
	glVertexAttribs4dvNVPROC glVertexAttribs4dvNV = (glVertexAttribs4dvNVPROC)((intptr_t)function_pointer);
	glVertexAttribs4dvNV(index, n, v_address);
}


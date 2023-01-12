/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glVertexAttribL1dEXTPROC) (GLuint index, GLdouble x);
typedef void (APIENTRY *glVertexAttribL2dEXTPROC) (GLuint index, GLdouble x, GLdouble y);
typedef void (APIENTRY *glVertexAttribL3dEXTPROC) (GLuint index, GLdouble x, GLdouble y, GLdouble z);
typedef void (APIENTRY *glVertexAttribL4dEXTPROC) (GLuint index, GLdouble x, GLdouble y, GLdouble z, GLdouble w);
typedef void (APIENTRY *glVertexAttribL1dvEXTPROC) (GLuint index, const GLdouble * v);
typedef void (APIENTRY *glVertexAttribL2dvEXTPROC) (GLuint index, const GLdouble * v);
typedef void (APIENTRY *glVertexAttribL3dvEXTPROC) (GLuint index, const GLdouble * v);
typedef void (APIENTRY *glVertexAttribL4dvEXTPROC) (GLuint index, const GLdouble * v);
typedef void (APIENTRY *glVertexAttribLPointerEXTPROC) (GLuint index, GLint size, GLenum type, GLsizei stride, const GLvoid * pointer);
typedef void (APIENTRY *glGetVertexAttribLdvEXTPROC) (GLuint index, GLenum pname, GLdouble * params);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexAttrib64bit_nglVertexAttribL1dEXT(JNIEnv *env, jclass clazz, jint index, jdouble x, jlong function_pointer) {
	glVertexAttribL1dEXTPROC glVertexAttribL1dEXT = (glVertexAttribL1dEXTPROC)((intptr_t)function_pointer);
	glVertexAttribL1dEXT(index, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexAttrib64bit_nglVertexAttribL2dEXT(JNIEnv *env, jclass clazz, jint index, jdouble x, jdouble y, jlong function_pointer) {
	glVertexAttribL2dEXTPROC glVertexAttribL2dEXT = (glVertexAttribL2dEXTPROC)((intptr_t)function_pointer);
	glVertexAttribL2dEXT(index, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexAttrib64bit_nglVertexAttribL3dEXT(JNIEnv *env, jclass clazz, jint index, jdouble x, jdouble y, jdouble z, jlong function_pointer) {
	glVertexAttribL3dEXTPROC glVertexAttribL3dEXT = (glVertexAttribL3dEXTPROC)((intptr_t)function_pointer);
	glVertexAttribL3dEXT(index, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexAttrib64bit_nglVertexAttribL4dEXT(JNIEnv *env, jclass clazz, jint index, jdouble x, jdouble y, jdouble z, jdouble w, jlong function_pointer) {
	glVertexAttribL4dEXTPROC glVertexAttribL4dEXT = (glVertexAttribL4dEXTPROC)((intptr_t)function_pointer);
	glVertexAttribL4dEXT(index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexAttrib64bit_nglVertexAttribL1dvEXT(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLdouble *v_address = (const GLdouble *)(intptr_t)v;
	glVertexAttribL1dvEXTPROC glVertexAttribL1dvEXT = (glVertexAttribL1dvEXTPROC)((intptr_t)function_pointer);
	glVertexAttribL1dvEXT(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexAttrib64bit_nglVertexAttribL2dvEXT(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLdouble *v_address = (const GLdouble *)(intptr_t)v;
	glVertexAttribL2dvEXTPROC glVertexAttribL2dvEXT = (glVertexAttribL2dvEXTPROC)((intptr_t)function_pointer);
	glVertexAttribL2dvEXT(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexAttrib64bit_nglVertexAttribL3dvEXT(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLdouble *v_address = (const GLdouble *)(intptr_t)v;
	glVertexAttribL3dvEXTPROC glVertexAttribL3dvEXT = (glVertexAttribL3dvEXTPROC)((intptr_t)function_pointer);
	glVertexAttribL3dvEXT(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexAttrib64bit_nglVertexAttribL4dvEXT(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLdouble *v_address = (const GLdouble *)(intptr_t)v;
	glVertexAttribL4dvEXTPROC glVertexAttribL4dvEXT = (glVertexAttribL4dvEXTPROC)((intptr_t)function_pointer);
	glVertexAttribL4dvEXT(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexAttrib64bit_nglVertexAttribLPointerEXT(JNIEnv *env, jclass clazz, jint index, jint size, jint type, jint stride, jlong pointer, jlong function_pointer) {
	const GLvoid *pointer_address = (const GLvoid *)(intptr_t)pointer;
	glVertexAttribLPointerEXTPROC glVertexAttribLPointerEXT = (glVertexAttribLPointerEXTPROC)((intptr_t)function_pointer);
	glVertexAttribLPointerEXT(index, size, type, stride, pointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexAttrib64bit_nglVertexAttribLPointerEXTBO(JNIEnv *env, jclass clazz, jint index, jint size, jint type, jint stride, jlong pointer_buffer_offset, jlong function_pointer) {
	const GLvoid *pointer_address = (const GLvoid *)(intptr_t)offsetToPointer(pointer_buffer_offset);
	glVertexAttribLPointerEXTPROC glVertexAttribLPointerEXT = (glVertexAttribLPointerEXTPROC)((intptr_t)function_pointer);
	glVertexAttribLPointerEXT(index, size, type, stride, pointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexAttrib64bit_nglGetVertexAttribLdvEXT(JNIEnv *env, jclass clazz, jint index, jint pname, jlong params, jlong function_pointer) {
	GLdouble *params_address = (GLdouble *)(intptr_t)params;
	glGetVertexAttribLdvEXTPROC glGetVertexAttribLdvEXT = (glGetVertexAttribLdvEXTPROC)((intptr_t)function_pointer);
	glGetVertexAttribLdvEXT(index, pname, params_address);
}


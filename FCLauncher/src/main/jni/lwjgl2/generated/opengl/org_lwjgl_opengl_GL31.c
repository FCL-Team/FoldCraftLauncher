/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glDrawArraysInstancedPROC) (GLenum mode, GLint first, GLsizei count, GLsizei primcount);
typedef void (APIENTRY *glDrawElementsInstancedPROC) (GLenum mode, GLsizei count, GLenum type, const GLvoid * indices, GLsizei primcount);
typedef void (APIENTRY *glCopyBufferSubDataPROC) (GLenum readtarget, GLenum writetarget, GLintptr readoffset, GLintptr writeoffset, GLsizeiptr size);
typedef void (APIENTRY *glPrimitiveRestartIndexPROC) (GLuint index);
typedef void (APIENTRY *glTexBufferPROC) (GLenum target, GLenum internalformat, GLuint buffer);
typedef void (APIENTRY *glGetUniformIndicesPROC) (GLuint program, GLsizei uniformCount, const GLchar ** uniformNames, GLuint * uniformIndices);
typedef void (APIENTRY *glGetActiveUniformsivPROC) (GLuint program, GLsizei uniformCount, const GLuint * uniformIndices, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetActiveUniformNamePROC) (GLuint program, GLuint uniformIndex, GLsizei bufSize, GLsizei * length, GLchar * uniformName);
typedef GLuint (APIENTRY *glGetUniformBlockIndexPROC) (GLuint program, const GLchar * uniformBlockName);
typedef void (APIENTRY *glGetActiveUniformBlockivPROC) (GLuint program, GLuint uniformBlockIndex, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetActiveUniformBlockNamePROC) (GLuint program, GLuint uniformBlockIndex, GLsizei bufSize, GLsizei * length, GLchar * uniformBlockName);
typedef void (APIENTRY *glUniformBlockBindingPROC) (GLuint program, GLuint uniformBlockIndex, GLuint uniformBlockBinding);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL31_nglDrawArraysInstanced(JNIEnv *env, jclass clazz, jint mode, jint first, jint count, jint primcount, jlong function_pointer) {
	glDrawArraysInstancedPROC glDrawArraysInstanced = (glDrawArraysInstancedPROC)((intptr_t)function_pointer);
	glDrawArraysInstanced(mode, first, count, primcount);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL31_nglDrawElementsInstanced(JNIEnv *env, jclass clazz, jint mode, jint count, jint type, jlong indices, jint primcount, jlong function_pointer) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)indices;
	glDrawElementsInstancedPROC glDrawElementsInstanced = (glDrawElementsInstancedPROC)((intptr_t)function_pointer);
	glDrawElementsInstanced(mode, count, type, indices_address, primcount);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL31_nglDrawElementsInstancedBO(JNIEnv *env, jclass clazz, jint mode, jint count, jint type, jlong indices_buffer_offset, jint primcount, jlong function_pointer) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)offsetToPointer(indices_buffer_offset);
	glDrawElementsInstancedPROC glDrawElementsInstanced = (glDrawElementsInstancedPROC)((intptr_t)function_pointer);
	glDrawElementsInstanced(mode, count, type, indices_address, primcount);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL31_nglCopyBufferSubData(JNIEnv *env, jclass clazz, jint readtarget, jint writetarget, jlong readoffset, jlong writeoffset, jlong size, jlong function_pointer) {
	glCopyBufferSubDataPROC glCopyBufferSubData = (glCopyBufferSubDataPROC)((intptr_t)function_pointer);
	glCopyBufferSubData(readtarget, writetarget, readoffset, writeoffset, size);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL31_nglPrimitiveRestartIndex(JNIEnv *env, jclass clazz, jint index, jlong function_pointer) {
	glPrimitiveRestartIndexPROC glPrimitiveRestartIndex = (glPrimitiveRestartIndexPROC)((intptr_t)function_pointer);
	glPrimitiveRestartIndex(index);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL31_nglTexBuffer(JNIEnv *env, jclass clazz, jint target, jint internalformat, jint buffer, jlong function_pointer) {
	glTexBufferPROC glTexBuffer = (glTexBufferPROC)((intptr_t)function_pointer);
	glTexBuffer(target, internalformat, buffer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL31_nglGetUniformIndices(JNIEnv *env, jclass clazz, jint program, jint uniformCount, jlong uniformNames, jlong uniformIndices, jlong function_pointer) {
	const GLchar *uniformNames_address = (const GLchar *)(intptr_t)uniformNames;
	int _str_i;
	GLchar *_str_address;
	GLchar **uniformNames_str = (GLchar **) malloc(uniformCount * sizeof(GLchar *));
	GLuint *uniformIndices_address = (GLuint *)(intptr_t)uniformIndices;
	glGetUniformIndicesPROC glGetUniformIndices = (glGetUniformIndicesPROC)((intptr_t)function_pointer);
	_str_i = 0;
	_str_address = (GLchar *)uniformNames_address;
	while ( _str_i < uniformCount ) {
		uniformNames_str[_str_i++] = _str_address;
		_str_address += strlen((const char *)_str_address) + 1;
	}
	glGetUniformIndices(program, uniformCount, (const GLchar **)uniformNames_str, uniformIndices_address);
	free(uniformNames_str);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL31_nglGetActiveUniformsiv(JNIEnv *env, jclass clazz, jint program, jint uniformCount, jlong uniformIndices, jint pname, jlong params, jlong function_pointer) {
	const GLuint *uniformIndices_address = (const GLuint *)(intptr_t)uniformIndices;
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetActiveUniformsivPROC glGetActiveUniformsiv = (glGetActiveUniformsivPROC)((intptr_t)function_pointer);
	glGetActiveUniformsiv(program, uniformCount, uniformIndices_address, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL31_nglGetActiveUniformName(JNIEnv *env, jclass clazz, jint program, jint uniformIndex, jint bufSize, jlong length, jlong uniformName, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLchar *uniformName_address = (GLchar *)(intptr_t)uniformName;
	glGetActiveUniformNamePROC glGetActiveUniformName = (glGetActiveUniformNamePROC)((intptr_t)function_pointer);
	glGetActiveUniformName(program, uniformIndex, bufSize, length_address, uniformName_address);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL31_nglGetUniformBlockIndex(JNIEnv *env, jclass clazz, jint program, jlong uniformBlockName, jlong function_pointer) {
	const GLchar *uniformBlockName_address = (const GLchar *)(intptr_t)uniformBlockName;
	glGetUniformBlockIndexPROC glGetUniformBlockIndex = (glGetUniformBlockIndexPROC)((intptr_t)function_pointer);
	GLuint __result = glGetUniformBlockIndex(program, uniformBlockName_address);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL31_nglGetActiveUniformBlockiv(JNIEnv *env, jclass clazz, jint program, jint uniformBlockIndex, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetActiveUniformBlockivPROC glGetActiveUniformBlockiv = (glGetActiveUniformBlockivPROC)((intptr_t)function_pointer);
	glGetActiveUniformBlockiv(program, uniformBlockIndex, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL31_nglGetActiveUniformBlockName(JNIEnv *env, jclass clazz, jint program, jint uniformBlockIndex, jint bufSize, jlong length, jlong uniformBlockName, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLchar *uniformBlockName_address = (GLchar *)(intptr_t)uniformBlockName;
	glGetActiveUniformBlockNamePROC glGetActiveUniformBlockName = (glGetActiveUniformBlockNamePROC)((intptr_t)function_pointer);
	glGetActiveUniformBlockName(program, uniformBlockIndex, bufSize, length_address, uniformBlockName_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL31_nglUniformBlockBinding(JNIEnv *env, jclass clazz, jint program, jint uniformBlockIndex, jint uniformBlockBinding, jlong function_pointer) {
	glUniformBlockBindingPROC glUniformBlockBinding = (glUniformBlockBindingPROC)((intptr_t)function_pointer);
	glUniformBlockBinding(program, uniformBlockIndex, uniformBlockBinding);
}


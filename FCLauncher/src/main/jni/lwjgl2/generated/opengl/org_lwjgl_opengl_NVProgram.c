/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glLoadProgramNVPROC) (GLenum target, GLuint programID, GLsizei length, const GLvoid * string);
typedef void (APIENTRY *glBindProgramNVPROC) (GLenum target, GLuint programID);
typedef void (APIENTRY *glDeleteProgramsNVPROC) (GLsizei n, const GLuint * programs);
typedef void (APIENTRY *glGenProgramsNVPROC) (GLsizei n, GLuint * programs);
typedef void (APIENTRY *glGetProgramivNVPROC) (GLuint programID, GLenum parameterName, GLint * params);
typedef void (APIENTRY *glGetProgramStringNVPROC) (GLuint programID, GLenum parameterName, GLvoid * paramString);
typedef GLboolean (APIENTRY *glIsProgramNVPROC) (GLuint programID);
typedef GLboolean (APIENTRY *glAreProgramsResidentNVPROC) (GLsizei n, const GLuint * programIDs, GLboolean * programResidences);
typedef void (APIENTRY *glRequestResidentProgramsNVPROC) (GLsizei n, GLuint * programIDs);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVProgram_nglLoadProgramNV(JNIEnv *env, jclass clazz, jint target, jint programID, jint length, jlong string, jlong function_pointer) {
	const GLvoid *string_address = (const GLvoid *)(intptr_t)string;
	glLoadProgramNVPROC glLoadProgramNV = (glLoadProgramNVPROC)((intptr_t)function_pointer);
	glLoadProgramNV(target, programID, length, string_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVProgram_nglBindProgramNV(JNIEnv *env, jclass clazz, jint target, jint programID, jlong function_pointer) {
	glBindProgramNVPROC glBindProgramNV = (glBindProgramNVPROC)((intptr_t)function_pointer);
	glBindProgramNV(target, programID);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVProgram_nglDeleteProgramsNV(JNIEnv *env, jclass clazz, jint n, jlong programs, jlong function_pointer) {
	const GLuint *programs_address = (const GLuint *)(intptr_t)programs;
	glDeleteProgramsNVPROC glDeleteProgramsNV = (glDeleteProgramsNVPROC)((intptr_t)function_pointer);
	glDeleteProgramsNV(n, programs_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVProgram_nglGenProgramsNV(JNIEnv *env, jclass clazz, jint n, jlong programs, jlong function_pointer) {
	GLuint *programs_address = (GLuint *)(intptr_t)programs;
	glGenProgramsNVPROC glGenProgramsNV = (glGenProgramsNVPROC)((intptr_t)function_pointer);
	glGenProgramsNV(n, programs_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVProgram_nglGetProgramivNV(JNIEnv *env, jclass clazz, jint programID, jint parameterName, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetProgramivNVPROC glGetProgramivNV = (glGetProgramivNVPROC)((intptr_t)function_pointer);
	glGetProgramivNV(programID, parameterName, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVProgram_nglGetProgramStringNV(JNIEnv *env, jclass clazz, jint programID, jint parameterName, jlong paramString, jlong function_pointer) {
	GLvoid *paramString_address = (GLvoid *)(intptr_t)paramString;
	glGetProgramStringNVPROC glGetProgramStringNV = (glGetProgramStringNVPROC)((intptr_t)function_pointer);
	glGetProgramStringNV(programID, parameterName, paramString_address);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_NVProgram_nglIsProgramNV(JNIEnv *env, jclass clazz, jint programID, jlong function_pointer) {
	glIsProgramNVPROC glIsProgramNV = (glIsProgramNVPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsProgramNV(programID);
	return __result;
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_NVProgram_nglAreProgramsResidentNV(JNIEnv *env, jclass clazz, jint n, jlong programIDs, jlong programResidences, jlong function_pointer) {
	const GLuint *programIDs_address = (const GLuint *)(intptr_t)programIDs;
	GLboolean *programResidences_address = (GLboolean *)(intptr_t)programResidences;
	glAreProgramsResidentNVPROC glAreProgramsResidentNV = (glAreProgramsResidentNVPROC)((intptr_t)function_pointer);
	GLboolean __result = glAreProgramsResidentNV(n, programIDs_address, programResidences_address);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVProgram_nglRequestResidentProgramsNV(JNIEnv *env, jclass clazz, jint n, jlong programIDs, jlong function_pointer) {
	GLuint *programIDs_address = (GLuint *)(intptr_t)programIDs;
	glRequestResidentProgramsNVPROC glRequestResidentProgramsNV = (glRequestResidentProgramsNVPROC)((intptr_t)function_pointer);
	glRequestResidentProgramsNV(n, programIDs_address);
}


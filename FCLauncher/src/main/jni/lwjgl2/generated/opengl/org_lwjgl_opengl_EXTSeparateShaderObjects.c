/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glUseShaderProgramEXTPROC) (GLenum type, GLuint program);
typedef void (APIENTRY *glActiveProgramEXTPROC) (GLuint program);
typedef GLuint (APIENTRY *glCreateShaderProgramEXTPROC) (GLenum type, const GLchar * string);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTSeparateShaderObjects_nglUseShaderProgramEXT(JNIEnv *env, jclass clazz, jint type, jint program, jlong function_pointer) {
	glUseShaderProgramEXTPROC glUseShaderProgramEXT = (glUseShaderProgramEXTPROC)((intptr_t)function_pointer);
	glUseShaderProgramEXT(type, program);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTSeparateShaderObjects_nglActiveProgramEXT(JNIEnv *env, jclass clazz, jint program, jlong function_pointer) {
	glActiveProgramEXTPROC glActiveProgramEXT = (glActiveProgramEXTPROC)((intptr_t)function_pointer);
	glActiveProgramEXT(program);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_EXTSeparateShaderObjects_nglCreateShaderProgramEXT(JNIEnv *env, jclass clazz, jint type, jlong string, jlong function_pointer) {
	const GLchar *string_address = (const GLchar *)(intptr_t)string;
	glCreateShaderProgramEXTPROC glCreateShaderProgramEXT = (glCreateShaderProgramEXTPROC)((intptr_t)function_pointer);
	GLuint __result = glCreateShaderProgramEXT(type, string_address);
	return __result;
}


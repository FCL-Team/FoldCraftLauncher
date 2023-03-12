/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glDeleteObjectARBPROC) (GLhandleARB obj);
typedef GLhandleARB (APIENTRY *glGetHandleARBPROC) (GLenum pname);
typedef void (APIENTRY *glDetachObjectARBPROC) (GLhandleARB containerObj, GLhandleARB attachedObj);
typedef GLhandleARB (APIENTRY *glCreateShaderObjectARBPROC) (GLenum shaderType);
typedef void (APIENTRY *glShaderSourceARBPROC) (GLhandleARB shader, GLsizei count, const GLcharARB ** string, const GLint* length);
typedef void (APIENTRY *glCompileShaderARBPROC) (GLhandleARB shaderObj);
typedef GLhandleARB (APIENTRY *glCreateProgramObjectARBPROC) ();
typedef void (APIENTRY *glAttachObjectARBPROC) (GLhandleARB containerObj, GLhandleARB obj);
typedef void (APIENTRY *glLinkProgramARBPROC) (GLhandleARB programObj);
typedef void (APIENTRY *glUseProgramObjectARBPROC) (GLhandleARB programObj);
typedef void (APIENTRY *glValidateProgramARBPROC) (GLhandleARB programObj);
typedef void (APIENTRY *glUniform1fARBPROC) (GLint location, GLfloat v0);
typedef void (APIENTRY *glUniform2fARBPROC) (GLint location, GLfloat v0, GLfloat v1);
typedef void (APIENTRY *glUniform3fARBPROC) (GLint location, GLfloat v0, GLfloat v1, GLfloat v2);
typedef void (APIENTRY *glUniform4fARBPROC) (GLint location, GLfloat v0, GLfloat v1, GLfloat v2, GLfloat v3);
typedef void (APIENTRY *glUniform1iARBPROC) (GLint location, GLint v0);
typedef void (APIENTRY *glUniform2iARBPROC) (GLint location, GLint v0, GLint v1);
typedef void (APIENTRY *glUniform3iARBPROC) (GLint location, GLint v0, GLint v1, GLint v2);
typedef void (APIENTRY *glUniform4iARBPROC) (GLint location, GLint v0, GLint v1, GLint v2, GLint v3);
typedef void (APIENTRY *glUniform1fvARBPROC) (GLint location, GLsizei count, const GLfloat * values);
typedef void (APIENTRY *glUniform2fvARBPROC) (GLint location, GLsizei count, const GLfloat * values);
typedef void (APIENTRY *glUniform3fvARBPROC) (GLint location, GLsizei count, const GLfloat * values);
typedef void (APIENTRY *glUniform4fvARBPROC) (GLint location, GLsizei count, const GLfloat * values);
typedef void (APIENTRY *glUniform1ivARBPROC) (GLint location, GLsizei count, const GLint * values);
typedef void (APIENTRY *glUniform2ivARBPROC) (GLint location, GLsizei count, const GLint * values);
typedef void (APIENTRY *glUniform3ivARBPROC) (GLint location, GLsizei count, const GLint * values);
typedef void (APIENTRY *glUniform4ivARBPROC) (GLint location, GLsizei count, const GLint * values);
typedef void (APIENTRY *glUniformMatrix2fvARBPROC) (GLint location, GLsizei count, GLboolean transpose, const GLfloat * matrices);
typedef void (APIENTRY *glUniformMatrix3fvARBPROC) (GLint location, GLsizei count, GLboolean transpose, const GLfloat * matrices);
typedef void (APIENTRY *glUniformMatrix4fvARBPROC) (GLint location, GLsizei count, GLboolean transpose, const GLfloat * matrices);
typedef void (APIENTRY *glGetObjectParameterfvARBPROC) (GLhandleARB obj, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetObjectParameterivARBPROC) (GLhandleARB obj, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetInfoLogARBPROC) (GLhandleARB obj, GLsizei maxLength, GLsizei * length, GLcharARB * infoLog);
typedef void (APIENTRY *glGetAttachedObjectsARBPROC) (GLhandleARB containerObj, GLsizei maxCount, GLsizei * count, GLhandleARB * obj);
typedef GLint (APIENTRY *glGetUniformLocationARBPROC) (GLhandleARB programObj, const GLcharARB * name);
typedef void (APIENTRY *glGetActiveUniformARBPROC) (GLhandleARB programObj, GLuint index, GLsizei maxLength, GLsizei * length, GLint * size, GLenum * type, GLcharARB * name);
typedef void (APIENTRY *glGetUniformfvARBPROC) (GLhandleARB programObj, GLint location, GLfloat * params);
typedef void (APIENTRY *glGetUniformivARBPROC) (GLhandleARB programObj, GLint location, GLint * params);
typedef void (APIENTRY *glGetShaderSourceARBPROC) (GLhandleARB obj, GLsizei maxLength, GLsizei * length, GLcharARB * source);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglDeleteObjectARB(JNIEnv *env, jclass clazz, jint obj, jlong function_pointer) {
	glDeleteObjectARBPROC glDeleteObjectARB = (glDeleteObjectARBPROC)((intptr_t)function_pointer);
	glDeleteObjectARB(obj);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglGetHandleARB(JNIEnv *env, jclass clazz, jint pname, jlong function_pointer) {
	glGetHandleARBPROC glGetHandleARB = (glGetHandleARBPROC)((intptr_t)function_pointer);
	GLhandleARB __result = glGetHandleARB(pname);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglDetachObjectARB(JNIEnv *env, jclass clazz, jint containerObj, jint attachedObj, jlong function_pointer) {
	glDetachObjectARBPROC glDetachObjectARB = (glDetachObjectARBPROC)((intptr_t)function_pointer);
	glDetachObjectARB(containerObj, attachedObj);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglCreateShaderObjectARB(JNIEnv *env, jclass clazz, jint shaderType, jlong function_pointer) {
	glCreateShaderObjectARBPROC glCreateShaderObjectARB = (glCreateShaderObjectARBPROC)((intptr_t)function_pointer);
	GLhandleARB __result = glCreateShaderObjectARB(shaderType);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglShaderSourceARB(JNIEnv *env, jclass clazz, jint shader, jint count, jlong string, jint length, jlong function_pointer) {
	const GLcharARB *string_address = (const GLcharARB *)(intptr_t)string;
	glShaderSourceARBPROC glShaderSourceARB = (glShaderSourceARBPROC)((intptr_t)function_pointer);
	glShaderSourceARB(shader, count, (const GLcharARB **)&string_address, (const GLint*)&length);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglShaderSourceARB3(JNIEnv *env, jclass clazz, jint shader, jint count, jlong strings, jlong length, jlong function_pointer) {
	const GLchar *strings_address = (const GLchar *)(intptr_t)strings;
	int _str_i;
	GLchar *_str_address;
	GLchar **strings_str = (GLchar **) malloc(count * sizeof(GLchar *));
	const GLint *length_address = (const GLint *)(intptr_t)length;
	glShaderSourceARBPROC glShaderSourceARB = (glShaderSourceARBPROC)((intptr_t)function_pointer);
	_str_i = 0;
	_str_address = (GLchar *)strings_address;
	while ( _str_i < count ) {
		strings_str[_str_i] = _str_address;
		_str_address += length_address[_str_i++];
	}
	glShaderSourceARB(shader, count, (const GLchar **)strings_str, length_address);
	free(strings_str);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglCompileShaderARB(JNIEnv *env, jclass clazz, jint shaderObj, jlong function_pointer) {
	glCompileShaderARBPROC glCompileShaderARB = (glCompileShaderARBPROC)((intptr_t)function_pointer);
	glCompileShaderARB(shaderObj);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglCreateProgramObjectARB(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glCreateProgramObjectARBPROC glCreateProgramObjectARB = (glCreateProgramObjectARBPROC)((intptr_t)function_pointer);
	GLhandleARB __result = glCreateProgramObjectARB();
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglAttachObjectARB(JNIEnv *env, jclass clazz, jint containerObj, jint obj, jlong function_pointer) {
	glAttachObjectARBPROC glAttachObjectARB = (glAttachObjectARBPROC)((intptr_t)function_pointer);
	glAttachObjectARB(containerObj, obj);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglLinkProgramARB(JNIEnv *env, jclass clazz, jint programObj, jlong function_pointer) {
	glLinkProgramARBPROC glLinkProgramARB = (glLinkProgramARBPROC)((intptr_t)function_pointer);
	glLinkProgramARB(programObj);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglUseProgramObjectARB(JNIEnv *env, jclass clazz, jint programObj, jlong function_pointer) {
	glUseProgramObjectARBPROC glUseProgramObjectARB = (glUseProgramObjectARBPROC)((intptr_t)function_pointer);
	glUseProgramObjectARB(programObj);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglValidateProgramARB(JNIEnv *env, jclass clazz, jint programObj, jlong function_pointer) {
	glValidateProgramARBPROC glValidateProgramARB = (glValidateProgramARBPROC)((intptr_t)function_pointer);
	glValidateProgramARB(programObj);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglUniform1fARB(JNIEnv *env, jclass clazz, jint location, jfloat v0, jlong function_pointer) {
	glUniform1fARBPROC glUniform1fARB = (glUniform1fARBPROC)((intptr_t)function_pointer);
	glUniform1fARB(location, v0);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglUniform2fARB(JNIEnv *env, jclass clazz, jint location, jfloat v0, jfloat v1, jlong function_pointer) {
	glUniform2fARBPROC glUniform2fARB = (glUniform2fARBPROC)((intptr_t)function_pointer);
	glUniform2fARB(location, v0, v1);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglUniform3fARB(JNIEnv *env, jclass clazz, jint location, jfloat v0, jfloat v1, jfloat v2, jlong function_pointer) {
	glUniform3fARBPROC glUniform3fARB = (glUniform3fARBPROC)((intptr_t)function_pointer);
	glUniform3fARB(location, v0, v1, v2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglUniform4fARB(JNIEnv *env, jclass clazz, jint location, jfloat v0, jfloat v1, jfloat v2, jfloat v3, jlong function_pointer) {
	glUniform4fARBPROC glUniform4fARB = (glUniform4fARBPROC)((intptr_t)function_pointer);
	glUniform4fARB(location, v0, v1, v2, v3);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglUniform1iARB(JNIEnv *env, jclass clazz, jint location, jint v0, jlong function_pointer) {
	glUniform1iARBPROC glUniform1iARB = (glUniform1iARBPROC)((intptr_t)function_pointer);
	glUniform1iARB(location, v0);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglUniform2iARB(JNIEnv *env, jclass clazz, jint location, jint v0, jint v1, jlong function_pointer) {
	glUniform2iARBPROC glUniform2iARB = (glUniform2iARBPROC)((intptr_t)function_pointer);
	glUniform2iARB(location, v0, v1);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglUniform3iARB(JNIEnv *env, jclass clazz, jint location, jint v0, jint v1, jint v2, jlong function_pointer) {
	glUniform3iARBPROC glUniform3iARB = (glUniform3iARBPROC)((intptr_t)function_pointer);
	glUniform3iARB(location, v0, v1, v2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglUniform4iARB(JNIEnv *env, jclass clazz, jint location, jint v0, jint v1, jint v2, jint v3, jlong function_pointer) {
	glUniform4iARBPROC glUniform4iARB = (glUniform4iARBPROC)((intptr_t)function_pointer);
	glUniform4iARB(location, v0, v1, v2, v3);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglUniform1fvARB(JNIEnv *env, jclass clazz, jint location, jint count, jlong values, jlong function_pointer) {
	const GLfloat *values_address = (const GLfloat *)(intptr_t)values;
	glUniform1fvARBPROC glUniform1fvARB = (glUniform1fvARBPROC)((intptr_t)function_pointer);
	glUniform1fvARB(location, count, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglUniform2fvARB(JNIEnv *env, jclass clazz, jint location, jint count, jlong values, jlong function_pointer) {
	const GLfloat *values_address = (const GLfloat *)(intptr_t)values;
	glUniform2fvARBPROC glUniform2fvARB = (glUniform2fvARBPROC)((intptr_t)function_pointer);
	glUniform2fvARB(location, count, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglUniform3fvARB(JNIEnv *env, jclass clazz, jint location, jint count, jlong values, jlong function_pointer) {
	const GLfloat *values_address = (const GLfloat *)(intptr_t)values;
	glUniform3fvARBPROC glUniform3fvARB = (glUniform3fvARBPROC)((intptr_t)function_pointer);
	glUniform3fvARB(location, count, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglUniform4fvARB(JNIEnv *env, jclass clazz, jint location, jint count, jlong values, jlong function_pointer) {
	const GLfloat *values_address = (const GLfloat *)(intptr_t)values;
	glUniform4fvARBPROC glUniform4fvARB = (glUniform4fvARBPROC)((intptr_t)function_pointer);
	glUniform4fvARB(location, count, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglUniform1ivARB(JNIEnv *env, jclass clazz, jint location, jint count, jlong values, jlong function_pointer) {
	const GLint *values_address = (const GLint *)(intptr_t)values;
	glUniform1ivARBPROC glUniform1ivARB = (glUniform1ivARBPROC)((intptr_t)function_pointer);
	glUniform1ivARB(location, count, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglUniform2ivARB(JNIEnv *env, jclass clazz, jint location, jint count, jlong values, jlong function_pointer) {
	const GLint *values_address = (const GLint *)(intptr_t)values;
	glUniform2ivARBPROC glUniform2ivARB = (glUniform2ivARBPROC)((intptr_t)function_pointer);
	glUniform2ivARB(location, count, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglUniform3ivARB(JNIEnv *env, jclass clazz, jint location, jint count, jlong values, jlong function_pointer) {
	const GLint *values_address = (const GLint *)(intptr_t)values;
	glUniform3ivARBPROC glUniform3ivARB = (glUniform3ivARBPROC)((intptr_t)function_pointer);
	glUniform3ivARB(location, count, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglUniform4ivARB(JNIEnv *env, jclass clazz, jint location, jint count, jlong values, jlong function_pointer) {
	const GLint *values_address = (const GLint *)(intptr_t)values;
	glUniform4ivARBPROC glUniform4ivARB = (glUniform4ivARBPROC)((intptr_t)function_pointer);
	glUniform4ivARB(location, count, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglUniformMatrix2fvARB(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong matrices, jlong function_pointer) {
	const GLfloat *matrices_address = (const GLfloat *)(intptr_t)matrices;
	glUniformMatrix2fvARBPROC glUniformMatrix2fvARB = (glUniformMatrix2fvARBPROC)((intptr_t)function_pointer);
	glUniformMatrix2fvARB(location, count, transpose, matrices_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglUniformMatrix3fvARB(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong matrices, jlong function_pointer) {
	const GLfloat *matrices_address = (const GLfloat *)(intptr_t)matrices;
	glUniformMatrix3fvARBPROC glUniformMatrix3fvARB = (glUniformMatrix3fvARBPROC)((intptr_t)function_pointer);
	glUniformMatrix3fvARB(location, count, transpose, matrices_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglUniformMatrix4fvARB(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong matrices, jlong function_pointer) {
	const GLfloat *matrices_address = (const GLfloat *)(intptr_t)matrices;
	glUniformMatrix4fvARBPROC glUniformMatrix4fvARB = (glUniformMatrix4fvARBPROC)((intptr_t)function_pointer);
	glUniformMatrix4fvARB(location, count, transpose, matrices_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglGetObjectParameterfvARB(JNIEnv *env, jclass clazz, jint obj, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetObjectParameterfvARBPROC glGetObjectParameterfvARB = (glGetObjectParameterfvARBPROC)((intptr_t)function_pointer);
	glGetObjectParameterfvARB(obj, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglGetObjectParameterivARB(JNIEnv *env, jclass clazz, jint obj, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetObjectParameterivARBPROC glGetObjectParameterivARB = (glGetObjectParameterivARBPROC)((intptr_t)function_pointer);
	glGetObjectParameterivARB(obj, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglGetInfoLogARB(JNIEnv *env, jclass clazz, jint obj, jint maxLength, jlong length, jlong infoLog, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLcharARB *infoLog_address = (GLcharARB *)(intptr_t)infoLog;
	glGetInfoLogARBPROC glGetInfoLogARB = (glGetInfoLogARBPROC)((intptr_t)function_pointer);
	glGetInfoLogARB(obj, maxLength, length_address, infoLog_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglGetAttachedObjectsARB(JNIEnv *env, jclass clazz, jint containerObj, jint maxCount, jlong count, jlong obj, jlong function_pointer) {
	GLsizei *count_address = (GLsizei *)(intptr_t)count;
	GLhandleARB *obj_address = (GLhandleARB *)(intptr_t)obj;
	glGetAttachedObjectsARBPROC glGetAttachedObjectsARB = (glGetAttachedObjectsARBPROC)((intptr_t)function_pointer);
	glGetAttachedObjectsARB(containerObj, maxCount, count_address, obj_address);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglGetUniformLocationARB(JNIEnv *env, jclass clazz, jint programObj, jlong name, jlong function_pointer) {
	const GLcharARB *name_address = (const GLcharARB *)(intptr_t)name;
	glGetUniformLocationARBPROC glGetUniformLocationARB = (glGetUniformLocationARBPROC)((intptr_t)function_pointer);
	GLint __result = glGetUniformLocationARB(programObj, name_address);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglGetActiveUniformARB(JNIEnv *env, jclass clazz, jint programObj, jint index, jint maxLength, jlong length, jlong size, jlong type, jlong name, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLint *size_address = (GLint *)(intptr_t)size;
	GLenum *type_address = (GLenum *)(intptr_t)type;
	GLcharARB *name_address = (GLcharARB *)(intptr_t)name;
	glGetActiveUniformARBPROC glGetActiveUniformARB = (glGetActiveUniformARBPROC)((intptr_t)function_pointer);
	glGetActiveUniformARB(programObj, index, maxLength, length_address, size_address, type_address, name_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglGetUniformfvARB(JNIEnv *env, jclass clazz, jint programObj, jint location, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetUniformfvARBPROC glGetUniformfvARB = (glGetUniformfvARBPROC)((intptr_t)function_pointer);
	glGetUniformfvARB(programObj, location, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglGetUniformivARB(JNIEnv *env, jclass clazz, jint programObj, jint location, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetUniformivARBPROC glGetUniformivARB = (glGetUniformivARBPROC)((intptr_t)function_pointer);
	glGetUniformivARB(programObj, location, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBShaderObjects_nglGetShaderSourceARB(JNIEnv *env, jclass clazz, jint obj, jint maxLength, jlong length, jlong source, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLcharARB *source_address = (GLcharARB *)(intptr_t)source;
	glGetShaderSourceARBPROC glGetShaderSourceARB = (glGetShaderSourceARBPROC)((intptr_t)function_pointer);
	glGetShaderSourceARB(obj, maxLength, length_address, source_address);
}


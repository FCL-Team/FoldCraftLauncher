/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glShaderSourcePROC) (GLuint shader, GLsizei count, const GLchar ** string, const GLint* length);
typedef GLint (APIENTRY *glCreateShaderPROC) (GLuint type);
typedef GLboolean (APIENTRY *glIsShaderPROC) (GLuint shader);
typedef void (APIENTRY *glCompileShaderPROC) (GLuint shader);
typedef void (APIENTRY *glDeleteShaderPROC) (GLuint shader);
typedef GLint (APIENTRY *glCreateProgramPROC) ();
typedef GLboolean (APIENTRY *glIsProgramPROC) (GLint program);
typedef void (APIENTRY *glAttachShaderPROC) (GLuint program, GLuint shader);
typedef void (APIENTRY *glDetachShaderPROC) (GLuint program, GLuint shader);
typedef void (APIENTRY *glLinkProgramPROC) (GLuint program);
typedef void (APIENTRY *glUseProgramPROC) (GLuint program);
typedef void (APIENTRY *glValidateProgramPROC) (GLuint program);
typedef void (APIENTRY *glDeleteProgramPROC) (GLuint program);
typedef void (APIENTRY *glUniform1fPROC) (GLint location, GLfloat v0);
typedef void (APIENTRY *glUniform2fPROC) (GLint location, GLfloat v0, GLfloat v1);
typedef void (APIENTRY *glUniform3fPROC) (GLint location, GLfloat v0, GLfloat v1, GLfloat v2);
typedef void (APIENTRY *glUniform4fPROC) (GLint location, GLfloat v0, GLfloat v1, GLfloat v2, GLfloat v3);
typedef void (APIENTRY *glUniform1iPROC) (GLint location, GLint v0);
typedef void (APIENTRY *glUniform2iPROC) (GLint location, GLint v0, GLint v1);
typedef void (APIENTRY *glUniform3iPROC) (GLint location, GLint v0, GLint v1, GLint v2);
typedef void (APIENTRY *glUniform4iPROC) (GLint location, GLint v0, GLint v1, GLint v2, GLint v3);
typedef void (APIENTRY *glUniform1fvPROC) (GLint location, GLsizei count, const GLfloat * values);
typedef void (APIENTRY *glUniform2fvPROC) (GLint location, GLsizei count, const GLfloat * values);
typedef void (APIENTRY *glUniform3fvPROC) (GLint location, GLsizei count, const GLfloat * values);
typedef void (APIENTRY *glUniform4fvPROC) (GLint location, GLsizei count, const GLfloat * values);
typedef void (APIENTRY *glUniform1ivPROC) (GLint location, GLsizei count, const GLint * values);
typedef void (APIENTRY *glUniform2ivPROC) (GLint location, GLsizei count, const GLint * values);
typedef void (APIENTRY *glUniform3ivPROC) (GLint location, GLsizei count, const GLint * values);
typedef void (APIENTRY *glUniform4ivPROC) (GLint location, GLsizei count, const GLint * values);
typedef void (APIENTRY *glUniformMatrix2fvPROC) (GLint location, GLsizei count, GLboolean transpose, const GLfloat * matrices);
typedef void (APIENTRY *glUniformMatrix3fvPROC) (GLint location, GLsizei count, GLboolean transpose, const GLfloat * matrices);
typedef void (APIENTRY *glUniformMatrix4fvPROC) (GLint location, GLsizei count, GLboolean transpose, const GLfloat * matrices);
typedef void (APIENTRY *glGetShaderivPROC) (GLuint shader, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetProgramivPROC) (GLuint program, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetShaderInfoLogPROC) (GLuint shader, GLsizei maxLength, GLsizei * length, GLchar * infoLog);
typedef void (APIENTRY *glGetProgramInfoLogPROC) (GLuint program, GLsizei maxLength, GLsizei * length, GLchar * infoLog);
typedef void (APIENTRY *glGetAttachedShadersPROC) (GLuint program, GLsizei maxCount, GLsizei * count, GLuint * shaders);
typedef GLint (APIENTRY *glGetUniformLocationPROC) (GLuint program, const GLchar * name);
typedef void (APIENTRY *glGetActiveUniformPROC) (GLuint program, GLuint index, GLsizei maxLength, GLsizei * length, GLsizei * size, GLenum * type, GLchar * name);
typedef void (APIENTRY *glGetUniformfvPROC) (GLuint program, GLint location, GLfloat * params);
typedef void (APIENTRY *glGetUniformivPROC) (GLuint program, GLint location, GLint * params);
typedef void (APIENTRY *glGetShaderSourcePROC) (GLuint shader, GLsizei maxLength, GLsizei * length, GLchar * source);
typedef void (APIENTRY *glVertexAttrib1sPROC) (GLuint index, GLshort x);
typedef void (APIENTRY *glVertexAttrib1fPROC) (GLuint index, GLfloat x);
typedef void (APIENTRY *glVertexAttrib1dPROC) (GLuint index, GLdouble x);
typedef void (APIENTRY *glVertexAttrib2sPROC) (GLuint index, GLshort x, GLshort y);
typedef void (APIENTRY *glVertexAttrib2fPROC) (GLuint index, GLfloat x, GLfloat y);
typedef void (APIENTRY *glVertexAttrib2dPROC) (GLuint index, GLdouble x, GLdouble y);
typedef void (APIENTRY *glVertexAttrib3sPROC) (GLuint index, GLshort x, GLshort y, GLshort z);
typedef void (APIENTRY *glVertexAttrib3fPROC) (GLuint index, GLfloat x, GLfloat y, GLfloat z);
typedef void (APIENTRY *glVertexAttrib3dPROC) (GLuint index, GLdouble x, GLdouble y, GLdouble z);
typedef void (APIENTRY *glVertexAttrib4sPROC) (GLuint index, GLshort x, GLshort y, GLshort z, GLshort w);
typedef void (APIENTRY *glVertexAttrib4fPROC) (GLuint index, GLfloat x, GLfloat y, GLfloat z, GLfloat w);
typedef void (APIENTRY *glVertexAttrib4dPROC) (GLuint index, GLdouble x, GLdouble y, GLdouble z, GLdouble w);
typedef void (APIENTRY *glVertexAttrib4NubPROC) (GLuint index, GLubyte x, GLubyte y, GLubyte z, GLubyte w);
typedef void (APIENTRY *glVertexAttribPointerPROC) (GLuint index, GLint size, GLenum type, GLboolean normalized, GLsizei stride, const GLvoid * buffer);
typedef void (APIENTRY *glEnableVertexAttribArrayPROC) (GLuint index);
typedef void (APIENTRY *glDisableVertexAttribArrayPROC) (GLuint index);
typedef void (APIENTRY *glGetVertexAttribfvPROC) (GLuint index, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetVertexAttribdvPROC) (GLuint index, GLenum pname, GLdouble * params);
typedef void (APIENTRY *glGetVertexAttribivPROC) (GLuint index, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetVertexAttribPointervPROC) (GLuint index, GLenum pname, GLvoid ** pointer);
typedef void (APIENTRY *glBindAttribLocationPROC) (GLuint program, GLuint index, const GLchar * name);
typedef void (APIENTRY *glGetActiveAttribPROC) (GLuint program, GLuint index, GLsizei maxLength, GLsizei * length, GLint * size, GLenum * type, GLchar * name);
typedef GLint (APIENTRY *glGetAttribLocationPROC) (GLuint program, const GLchar * name);
typedef void (APIENTRY *glDrawBuffersPROC) (GLsizei size, const GLenum * buffers);
typedef void (APIENTRY *glStencilOpSeparatePROC) (GLenum face, GLenum sfail, GLenum dpfail, GLenum dppass);
typedef void (APIENTRY *glStencilFuncSeparatePROC) (GLenum face, GLenum func, GLint ref, GLuint mask);
typedef void (APIENTRY *glStencilMaskSeparatePROC) (GLenum face, GLuint mask);
typedef void (APIENTRY *glBlendEquationSeparatePROC) (GLenum modeRGB, GLenum modeAlpha);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglShaderSource(JNIEnv *env, jclass clazz, jint shader, jint count, jlong string, jint length, jlong function_pointer) {
	const GLchar *string_address = (const GLchar *)(intptr_t)string;
	glShaderSourcePROC glShaderSource = (glShaderSourcePROC)((intptr_t)function_pointer);
	glShaderSource(shader, count, (const GLchar **)&string_address, (const GLint*)&length);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglShaderSource3(JNIEnv *env, jclass clazz, jint shader, jint count, jlong strings, jlong length, jlong function_pointer) {
	const GLchar *strings_address = (const GLchar *)(intptr_t)strings;
	int _str_i;
	GLchar *_str_address;
	GLchar **strings_str = (GLchar **) malloc(count * sizeof(GLchar *));
	const GLint *length_address = (const GLint *)(intptr_t)length;
	glShaderSourcePROC glShaderSource = (glShaderSourcePROC)((intptr_t)function_pointer);
	_str_i = 0;
	_str_address = (GLchar *)strings_address;
	while ( _str_i < count ) {
		strings_str[_str_i] = _str_address;
		_str_address += length_address[_str_i++];
	}
	glShaderSource(shader, count, (const GLchar **)strings_str, length_address);
	free(strings_str);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL20_nglCreateShader(JNIEnv *env, jclass clazz, jint type, jlong function_pointer) {
	glCreateShaderPROC glCreateShader = (glCreateShaderPROC)((intptr_t)function_pointer);
	GLint __result = glCreateShader(type);
	return __result;
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_GL20_nglIsShader(JNIEnv *env, jclass clazz, jint shader, jlong function_pointer) {
	glIsShaderPROC glIsShader = (glIsShaderPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsShader(shader);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglCompileShader(JNIEnv *env, jclass clazz, jint shader, jlong function_pointer) {
	glCompileShaderPROC glCompileShader = (glCompileShaderPROC)((intptr_t)function_pointer);
	glCompileShader(shader);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglDeleteShader(JNIEnv *env, jclass clazz, jint shader, jlong function_pointer) {
	glDeleteShaderPROC glDeleteShader = (glDeleteShaderPROC)((intptr_t)function_pointer);
	glDeleteShader(shader);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL20_nglCreateProgram(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glCreateProgramPROC glCreateProgram = (glCreateProgramPROC)((intptr_t)function_pointer);
	GLint __result = glCreateProgram();
	return __result;
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_GL20_nglIsProgram(JNIEnv *env, jclass clazz, jint program, jlong function_pointer) {
	glIsProgramPROC glIsProgram = (glIsProgramPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsProgram(program);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglAttachShader(JNIEnv *env, jclass clazz, jint program, jint shader, jlong function_pointer) {
	glAttachShaderPROC glAttachShader = (glAttachShaderPROC)((intptr_t)function_pointer);
	glAttachShader(program, shader);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglDetachShader(JNIEnv *env, jclass clazz, jint program, jint shader, jlong function_pointer) {
	glDetachShaderPROC glDetachShader = (glDetachShaderPROC)((intptr_t)function_pointer);
	glDetachShader(program, shader);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglLinkProgram(JNIEnv *env, jclass clazz, jint program, jlong function_pointer) {
	glLinkProgramPROC glLinkProgram = (glLinkProgramPROC)((intptr_t)function_pointer);
	glLinkProgram(program);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglUseProgram(JNIEnv *env, jclass clazz, jint program, jlong function_pointer) {
	glUseProgramPROC glUseProgram = (glUseProgramPROC)((intptr_t)function_pointer);
	glUseProgram(program);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglValidateProgram(JNIEnv *env, jclass clazz, jint program, jlong function_pointer) {
	glValidateProgramPROC glValidateProgram = (glValidateProgramPROC)((intptr_t)function_pointer);
	glValidateProgram(program);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglDeleteProgram(JNIEnv *env, jclass clazz, jint program, jlong function_pointer) {
	glDeleteProgramPROC glDeleteProgram = (glDeleteProgramPROC)((intptr_t)function_pointer);
	glDeleteProgram(program);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglUniform1f(JNIEnv *env, jclass clazz, jint location, jfloat v0, jlong function_pointer) {
	glUniform1fPROC glUniform1f = (glUniform1fPROC)((intptr_t)function_pointer);
	glUniform1f(location, v0);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglUniform2f(JNIEnv *env, jclass clazz, jint location, jfloat v0, jfloat v1, jlong function_pointer) {
	glUniform2fPROC glUniform2f = (glUniform2fPROC)((intptr_t)function_pointer);
	glUniform2f(location, v0, v1);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglUniform3f(JNIEnv *env, jclass clazz, jint location, jfloat v0, jfloat v1, jfloat v2, jlong function_pointer) {
	glUniform3fPROC glUniform3f = (glUniform3fPROC)((intptr_t)function_pointer);
	glUniform3f(location, v0, v1, v2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglUniform4f(JNIEnv *env, jclass clazz, jint location, jfloat v0, jfloat v1, jfloat v2, jfloat v3, jlong function_pointer) {
	glUniform4fPROC glUniform4f = (glUniform4fPROC)((intptr_t)function_pointer);
	glUniform4f(location, v0, v1, v2, v3);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglUniform1i(JNIEnv *env, jclass clazz, jint location, jint v0, jlong function_pointer) {
	glUniform1iPROC glUniform1i = (glUniform1iPROC)((intptr_t)function_pointer);
	glUniform1i(location, v0);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglUniform2i(JNIEnv *env, jclass clazz, jint location, jint v0, jint v1, jlong function_pointer) {
	glUniform2iPROC glUniform2i = (glUniform2iPROC)((intptr_t)function_pointer);
	glUniform2i(location, v0, v1);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglUniform3i(JNIEnv *env, jclass clazz, jint location, jint v0, jint v1, jint v2, jlong function_pointer) {
	glUniform3iPROC glUniform3i = (glUniform3iPROC)((intptr_t)function_pointer);
	glUniform3i(location, v0, v1, v2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglUniform4i(JNIEnv *env, jclass clazz, jint location, jint v0, jint v1, jint v2, jint v3, jlong function_pointer) {
	glUniform4iPROC glUniform4i = (glUniform4iPROC)((intptr_t)function_pointer);
	glUniform4i(location, v0, v1, v2, v3);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglUniform1fv(JNIEnv *env, jclass clazz, jint location, jint count, jlong values, jlong function_pointer) {
	const GLfloat *values_address = (const GLfloat *)(intptr_t)values;
	glUniform1fvPROC glUniform1fv = (glUniform1fvPROC)((intptr_t)function_pointer);
	glUniform1fv(location, count, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglUniform2fv(JNIEnv *env, jclass clazz, jint location, jint count, jlong values, jlong function_pointer) {
	const GLfloat *values_address = (const GLfloat *)(intptr_t)values;
	glUniform2fvPROC glUniform2fv = (glUniform2fvPROC)((intptr_t)function_pointer);
	glUniform2fv(location, count, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglUniform3fv(JNIEnv *env, jclass clazz, jint location, jint count, jlong values, jlong function_pointer) {
	const GLfloat *values_address = (const GLfloat *)(intptr_t)values;
	glUniform3fvPROC glUniform3fv = (glUniform3fvPROC)((intptr_t)function_pointer);
	glUniform3fv(location, count, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglUniform4fv(JNIEnv *env, jclass clazz, jint location, jint count, jlong values, jlong function_pointer) {
	const GLfloat *values_address = (const GLfloat *)(intptr_t)values;
	glUniform4fvPROC glUniform4fv = (glUniform4fvPROC)((intptr_t)function_pointer);
	glUniform4fv(location, count, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglUniform1iv(JNIEnv *env, jclass clazz, jint location, jint count, jlong values, jlong function_pointer) {
	const GLint *values_address = (const GLint *)(intptr_t)values;
	glUniform1ivPROC glUniform1iv = (glUniform1ivPROC)((intptr_t)function_pointer);
	glUniform1iv(location, count, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglUniform2iv(JNIEnv *env, jclass clazz, jint location, jint count, jlong values, jlong function_pointer) {
	const GLint *values_address = (const GLint *)(intptr_t)values;
	glUniform2ivPROC glUniform2iv = (glUniform2ivPROC)((intptr_t)function_pointer);
	glUniform2iv(location, count, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglUniform3iv(JNIEnv *env, jclass clazz, jint location, jint count, jlong values, jlong function_pointer) {
	const GLint *values_address = (const GLint *)(intptr_t)values;
	glUniform3ivPROC glUniform3iv = (glUniform3ivPROC)((intptr_t)function_pointer);
	glUniform3iv(location, count, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglUniform4iv(JNIEnv *env, jclass clazz, jint location, jint count, jlong values, jlong function_pointer) {
	const GLint *values_address = (const GLint *)(intptr_t)values;
	glUniform4ivPROC glUniform4iv = (glUniform4ivPROC)((intptr_t)function_pointer);
	glUniform4iv(location, count, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglUniformMatrix2fv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong matrices, jlong function_pointer) {
	const GLfloat *matrices_address = (const GLfloat *)(intptr_t)matrices;
	glUniformMatrix2fvPROC glUniformMatrix2fv = (glUniformMatrix2fvPROC)((intptr_t)function_pointer);
	glUniformMatrix2fv(location, count, transpose, matrices_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglUniformMatrix3fv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong matrices, jlong function_pointer) {
	const GLfloat *matrices_address = (const GLfloat *)(intptr_t)matrices;
	glUniformMatrix3fvPROC glUniformMatrix3fv = (glUniformMatrix3fvPROC)((intptr_t)function_pointer);
	glUniformMatrix3fv(location, count, transpose, matrices_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglUniformMatrix4fv(JNIEnv *env, jclass clazz, jint location, jint count, jboolean transpose, jlong matrices, jlong function_pointer) {
	const GLfloat *matrices_address = (const GLfloat *)(intptr_t)matrices;
	glUniformMatrix4fvPROC glUniformMatrix4fv = (glUniformMatrix4fvPROC)((intptr_t)function_pointer);
	glUniformMatrix4fv(location, count, transpose, matrices_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglGetShaderiv(JNIEnv *env, jclass clazz, jint shader, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetShaderivPROC glGetShaderiv = (glGetShaderivPROC)((intptr_t)function_pointer);
	glGetShaderiv(shader, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglGetProgramiv(JNIEnv *env, jclass clazz, jint program, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetProgramivPROC glGetProgramiv = (glGetProgramivPROC)((intptr_t)function_pointer);
	glGetProgramiv(program, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglGetShaderInfoLog(JNIEnv *env, jclass clazz, jint shader, jint maxLength, jlong length, jlong infoLog, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLchar *infoLog_address = (GLchar *)(intptr_t)infoLog;
	glGetShaderInfoLogPROC glGetShaderInfoLog = (glGetShaderInfoLogPROC)((intptr_t)function_pointer);
	glGetShaderInfoLog(shader, maxLength, length_address, infoLog_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglGetProgramInfoLog(JNIEnv *env, jclass clazz, jint program, jint maxLength, jlong length, jlong infoLog, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLchar *infoLog_address = (GLchar *)(intptr_t)infoLog;
	glGetProgramInfoLogPROC glGetProgramInfoLog = (glGetProgramInfoLogPROC)((intptr_t)function_pointer);
	glGetProgramInfoLog(program, maxLength, length_address, infoLog_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglGetAttachedShaders(JNIEnv *env, jclass clazz, jint program, jint maxCount, jlong count, jlong shaders, jlong function_pointer) {
	GLsizei *count_address = (GLsizei *)(intptr_t)count;
	GLuint *shaders_address = (GLuint *)(intptr_t)shaders;
	glGetAttachedShadersPROC glGetAttachedShaders = (glGetAttachedShadersPROC)((intptr_t)function_pointer);
	glGetAttachedShaders(program, maxCount, count_address, shaders_address);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL20_nglGetUniformLocation(JNIEnv *env, jclass clazz, jint program, jlong name, jlong function_pointer) {
	const GLchar *name_address = (const GLchar *)(intptr_t)name;
	glGetUniformLocationPROC glGetUniformLocation = (glGetUniformLocationPROC)((intptr_t)function_pointer);
	GLint __result = glGetUniformLocation(program, name_address);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglGetActiveUniform(JNIEnv *env, jclass clazz, jint program, jint index, jint maxLength, jlong length, jlong size, jlong type, jlong name, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLsizei *size_address = (GLsizei *)(intptr_t)size;
	GLenum *type_address = (GLenum *)(intptr_t)type;
	GLchar *name_address = (GLchar *)(intptr_t)name;
	glGetActiveUniformPROC glGetActiveUniform = (glGetActiveUniformPROC)((intptr_t)function_pointer);
	glGetActiveUniform(program, index, maxLength, length_address, size_address, type_address, name_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglGetUniformfv(JNIEnv *env, jclass clazz, jint program, jint location, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetUniformfvPROC glGetUniformfv = (glGetUniformfvPROC)((intptr_t)function_pointer);
	glGetUniformfv(program, location, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglGetUniformiv(JNIEnv *env, jclass clazz, jint program, jint location, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetUniformivPROC glGetUniformiv = (glGetUniformivPROC)((intptr_t)function_pointer);
	glGetUniformiv(program, location, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglGetShaderSource(JNIEnv *env, jclass clazz, jint shader, jint maxLength, jlong length, jlong source, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLchar *source_address = (GLchar *)(intptr_t)source;
	glGetShaderSourcePROC glGetShaderSource = (glGetShaderSourcePROC)((intptr_t)function_pointer);
	glGetShaderSource(shader, maxLength, length_address, source_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglVertexAttrib1s(JNIEnv *env, jclass clazz, jint index, jshort x, jlong function_pointer) {
	glVertexAttrib1sPROC glVertexAttrib1s = (glVertexAttrib1sPROC)((intptr_t)function_pointer);
	glVertexAttrib1s(index, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglVertexAttrib1f(JNIEnv *env, jclass clazz, jint index, jfloat x, jlong function_pointer) {
	glVertexAttrib1fPROC glVertexAttrib1f = (glVertexAttrib1fPROC)((intptr_t)function_pointer);
	glVertexAttrib1f(index, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglVertexAttrib1d(JNIEnv *env, jclass clazz, jint index, jdouble x, jlong function_pointer) {
	glVertexAttrib1dPROC glVertexAttrib1d = (glVertexAttrib1dPROC)((intptr_t)function_pointer);
	glVertexAttrib1d(index, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglVertexAttrib2s(JNIEnv *env, jclass clazz, jint index, jshort x, jshort y, jlong function_pointer) {
	glVertexAttrib2sPROC glVertexAttrib2s = (glVertexAttrib2sPROC)((intptr_t)function_pointer);
	glVertexAttrib2s(index, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglVertexAttrib2f(JNIEnv *env, jclass clazz, jint index, jfloat x, jfloat y, jlong function_pointer) {
	glVertexAttrib2fPROC glVertexAttrib2f = (glVertexAttrib2fPROC)((intptr_t)function_pointer);
	glVertexAttrib2f(index, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglVertexAttrib2d(JNIEnv *env, jclass clazz, jint index, jdouble x, jdouble y, jlong function_pointer) {
	glVertexAttrib2dPROC glVertexAttrib2d = (glVertexAttrib2dPROC)((intptr_t)function_pointer);
	glVertexAttrib2d(index, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglVertexAttrib3s(JNIEnv *env, jclass clazz, jint index, jshort x, jshort y, jshort z, jlong function_pointer) {
	glVertexAttrib3sPROC glVertexAttrib3s = (glVertexAttrib3sPROC)((intptr_t)function_pointer);
	glVertexAttrib3s(index, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglVertexAttrib3f(JNIEnv *env, jclass clazz, jint index, jfloat x, jfloat y, jfloat z, jlong function_pointer) {
	glVertexAttrib3fPROC glVertexAttrib3f = (glVertexAttrib3fPROC)((intptr_t)function_pointer);
	glVertexAttrib3f(index, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglVertexAttrib3d(JNIEnv *env, jclass clazz, jint index, jdouble x, jdouble y, jdouble z, jlong function_pointer) {
	glVertexAttrib3dPROC glVertexAttrib3d = (glVertexAttrib3dPROC)((intptr_t)function_pointer);
	glVertexAttrib3d(index, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglVertexAttrib4s(JNIEnv *env, jclass clazz, jint index, jshort x, jshort y, jshort z, jshort w, jlong function_pointer) {
	glVertexAttrib4sPROC glVertexAttrib4s = (glVertexAttrib4sPROC)((intptr_t)function_pointer);
	glVertexAttrib4s(index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglVertexAttrib4f(JNIEnv *env, jclass clazz, jint index, jfloat x, jfloat y, jfloat z, jfloat w, jlong function_pointer) {
	glVertexAttrib4fPROC glVertexAttrib4f = (glVertexAttrib4fPROC)((intptr_t)function_pointer);
	glVertexAttrib4f(index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglVertexAttrib4d(JNIEnv *env, jclass clazz, jint index, jdouble x, jdouble y, jdouble z, jdouble w, jlong function_pointer) {
	glVertexAttrib4dPROC glVertexAttrib4d = (glVertexAttrib4dPROC)((intptr_t)function_pointer);
	glVertexAttrib4d(index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglVertexAttrib4Nub(JNIEnv *env, jclass clazz, jint index, jbyte x, jbyte y, jbyte z, jbyte w, jlong function_pointer) {
	glVertexAttrib4NubPROC glVertexAttrib4Nub = (glVertexAttrib4NubPROC)((intptr_t)function_pointer);
	glVertexAttrib4Nub(index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglVertexAttribPointer(JNIEnv *env, jclass clazz, jint index, jint size, jint type, jboolean normalized, jint stride, jlong buffer, jlong function_pointer) {
	const GLvoid *buffer_address = (const GLvoid *)(intptr_t)buffer;
	glVertexAttribPointerPROC glVertexAttribPointer = (glVertexAttribPointerPROC)((intptr_t)function_pointer);
	glVertexAttribPointer(index, size, type, normalized, stride, buffer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglVertexAttribPointerBO(JNIEnv *env, jclass clazz, jint index, jint size, jint type, jboolean normalized, jint stride, jlong buffer_buffer_offset, jlong function_pointer) {
	const GLvoid *buffer_address = (const GLvoid *)(intptr_t)offsetToPointer(buffer_buffer_offset);
	glVertexAttribPointerPROC glVertexAttribPointer = (glVertexAttribPointerPROC)((intptr_t)function_pointer);
	glVertexAttribPointer(index, size, type, normalized, stride, buffer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglEnableVertexAttribArray(JNIEnv *env, jclass clazz, jint index, jlong function_pointer) {
	glEnableVertexAttribArrayPROC glEnableVertexAttribArray = (glEnableVertexAttribArrayPROC)((intptr_t)function_pointer);
	glEnableVertexAttribArray(index);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglDisableVertexAttribArray(JNIEnv *env, jclass clazz, jint index, jlong function_pointer) {
	glDisableVertexAttribArrayPROC glDisableVertexAttribArray = (glDisableVertexAttribArrayPROC)((intptr_t)function_pointer);
	glDisableVertexAttribArray(index);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglGetVertexAttribfv(JNIEnv *env, jclass clazz, jint index, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetVertexAttribfvPROC glGetVertexAttribfv = (glGetVertexAttribfvPROC)((intptr_t)function_pointer);
	glGetVertexAttribfv(index, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglGetVertexAttribdv(JNIEnv *env, jclass clazz, jint index, jint pname, jlong params, jlong function_pointer) {
	GLdouble *params_address = (GLdouble *)(intptr_t)params;
	glGetVertexAttribdvPROC glGetVertexAttribdv = (glGetVertexAttribdvPROC)((intptr_t)function_pointer);
	glGetVertexAttribdv(index, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglGetVertexAttribiv(JNIEnv *env, jclass clazz, jint index, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetVertexAttribivPROC glGetVertexAttribiv = (glGetVertexAttribivPROC)((intptr_t)function_pointer);
	glGetVertexAttribiv(index, pname, params_address);
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_GL20_nglGetVertexAttribPointerv(JNIEnv *env, jclass clazz, jint index, jint pname, jlong result_size, jlong function_pointer) {
	glGetVertexAttribPointervPROC glGetVertexAttribPointerv = (glGetVertexAttribPointervPROC)((intptr_t)function_pointer);
	GLvoid * __result;
	glGetVertexAttribPointerv(index, pname, &__result);
	return safeNewBuffer(env, __result, result_size);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglGetVertexAttribPointerv2(JNIEnv *env, jclass clazz, jint index, jint pname, jlong pointer, jlong function_pointer) {
	GLvoid *pointer_address = (GLvoid *)(intptr_t)pointer;
	glGetVertexAttribPointervPROC glGetVertexAttribPointerv = (glGetVertexAttribPointervPROC)((intptr_t)function_pointer);
	glGetVertexAttribPointerv(index, pname, pointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglBindAttribLocation(JNIEnv *env, jclass clazz, jint program, jint index, jlong name, jlong function_pointer) {
	const GLchar *name_address = (const GLchar *)(intptr_t)name;
	glBindAttribLocationPROC glBindAttribLocation = (glBindAttribLocationPROC)((intptr_t)function_pointer);
	glBindAttribLocation(program, index, name_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglGetActiveAttrib(JNIEnv *env, jclass clazz, jint program, jint index, jint maxLength, jlong length, jlong size, jlong type, jlong name, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLint *size_address = (GLint *)(intptr_t)size;
	GLenum *type_address = (GLenum *)(intptr_t)type;
	GLchar *name_address = (GLchar *)(intptr_t)name;
	glGetActiveAttribPROC glGetActiveAttrib = (glGetActiveAttribPROC)((intptr_t)function_pointer);
	glGetActiveAttrib(program, index, maxLength, length_address, size_address, type_address, name_address);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL20_nglGetAttribLocation(JNIEnv *env, jclass clazz, jint program, jlong name, jlong function_pointer) {
	const GLchar *name_address = (const GLchar *)(intptr_t)name;
	glGetAttribLocationPROC glGetAttribLocation = (glGetAttribLocationPROC)((intptr_t)function_pointer);
	GLint __result = glGetAttribLocation(program, name_address);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglDrawBuffers(JNIEnv *env, jclass clazz, jint size, jlong buffers, jlong function_pointer) {
	const GLenum *buffers_address = (const GLenum *)(intptr_t)buffers;
	glDrawBuffersPROC glDrawBuffers = (glDrawBuffersPROC)((intptr_t)function_pointer);
	glDrawBuffers(size, buffers_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglStencilOpSeparate(JNIEnv *env, jclass clazz, jint face, jint sfail, jint dpfail, jint dppass, jlong function_pointer) {
	glStencilOpSeparatePROC glStencilOpSeparate = (glStencilOpSeparatePROC)((intptr_t)function_pointer);
	glStencilOpSeparate(face, sfail, dpfail, dppass);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglStencilFuncSeparate(JNIEnv *env, jclass clazz, jint face, jint func, jint ref, jint mask, jlong function_pointer) {
	glStencilFuncSeparatePROC glStencilFuncSeparate = (glStencilFuncSeparatePROC)((intptr_t)function_pointer);
	glStencilFuncSeparate(face, func, ref, mask);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglStencilMaskSeparate(JNIEnv *env, jclass clazz, jint face, jint mask, jlong function_pointer) {
	glStencilMaskSeparatePROC glStencilMaskSeparate = (glStencilMaskSeparatePROC)((intptr_t)function_pointer);
	glStencilMaskSeparate(face, mask);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL20_nglBlendEquationSeparate(JNIEnv *env, jclass clazz, jint modeRGB, jint modeAlpha, jlong function_pointer) {
	glBlendEquationSeparatePROC glBlendEquationSeparate = (glBlendEquationSeparatePROC)((intptr_t)function_pointer);
	glBlendEquationSeparate(modeRGB, modeAlpha);
}


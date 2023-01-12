/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glReleaseShaderCompilerPROC) ();
typedef void (APIENTRY *glShaderBinaryPROC) (GLsizei count, const GLuint * shaders, GLenum binaryformat, const GLvoid * binary, GLsizei length);
typedef void (APIENTRY *glGetShaderPrecisionFormatPROC) (GLenum shadertype, GLenum precisiontype, GLint * range, GLint * precision);
typedef void (APIENTRY *glDepthRangefPROC) (GLfloat n, GLfloat f);
typedef void (APIENTRY *glClearDepthfPROC) (GLfloat d);
typedef void (APIENTRY *glGetProgramBinaryPROC) (GLuint program, GLsizei bufSize, GLsizei * length, GLenum * binaryFormat, GLvoid * binary);
typedef void (APIENTRY *glProgramBinaryPROC) (GLuint program, GLenum binaryFormat, const GLvoid * binary, GLsizei length);
typedef void (APIENTRY *glProgramParameteriPROC) (GLuint program, GLenum pname, GLint value);
typedef void (APIENTRY *glUseProgramStagesPROC) (GLuint pipeline, GLbitfield stages, GLuint program);
typedef void (APIENTRY *glActiveShaderProgramPROC) (GLuint pipeline, GLuint program);
typedef GLuint (APIENTRY *glCreateShaderProgramvPROC) (GLenum type, GLsizei count, const GLchar ** string);
typedef void (APIENTRY *glBindProgramPipelinePROC) (GLuint pipeline);
typedef void (APIENTRY *glDeleteProgramPipelinesPROC) (GLsizei n, const GLuint * pipelines);
typedef void (APIENTRY *glGenProgramPipelinesPROC) (GLsizei n, GLuint * pipelines);
typedef GLboolean (APIENTRY *glIsProgramPipelinePROC) (GLuint pipeline);
typedef void (APIENTRY *glGetProgramPipelineivPROC) (GLuint pipeline, GLenum pname, GLint * params);
typedef void (APIENTRY *glProgramUniform1iPROC) (GLuint program, GLint location, GLint v0);
typedef void (APIENTRY *glProgramUniform2iPROC) (GLuint program, GLint location, GLint v0, GLint v1);
typedef void (APIENTRY *glProgramUniform3iPROC) (GLuint program, GLint location, GLint v0, GLint v1, GLint v2);
typedef void (APIENTRY *glProgramUniform4iPROC) (GLuint program, GLint location, GLint v0, GLint v1, GLint v2, GLint v3);
typedef void (APIENTRY *glProgramUniform1fPROC) (GLuint program, GLint location, GLfloat v0);
typedef void (APIENTRY *glProgramUniform2fPROC) (GLuint program, GLint location, GLfloat v0, GLfloat v1);
typedef void (APIENTRY *glProgramUniform3fPROC) (GLuint program, GLint location, GLfloat v0, GLfloat v1, GLfloat v2);
typedef void (APIENTRY *glProgramUniform4fPROC) (GLuint program, GLint location, GLfloat v0, GLfloat v1, GLfloat v2, GLfloat v3);
typedef void (APIENTRY *glProgramUniform1dPROC) (GLuint program, GLint location, GLdouble v0);
typedef void (APIENTRY *glProgramUniform2dPROC) (GLuint program, GLint location, GLdouble v0, GLdouble v1);
typedef void (APIENTRY *glProgramUniform3dPROC) (GLuint program, GLint location, GLdouble v0, GLdouble v1, GLdouble v2);
typedef void (APIENTRY *glProgramUniform4dPROC) (GLuint program, GLint location, GLdouble v0, GLdouble v1, GLdouble v2, GLdouble v3);
typedef void (APIENTRY *glProgramUniform1ivPROC) (GLuint program, GLint location, GLsizei count, const GLint * value);
typedef void (APIENTRY *glProgramUniform2ivPROC) (GLuint program, GLint location, GLsizei count, const GLint * value);
typedef void (APIENTRY *glProgramUniform3ivPROC) (GLuint program, GLint location, GLsizei count, const GLint * value);
typedef void (APIENTRY *glProgramUniform4ivPROC) (GLuint program, GLint location, GLsizei count, const GLint * value);
typedef void (APIENTRY *glProgramUniform1fvPROC) (GLuint program, GLint location, GLsizei count, const GLfloat * value);
typedef void (APIENTRY *glProgramUniform2fvPROC) (GLuint program, GLint location, GLsizei count, const GLfloat * value);
typedef void (APIENTRY *glProgramUniform3fvPROC) (GLuint program, GLint location, GLsizei count, const GLfloat * value);
typedef void (APIENTRY *glProgramUniform4fvPROC) (GLuint program, GLint location, GLsizei count, const GLfloat * value);
typedef void (APIENTRY *glProgramUniform1dvPROC) (GLuint program, GLint location, GLsizei count, const GLdouble * value);
typedef void (APIENTRY *glProgramUniform2dvPROC) (GLuint program, GLint location, GLsizei count, const GLdouble * value);
typedef void (APIENTRY *glProgramUniform3dvPROC) (GLuint program, GLint location, GLsizei count, const GLdouble * value);
typedef void (APIENTRY *glProgramUniform4dvPROC) (GLuint program, GLint location, GLsizei count, const GLdouble * value);
typedef void (APIENTRY *glProgramUniform1uiPROC) (GLuint program, GLint location, GLint v0);
typedef void (APIENTRY *glProgramUniform2uiPROC) (GLuint program, GLint location, GLint v0, GLint v1);
typedef void (APIENTRY *glProgramUniform3uiPROC) (GLuint program, GLint location, GLint v0, GLint v1, GLint v2);
typedef void (APIENTRY *glProgramUniform4uiPROC) (GLuint program, GLint location, GLint v0, GLint v1, GLint v2, GLint v3);
typedef void (APIENTRY *glProgramUniform1uivPROC) (GLuint program, GLint location, GLsizei count, const GLint * value);
typedef void (APIENTRY *glProgramUniform2uivPROC) (GLuint program, GLint location, GLsizei count, const GLint * value);
typedef void (APIENTRY *glProgramUniform3uivPROC) (GLuint program, GLint location, GLsizei count, const GLint * value);
typedef void (APIENTRY *glProgramUniform4uivPROC) (GLuint program, GLint location, GLsizei count, const GLint * value);
typedef void (APIENTRY *glProgramUniformMatrix2fvPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLfloat * value);
typedef void (APIENTRY *glProgramUniformMatrix3fvPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLfloat * value);
typedef void (APIENTRY *glProgramUniformMatrix4fvPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLfloat * value);
typedef void (APIENTRY *glProgramUniformMatrix2dvPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glProgramUniformMatrix3dvPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glProgramUniformMatrix4dvPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glProgramUniformMatrix2x3fvPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLfloat * value);
typedef void (APIENTRY *glProgramUniformMatrix3x2fvPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLfloat * value);
typedef void (APIENTRY *glProgramUniformMatrix2x4fvPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLfloat * value);
typedef void (APIENTRY *glProgramUniformMatrix4x2fvPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLfloat * value);
typedef void (APIENTRY *glProgramUniformMatrix3x4fvPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLfloat * value);
typedef void (APIENTRY *glProgramUniformMatrix4x3fvPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLfloat * value);
typedef void (APIENTRY *glProgramUniformMatrix2x3dvPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glProgramUniformMatrix3x2dvPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glProgramUniformMatrix2x4dvPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glProgramUniformMatrix4x2dvPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glProgramUniformMatrix3x4dvPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glProgramUniformMatrix4x3dvPROC) (GLuint program, GLint location, GLsizei count, GLboolean transpose, const GLdouble * value);
typedef void (APIENTRY *glValidateProgramPipelinePROC) (GLuint pipeline);
typedef void (APIENTRY *glGetProgramPipelineInfoLogPROC) (GLuint pipeline, GLsizei bufSize, GLsizei * length, GLchar * infoLog);
typedef void (APIENTRY *glVertexAttribL1dPROC) (GLuint index, GLdouble x);
typedef void (APIENTRY *glVertexAttribL2dPROC) (GLuint index, GLdouble x, GLdouble y);
typedef void (APIENTRY *glVertexAttribL3dPROC) (GLuint index, GLdouble x, GLdouble y, GLdouble z);
typedef void (APIENTRY *glVertexAttribL4dPROC) (GLuint index, GLdouble x, GLdouble y, GLdouble z, GLdouble w);
typedef void (APIENTRY *glVertexAttribL1dvPROC) (GLuint index, const GLdouble * v);
typedef void (APIENTRY *glVertexAttribL2dvPROC) (GLuint index, const GLdouble * v);
typedef void (APIENTRY *glVertexAttribL3dvPROC) (GLuint index, const GLdouble * v);
typedef void (APIENTRY *glVertexAttribL4dvPROC) (GLuint index, const GLdouble * v);
typedef void (APIENTRY *glVertexAttribLPointerPROC) (GLuint index, GLint size, GLenum type, GLsizei stride, const GLvoid * pointer);
typedef void (APIENTRY *glGetVertexAttribLdvPROC) (GLuint index, GLenum pname, GLdouble * params);
typedef void (APIENTRY *glViewportArrayvPROC) (GLuint first, GLsizei count, const GLfloat * v);
typedef void (APIENTRY *glViewportIndexedfPROC) (GLuint index, GLfloat x, GLfloat y, GLfloat w, GLfloat h);
typedef void (APIENTRY *glViewportIndexedfvPROC) (GLuint index, const GLfloat * v);
typedef void (APIENTRY *glScissorArrayvPROC) (GLuint first, GLsizei count, const GLint * v);
typedef void (APIENTRY *glScissorIndexedPROC) (GLuint index, GLint left, GLint bottom, GLsizei width, GLsizei height);
typedef void (APIENTRY *glScissorIndexedvPROC) (GLuint index, const GLint * v);
typedef void (APIENTRY *glDepthRangeArrayvPROC) (GLuint first, GLsizei count, const GLdouble * v);
typedef void (APIENTRY *glDepthRangeIndexedPROC) (GLuint index, GLdouble n, GLdouble f);
typedef void (APIENTRY *glGetFloati_vPROC) (GLenum target, GLuint index, GLfloat * data);
typedef void (APIENTRY *glGetDoublei_vPROC) (GLenum target, GLuint index, GLdouble * data);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglReleaseShaderCompiler(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glReleaseShaderCompilerPROC glReleaseShaderCompiler = (glReleaseShaderCompilerPROC)((intptr_t)function_pointer);
	glReleaseShaderCompiler();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglShaderBinary(JNIEnv *env, jclass clazz, jint count, jlong shaders, jint binaryformat, jlong binary, jint length, jlong function_pointer) {
	const GLuint *shaders_address = (const GLuint *)(intptr_t)shaders;
	const GLvoid *binary_address = (const GLvoid *)(intptr_t)binary;
	glShaderBinaryPROC glShaderBinary = (glShaderBinaryPROC)((intptr_t)function_pointer);
	glShaderBinary(count, shaders_address, binaryformat, binary_address, length);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglGetShaderPrecisionFormat(JNIEnv *env, jclass clazz, jint shadertype, jint precisiontype, jlong range, jlong precision, jlong function_pointer) {
	GLint *range_address = (GLint *)(intptr_t)range;
	GLint *precision_address = (GLint *)(intptr_t)precision;
	glGetShaderPrecisionFormatPROC glGetShaderPrecisionFormat = (glGetShaderPrecisionFormatPROC)((intptr_t)function_pointer);
	glGetShaderPrecisionFormat(shadertype, precisiontype, range_address, precision_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglDepthRangef(JNIEnv *env, jclass clazz, jfloat n, jfloat f, jlong function_pointer) {
	glDepthRangefPROC glDepthRangef = (glDepthRangefPROC)((intptr_t)function_pointer);
	glDepthRangef(n, f);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglClearDepthf(JNIEnv *env, jclass clazz, jfloat d, jlong function_pointer) {
	glClearDepthfPROC glClearDepthf = (glClearDepthfPROC)((intptr_t)function_pointer);
	glClearDepthf(d);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglGetProgramBinary(JNIEnv *env, jclass clazz, jint program, jint bufSize, jlong length, jlong binaryFormat, jlong binary, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLenum *binaryFormat_address = (GLenum *)(intptr_t)binaryFormat;
	GLvoid *binary_address = (GLvoid *)(intptr_t)binary;
	glGetProgramBinaryPROC glGetProgramBinary = (glGetProgramBinaryPROC)((intptr_t)function_pointer);
	glGetProgramBinary(program, bufSize, length_address, binaryFormat_address, binary_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramBinary(JNIEnv *env, jclass clazz, jint program, jint binaryFormat, jlong binary, jint length, jlong function_pointer) {
	const GLvoid *binary_address = (const GLvoid *)(intptr_t)binary;
	glProgramBinaryPROC glProgramBinary = (glProgramBinaryPROC)((intptr_t)function_pointer);
	glProgramBinary(program, binaryFormat, binary_address, length);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramParameteri(JNIEnv *env, jclass clazz, jint program, jint pname, jint value, jlong function_pointer) {
	glProgramParameteriPROC glProgramParameteri = (glProgramParameteriPROC)((intptr_t)function_pointer);
	glProgramParameteri(program, pname, value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglUseProgramStages(JNIEnv *env, jclass clazz, jint pipeline, jint stages, jint program, jlong function_pointer) {
	glUseProgramStagesPROC glUseProgramStages = (glUseProgramStagesPROC)((intptr_t)function_pointer);
	glUseProgramStages(pipeline, stages, program);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglActiveShaderProgram(JNIEnv *env, jclass clazz, jint pipeline, jint program, jlong function_pointer) {
	glActiveShaderProgramPROC glActiveShaderProgram = (glActiveShaderProgramPROC)((intptr_t)function_pointer);
	glActiveShaderProgram(pipeline, program);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL41_nglCreateShaderProgramv(JNIEnv *env, jclass clazz, jint type, jint count, jlong string, jlong function_pointer) {
	const GLchar *string_address = (const GLchar *)(intptr_t)string;
	glCreateShaderProgramvPROC glCreateShaderProgramv = (glCreateShaderProgramvPROC)((intptr_t)function_pointer);
	GLuint __result = glCreateShaderProgramv(type, count, (const GLchar **)&string_address);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL41_nglCreateShaderProgramv2(JNIEnv *env, jclass clazz, jint type, jint count, jlong strings, jlong function_pointer) {
	const GLchar *strings_address = (const GLchar *)(intptr_t)strings;
	int _str_i;
	GLchar *_str_address;
	GLchar **strings_str = (GLchar **) malloc(count * sizeof(GLchar *));
	glCreateShaderProgramvPROC glCreateShaderProgramv = (glCreateShaderProgramvPROC)((intptr_t)function_pointer);
	GLuint __result;
	_str_i = 0;
	_str_address = (GLchar *)strings_address;
	while ( _str_i < count ) {
		strings_str[_str_i++] = _str_address;
		_str_address += strlen((const char *)_str_address) + 1;
	}
	__result = glCreateShaderProgramv(type, count, (const GLchar **)&strings_str);
	free(strings_str);
	return __result;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL41_nglCreateShaderProgramv3(JNIEnv *env, jclass clazz, jint type, jint count, jobjectArray strings, jlong function_pointer) {
	int _ptr_i;
	jobject _ptr_object;
	GLchar **strings_ptr = (GLchar **) malloc(count * sizeof(GLchar *));
	glCreateShaderProgramvPROC glCreateShaderProgramv = (glCreateShaderProgramvPROC)((intptr_t)function_pointer);
	GLuint __result;
	_ptr_i = 0;
	while ( _ptr_i < count ) {
		_ptr_object = (*env)->GetObjectArrayElement(env, strings, _ptr_i);
		strings_ptr[_ptr_i++] = (GLchar *)(*env)->GetDirectBufferAddress(env, _ptr_object);
	}
	__result = glCreateShaderProgramv(type, count, (const GLchar **)strings_ptr);
	free(strings_ptr);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglBindProgramPipeline(JNIEnv *env, jclass clazz, jint pipeline, jlong function_pointer) {
	glBindProgramPipelinePROC glBindProgramPipeline = (glBindProgramPipelinePROC)((intptr_t)function_pointer);
	glBindProgramPipeline(pipeline);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglDeleteProgramPipelines(JNIEnv *env, jclass clazz, jint n, jlong pipelines, jlong function_pointer) {
	const GLuint *pipelines_address = (const GLuint *)(intptr_t)pipelines;
	glDeleteProgramPipelinesPROC glDeleteProgramPipelines = (glDeleteProgramPipelinesPROC)((intptr_t)function_pointer);
	glDeleteProgramPipelines(n, pipelines_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglGenProgramPipelines(JNIEnv *env, jclass clazz, jint n, jlong pipelines, jlong function_pointer) {
	GLuint *pipelines_address = (GLuint *)(intptr_t)pipelines;
	glGenProgramPipelinesPROC glGenProgramPipelines = (glGenProgramPipelinesPROC)((intptr_t)function_pointer);
	glGenProgramPipelines(n, pipelines_address);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_GL41_nglIsProgramPipeline(JNIEnv *env, jclass clazz, jint pipeline, jlong function_pointer) {
	glIsProgramPipelinePROC glIsProgramPipeline = (glIsProgramPipelinePROC)((intptr_t)function_pointer);
	GLboolean __result = glIsProgramPipeline(pipeline);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglGetProgramPipelineiv(JNIEnv *env, jclass clazz, jint pipeline, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetProgramPipelineivPROC glGetProgramPipelineiv = (glGetProgramPipelineivPROC)((intptr_t)function_pointer);
	glGetProgramPipelineiv(pipeline, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform1i(JNIEnv *env, jclass clazz, jint program, jint location, jint v0, jlong function_pointer) {
	glProgramUniform1iPROC glProgramUniform1i = (glProgramUniform1iPROC)((intptr_t)function_pointer);
	glProgramUniform1i(program, location, v0);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform2i(JNIEnv *env, jclass clazz, jint program, jint location, jint v0, jint v1, jlong function_pointer) {
	glProgramUniform2iPROC glProgramUniform2i = (glProgramUniform2iPROC)((intptr_t)function_pointer);
	glProgramUniform2i(program, location, v0, v1);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform3i(JNIEnv *env, jclass clazz, jint program, jint location, jint v0, jint v1, jint v2, jlong function_pointer) {
	glProgramUniform3iPROC glProgramUniform3i = (glProgramUniform3iPROC)((intptr_t)function_pointer);
	glProgramUniform3i(program, location, v0, v1, v2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform4i(JNIEnv *env, jclass clazz, jint program, jint location, jint v0, jint v1, jint v2, jint v3, jlong function_pointer) {
	glProgramUniform4iPROC glProgramUniform4i = (glProgramUniform4iPROC)((intptr_t)function_pointer);
	glProgramUniform4i(program, location, v0, v1, v2, v3);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform1f(JNIEnv *env, jclass clazz, jint program, jint location, jfloat v0, jlong function_pointer) {
	glProgramUniform1fPROC glProgramUniform1f = (glProgramUniform1fPROC)((intptr_t)function_pointer);
	glProgramUniform1f(program, location, v0);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform2f(JNIEnv *env, jclass clazz, jint program, jint location, jfloat v0, jfloat v1, jlong function_pointer) {
	glProgramUniform2fPROC glProgramUniform2f = (glProgramUniform2fPROC)((intptr_t)function_pointer);
	glProgramUniform2f(program, location, v0, v1);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform3f(JNIEnv *env, jclass clazz, jint program, jint location, jfloat v0, jfloat v1, jfloat v2, jlong function_pointer) {
	glProgramUniform3fPROC glProgramUniform3f = (glProgramUniform3fPROC)((intptr_t)function_pointer);
	glProgramUniform3f(program, location, v0, v1, v2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform4f(JNIEnv *env, jclass clazz, jint program, jint location, jfloat v0, jfloat v1, jfloat v2, jfloat v3, jlong function_pointer) {
	glProgramUniform4fPROC glProgramUniform4f = (glProgramUniform4fPROC)((intptr_t)function_pointer);
	glProgramUniform4f(program, location, v0, v1, v2, v3);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform1d(JNIEnv *env, jclass clazz, jint program, jint location, jdouble v0, jlong function_pointer) {
	glProgramUniform1dPROC glProgramUniform1d = (glProgramUniform1dPROC)((intptr_t)function_pointer);
	glProgramUniform1d(program, location, v0);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform2d(JNIEnv *env, jclass clazz, jint program, jint location, jdouble v0, jdouble v1, jlong function_pointer) {
	glProgramUniform2dPROC glProgramUniform2d = (glProgramUniform2dPROC)((intptr_t)function_pointer);
	glProgramUniform2d(program, location, v0, v1);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform3d(JNIEnv *env, jclass clazz, jint program, jint location, jdouble v0, jdouble v1, jdouble v2, jlong function_pointer) {
	glProgramUniform3dPROC glProgramUniform3d = (glProgramUniform3dPROC)((intptr_t)function_pointer);
	glProgramUniform3d(program, location, v0, v1, v2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform4d(JNIEnv *env, jclass clazz, jint program, jint location, jdouble v0, jdouble v1, jdouble v2, jdouble v3, jlong function_pointer) {
	glProgramUniform4dPROC glProgramUniform4d = (glProgramUniform4dPROC)((intptr_t)function_pointer);
	glProgramUniform4d(program, location, v0, v1, v2, v3);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform1iv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLint *value_address = (const GLint *)(intptr_t)value;
	glProgramUniform1ivPROC glProgramUniform1iv = (glProgramUniform1ivPROC)((intptr_t)function_pointer);
	glProgramUniform1iv(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform2iv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLint *value_address = (const GLint *)(intptr_t)value;
	glProgramUniform2ivPROC glProgramUniform2iv = (glProgramUniform2ivPROC)((intptr_t)function_pointer);
	glProgramUniform2iv(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform3iv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLint *value_address = (const GLint *)(intptr_t)value;
	glProgramUniform3ivPROC glProgramUniform3iv = (glProgramUniform3ivPROC)((intptr_t)function_pointer);
	glProgramUniform3iv(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform4iv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLint *value_address = (const GLint *)(intptr_t)value;
	glProgramUniform4ivPROC glProgramUniform4iv = (glProgramUniform4ivPROC)((intptr_t)function_pointer);
	glProgramUniform4iv(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform1fv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLfloat *value_address = (const GLfloat *)(intptr_t)value;
	glProgramUniform1fvPROC glProgramUniform1fv = (glProgramUniform1fvPROC)((intptr_t)function_pointer);
	glProgramUniform1fv(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform2fv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLfloat *value_address = (const GLfloat *)(intptr_t)value;
	glProgramUniform2fvPROC glProgramUniform2fv = (glProgramUniform2fvPROC)((intptr_t)function_pointer);
	glProgramUniform2fv(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform3fv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLfloat *value_address = (const GLfloat *)(intptr_t)value;
	glProgramUniform3fvPROC glProgramUniform3fv = (glProgramUniform3fvPROC)((intptr_t)function_pointer);
	glProgramUniform3fv(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform4fv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLfloat *value_address = (const GLfloat *)(intptr_t)value;
	glProgramUniform4fvPROC glProgramUniform4fv = (glProgramUniform4fvPROC)((intptr_t)function_pointer);
	glProgramUniform4fv(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform1dv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniform1dvPROC glProgramUniform1dv = (glProgramUniform1dvPROC)((intptr_t)function_pointer);
	glProgramUniform1dv(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform2dv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniform2dvPROC glProgramUniform2dv = (glProgramUniform2dvPROC)((intptr_t)function_pointer);
	glProgramUniform2dv(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform3dv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniform3dvPROC glProgramUniform3dv = (glProgramUniform3dvPROC)((intptr_t)function_pointer);
	glProgramUniform3dv(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform4dv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniform4dvPROC glProgramUniform4dv = (glProgramUniform4dvPROC)((intptr_t)function_pointer);
	glProgramUniform4dv(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform1ui(JNIEnv *env, jclass clazz, jint program, jint location, jint v0, jlong function_pointer) {
	glProgramUniform1uiPROC glProgramUniform1ui = (glProgramUniform1uiPROC)((intptr_t)function_pointer);
	glProgramUniform1ui(program, location, v0);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform2ui(JNIEnv *env, jclass clazz, jint program, jint location, jint v0, jint v1, jlong function_pointer) {
	glProgramUniform2uiPROC glProgramUniform2ui = (glProgramUniform2uiPROC)((intptr_t)function_pointer);
	glProgramUniform2ui(program, location, v0, v1);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform3ui(JNIEnv *env, jclass clazz, jint program, jint location, jint v0, jint v1, jint v2, jlong function_pointer) {
	glProgramUniform3uiPROC glProgramUniform3ui = (glProgramUniform3uiPROC)((intptr_t)function_pointer);
	glProgramUniform3ui(program, location, v0, v1, v2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform4ui(JNIEnv *env, jclass clazz, jint program, jint location, jint v0, jint v1, jint v2, jint v3, jlong function_pointer) {
	glProgramUniform4uiPROC glProgramUniform4ui = (glProgramUniform4uiPROC)((intptr_t)function_pointer);
	glProgramUniform4ui(program, location, v0, v1, v2, v3);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform1uiv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLint *value_address = (const GLint *)(intptr_t)value;
	glProgramUniform1uivPROC glProgramUniform1uiv = (glProgramUniform1uivPROC)((intptr_t)function_pointer);
	glProgramUniform1uiv(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform2uiv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLint *value_address = (const GLint *)(intptr_t)value;
	glProgramUniform2uivPROC glProgramUniform2uiv = (glProgramUniform2uivPROC)((intptr_t)function_pointer);
	glProgramUniform2uiv(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform3uiv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLint *value_address = (const GLint *)(intptr_t)value;
	glProgramUniform3uivPROC glProgramUniform3uiv = (glProgramUniform3uivPROC)((intptr_t)function_pointer);
	glProgramUniform3uiv(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniform4uiv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jlong value, jlong function_pointer) {
	const GLint *value_address = (const GLint *)(intptr_t)value;
	glProgramUniform4uivPROC glProgramUniform4uiv = (glProgramUniform4uivPROC)((intptr_t)function_pointer);
	glProgramUniform4uiv(program, location, count, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniformMatrix2fv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLfloat *value_address = (const GLfloat *)(intptr_t)value;
	glProgramUniformMatrix2fvPROC glProgramUniformMatrix2fv = (glProgramUniformMatrix2fvPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix2fv(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniformMatrix3fv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLfloat *value_address = (const GLfloat *)(intptr_t)value;
	glProgramUniformMatrix3fvPROC glProgramUniformMatrix3fv = (glProgramUniformMatrix3fvPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix3fv(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniformMatrix4fv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLfloat *value_address = (const GLfloat *)(intptr_t)value;
	glProgramUniformMatrix4fvPROC glProgramUniformMatrix4fv = (glProgramUniformMatrix4fvPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix4fv(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniformMatrix2dv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniformMatrix2dvPROC glProgramUniformMatrix2dv = (glProgramUniformMatrix2dvPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix2dv(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniformMatrix3dv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniformMatrix3dvPROC glProgramUniformMatrix3dv = (glProgramUniformMatrix3dvPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix3dv(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniformMatrix4dv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniformMatrix4dvPROC glProgramUniformMatrix4dv = (glProgramUniformMatrix4dvPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix4dv(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniformMatrix2x3fv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLfloat *value_address = (const GLfloat *)(intptr_t)value;
	glProgramUniformMatrix2x3fvPROC glProgramUniformMatrix2x3fv = (glProgramUniformMatrix2x3fvPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix2x3fv(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniformMatrix3x2fv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLfloat *value_address = (const GLfloat *)(intptr_t)value;
	glProgramUniformMatrix3x2fvPROC glProgramUniformMatrix3x2fv = (glProgramUniformMatrix3x2fvPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix3x2fv(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniformMatrix2x4fv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLfloat *value_address = (const GLfloat *)(intptr_t)value;
	glProgramUniformMatrix2x4fvPROC glProgramUniformMatrix2x4fv = (glProgramUniformMatrix2x4fvPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix2x4fv(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniformMatrix4x2fv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLfloat *value_address = (const GLfloat *)(intptr_t)value;
	glProgramUniformMatrix4x2fvPROC glProgramUniformMatrix4x2fv = (glProgramUniformMatrix4x2fvPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix4x2fv(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniformMatrix3x4fv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLfloat *value_address = (const GLfloat *)(intptr_t)value;
	glProgramUniformMatrix3x4fvPROC glProgramUniformMatrix3x4fv = (glProgramUniformMatrix3x4fvPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix3x4fv(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniformMatrix4x3fv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLfloat *value_address = (const GLfloat *)(intptr_t)value;
	glProgramUniformMatrix4x3fvPROC glProgramUniformMatrix4x3fv = (glProgramUniformMatrix4x3fvPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix4x3fv(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniformMatrix2x3dv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniformMatrix2x3dvPROC glProgramUniformMatrix2x3dv = (glProgramUniformMatrix2x3dvPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix2x3dv(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniformMatrix3x2dv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniformMatrix3x2dvPROC glProgramUniformMatrix3x2dv = (glProgramUniformMatrix3x2dvPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix3x2dv(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniformMatrix2x4dv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniformMatrix2x4dvPROC glProgramUniformMatrix2x4dv = (glProgramUniformMatrix2x4dvPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix2x4dv(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniformMatrix4x2dv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniformMatrix4x2dvPROC glProgramUniformMatrix4x2dv = (glProgramUniformMatrix4x2dvPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix4x2dv(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniformMatrix3x4dv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniformMatrix3x4dvPROC glProgramUniformMatrix3x4dv = (glProgramUniformMatrix3x4dvPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix3x4dv(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglProgramUniformMatrix4x3dv(JNIEnv *env, jclass clazz, jint program, jint location, jint count, jboolean transpose, jlong value, jlong function_pointer) {
	const GLdouble *value_address = (const GLdouble *)(intptr_t)value;
	glProgramUniformMatrix4x3dvPROC glProgramUniformMatrix4x3dv = (glProgramUniformMatrix4x3dvPROC)((intptr_t)function_pointer);
	glProgramUniformMatrix4x3dv(program, location, count, transpose, value_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglValidateProgramPipeline(JNIEnv *env, jclass clazz, jint pipeline, jlong function_pointer) {
	glValidateProgramPipelinePROC glValidateProgramPipeline = (glValidateProgramPipelinePROC)((intptr_t)function_pointer);
	glValidateProgramPipeline(pipeline);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglGetProgramPipelineInfoLog(JNIEnv *env, jclass clazz, jint pipeline, jint bufSize, jlong length, jlong infoLog, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLchar *infoLog_address = (GLchar *)(intptr_t)infoLog;
	glGetProgramPipelineInfoLogPROC glGetProgramPipelineInfoLog = (glGetProgramPipelineInfoLogPROC)((intptr_t)function_pointer);
	glGetProgramPipelineInfoLog(pipeline, bufSize, length_address, infoLog_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglVertexAttribL1d(JNIEnv *env, jclass clazz, jint index, jdouble x, jlong function_pointer) {
	glVertexAttribL1dPROC glVertexAttribL1d = (glVertexAttribL1dPROC)((intptr_t)function_pointer);
	glVertexAttribL1d(index, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglVertexAttribL2d(JNIEnv *env, jclass clazz, jint index, jdouble x, jdouble y, jlong function_pointer) {
	glVertexAttribL2dPROC glVertexAttribL2d = (glVertexAttribL2dPROC)((intptr_t)function_pointer);
	glVertexAttribL2d(index, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglVertexAttribL3d(JNIEnv *env, jclass clazz, jint index, jdouble x, jdouble y, jdouble z, jlong function_pointer) {
	glVertexAttribL3dPROC glVertexAttribL3d = (glVertexAttribL3dPROC)((intptr_t)function_pointer);
	glVertexAttribL3d(index, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglVertexAttribL4d(JNIEnv *env, jclass clazz, jint index, jdouble x, jdouble y, jdouble z, jdouble w, jlong function_pointer) {
	glVertexAttribL4dPROC glVertexAttribL4d = (glVertexAttribL4dPROC)((intptr_t)function_pointer);
	glVertexAttribL4d(index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglVertexAttribL1dv(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLdouble *v_address = (const GLdouble *)(intptr_t)v;
	glVertexAttribL1dvPROC glVertexAttribL1dv = (glVertexAttribL1dvPROC)((intptr_t)function_pointer);
	glVertexAttribL1dv(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglVertexAttribL2dv(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLdouble *v_address = (const GLdouble *)(intptr_t)v;
	glVertexAttribL2dvPROC glVertexAttribL2dv = (glVertexAttribL2dvPROC)((intptr_t)function_pointer);
	glVertexAttribL2dv(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglVertexAttribL3dv(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLdouble *v_address = (const GLdouble *)(intptr_t)v;
	glVertexAttribL3dvPROC glVertexAttribL3dv = (glVertexAttribL3dvPROC)((intptr_t)function_pointer);
	glVertexAttribL3dv(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglVertexAttribL4dv(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLdouble *v_address = (const GLdouble *)(intptr_t)v;
	glVertexAttribL4dvPROC glVertexAttribL4dv = (glVertexAttribL4dvPROC)((intptr_t)function_pointer);
	glVertexAttribL4dv(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglVertexAttribLPointer(JNIEnv *env, jclass clazz, jint index, jint size, jint type, jint stride, jlong pointer, jlong function_pointer) {
	const GLvoid *pointer_address = (const GLvoid *)(intptr_t)pointer;
	glVertexAttribLPointerPROC glVertexAttribLPointer = (glVertexAttribLPointerPROC)((intptr_t)function_pointer);
	glVertexAttribLPointer(index, size, type, stride, pointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglVertexAttribLPointerBO(JNIEnv *env, jclass clazz, jint index, jint size, jint type, jint stride, jlong pointer_buffer_offset, jlong function_pointer) {
	const GLvoid *pointer_address = (const GLvoid *)(intptr_t)offsetToPointer(pointer_buffer_offset);
	glVertexAttribLPointerPROC glVertexAttribLPointer = (glVertexAttribLPointerPROC)((intptr_t)function_pointer);
	glVertexAttribLPointer(index, size, type, stride, pointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglGetVertexAttribLdv(JNIEnv *env, jclass clazz, jint index, jint pname, jlong params, jlong function_pointer) {
	GLdouble *params_address = (GLdouble *)(intptr_t)params;
	glGetVertexAttribLdvPROC glGetVertexAttribLdv = (glGetVertexAttribLdvPROC)((intptr_t)function_pointer);
	glGetVertexAttribLdv(index, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglViewportArrayv(JNIEnv *env, jclass clazz, jint first, jint count, jlong v, jlong function_pointer) {
	const GLfloat *v_address = (const GLfloat *)(intptr_t)v;
	glViewportArrayvPROC glViewportArrayv = (glViewportArrayvPROC)((intptr_t)function_pointer);
	glViewportArrayv(first, count, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglViewportIndexedf(JNIEnv *env, jclass clazz, jint index, jfloat x, jfloat y, jfloat w, jfloat h, jlong function_pointer) {
	glViewportIndexedfPROC glViewportIndexedf = (glViewportIndexedfPROC)((intptr_t)function_pointer);
	glViewportIndexedf(index, x, y, w, h);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglViewportIndexedfv(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLfloat *v_address = (const GLfloat *)(intptr_t)v;
	glViewportIndexedfvPROC glViewportIndexedfv = (glViewportIndexedfvPROC)((intptr_t)function_pointer);
	glViewportIndexedfv(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglScissorArrayv(JNIEnv *env, jclass clazz, jint first, jint count, jlong v, jlong function_pointer) {
	const GLint *v_address = (const GLint *)(intptr_t)v;
	glScissorArrayvPROC glScissorArrayv = (glScissorArrayvPROC)((intptr_t)function_pointer);
	glScissorArrayv(first, count, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglScissorIndexed(JNIEnv *env, jclass clazz, jint index, jint left, jint bottom, jint width, jint height, jlong function_pointer) {
	glScissorIndexedPROC glScissorIndexed = (glScissorIndexedPROC)((intptr_t)function_pointer);
	glScissorIndexed(index, left, bottom, width, height);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglScissorIndexedv(JNIEnv *env, jclass clazz, jint index, jlong v, jlong function_pointer) {
	const GLint *v_address = (const GLint *)(intptr_t)v;
	glScissorIndexedvPROC glScissorIndexedv = (glScissorIndexedvPROC)((intptr_t)function_pointer);
	glScissorIndexedv(index, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglDepthRangeArrayv(JNIEnv *env, jclass clazz, jint first, jint count, jlong v, jlong function_pointer) {
	const GLdouble *v_address = (const GLdouble *)(intptr_t)v;
	glDepthRangeArrayvPROC glDepthRangeArrayv = (glDepthRangeArrayvPROC)((intptr_t)function_pointer);
	glDepthRangeArrayv(first, count, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglDepthRangeIndexed(JNIEnv *env, jclass clazz, jint index, jdouble n, jdouble f, jlong function_pointer) {
	glDepthRangeIndexedPROC glDepthRangeIndexed = (glDepthRangeIndexedPROC)((intptr_t)function_pointer);
	glDepthRangeIndexed(index, n, f);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglGetFloati_1v(JNIEnv *env, jclass clazz, jint target, jint index, jlong data, jlong function_pointer) {
	GLfloat *data_address = (GLfloat *)(intptr_t)data;
	glGetFloati_vPROC glGetFloati_v = (glGetFloati_vPROC)((intptr_t)function_pointer);
	glGetFloati_v(target, index, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL41_nglGetDoublei_1v(JNIEnv *env, jclass clazz, jint target, jint index, jlong data, jlong function_pointer) {
	GLdouble *data_address = (GLdouble *)(intptr_t)data;
	glGetDoublei_vPROC glGetDoublei_v = (glGetDoublei_vPROC)((intptr_t)function_pointer);
	glGetDoublei_v(target, index, data_address);
}


/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glVertexAttribArrayObjectATIPROC) (GLuint index, GLint size, GLenum type, GLboolean normalized, GLsizei stride, GLuint buffer, GLuint offset);
typedef void (APIENTRY *glGetVertexAttribArrayObjectfvATIPROC) (GLuint index, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetVertexAttribArrayObjectivATIPROC) (GLuint index, GLenum pname, GLint * params);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexAttribArrayObject_nglVertexAttribArrayObjectATI(JNIEnv *env, jclass clazz, jint index, jint size, jint type, jboolean normalized, jint stride, jint buffer, jint offset, jlong function_pointer) {
	glVertexAttribArrayObjectATIPROC glVertexAttribArrayObjectATI = (glVertexAttribArrayObjectATIPROC)((intptr_t)function_pointer);
	glVertexAttribArrayObjectATI(index, size, type, normalized, stride, buffer, offset);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexAttribArrayObject_nglGetVertexAttribArrayObjectfvATI(JNIEnv *env, jclass clazz, jint index, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetVertexAttribArrayObjectfvATIPROC glGetVertexAttribArrayObjectfvATI = (glGetVertexAttribArrayObjectfvATIPROC)((intptr_t)function_pointer);
	glGetVertexAttribArrayObjectfvATI(index, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATIVertexAttribArrayObject_nglGetVertexAttribArrayObjectivATI(JNIEnv *env, jclass clazz, jint index, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetVertexAttribArrayObjectivATIPROC glGetVertexAttribArrayObjectivATI = (glGetVertexAttribArrayObjectivATIPROC)((intptr_t)function_pointer);
	glGetVertexAttribArrayObjectivATI(index, pname, params_address);
}


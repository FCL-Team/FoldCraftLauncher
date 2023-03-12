/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glVertexArrayVertexAttribLOffsetEXTPROC) (GLuint vaobj, GLuint buffer, GLuint index, GLint size, GLenum type, GLsizei stride, GLintptr offset);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBVertexAttrib64bit_nglVertexArrayVertexAttribLOffsetEXT(JNIEnv *env, jclass clazz, jint vaobj, jint buffer, jint index, jint size, jint type, jint stride, jlong offset, jlong function_pointer) {
	glVertexArrayVertexAttribLOffsetEXTPROC glVertexArrayVertexAttribLOffsetEXT = (glVertexArrayVertexAttribLOffsetEXTPROC)((intptr_t)function_pointer);
	glVertexArrayVertexAttribLOffsetEXT(vaobj, buffer, index, size, type, stride, offset);
}


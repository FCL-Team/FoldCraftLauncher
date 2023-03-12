/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glSecondaryColor3bEXTPROC) (GLbyte red, GLbyte green, GLbyte blue);
typedef void (APIENTRY *glSecondaryColor3fEXTPROC) (GLfloat red, GLfloat green, GLfloat blue);
typedef void (APIENTRY *glSecondaryColor3dEXTPROC) (GLdouble red, GLdouble green, GLdouble blue);
typedef void (APIENTRY *glSecondaryColor3ubEXTPROC) (GLubyte red, GLubyte green, GLubyte blue);
typedef void (APIENTRY *glSecondaryColorPointerEXTPROC) (GLint size, GLenum type, GLsizei stride, const GLvoid * pPointer);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTSecondaryColor_nglSecondaryColor3bEXT(JNIEnv *env, jclass clazz, jbyte red, jbyte green, jbyte blue, jlong function_pointer) {
	glSecondaryColor3bEXTPROC glSecondaryColor3bEXT = (glSecondaryColor3bEXTPROC)((intptr_t)function_pointer);
	glSecondaryColor3bEXT(red, green, blue);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTSecondaryColor_nglSecondaryColor3fEXT(JNIEnv *env, jclass clazz, jfloat red, jfloat green, jfloat blue, jlong function_pointer) {
	glSecondaryColor3fEXTPROC glSecondaryColor3fEXT = (glSecondaryColor3fEXTPROC)((intptr_t)function_pointer);
	glSecondaryColor3fEXT(red, green, blue);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTSecondaryColor_nglSecondaryColor3dEXT(JNIEnv *env, jclass clazz, jdouble red, jdouble green, jdouble blue, jlong function_pointer) {
	glSecondaryColor3dEXTPROC glSecondaryColor3dEXT = (glSecondaryColor3dEXTPROC)((intptr_t)function_pointer);
	glSecondaryColor3dEXT(red, green, blue);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTSecondaryColor_nglSecondaryColor3ubEXT(JNIEnv *env, jclass clazz, jbyte red, jbyte green, jbyte blue, jlong function_pointer) {
	glSecondaryColor3ubEXTPROC glSecondaryColor3ubEXT = (glSecondaryColor3ubEXTPROC)((intptr_t)function_pointer);
	glSecondaryColor3ubEXT(red, green, blue);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTSecondaryColor_nglSecondaryColorPointerEXT(JNIEnv *env, jclass clazz, jint size, jint type, jint stride, jlong pPointer, jlong function_pointer) {
	const GLvoid *pPointer_address = (const GLvoid *)(intptr_t)pPointer;
	glSecondaryColorPointerEXTPROC glSecondaryColorPointerEXT = (glSecondaryColorPointerEXTPROC)((intptr_t)function_pointer);
	glSecondaryColorPointerEXT(size, type, stride, pPointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTSecondaryColor_nglSecondaryColorPointerEXTBO(JNIEnv *env, jclass clazz, jint size, jint type, jint stride, jlong pPointer_buffer_offset, jlong function_pointer) {
	const GLvoid *pPointer_address = (const GLvoid *)(intptr_t)offsetToPointer(pPointer_buffer_offset);
	glSecondaryColorPointerEXTPROC glSecondaryColorPointerEXT = (glSecondaryColorPointerEXTPROC)((intptr_t)function_pointer);
	glSecondaryColorPointerEXT(size, type, stride, pPointer_address);
}


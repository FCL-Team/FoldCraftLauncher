/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glVertex2hNVPROC) (GLhalf x, GLhalf y);
typedef void (APIENTRY *glVertex3hNVPROC) (GLhalf x, GLhalf y, GLhalf z);
typedef void (APIENTRY *glVertex4hNVPROC) (GLhalf x, GLhalf y, GLhalf z, GLhalf w);
typedef void (APIENTRY *glNormal3hNVPROC) (GLhalf nx, GLhalf ny, GLhalf nz);
typedef void (APIENTRY *glColor3hNVPROC) (GLhalf red, GLhalf green, GLhalf blue);
typedef void (APIENTRY *glColor4hNVPROC) (GLhalf red, GLhalf green, GLhalf blue, GLhalf alpha);
typedef void (APIENTRY *glTexCoord1hNVPROC) (GLhalf s);
typedef void (APIENTRY *glTexCoord2hNVPROC) (GLhalf s, GLhalf t);
typedef void (APIENTRY *glTexCoord3hNVPROC) (GLhalf s, GLhalf t, GLhalf r);
typedef void (APIENTRY *glTexCoord4hNVPROC) (GLhalf s, GLhalf t, GLhalf r, GLhalf q);
typedef void (APIENTRY *glMultiTexCoord1hNVPROC) (GLenum target, GLhalf s);
typedef void (APIENTRY *glMultiTexCoord2hNVPROC) (GLenum target, GLhalf s, GLhalf t);
typedef void (APIENTRY *glMultiTexCoord3hNVPROC) (GLenum target, GLhalf s, GLhalf t, GLhalf r);
typedef void (APIENTRY *glMultiTexCoord4hNVPROC) (GLenum target, GLhalf s, GLhalf t, GLhalf r, GLhalf q);
typedef void (APIENTRY *glFogCoordhNVPROC) (GLhalf fog);
typedef void (APIENTRY *glSecondaryColor3hNVPROC) (GLhalf red, GLhalf green, GLhalf blue);
typedef void (APIENTRY *glVertexWeighthNVPROC) (GLhalf weight);
typedef void (APIENTRY *glVertexAttrib1hNVPROC) (GLuint index, GLhalf x);
typedef void (APIENTRY *glVertexAttrib2hNVPROC) (GLuint index, GLhalf x, GLhalf y);
typedef void (APIENTRY *glVertexAttrib3hNVPROC) (GLuint index, GLhalf x, GLhalf y, GLhalf z);
typedef void (APIENTRY *glVertexAttrib4hNVPROC) (GLuint index, GLhalf x, GLhalf y, GLhalf z, GLhalf w);
typedef void (APIENTRY *glVertexAttribs1hvNVPROC) (GLuint index, GLsizei n, const GLhalf * attribs);
typedef void (APIENTRY *glVertexAttribs2hvNVPROC) (GLuint index, GLsizei n, const GLhalf * attribs);
typedef void (APIENTRY *glVertexAttribs3hvNVPROC) (GLuint index, GLsizei n, const GLhalf * attribs);
typedef void (APIENTRY *glVertexAttribs4hvNVPROC) (GLuint index, GLsizei n, const GLhalf * attribs);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglVertex2hNV(JNIEnv *env, jclass clazz, jshort x, jshort y, jlong function_pointer) {
	glVertex2hNVPROC glVertex2hNV = (glVertex2hNVPROC)((intptr_t)function_pointer);
	glVertex2hNV(x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglVertex3hNV(JNIEnv *env, jclass clazz, jshort x, jshort y, jshort z, jlong function_pointer) {
	glVertex3hNVPROC glVertex3hNV = (glVertex3hNVPROC)((intptr_t)function_pointer);
	glVertex3hNV(x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglVertex4hNV(JNIEnv *env, jclass clazz, jshort x, jshort y, jshort z, jshort w, jlong function_pointer) {
	glVertex4hNVPROC glVertex4hNV = (glVertex4hNVPROC)((intptr_t)function_pointer);
	glVertex4hNV(x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglNormal3hNV(JNIEnv *env, jclass clazz, jshort nx, jshort ny, jshort nz, jlong function_pointer) {
	glNormal3hNVPROC glNormal3hNV = (glNormal3hNVPROC)((intptr_t)function_pointer);
	glNormal3hNV(nx, ny, nz);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglColor3hNV(JNIEnv *env, jclass clazz, jshort red, jshort green, jshort blue, jlong function_pointer) {
	glColor3hNVPROC glColor3hNV = (glColor3hNVPROC)((intptr_t)function_pointer);
	glColor3hNV(red, green, blue);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglColor4hNV(JNIEnv *env, jclass clazz, jshort red, jshort green, jshort blue, jshort alpha, jlong function_pointer) {
	glColor4hNVPROC glColor4hNV = (glColor4hNVPROC)((intptr_t)function_pointer);
	glColor4hNV(red, green, blue, alpha);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglTexCoord1hNV(JNIEnv *env, jclass clazz, jshort s, jlong function_pointer) {
	glTexCoord1hNVPROC glTexCoord1hNV = (glTexCoord1hNVPROC)((intptr_t)function_pointer);
	glTexCoord1hNV(s);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglTexCoord2hNV(JNIEnv *env, jclass clazz, jshort s, jshort t, jlong function_pointer) {
	glTexCoord2hNVPROC glTexCoord2hNV = (glTexCoord2hNVPROC)((intptr_t)function_pointer);
	glTexCoord2hNV(s, t);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglTexCoord3hNV(JNIEnv *env, jclass clazz, jshort s, jshort t, jshort r, jlong function_pointer) {
	glTexCoord3hNVPROC glTexCoord3hNV = (glTexCoord3hNVPROC)((intptr_t)function_pointer);
	glTexCoord3hNV(s, t, r);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglTexCoord4hNV(JNIEnv *env, jclass clazz, jshort s, jshort t, jshort r, jshort q, jlong function_pointer) {
	glTexCoord4hNVPROC glTexCoord4hNV = (glTexCoord4hNVPROC)((intptr_t)function_pointer);
	glTexCoord4hNV(s, t, r, q);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglMultiTexCoord1hNV(JNIEnv *env, jclass clazz, jint target, jshort s, jlong function_pointer) {
	glMultiTexCoord1hNVPROC glMultiTexCoord1hNV = (glMultiTexCoord1hNVPROC)((intptr_t)function_pointer);
	glMultiTexCoord1hNV(target, s);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglMultiTexCoord2hNV(JNIEnv *env, jclass clazz, jint target, jshort s, jshort t, jlong function_pointer) {
	glMultiTexCoord2hNVPROC glMultiTexCoord2hNV = (glMultiTexCoord2hNVPROC)((intptr_t)function_pointer);
	glMultiTexCoord2hNV(target, s, t);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglMultiTexCoord3hNV(JNIEnv *env, jclass clazz, jint target, jshort s, jshort t, jshort r, jlong function_pointer) {
	glMultiTexCoord3hNVPROC glMultiTexCoord3hNV = (glMultiTexCoord3hNVPROC)((intptr_t)function_pointer);
	glMultiTexCoord3hNV(target, s, t, r);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglMultiTexCoord4hNV(JNIEnv *env, jclass clazz, jint target, jshort s, jshort t, jshort r, jshort q, jlong function_pointer) {
	glMultiTexCoord4hNVPROC glMultiTexCoord4hNV = (glMultiTexCoord4hNVPROC)((intptr_t)function_pointer);
	glMultiTexCoord4hNV(target, s, t, r, q);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglFogCoordhNV(JNIEnv *env, jclass clazz, jshort fog, jlong function_pointer) {
	glFogCoordhNVPROC glFogCoordhNV = (glFogCoordhNVPROC)((intptr_t)function_pointer);
	glFogCoordhNV(fog);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglSecondaryColor3hNV(JNIEnv *env, jclass clazz, jshort red, jshort green, jshort blue, jlong function_pointer) {
	glSecondaryColor3hNVPROC glSecondaryColor3hNV = (glSecondaryColor3hNVPROC)((intptr_t)function_pointer);
	glSecondaryColor3hNV(red, green, blue);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglVertexWeighthNV(JNIEnv *env, jclass clazz, jshort weight, jlong function_pointer) {
	glVertexWeighthNVPROC glVertexWeighthNV = (glVertexWeighthNVPROC)((intptr_t)function_pointer);
	glVertexWeighthNV(weight);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglVertexAttrib1hNV(JNIEnv *env, jclass clazz, jint index, jshort x, jlong function_pointer) {
	glVertexAttrib1hNVPROC glVertexAttrib1hNV = (glVertexAttrib1hNVPROC)((intptr_t)function_pointer);
	glVertexAttrib1hNV(index, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglVertexAttrib2hNV(JNIEnv *env, jclass clazz, jint index, jshort x, jshort y, jlong function_pointer) {
	glVertexAttrib2hNVPROC glVertexAttrib2hNV = (glVertexAttrib2hNVPROC)((intptr_t)function_pointer);
	glVertexAttrib2hNV(index, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglVertexAttrib3hNV(JNIEnv *env, jclass clazz, jint index, jshort x, jshort y, jshort z, jlong function_pointer) {
	glVertexAttrib3hNVPROC glVertexAttrib3hNV = (glVertexAttrib3hNVPROC)((intptr_t)function_pointer);
	glVertexAttrib3hNV(index, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglVertexAttrib4hNV(JNIEnv *env, jclass clazz, jint index, jshort x, jshort y, jshort z, jshort w, jlong function_pointer) {
	glVertexAttrib4hNVPROC glVertexAttrib4hNV = (glVertexAttrib4hNVPROC)((intptr_t)function_pointer);
	glVertexAttrib4hNV(index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglVertexAttribs1hvNV(JNIEnv *env, jclass clazz, jint index, jint n, jlong attribs, jlong function_pointer) {
	const GLhalf *attribs_address = (const GLhalf *)(intptr_t)attribs;
	glVertexAttribs1hvNVPROC glVertexAttribs1hvNV = (glVertexAttribs1hvNVPROC)((intptr_t)function_pointer);
	glVertexAttribs1hvNV(index, n, attribs_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglVertexAttribs2hvNV(JNIEnv *env, jclass clazz, jint index, jint n, jlong attribs, jlong function_pointer) {
	const GLhalf *attribs_address = (const GLhalf *)(intptr_t)attribs;
	glVertexAttribs2hvNVPROC glVertexAttribs2hvNV = (glVertexAttribs2hvNVPROC)((intptr_t)function_pointer);
	glVertexAttribs2hvNV(index, n, attribs_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglVertexAttribs3hvNV(JNIEnv *env, jclass clazz, jint index, jint n, jlong attribs, jlong function_pointer) {
	const GLhalf *attribs_address = (const GLhalf *)(intptr_t)attribs;
	glVertexAttribs3hvNVPROC glVertexAttribs3hvNV = (glVertexAttribs3hvNVPROC)((intptr_t)function_pointer);
	glVertexAttribs3hvNV(index, n, attribs_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVHalfFloat_nglVertexAttribs4hvNV(JNIEnv *env, jclass clazz, jint index, jint n, jlong attribs, jlong function_pointer) {
	const GLhalf *attribs_address = (const GLhalf *)(intptr_t)attribs;
	glVertexAttribs4hvNVPROC glVertexAttribs4hvNV = (glVertexAttribs4hvNVPROC)((intptr_t)function_pointer);
	glVertexAttribs4hvNV(index, n, attribs_address);
}


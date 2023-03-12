/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glBufferParameteriAPPLEPROC) (GLenum target, GLenum pname, GLint param);
typedef void (APIENTRY *glFlushMappedBufferRangeAPPLEPROC) (GLenum target, GLintptr offset, GLsizeiptr size);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_APPLEFlushBufferRange_nglBufferParameteriAPPLE(JNIEnv *env, jclass clazz, jint target, jint pname, jint param, jlong function_pointer) {
	glBufferParameteriAPPLEPROC glBufferParameteriAPPLE = (glBufferParameteriAPPLEPROC)((intptr_t)function_pointer);
	glBufferParameteriAPPLE(target, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_APPLEFlushBufferRange_nglFlushMappedBufferRangeAPPLE(JNIEnv *env, jclass clazz, jint target, jlong offset, jlong size, jlong function_pointer) {
	glFlushMappedBufferRangeAPPLEPROC glFlushMappedBufferRangeAPPLE = (glFlushMappedBufferRangeAPPLEPROC)((intptr_t)function_pointer);
	glFlushMappedBufferRangeAPPLE(target, offset, size);
}


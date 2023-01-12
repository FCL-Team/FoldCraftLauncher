/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extcl.h"

typedef CL_API_ENTRY cl_mem (CL_API_CALL *clCreateFromGLTexturePROC) (cl_context context, cl_mem_flags flags, GLenum target, GLint miplevel, GLuint texture, cl_int * errcode_ret);

JNIEXPORT jlong JNICALL Java_org_lwjgl_opencl_CL12GL_nclCreateFromGLTexture(JNIEnv *env, jclass clazz, jlong context, jlong flags, jint target, jint miplevel, jint texture, jlong errcode_ret, jlong function_pointer) {
	cl_int *errcode_ret_address = (cl_int *)(intptr_t)errcode_ret;
	clCreateFromGLTexturePROC clCreateFromGLTexture = (clCreateFromGLTexturePROC)((intptr_t)function_pointer);
	cl_mem __result = clCreateFromGLTexture((cl_context)(intptr_t)context, flags, target, miplevel, texture, errcode_ret_address);
	return (intptr_t)__result;
}


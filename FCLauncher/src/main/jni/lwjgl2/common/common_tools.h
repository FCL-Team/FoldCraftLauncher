/*
 * Copyright (c) 2002-2008 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * $Id$
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision$
 */

#ifndef _COMMON_TOOLS_H
#define _COMMON_TOOLS_H

#include <jni.h>
#include <string.h>
#include <stdlib.h>

#define ATTRIB_LIST_SIZE (256)

typedef struct {
	int current_index;
	int attribs[ATTRIB_LIST_SIZE];
} attrib_list_t;

#ifndef __cplusplus
#ifndef bool
typedef enum {false, true} bool;
#endif
#endif

#ifdef _MSC_VER
#define inline __inline
#include <Basetsd.h>
#else
#include <inttypes.h>
#endif

static inline void * safeGetBufferAddress(JNIEnv *env, jobject buffer) {
	if (buffer != NULL) {
#ifdef __cplusplus
		return (void *)((char *)env->GetDirectBufferAddress(buffer));
#else
		return (void *)((char *)(*env)->GetDirectBufferAddress(env, buffer));
#endif
	} else
		return NULL;
}

static inline jobject safeNewBuffer(JNIEnv *env, void *p, jlong capacity) {
	if (p != NULL) {
#ifdef __cplusplus
		return env->NewDirectByteBuffer(p, capacity);
#else
		return (*env)->NewDirectByteBuffer(env, p, capacity);
#endif
	} else
		return NULL;
}

static inline jobject safeNewBufferCached(JNIEnv *env, void *p, jlong size, jobject old_buffer) {
	if (old_buffer != NULL) {
		void *old_buffer_address = (*env)->GetDirectBufferAddress(env, old_buffer);
		jlong capacity = (*env)->GetDirectBufferCapacity(env, old_buffer);
		if (old_buffer_address == p && capacity == size)
			return old_buffer;
	}
	return safeNewBuffer(env, p, size);

}

static inline void *offsetToPointer(jlong offset) {
	return (char *)NULL + offset;
}

typedef void *(* ExtGetProcAddressPROC) (const char *func_name);
typedef struct {
	char *method_name;
	char *signature;
	void *method_pointer;

	char *ext_function_name;
	void **ext_function_pointer;
	bool optional;
} JavaMethodAndExtFunction;

typedef struct {
	char *ext_function_name;
	void **ext_function_pointer;
} ExtFunction;

#define NUMFUNCTIONS(x) (sizeof(x)/sizeof(JavaMethodAndExtFunction));

#ifdef __cplusplus
extern "C" {
#endif

extern JNIEnv *getThreadEnv();
extern JNIEnv *attachCurrentThread();
extern void detachCurrentThread();
extern void initAttribList(attrib_list_t *list);
extern void putAttrib(attrib_list_t *list, int attrib);

extern bool isDebugEnabled(void);
extern jstring getVersionString(JNIEnv *env);
extern void throwGeneralException(JNIEnv * env, const char *exception_name, const char * err);
extern void throwFormattedRuntimeException(JNIEnv * env, const char *format, ...);
extern void throwException(JNIEnv *env, const char *msg);
extern void throwFormattedException(JNIEnv * env, const char *format, ...);
extern void throwFMODException(JNIEnv * env, const char * err);
extern void setDebugEnabled(bool enable);
extern void printfDebugJava(JNIEnv *env, const char *format, ...);
extern void printfDebug(const char *format, ...);
extern bool getBooleanProperty(JNIEnv *env, const char* propertyName);
extern char * GetStringNativeChars(JNIEnv *env, jstring jstr);
extern jstring NewStringNativeWithLength(JNIEnv *env, const char *str, jsize length);
extern jstring NewStringNativeUnsigned(JNIEnv *env, const unsigned char *str);
extern jobject NewReadOnlyDirectByteBuffer(JNIEnv* env, const void* address, jlong capacity);
extern jobject newJavaManagedByteBuffer(JNIEnv *env, const int size);
extern bool positionBuffer(JNIEnv *env, jobject buffer, jint position);
extern jlong getPointerWrapperAddress(JNIEnv *env, jobject wrapper);

extern void ext_InitializeClass(JNIEnv *env, jclass clazz, ExtGetProcAddressPROC gpa, int num_functions, JavaMethodAndExtFunction *functions);
extern bool ext_InitializeFunctions(ExtGetProcAddressPROC gpa, int num_functions, ExtFunction *functions);

#ifdef __cplusplus
}
#endif

#endif


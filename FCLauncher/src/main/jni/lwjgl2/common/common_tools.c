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

#include <jni.h>
#include <stdlib.h>
#include "common_tools.h"
#include "org_lwjgl_DefaultSysImplementation.h"

static bool debug = false;
static JavaVM *jvm;

static jmethodID mByteBuffer_asReadOnlyBuffer;
static jmethodID mPointerWrapper_getPointer;

void initAttribList(attrib_list_t *list) {
	list->current_index = 0;
}

void putAttrib(attrib_list_t *list, int attrib) {
	if (list->current_index == ATTRIB_LIST_SIZE) {
		printfDebug("Ignoring attrib %d: attrib list size too small", attrib);
		return;
	}
	list->attribs[list->current_index] = attrib;
	list->current_index++;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_DefaultSysImplementation_getPointerSize(JNIEnv *env, jclass clazz) {
    return (jint)sizeof(void *);
}

JNIEXPORT void JNICALL Java_org_lwjgl_DefaultSysImplementation_setDebug
  (JNIEnv *env, jobject ignored, jboolean enable) {
	  debug = enable == JNI_TRUE ? true : false;
}

bool isDebugEnabled(void) {
	return debug;
}

static int do_vsnprintf(char* buffer, size_t buffer_size, const char *format, va_list ap) {
#ifdef _MSC_VER
	return vsnprintf_s(buffer, buffer_size, _TRUNCATE, format, ap);
#else
	va_list cp_ap;
	va_copy(cp_ap, ap);
	int res = vsnprintf(buffer, buffer_size, format, cp_ap);
	va_end(cp_ap);
	return res;
#endif
}

static jstring sprintfJavaString(JNIEnv *env, const char *format, va_list ap) {
	int buffer_size = 2048;
	char *buffer;
	jstring str;
	int str_size;
	buffer = (char *)malloc(sizeof(char)*buffer_size);
	if (buffer == NULL)
		return NULL;
	str_size = do_vsnprintf(buffer, buffer_size, format, ap);
	if (str_size > buffer_size) {
		free(buffer);
		buffer_size = str_size + 1;
		buffer = (char *)malloc(sizeof(char)*buffer_size);
		if (buffer == NULL)
			return NULL;
		do_vsnprintf(buffer, buffer_size, format, ap);
	}
	str = (*env)->NewStringUTF(env, buffer);
	free(buffer);
	return str;
}

void printfDebugJava(JNIEnv *env, const char *format, ...) {
	jstring str;
	jclass org_lwjgl_LWJGLUtil_class;
	jmethodID log_method;
	va_list ap;
	if (isDebugEnabled() && !(*env)->ExceptionOccurred(env)) {
		va_start(ap, format);
		str = sprintfJavaString(env, format, ap);
		va_end(ap);
		org_lwjgl_LWJGLUtil_class = (*env)->FindClass(env, "org/lwjgl/LWJGLUtil");
		if (org_lwjgl_LWJGLUtil_class == NULL)
			return;
		log_method = (*env)->GetStaticMethodID(env, org_lwjgl_LWJGLUtil_class, "log", "(Ljava/lang/CharSequence;)V");
		if (log_method == NULL)
			return;
		(*env)->CallStaticVoidMethod(env, org_lwjgl_LWJGLUtil_class, log_method, str);
	}
}

void printfDebug(const char *format, ...) {
	va_list ap;
	va_start(ap, format);
	if (isDebugEnabled())
		vfprintf(stderr, format, ap);
	va_end(ap);
}

static void throwFormattedGeneralException(JNIEnv * env, const char *exception_name, const char *format, va_list ap) {
	jclass cls;
	jstring str;
	jmethodID exception_constructor;
	jobject exception;

	if ((*env)->ExceptionCheck(env) == JNI_TRUE)
		return; // The JVM crashes if we try to throw two exceptions from one native call
	str = sprintfJavaString(env, format, ap);
	cls = (*env)->FindClass(env, exception_name);
    exception_constructor = (*env)->GetMethodID(env, cls, "<init>", "(Ljava/lang/String;)V");
	exception = (*env)->NewObject(env, cls, exception_constructor, str);
	(*env)->Throw(env, exception);
}

void throwGeneralException(JNIEnv * env, const char *exception_name, const char * err) {
	jclass cls;

	if ((*env)->ExceptionCheck(env) == JNI_TRUE)
		return; // The JVM crashes if we try to throw two exceptions from one native call
	cls = (*env)->FindClass(env, exception_name);
	(*env)->ThrowNew(env, cls, err);
}

void throwFMODException(JNIEnv * env, const char * err) {
	throwGeneralException(env, "org/lwjgl/fmod3/FMODException", err);
}

void throwFormattedRuntimeException(JNIEnv * env, const char *format, ...) {
	va_list ap;
	va_start(ap, format);
	throwFormattedGeneralException(env, "java/lang/RuntimeException", format, ap);
	va_end(ap);
}

void throwFormattedException(JNIEnv * env, const char *format, ...) {
	va_list ap;
	va_start(ap, format);
	throwFormattedGeneralException(env, "org/lwjgl/LWJGLException", format, ap);
	va_end(ap);
}

void throwException(JNIEnv * env, const char * err) {
	throwGeneralException(env, "org/lwjgl/LWJGLException", err);
}

// retrieves the locale-specific C string
char * GetStringNativeChars(JNIEnv *env, jstring jstr) {
  jbyteArray bytes = 0;
  jthrowable exc;
  char *result = 0;
  jclass jcls_str;
  jmethodID MID_String_getBytes;

  /* out of memory error? */
  if ((*env)->EnsureLocalCapacity(env, 2) < 0) {
    return 0;
  }

  // aquire getBytes method
  jcls_str = (*env)->FindClass(env, "java/lang/String");
  MID_String_getBytes = (*env)->GetMethodID(env, jcls_str, "getBytes", "()[B");

  // get the bytes
  bytes = (jbyteArray) (*env)->CallObjectMethod(env, jstr, MID_String_getBytes);
  exc = (*env)->ExceptionOccurred(env);

  // if no exception occured while getting bytes - continue
  if (!exc) {
    jint len = (*env)->GetArrayLength(env, bytes);
    result = (char *) malloc(len + 1);
    if (result == 0) {
      throwGeneralException(env, "java/lang/OutOfMemoryError", NULL);
      (*env)->DeleteLocalRef(env, bytes);
      return 0;
    }
    (*env)->GetByteArrayRegion(env, bytes, 0, len, (jbyte *) result);
    result[len] = 0; /* NULL-terminate */
  } else {
    (*env)->DeleteLocalRef(env, exc);
  }
  (*env)->DeleteLocalRef(env, bytes);
  return (char*) result;
}

/* creates locale specific string, unsigned argument to
 * match GLuchar and ALuchar types
 */
jstring NewStringNativeUnsigned(JNIEnv *env, const unsigned char *ustr) {
	const char *str = (const char *)ustr;
	if (str == NULL)
		return NULL;
	return NewStringNativeWithLength(env, str, (jsize)strlen(str));
}

// creates locale specific string
jstring NewStringNativeWithLength(JNIEnv *env, const char *str, jsize length) {
  jclass jcls_str;
  jmethodID jmethod_str;
  jstring result;
  jbyteArray bytes;
  if (str==NULL) {
    return NULL;
  }

  jcls_str = (*env)->FindClass(env,"java/lang/String");
  if (jcls_str == NULL)
	  return NULL;
  jmethod_str = (*env)->GetMethodID(env,jcls_str, "<init>", "([B)V");
  if (jmethod_str == NULL)
	  return NULL;

  bytes = 0;

  if ((*env)->EnsureLocalCapacity(env,2) < 0) {
    return NULL; /* out of memory error */
  }

  bytes = (*env)->NewByteArray(env,length);
  if (bytes != NULL) {
    (*env)->SetByteArrayRegion(env,bytes, 0, length, (jbyte *)str);
    result = (jstring)(*env)->NewObject(env,jcls_str, jmethod_str, bytes);
    (*env)->DeleteLocalRef(env,bytes);
    return result;
  } /* else fall through */
  return NULL;
}

bool ext_InitializeFunctions(ExtGetProcAddressPROC gpa, int num_functions, ExtFunction *functions) {
	int i;
	void **ext_function_pointer_pointer;
	for (i = 0; i < num_functions; i++) {
		ExtFunction *function = functions + i;
		if (function->ext_function_name != NULL) {
			void *ext_func_pointer = gpa(function->ext_function_name);
			if (ext_func_pointer == NULL)
				return false;
			ext_function_pointer_pointer = function->ext_function_pointer;
			*ext_function_pointer_pointer = ext_func_pointer;
		}
	}
	return true;
}

jobject NewReadOnlyDirectByteBuffer(JNIEnv* env, const void* address, jlong capacity) {
    jobject buffer = (*env)->NewDirectByteBuffer(env, (void *)address, capacity);
    return (*env)->CallObjectMethod(env, buffer, mByteBuffer_asReadOnlyBuffer);
}

jobject newJavaManagedByteBuffer(JNIEnv *env, const int size) {
  jclass bufferutils_class = (*env)->FindClass(env, "org/lwjgl/BufferUtils");
  jmethodID createByteBuffer = (*env)->GetStaticMethodID(env, bufferutils_class, "createByteBuffer", "(I)Ljava/nio/ByteBuffer;");
  jobject buffer = (*env)->CallStaticObjectMethod(env, bufferutils_class, createByteBuffer, size);
  return buffer;
}

void ext_InitializeClass(JNIEnv *env, jclass clazz, ExtGetProcAddressPROC gpa, int num_functions, JavaMethodAndExtFunction *functions) {
	JNINativeMethod *methods;
	JavaMethodAndExtFunction *function;
	void *ext_func_pointer;
	void **ext_function_pointer_pointer;
	JNINativeMethod *method;
	int i, num_natives = 0;
	if (clazz == NULL) {
		throwException(env, "Null class");
		return;
	}
	methods = (JNINativeMethod *)malloc(num_functions*sizeof(JNINativeMethod));
	for (i = 0; i < num_functions; i++) {
		function = functions + i;
		if (function->ext_function_name != NULL) {
			ext_func_pointer = gpa(function->ext_function_name);
			if (ext_func_pointer == NULL) {
				if ( function->optional )
					continue;

				free(methods);
				throwException(env, "Missing driver symbols");
				return;
			}
			ext_function_pointer_pointer = function->ext_function_pointer;
			*ext_function_pointer_pointer = ext_func_pointer;
		}
		method = methods + num_natives;
		method->name = function->method_name;
		method->signature = function->signature;
		method->fnPtr = function->method_pointer;

		num_natives++;
	}
	(*env)->RegisterNatives(env, clazz, methods, num_natives);
	free(methods);
}

bool getBooleanProperty(JNIEnv *env, const char* propertyName) {
  jstring property = NewStringNativeWithLength(env, propertyName, (jsize)strlen(propertyName));
  jclass org_lwjgl_LWJGLUtil_class;
  jmethodID getBoolean;
  if (property == NULL)
	  return false;
  org_lwjgl_LWJGLUtil_class = (*env)->FindClass(env, "org/lwjgl/LWJGLUtil");
  if (org_lwjgl_LWJGLUtil_class == NULL)
	  return false;
  getBoolean = (*env)->GetStaticMethodID(env, org_lwjgl_LWJGLUtil_class, "getPrivilegedBoolean", "(Ljava/lang/String;)Z");
  if (getBoolean == NULL)
	  return false;
  return (*env)->CallStaticBooleanMethod(env, org_lwjgl_LWJGLUtil_class, getBoolean, property) ? true : false;
}

jlong getPointerWrapperAddress(JNIEnv *env, jobject wrapper) {
    return (*env)->CallLongMethod(env, wrapper, mPointerWrapper_getPointer);
}

JNIEnv *getThreadEnv() {
	JNIEnv *env;
	(*jvm)->GetEnv(jvm, (void *)&env, JNI_VERSION_1_4);
	return env;
}

JNIEnv *attachCurrentThread() {
    JNIEnv *env;
    (*jvm)->AttachCurrentThread(jvm, (void **)&env, NULL);
    return env;
}

void detachCurrentThread() {
    (*jvm)->DetachCurrentThread(jvm);
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    jclass clazz;

    jvm = vm;
    env = getThreadEnv();

    clazz = (*env)->FindClass(env, "java/nio/ByteBuffer");
    mByteBuffer_asReadOnlyBuffer = (*env)->GetMethodID(env, clazz, "asReadOnlyBuffer", "()Ljava/nio/ByteBuffer;");

    clazz = (*env)->FindClass(env, "org/lwjgl/PointerWrapper");
    mPointerWrapper_getPointer = (*env)->GetMethodID(env, clazz, "getPointer", "()J");

    return JNI_VERSION_1_4;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved) {
}

bool positionBuffer(JNIEnv *env, jobject buffer, jint position) {
	jclass buffer_class;
	jmethodID position_method;

	buffer_class = (*env)->GetObjectClass(env, buffer);
	if (buffer_class == NULL)
		return false;
	position_method = (*env)->GetMethodID(env, buffer_class, "position", "(I)Ljava/nio/Buffer;");
	if (position_method == NULL)
		return false;
	(*env)->CallObjectMethod(env, buffer, position_method, position);
	return true;
}

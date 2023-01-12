#include "org_lwjgl_BufferUtils.h"
#include "common_tools.h"

JNIEXPORT void JNICALL Java_org_lwjgl_BufferUtils_zeroBuffer0(JNIEnv *env, jclass clazz, jobject buffer, jlong offset, jlong size) {
	memset((char*)(*env)->GetDirectBufferAddress(env, buffer) + (size_t)offset, 0, (size_t)size);
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_BufferUtils_getBufferAddress(JNIEnv *env, jclass clazz, jobject buffer) {
    return (intptr_t)(*env)->GetDirectBufferAddress(env, buffer);
}

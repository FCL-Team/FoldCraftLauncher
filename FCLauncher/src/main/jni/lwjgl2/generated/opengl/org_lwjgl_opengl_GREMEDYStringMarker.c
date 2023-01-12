/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glStringMarkerGREMEDYPROC) (GLsizei len, const GLbyte * string);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GREMEDYStringMarker_nglStringMarkerGREMEDY(JNIEnv *env, jclass clazz, jint len, jlong string, jlong function_pointer) {
	const GLbyte *string_address = (const GLbyte *)(intptr_t)string;
	glStringMarkerGREMEDYPROC glStringMarkerGREMEDY = (glStringMarkerGREMEDYPROC)((intptr_t)function_pointer);
	glStringMarkerGREMEDY(len, string_address);
}


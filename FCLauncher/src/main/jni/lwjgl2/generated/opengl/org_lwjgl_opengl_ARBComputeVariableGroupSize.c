/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glDispatchComputeGroupSizeARBPROC) (GLuint num_groups_x, GLuint num_groups_y, GLuint num_groups_z, GLuint group_size_x, GLuint group_size_y, GLuint group_size_z);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBComputeVariableGroupSize_nglDispatchComputeGroupSizeARB(JNIEnv *env, jclass clazz, jint num_groups_x, jint num_groups_y, jint num_groups_z, jint group_size_x, jint group_size_y, jint group_size_z, jlong function_pointer) {
	glDispatchComputeGroupSizeARBPROC glDispatchComputeGroupSizeARB = (glDispatchComputeGroupSizeARBPROC)((intptr_t)function_pointer);
	glDispatchComputeGroupSizeARB(num_groups_x, num_groups_y, num_groups_z, group_size_x, group_size_y, group_size_z);
}


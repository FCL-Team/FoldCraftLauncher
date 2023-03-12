/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glGenNamesAMDPROC) (GLenum identifier, GLuint num, GLuint * names);
typedef void (APIENTRY *glDeleteNamesAMDPROC) (GLenum identifier, GLsizei num, const GLuint * names);
typedef GLboolean (APIENTRY *glIsNameAMDPROC) (GLenum identifier, GLuint name);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDNameGenDelete_nglGenNamesAMD(JNIEnv *env, jclass clazz, jint identifier, jint num, jlong names, jlong function_pointer) {
	GLuint *names_address = (GLuint *)(intptr_t)names;
	glGenNamesAMDPROC glGenNamesAMD = (glGenNamesAMDPROC)((intptr_t)function_pointer);
	glGenNamesAMD(identifier, num, names_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDNameGenDelete_nglDeleteNamesAMD(JNIEnv *env, jclass clazz, jint identifier, jint num, jlong names, jlong function_pointer) {
	const GLuint *names_address = (const GLuint *)(intptr_t)names;
	glDeleteNamesAMDPROC glDeleteNamesAMD = (glDeleteNamesAMDPROC)((intptr_t)function_pointer);
	glDeleteNamesAMD(identifier, num, names_address);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_AMDNameGenDelete_nglIsNameAMD(JNIEnv *env, jclass clazz, jint identifier, jint name, jlong function_pointer) {
	glIsNameAMDPROC glIsNameAMD = (glIsNameAMDPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsNameAMD(identifier, name);
	return __result;
}


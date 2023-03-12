/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glStencilOpSeparateATIPROC) (GLenum face, GLenum sfail, GLenum dpfail, GLenum dppass);
typedef void (APIENTRY *glStencilFuncSeparateATIPROC) (GLenum frontfunc, GLenum backfunc, GLint ref, GLuint mask);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATISeparateStencil_nglStencilOpSeparateATI(JNIEnv *env, jclass clazz, jint face, jint sfail, jint dpfail, jint dppass, jlong function_pointer) {
	glStencilOpSeparateATIPROC glStencilOpSeparateATI = (glStencilOpSeparateATIPROC)((intptr_t)function_pointer);
	glStencilOpSeparateATI(face, sfail, dpfail, dppass);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ATISeparateStencil_nglStencilFuncSeparateATI(JNIEnv *env, jclass clazz, jint frontfunc, jint backfunc, jint ref, jint mask, jlong function_pointer) {
	glStencilFuncSeparateATIPROC glStencilFuncSeparateATI = (glStencilFuncSeparateATIPROC)((intptr_t)function_pointer);
	glStencilFuncSeparateATI(frontfunc, backfunc, ref, mask);
}


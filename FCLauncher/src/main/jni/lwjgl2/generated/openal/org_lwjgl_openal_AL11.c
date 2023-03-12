/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extal.h"

typedef ALvoid (ALAPIENTRY *alListener3iPROC) (ALenum pname, ALint v1, ALint v2, ALint v3);
typedef ALvoid (ALAPIENTRY *alGetListenerivPROC) (ALenum pname, ALfloat * intdata);
typedef ALvoid (ALAPIENTRY *alSource3iPROC) (ALuint source, ALenum pname, ALint v1, ALint v2, ALint v3);
typedef ALvoid (ALAPIENTRY *alSourceivPROC) (ALuint source, ALenum pname, const ALint * value);
typedef ALvoid (ALAPIENTRY *alBufferfPROC) (ALuint buffer, ALenum pname, ALfloat value);
typedef ALvoid (ALAPIENTRY *alBuffer3fPROC) (ALuint buffer, ALenum pname, ALfloat v1, ALfloat v2, ALfloat v3);
typedef ALvoid (ALAPIENTRY *alBufferfvPROC) (ALuint buffer, ALenum pname, const ALfloat * value);
typedef ALvoid (ALAPIENTRY *alBufferiPROC) (ALuint buffer, ALenum pname, ALint value);
typedef ALvoid (ALAPIENTRY *alBuffer3iPROC) (ALuint buffer, ALenum pname, ALint v1, ALint v2, ALint v3);
typedef ALvoid (ALAPIENTRY *alBufferivPROC) (ALuint buffer, ALenum pname, const ALint * value);
typedef ALvoid (ALAPIENTRY *alGetBufferiPROC) (ALuint buffer, ALenum pname, ALint* value);
typedef ALvoid (ALAPIENTRY *alGetBufferivPROC) (ALuint buffer, ALenum pname, ALint * values);
typedef ALvoid (ALAPIENTRY *alGetBufferfPROC) (ALuint buffer, ALenum pname, ALfloat* value);
typedef ALvoid (ALAPIENTRY *alGetBufferfvPROC) (ALuint buffer, ALenum pname, ALfloat * values);
typedef ALvoid (ALAPIENTRY *alSpeedOfSoundPROC) (ALfloat value);

static alListener3iPROC alListener3i;
static alGetListenerivPROC alGetListeneriv;
static alSource3iPROC alSource3i;
static alSourceivPROC alSourceiv;
static alBufferfPROC alBufferf;
static alBuffer3fPROC alBuffer3f;
static alBufferfvPROC alBufferfv;
static alBufferiPROC alBufferi;
static alBuffer3iPROC alBuffer3i;
static alBufferivPROC alBufferiv;
static alGetBufferiPROC alGetBufferi;
static alGetBufferivPROC alGetBufferiv;
static alGetBufferfPROC alGetBufferf;
static alGetBufferfvPROC alGetBufferfv;
static alSpeedOfSoundPROC alSpeedOfSound;

static void JNICALL Java_org_lwjgl_openal_AL11_nalListener3i(JNIEnv *env, jclass clazz, jint pname, jint v1, jint v2, jint v3) {
	alListener3i(pname, v1, v2, v3);
}

static void JNICALL Java_org_lwjgl_openal_AL11_nalGetListeneriv(JNIEnv *env, jclass clazz, jint pname, jlong intdata) {
	ALfloat *intdata_address = (ALfloat *)(intptr_t)intdata;
	alGetListeneriv(pname, intdata_address);
}

static void JNICALL Java_org_lwjgl_openal_AL11_nalSource3i(JNIEnv *env, jclass clazz, jint source, jint pname, jint v1, jint v2, jint v3) {
	alSource3i(source, pname, v1, v2, v3);
}

static void JNICALL Java_org_lwjgl_openal_AL11_nalSourceiv(JNIEnv *env, jclass clazz, jint source, jint pname, jlong value) {
	const ALint *value_address = (const ALint *)(intptr_t)value;
	alSourceiv(source, pname, value_address);
}

static void JNICALL Java_org_lwjgl_openal_AL11_nalBufferf(JNIEnv *env, jclass clazz, jint buffer, jint pname, jfloat value) {
	alBufferf(buffer, pname, value);
}

static void JNICALL Java_org_lwjgl_openal_AL11_nalBuffer3f(JNIEnv *env, jclass clazz, jint buffer, jint pname, jfloat v1, jfloat v2, jfloat v3) {
	alBuffer3f(buffer, pname, v1, v2, v3);
}

static void JNICALL Java_org_lwjgl_openal_AL11_nalBufferfv(JNIEnv *env, jclass clazz, jint buffer, jint pname, jlong value) {
	const ALfloat *value_address = (const ALfloat *)(intptr_t)value;
	alBufferfv(buffer, pname, value_address);
}

static void JNICALL Java_org_lwjgl_openal_AL11_nalBufferi(JNIEnv *env, jclass clazz, jint buffer, jint pname, jint value) {
	alBufferi(buffer, pname, value);
}

static void JNICALL Java_org_lwjgl_openal_AL11_nalBuffer3i(JNIEnv *env, jclass clazz, jint buffer, jint pname, jint v1, jint v2, jint v3) {
	alBuffer3i(buffer, pname, v1, v2, v3);
}

static void JNICALL Java_org_lwjgl_openal_AL11_nalBufferiv(JNIEnv *env, jclass clazz, jint buffer, jint pname, jlong value) {
	const ALint *value_address = (const ALint *)(intptr_t)value;
	alBufferiv(buffer, pname, value_address);
}

static jint JNICALL Java_org_lwjgl_openal_AL11_nalGetBufferi(JNIEnv *env, jclass clazz, jint buffer, jint pname) {
	ALint __result;
	alGetBufferi(buffer, pname, &__result);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_AL11_nalGetBufferiv(JNIEnv *env, jclass clazz, jint buffer, jint pname, jlong values) {
	ALint *values_address = (ALint *)(intptr_t)values;
	alGetBufferiv(buffer, pname, values_address);
}

static jfloat JNICALL Java_org_lwjgl_openal_AL11_nalGetBufferf(JNIEnv *env, jclass clazz, jint buffer, jint pname) {
	ALfloat __result;
	alGetBufferf(buffer, pname, &__result);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_AL11_nalGetBufferfv(JNIEnv *env, jclass clazz, jint buffer, jint pname, jlong values) {
	ALfloat *values_address = (ALfloat *)(intptr_t)values;
	alGetBufferfv(buffer, pname, values_address);
}

static void JNICALL Java_org_lwjgl_openal_AL11_nalSpeedOfSound(JNIEnv *env, jclass clazz, jfloat value) {
	alSpeedOfSound(value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_openal_AL11_initNativeStubs(JNIEnv *env, jclass clazz) {
	JavaMethodAndExtFunction functions[] = {
		{"nalListener3i", "(IIII)V", (void *)&Java_org_lwjgl_openal_AL11_nalListener3i, "alListener3i", (void *)&alListener3i, false},
		{"nalGetListeneriv", "(IJ)V", (void *)&Java_org_lwjgl_openal_AL11_nalGetListeneriv, "alGetListeneriv", (void *)&alGetListeneriv, false},
		{"nalSource3i", "(IIIII)V", (void *)&Java_org_lwjgl_openal_AL11_nalSource3i, "alSource3i", (void *)&alSource3i, false},
		{"nalSourceiv", "(IIJ)V", (void *)&Java_org_lwjgl_openal_AL11_nalSourceiv, "alSourceiv", (void *)&alSourceiv, false},
		{"nalBufferf", "(IIF)V", (void *)&Java_org_lwjgl_openal_AL11_nalBufferf, "alBufferf", (void *)&alBufferf, false},
		{"nalBuffer3f", "(IIFFF)V", (void *)&Java_org_lwjgl_openal_AL11_nalBuffer3f, "alBuffer3f", (void *)&alBuffer3f, false},
		{"nalBufferfv", "(IIJ)V", (void *)&Java_org_lwjgl_openal_AL11_nalBufferfv, "alBufferfv", (void *)&alBufferfv, false},
		{"nalBufferi", "(III)V", (void *)&Java_org_lwjgl_openal_AL11_nalBufferi, "alBufferi", (void *)&alBufferi, false},
		{"nalBuffer3i", "(IIIII)V", (void *)&Java_org_lwjgl_openal_AL11_nalBuffer3i, "alBuffer3i", (void *)&alBuffer3i, false},
		{"nalBufferiv", "(IIJ)V", (void *)&Java_org_lwjgl_openal_AL11_nalBufferiv, "alBufferiv", (void *)&alBufferiv, false},
		{"nalGetBufferi", "(II)I", (void *)&Java_org_lwjgl_openal_AL11_nalGetBufferi, "alGetBufferi", (void *)&alGetBufferi, false},
		{"nalGetBufferiv", "(IIJ)V", (void *)&Java_org_lwjgl_openal_AL11_nalGetBufferiv, "alGetBufferiv", (void *)&alGetBufferiv, false},
		{"nalGetBufferf", "(II)F", (void *)&Java_org_lwjgl_openal_AL11_nalGetBufferf, "alGetBufferf", (void *)&alGetBufferf, false},
		{"nalGetBufferfv", "(IIJ)V", (void *)&Java_org_lwjgl_openal_AL11_nalGetBufferfv, "alGetBufferfv", (void *)&alGetBufferfv, false},
		{"nalSpeedOfSound", "(F)V", (void *)&Java_org_lwjgl_openal_AL11_nalSpeedOfSound, "alSpeedOfSound", (void *)&alSpeedOfSound, false}
	};
	int num_functions = NUMFUNCTIONS(functions);
	extal_InitializeClass(env, clazz, num_functions, functions);
}

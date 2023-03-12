/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extal.h"

typedef ALvoid (ALAPIENTRY *alEnablePROC) (ALint capability);
typedef ALvoid (ALAPIENTRY *alDisablePROC) (ALenum capability);
typedef ALboolean (ALAPIENTRY *alIsEnabledPROC) (ALenum capability);
typedef ALboolean (ALAPIENTRY *alGetBooleanPROC) (ALenum pname);
typedef ALint (ALAPIENTRY *alGetIntegerPROC) (ALenum pname);
typedef ALfloat (ALAPIENTRY *alGetFloatPROC) (ALenum pname);
typedef ALdouble (ALAPIENTRY *alGetDoublePROC) (ALenum pname);
typedef ALvoid (ALAPIENTRY *alGetIntegervPROC) (ALenum pname, ALint * data);
typedef ALvoid (ALAPIENTRY *alGetFloatvPROC) (ALenum pname, ALfloat * data);
typedef ALvoid (ALAPIENTRY *alGetDoublevPROC) (ALenum pname, ALdouble * data);
typedef ALubyte * (ALAPIENTRY *alGetStringPROC) (ALenum pname);
typedef ALenum (ALAPIENTRY *alGetErrorPROC) ();
typedef ALboolean (ALAPIENTRY *alIsExtensionPresentPROC) (ALubyte * fname);
typedef ALenum (ALAPIENTRY *alGetEnumValuePROC) (ALubyte * ename);
typedef ALvoid (ALAPIENTRY *alListeneriPROC) (ALenum pname, ALint value);
typedef ALvoid (ALAPIENTRY *alListenerfPROC) (ALenum pname, ALfloat value);
typedef ALvoid (ALAPIENTRY *alListenerfvPROC) (ALenum pname, const ALfloat * value);
typedef ALvoid (ALAPIENTRY *alListener3fPROC) (ALenum pname, ALfloat v1, ALfloat v2, ALfloat v3);
typedef void (ALAPIENTRY *alGetListeneriPROC) (ALenum pname, ALint* value);
typedef void (ALAPIENTRY *alGetListenerfPROC) (ALenum pname, ALfloat* value);
typedef ALvoid (ALAPIENTRY *alGetListenerfvPROC) (ALenum pname, ALfloat * floatdata);
typedef ALvoid (ALAPIENTRY *alGenSourcesPROC) (ALsizei n, ALuint * sources);
typedef ALvoid (ALAPIENTRY *alDeleteSourcesPROC) (ALsizei n, ALuint * sources);
typedef ALboolean (ALAPIENTRY *alIsSourcePROC) (ALuint id);
typedef ALvoid (ALAPIENTRY *alSourceiPROC) (ALuint source, ALenum pname, ALint value);
typedef ALvoid (ALAPIENTRY *alSourcefPROC) (ALuint source, ALenum pname, ALfloat value);
typedef ALvoid (ALAPIENTRY *alSourcefvPROC) (ALuint source, ALenum pname, const ALfloat * value);
typedef ALvoid (ALAPIENTRY *alSource3fPROC) (ALuint source, ALenum pname, ALfloat v1, ALfloat v2, ALfloat v3);
typedef ALvoid (ALAPIENTRY *alGetSourceiPROC) (ALuint source, ALenum pname, ALint* value);
typedef ALvoid (ALAPIENTRY *alGetSourcefPROC) (ALuint source, ALenum pname, ALfloat* value);
typedef ALvoid (ALAPIENTRY *alGetSourcefvPROC) (ALuint source, ALenum pname, ALfloat * floatdata);
typedef ALvoid (ALAPIENTRY *alSourcePlayvPROC) (ALsizei n, ALuint * sources);
typedef ALvoid (ALAPIENTRY *alSourcePausevPROC) (ALsizei n, ALuint * sources);
typedef ALvoid (ALAPIENTRY *alSourceStopvPROC) (ALsizei n, ALuint * sources);
typedef ALvoid (ALAPIENTRY *alSourceRewindvPROC) (ALsizei n, ALuint * sources);
typedef ALvoid (ALAPIENTRY *alSourcePlayPROC) (ALuint source);
typedef ALvoid (ALAPIENTRY *alSourcePausePROC) (ALuint source);
typedef ALvoid (ALAPIENTRY *alSourceStopPROC) (ALuint source);
typedef ALvoid (ALAPIENTRY *alSourceRewindPROC) (ALuint source);
typedef ALvoid (ALAPIENTRY *alGenBuffersPROC) (ALsizei n, ALuint * buffers);
typedef ALvoid (ALAPIENTRY *alDeleteBuffersPROC) (ALsizei n, ALuint * buffers);
typedef ALboolean (ALAPIENTRY *alIsBufferPROC) (ALuint buffer);
typedef ALvoid (ALAPIENTRY *alBufferDataPROC) (ALuint buffer, ALenum format, ALvoid * data, ALsizei size, ALsizei freq);
typedef ALvoid (ALAPIENTRY *alGetBufferiPROC) (ALuint buffer, ALenum pname, ALint* value);
typedef ALvoid (ALAPIENTRY *alGetBufferfPROC) (ALuint buffer, ALenum pname, ALfloat* value);
typedef ALvoid (ALAPIENTRY *alSourceQueueBuffersPROC) (ALuint source, ALsizei n, ALuint * buffers);
typedef ALvoid (ALAPIENTRY *alSourceUnqueueBuffersPROC) (ALuint source, ALsizei n, ALuint * buffers);
typedef ALvoid (ALAPIENTRY *alDistanceModelPROC) (ALenum value);
typedef ALvoid (ALAPIENTRY *alDopplerFactorPROC) (ALfloat value);
typedef ALvoid (ALAPIENTRY *alDopplerVelocityPROC) (ALfloat value);

static alEnablePROC alEnable;
static alDisablePROC alDisable;
static alIsEnabledPROC alIsEnabled;
static alGetBooleanPROC alGetBoolean;
static alGetIntegerPROC alGetInteger;
static alGetFloatPROC alGetFloat;
static alGetDoublePROC alGetDouble;
static alGetIntegervPROC alGetIntegerv;
static alGetFloatvPROC alGetFloatv;
static alGetDoublevPROC alGetDoublev;
static alGetStringPROC alGetString;
static alGetErrorPROC alGetError;
static alIsExtensionPresentPROC alIsExtensionPresent;
static alGetEnumValuePROC alGetEnumValue;
static alListeneriPROC alListeneri;
static alListenerfPROC alListenerf;
static alListenerfvPROC alListenerfv;
static alListener3fPROC alListener3f;
static alGetListeneriPROC alGetListeneri;
static alGetListenerfPROC alGetListenerf;
static alGetListenerfvPROC alGetListenerfv;
static alGenSourcesPROC alGenSources;
static alDeleteSourcesPROC alDeleteSources;
static alIsSourcePROC alIsSource;
static alSourceiPROC alSourcei;
static alSourcefPROC alSourcef;
static alSourcefvPROC alSourcefv;
static alSource3fPROC alSource3f;
static alGetSourceiPROC alGetSourcei;
static alGetSourcefPROC alGetSourcef;
static alGetSourcefvPROC alGetSourcefv;
static alSourcePlayvPROC alSourcePlayv;
static alSourcePausevPROC alSourcePausev;
static alSourceStopvPROC alSourceStopv;
static alSourceRewindvPROC alSourceRewindv;
static alSourcePlayPROC alSourcePlay;
static alSourcePausePROC alSourcePause;
static alSourceStopPROC alSourceStop;
static alSourceRewindPROC alSourceRewind;
static alGenBuffersPROC alGenBuffers;
static alDeleteBuffersPROC alDeleteBuffers;
static alIsBufferPROC alIsBuffer;
static alBufferDataPROC alBufferData;
static alGetBufferiPROC alGetBufferi;
static alGetBufferfPROC alGetBufferf;
static alSourceQueueBuffersPROC alSourceQueueBuffers;
static alSourceUnqueueBuffersPROC alSourceUnqueueBuffers;
static alDistanceModelPROC alDistanceModel;
static alDopplerFactorPROC alDopplerFactor;
static alDopplerVelocityPROC alDopplerVelocity;

static void JNICALL Java_org_lwjgl_openal_AL10_nalEnable(JNIEnv *env, jclass clazz, jint capability) {
	alEnable(capability);
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalDisable(JNIEnv *env, jclass clazz, jint capability) {
	alDisable(capability);
}

static jboolean JNICALL Java_org_lwjgl_openal_AL10_nalIsEnabled(JNIEnv *env, jclass clazz, jint capability) {
	ALboolean __result = alIsEnabled(capability);
	return __result;
}

static jboolean JNICALL Java_org_lwjgl_openal_AL10_nalGetBoolean(JNIEnv *env, jclass clazz, jint pname) {
	ALboolean __result = alGetBoolean(pname);
	return __result;
}

static jint JNICALL Java_org_lwjgl_openal_AL10_nalGetInteger(JNIEnv *env, jclass clazz, jint pname) {
	ALint __result = alGetInteger(pname);
	return __result;
}

static jfloat JNICALL Java_org_lwjgl_openal_AL10_nalGetFloat(JNIEnv *env, jclass clazz, jint pname) {
	ALfloat __result = alGetFloat(pname);
	return __result;
}

static jdouble JNICALL Java_org_lwjgl_openal_AL10_nalGetDouble(JNIEnv *env, jclass clazz, jint pname) {
	ALdouble __result = alGetDouble(pname);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalGetIntegerv(JNIEnv *env, jclass clazz, jint pname, jlong data) {
	ALint *data_address = (ALint *)(intptr_t)data;
	alGetIntegerv(pname, data_address);
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalGetFloatv(JNIEnv *env, jclass clazz, jint pname, jlong data) {
	ALfloat *data_address = (ALfloat *)(intptr_t)data;
	alGetFloatv(pname, data_address);
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalGetDoublev(JNIEnv *env, jclass clazz, jint pname, jlong data) {
	ALdouble *data_address = (ALdouble *)(intptr_t)data;
	alGetDoublev(pname, data_address);
}

static jobject JNICALL Java_org_lwjgl_openal_AL10_nalGetString(JNIEnv *env, jclass clazz, jint pname) {
	ALubyte * __result = alGetString(pname);
	return NewStringNativeUnsigned(env, __result);
}

static jint JNICALL Java_org_lwjgl_openal_AL10_nalGetError(JNIEnv *env, jclass clazz) {
	ALenum __result = alGetError();
	return __result;
}

static jboolean JNICALL Java_org_lwjgl_openal_AL10_nalIsExtensionPresent(JNIEnv *env, jclass clazz, jobject fname) {
	ALubyte *fname_address = (ALubyte *)(intptr_t)GetStringNativeChars(env, fname);
	ALboolean __result = alIsExtensionPresent(fname_address);
	free(fname_address);
	return __result;
}

static jint JNICALL Java_org_lwjgl_openal_AL10_nalGetEnumValue(JNIEnv *env, jclass clazz, jobject ename) {
	ALubyte *ename_address = (ALubyte *)(intptr_t)GetStringNativeChars(env, ename);
	ALenum __result = alGetEnumValue(ename_address);
	free(ename_address);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalListeneri(JNIEnv *env, jclass clazz, jint pname, jint value) {
	alListeneri(pname, value);
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalListenerf(JNIEnv *env, jclass clazz, jint pname, jfloat value) {
	alListenerf(pname, value);
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalListenerfv(JNIEnv *env, jclass clazz, jint pname, jlong value) {
	const ALfloat *value_address = (const ALfloat *)(intptr_t)value;
	alListenerfv(pname, value_address);
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalListener3f(JNIEnv *env, jclass clazz, jint pname, jfloat v1, jfloat v2, jfloat v3) {
	alListener3f(pname, v1, v2, v3);
}

static jint JNICALL Java_org_lwjgl_openal_AL10_nalGetListeneri(JNIEnv *env, jclass clazz, jint pname) {
	ALint __result;
	alGetListeneri(pname, &__result);
	return __result;
}

static jfloat JNICALL Java_org_lwjgl_openal_AL10_nalGetListenerf(JNIEnv *env, jclass clazz, jint pname) {
	ALfloat __result;
	alGetListenerf(pname, &__result);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalGetListenerfv(JNIEnv *env, jclass clazz, jint pname, jlong floatdata) {
	ALfloat *floatdata_address = (ALfloat *)(intptr_t)floatdata;
	alGetListenerfv(pname, floatdata_address);
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalGenSources(JNIEnv *env, jclass clazz, jint n, jlong sources) {
	ALuint *sources_address = (ALuint *)(intptr_t)sources;
	alGenSources(n, sources_address);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_openal_AL10_nalGenSources2(JNIEnv *env, jclass clazz, jint n) {
	ALuint __result;
	alGenSources(n, &__result);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalDeleteSources(JNIEnv *env, jclass clazz, jint n, jlong sources) {
	ALuint *sources_address = (ALuint *)(intptr_t)sources;
	alDeleteSources(n, sources_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_openal_AL10_nalDeleteSources2(JNIEnv *env, jclass clazz, jint n, jint source) {
	alDeleteSources(n, (ALuint*)&source);
}

static jboolean JNICALL Java_org_lwjgl_openal_AL10_nalIsSource(JNIEnv *env, jclass clazz, jint id) {
	ALboolean __result = alIsSource(id);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalSourcei(JNIEnv *env, jclass clazz, jint source, jint pname, jint value) {
	alSourcei(source, pname, value);
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalSourcef(JNIEnv *env, jclass clazz, jint source, jint pname, jfloat value) {
	alSourcef(source, pname, value);
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalSourcefv(JNIEnv *env, jclass clazz, jint source, jint pname, jlong value) {
	const ALfloat *value_address = (const ALfloat *)(intptr_t)value;
	alSourcefv(source, pname, value_address);
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalSource3f(JNIEnv *env, jclass clazz, jint source, jint pname, jfloat v1, jfloat v2, jfloat v3) {
	alSource3f(source, pname, v1, v2, v3);
}

static jint JNICALL Java_org_lwjgl_openal_AL10_nalGetSourcei(JNIEnv *env, jclass clazz, jint source, jint pname) {
	ALint __result;
	alGetSourcei(source, pname, &__result);
	return __result;
}

static jfloat JNICALL Java_org_lwjgl_openal_AL10_nalGetSourcef(JNIEnv *env, jclass clazz, jint source, jint pname) {
	ALfloat __result;
	alGetSourcef(source, pname, &__result);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalGetSourcefv(JNIEnv *env, jclass clazz, jint source, jint pname, jlong floatdata) {
	ALfloat *floatdata_address = (ALfloat *)(intptr_t)floatdata;
	alGetSourcefv(source, pname, floatdata_address);
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalSourcePlayv(JNIEnv *env, jclass clazz, jint n, jlong sources) {
	ALuint *sources_address = (ALuint *)(intptr_t)sources;
	alSourcePlayv(n, sources_address);
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalSourcePausev(JNIEnv *env, jclass clazz, jint n, jlong sources) {
	ALuint *sources_address = (ALuint *)(intptr_t)sources;
	alSourcePausev(n, sources_address);
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalSourceStopv(JNIEnv *env, jclass clazz, jint n, jlong sources) {
	ALuint *sources_address = (ALuint *)(intptr_t)sources;
	alSourceStopv(n, sources_address);
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalSourceRewindv(JNIEnv *env, jclass clazz, jint n, jlong sources) {
	ALuint *sources_address = (ALuint *)(intptr_t)sources;
	alSourceRewindv(n, sources_address);
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalSourcePlay(JNIEnv *env, jclass clazz, jint source) {
	alSourcePlay(source);
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalSourcePause(JNIEnv *env, jclass clazz, jint source) {
	alSourcePause(source);
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalSourceStop(JNIEnv *env, jclass clazz, jint source) {
	alSourceStop(source);
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalSourceRewind(JNIEnv *env, jclass clazz, jint source) {
	alSourceRewind(source);
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalGenBuffers(JNIEnv *env, jclass clazz, jint n, jlong buffers) {
	ALuint *buffers_address = (ALuint *)(intptr_t)buffers;
	alGenBuffers(n, buffers_address);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_openal_AL10_nalGenBuffers2(JNIEnv *env, jclass clazz, jint n) {
	ALuint __result;
	alGenBuffers(n, &__result);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalDeleteBuffers(JNIEnv *env, jclass clazz, jint n, jlong buffers) {
	ALuint *buffers_address = (ALuint *)(intptr_t)buffers;
	alDeleteBuffers(n, buffers_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_openal_AL10_nalDeleteBuffers2(JNIEnv *env, jclass clazz, jint n, jint buffer) {
	alDeleteBuffers(n, (ALuint*)&buffer);
}

static jboolean JNICALL Java_org_lwjgl_openal_AL10_nalIsBuffer(JNIEnv *env, jclass clazz, jint buffer) {
	ALboolean __result = alIsBuffer(buffer);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalBufferData(JNIEnv *env, jclass clazz, jint buffer, jint format, jlong data, jint size, jint freq) {
	ALvoid *data_address = (ALvoid *)(intptr_t)data;
	alBufferData(buffer, format, data_address, size, freq);
}

static jint JNICALL Java_org_lwjgl_openal_AL10_nalGetBufferi(JNIEnv *env, jclass clazz, jint buffer, jint pname) {
	ALint __result;
	alGetBufferi(buffer, pname, &__result);
	return __result;
}

static jfloat JNICALL Java_org_lwjgl_openal_AL10_nalGetBufferf(JNIEnv *env, jclass clazz, jint buffer, jint pname) {
	ALfloat __result;
	alGetBufferf(buffer, pname, &__result);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalSourceQueueBuffers(JNIEnv *env, jclass clazz, jint source, jint n, jlong buffers) {
	ALuint *buffers_address = (ALuint *)(intptr_t)buffers;
	alSourceQueueBuffers(source, n, buffers_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_openal_AL10_nalSourceQueueBuffers2(JNIEnv *env, jclass clazz, jint source, jint n, jint buffer) {
	alSourceQueueBuffers(source, n, (ALuint*)&buffer);
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalSourceUnqueueBuffers(JNIEnv *env, jclass clazz, jint source, jint n, jlong buffers) {
	ALuint *buffers_address = (ALuint *)(intptr_t)buffers;
	alSourceUnqueueBuffers(source, n, buffers_address);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_openal_AL10_nalSourceUnqueueBuffers2(JNIEnv *env, jclass clazz, jint source, jint n) {
	ALuint __result;
	alSourceUnqueueBuffers(source, n, &__result);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalDistanceModel(JNIEnv *env, jclass clazz, jint value) {
	alDistanceModel(value);
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalDopplerFactor(JNIEnv *env, jclass clazz, jfloat value) {
	alDopplerFactor(value);
}

static void JNICALL Java_org_lwjgl_openal_AL10_nalDopplerVelocity(JNIEnv *env, jclass clazz, jfloat value) {
	alDopplerVelocity(value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_openal_AL10_initNativeStubs(JNIEnv *env, jclass clazz) {
	JavaMethodAndExtFunction functions[] = {
		{"nalEnable", "(I)V", (void *)&Java_org_lwjgl_openal_AL10_nalEnable, "alEnable", (void *)&alEnable, false},
		{"nalDisable", "(I)V", (void *)&Java_org_lwjgl_openal_AL10_nalDisable, "alDisable", (void *)&alDisable, false},
		{"nalIsEnabled", "(I)Z", (void *)&Java_org_lwjgl_openal_AL10_nalIsEnabled, "alIsEnabled", (void *)&alIsEnabled, false},
		{"nalGetBoolean", "(I)Z", (void *)&Java_org_lwjgl_openal_AL10_nalGetBoolean, "alGetBoolean", (void *)&alGetBoolean, false},
		{"nalGetInteger", "(I)I", (void *)&Java_org_lwjgl_openal_AL10_nalGetInteger, "alGetInteger", (void *)&alGetInteger, false},
		{"nalGetFloat", "(I)F", (void *)&Java_org_lwjgl_openal_AL10_nalGetFloat, "alGetFloat", (void *)&alGetFloat, false},
		{"nalGetDouble", "(I)D", (void *)&Java_org_lwjgl_openal_AL10_nalGetDouble, "alGetDouble", (void *)&alGetDouble, false},
		{"nalGetIntegerv", "(IJ)V", (void *)&Java_org_lwjgl_openal_AL10_nalGetIntegerv, "alGetIntegerv", (void *)&alGetIntegerv, false},
		{"nalGetFloatv", "(IJ)V", (void *)&Java_org_lwjgl_openal_AL10_nalGetFloatv, "alGetFloatv", (void *)&alGetFloatv, false},
		{"nalGetDoublev", "(IJ)V", (void *)&Java_org_lwjgl_openal_AL10_nalGetDoublev, "alGetDoublev", (void *)&alGetDoublev, false},
		{"nalGetString", "(I)Ljava/lang/String;", (void *)&Java_org_lwjgl_openal_AL10_nalGetString, "alGetString", (void *)&alGetString, false},
		{"nalGetError", "()I", (void *)&Java_org_lwjgl_openal_AL10_nalGetError, "alGetError", (void *)&alGetError, false},
		{"nalIsExtensionPresent", "(Ljava/lang/String;)Z", (void *)&Java_org_lwjgl_openal_AL10_nalIsExtensionPresent, "alIsExtensionPresent", (void *)&alIsExtensionPresent, false},
		{"nalGetEnumValue", "(Ljava/lang/String;)I", (void *)&Java_org_lwjgl_openal_AL10_nalGetEnumValue, "alGetEnumValue", (void *)&alGetEnumValue, false},
		{"nalListeneri", "(II)V", (void *)&Java_org_lwjgl_openal_AL10_nalListeneri, "alListeneri", (void *)&alListeneri, false},
		{"nalListenerf", "(IF)V", (void *)&Java_org_lwjgl_openal_AL10_nalListenerf, "alListenerf", (void *)&alListenerf, false},
		{"nalListenerfv", "(IJ)V", (void *)&Java_org_lwjgl_openal_AL10_nalListenerfv, "alListenerfv", (void *)&alListenerfv, false},
		{"nalListener3f", "(IFFF)V", (void *)&Java_org_lwjgl_openal_AL10_nalListener3f, "alListener3f", (void *)&alListener3f, false},
		{"nalGetListeneri", "(I)I", (void *)&Java_org_lwjgl_openal_AL10_nalGetListeneri, "alGetListeneri", (void *)&alGetListeneri, false},
		{"nalGetListenerf", "(I)F", (void *)&Java_org_lwjgl_openal_AL10_nalGetListenerf, "alGetListenerf", (void *)&alGetListenerf, false},
		{"nalGetListenerfv", "(IJ)V", (void *)&Java_org_lwjgl_openal_AL10_nalGetListenerfv, "alGetListenerfv", (void *)&alGetListenerfv, false},
		{"nalGenSources", "(IJ)V", (void *)&Java_org_lwjgl_openal_AL10_nalGenSources, "alGenSources", (void *)&alGenSources, false},
		{"nalGenSources2", "(I)I", (void *)&Java_org_lwjgl_openal_AL10_nalGenSources2, "alGenSources", (void *)&alGenSources, false},
		{"nalDeleteSources", "(IJ)V", (void *)&Java_org_lwjgl_openal_AL10_nalDeleteSources, "alDeleteSources", (void *)&alDeleteSources, false},
		{"nalDeleteSources2", "(II)V", (void *)&Java_org_lwjgl_openal_AL10_nalDeleteSources2, "alDeleteSources", (void *)&alDeleteSources, false},
		{"nalIsSource", "(I)Z", (void *)&Java_org_lwjgl_openal_AL10_nalIsSource, "alIsSource", (void *)&alIsSource, false},
		{"nalSourcei", "(III)V", (void *)&Java_org_lwjgl_openal_AL10_nalSourcei, "alSourcei", (void *)&alSourcei, false},
		{"nalSourcef", "(IIF)V", (void *)&Java_org_lwjgl_openal_AL10_nalSourcef, "alSourcef", (void *)&alSourcef, false},
		{"nalSourcefv", "(IIJ)V", (void *)&Java_org_lwjgl_openal_AL10_nalSourcefv, "alSourcefv", (void *)&alSourcefv, false},
		{"nalSource3f", "(IIFFF)V", (void *)&Java_org_lwjgl_openal_AL10_nalSource3f, "alSource3f", (void *)&alSource3f, false},
		{"nalGetSourcei", "(II)I", (void *)&Java_org_lwjgl_openal_AL10_nalGetSourcei, "alGetSourcei", (void *)&alGetSourcei, false},
		{"nalGetSourcef", "(II)F", (void *)&Java_org_lwjgl_openal_AL10_nalGetSourcef, "alGetSourcef", (void *)&alGetSourcef, false},
		{"nalGetSourcefv", "(IIJ)V", (void *)&Java_org_lwjgl_openal_AL10_nalGetSourcefv, "alGetSourcefv", (void *)&alGetSourcefv, false},
		{"nalSourcePlayv", "(IJ)V", (void *)&Java_org_lwjgl_openal_AL10_nalSourcePlayv, "alSourcePlayv", (void *)&alSourcePlayv, false},
		{"nalSourcePausev", "(IJ)V", (void *)&Java_org_lwjgl_openal_AL10_nalSourcePausev, "alSourcePausev", (void *)&alSourcePausev, false},
		{"nalSourceStopv", "(IJ)V", (void *)&Java_org_lwjgl_openal_AL10_nalSourceStopv, "alSourceStopv", (void *)&alSourceStopv, false},
		{"nalSourceRewindv", "(IJ)V", (void *)&Java_org_lwjgl_openal_AL10_nalSourceRewindv, "alSourceRewindv", (void *)&alSourceRewindv, false},
		{"nalSourcePlay", "(I)V", (void *)&Java_org_lwjgl_openal_AL10_nalSourcePlay, "alSourcePlay", (void *)&alSourcePlay, false},
		{"nalSourcePause", "(I)V", (void *)&Java_org_lwjgl_openal_AL10_nalSourcePause, "alSourcePause", (void *)&alSourcePause, false},
		{"nalSourceStop", "(I)V", (void *)&Java_org_lwjgl_openal_AL10_nalSourceStop, "alSourceStop", (void *)&alSourceStop, false},
		{"nalSourceRewind", "(I)V", (void *)&Java_org_lwjgl_openal_AL10_nalSourceRewind, "alSourceRewind", (void *)&alSourceRewind, false},
		{"nalGenBuffers", "(IJ)V", (void *)&Java_org_lwjgl_openal_AL10_nalGenBuffers, "alGenBuffers", (void *)&alGenBuffers, false},
		{"nalGenBuffers2", "(I)I", (void *)&Java_org_lwjgl_openal_AL10_nalGenBuffers2, "alGenBuffers", (void *)&alGenBuffers, false},
		{"nalDeleteBuffers", "(IJ)V", (void *)&Java_org_lwjgl_openal_AL10_nalDeleteBuffers, "alDeleteBuffers", (void *)&alDeleteBuffers, false},
		{"nalDeleteBuffers2", "(II)V", (void *)&Java_org_lwjgl_openal_AL10_nalDeleteBuffers2, "alDeleteBuffers", (void *)&alDeleteBuffers, false},
		{"nalIsBuffer", "(I)Z", (void *)&Java_org_lwjgl_openal_AL10_nalIsBuffer, "alIsBuffer", (void *)&alIsBuffer, false},
		{"nalBufferData", "(IIJII)V", (void *)&Java_org_lwjgl_openal_AL10_nalBufferData, "alBufferData", (void *)&alBufferData, false},
		{"nalGetBufferi", "(II)I", (void *)&Java_org_lwjgl_openal_AL10_nalGetBufferi, "alGetBufferi", (void *)&alGetBufferi, false},
		{"nalGetBufferf", "(II)F", (void *)&Java_org_lwjgl_openal_AL10_nalGetBufferf, "alGetBufferf", (void *)&alGetBufferf, false},
		{"nalSourceQueueBuffers", "(IIJ)V", (void *)&Java_org_lwjgl_openal_AL10_nalSourceQueueBuffers, "alSourceQueueBuffers", (void *)&alSourceQueueBuffers, false},
		{"nalSourceQueueBuffers2", "(III)V", (void *)&Java_org_lwjgl_openal_AL10_nalSourceQueueBuffers2, "alSourceQueueBuffers", (void *)&alSourceQueueBuffers, false},
		{"nalSourceUnqueueBuffers", "(IIJ)V", (void *)&Java_org_lwjgl_openal_AL10_nalSourceUnqueueBuffers, "alSourceUnqueueBuffers", (void *)&alSourceUnqueueBuffers, false},
		{"nalSourceUnqueueBuffers2", "(II)I", (void *)&Java_org_lwjgl_openal_AL10_nalSourceUnqueueBuffers2, "alSourceUnqueueBuffers", (void *)&alSourceUnqueueBuffers, false},
		{"nalDistanceModel", "(I)V", (void *)&Java_org_lwjgl_openal_AL10_nalDistanceModel, "alDistanceModel", (void *)&alDistanceModel, false},
		{"nalDopplerFactor", "(F)V", (void *)&Java_org_lwjgl_openal_AL10_nalDopplerFactor, "alDopplerFactor", (void *)&alDopplerFactor, false},
		{"nalDopplerVelocity", "(F)V", (void *)&Java_org_lwjgl_openal_AL10_nalDopplerVelocity, "alDopplerVelocity", (void *)&alDopplerVelocity, false}
	};
	int num_functions = NUMFUNCTIONS(functions);
	extal_InitializeClass(env, clazz, num_functions, functions);
}

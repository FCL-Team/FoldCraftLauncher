/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extal.h"

typedef ALvoid (ALAPIENTRY *alGenAuxiliaryEffectSlotsPROC) (ALsizei n, ALuint * auxiliaryeffectslots);
typedef ALvoid (ALAPIENTRY *alDeleteAuxiliaryEffectSlotsPROC) (ALsizei n, ALuint * auxiliaryeffectslots);
typedef ALboolean (ALAPIENTRY *alIsAuxiliaryEffectSlotPROC) (ALuint auxiliaryeffectslot);
typedef ALvoid (ALAPIENTRY *alAuxiliaryEffectSlotiPROC) (ALuint auxiliaryeffectslot, ALenum param, ALint value);
typedef ALvoid (ALAPIENTRY *alAuxiliaryEffectSlotivPROC) (ALuint auxiliaryeffectslot, ALenum param, const ALint * values);
typedef ALvoid (ALAPIENTRY *alAuxiliaryEffectSlotfPROC) (ALuint auxiliaryeffectslot, ALenum param, ALfloat value);
typedef ALvoid (ALAPIENTRY *alAuxiliaryEffectSlotfvPROC) (ALuint auxiliaryeffectslot, ALenum param, const ALfloat * values);
typedef ALvoid (ALAPIENTRY *alGetAuxiliaryEffectSlotiPROC) (ALuint auxiliaryeffectslot, ALenum param, ALint* value);
typedef ALvoid (ALAPIENTRY *alGetAuxiliaryEffectSlotivPROC) (ALuint auxiliaryeffectslot, ALenum param, ALint * intdata);
typedef ALvoid (ALAPIENTRY *alGetAuxiliaryEffectSlotfPROC) (ALuint auxiliaryeffectslot, ALenum param, ALfloat* value);
typedef ALvoid (ALAPIENTRY *alGetAuxiliaryEffectSlotfvPROC) (ALuint auxiliaryeffectslot, ALenum param, ALfloat * floatdata);
typedef ALvoid (ALAPIENTRY *alGenEffectsPROC) (ALsizei n, ALuint * effects);
typedef ALvoid (ALAPIENTRY *alDeleteEffectsPROC) (ALsizei n, ALuint * effects);
typedef ALboolean (ALAPIENTRY *alIsEffectPROC) (ALuint effect);
typedef ALvoid (ALAPIENTRY *alEffectiPROC) (ALuint effect, ALenum param, ALint value);
typedef ALvoid (ALAPIENTRY *alEffectivPROC) (ALuint effect, ALenum param, const ALint * values);
typedef ALvoid (ALAPIENTRY *alEffectfPROC) (ALuint effect, ALenum param, ALfloat value);
typedef ALvoid (ALAPIENTRY *alEffectfvPROC) (ALuint effect, ALenum param, const ALfloat * values);
typedef ALvoid (ALAPIENTRY *alGetEffectiPROC) (ALuint effect, ALenum param, ALint* value);
typedef ALvoid (ALAPIENTRY *alGetEffectivPROC) (ALuint effect, ALenum param, ALint * intdata);
typedef ALvoid (ALAPIENTRY *alGetEffectfPROC) (ALuint effect, ALenum param, ALfloat* value);
typedef ALvoid (ALAPIENTRY *alGetEffectfvPROC) (ALuint effect, ALenum param, ALfloat * floatdata);
typedef ALvoid (ALAPIENTRY *alGenFiltersPROC) (ALsizei n, ALuint * filters);
typedef ALvoid (ALAPIENTRY *alDeleteFiltersPROC) (ALsizei n, ALuint * filters);
typedef ALboolean (ALAPIENTRY *alIsFilterPROC) (ALuint filter);
typedef ALvoid (ALAPIENTRY *alFilteriPROC) (ALuint filter, ALenum param, ALint value);
typedef ALvoid (ALAPIENTRY *alFilterivPROC) (ALuint filter, ALenum param, const ALint * values);
typedef ALvoid (ALAPIENTRY *alFilterfPROC) (ALuint filter, ALenum param, ALfloat value);
typedef ALvoid (ALAPIENTRY *alFilterfvPROC) (ALuint filter, ALenum param, const ALfloat * values);
typedef ALvoid (ALAPIENTRY *alGetFilteriPROC) (ALuint filter, ALenum param, ALint* value);
typedef ALvoid (ALAPIENTRY *alGetFilterivPROC) (ALuint filter, ALenum param, ALint * intdata);
typedef ALvoid (ALAPIENTRY *alGetFilterfPROC) (ALuint filter, ALenum param, ALfloat* value);
typedef ALvoid (ALAPIENTRY *alGetFilterfvPROC) (ALuint filter, ALenum param, ALfloat * floatdata);

static alGenAuxiliaryEffectSlotsPROC alGenAuxiliaryEffectSlots;
static alDeleteAuxiliaryEffectSlotsPROC alDeleteAuxiliaryEffectSlots;
static alIsAuxiliaryEffectSlotPROC alIsAuxiliaryEffectSlot;
static alAuxiliaryEffectSlotiPROC alAuxiliaryEffectSloti;
static alAuxiliaryEffectSlotivPROC alAuxiliaryEffectSlotiv;
static alAuxiliaryEffectSlotfPROC alAuxiliaryEffectSlotf;
static alAuxiliaryEffectSlotfvPROC alAuxiliaryEffectSlotfv;
static alGetAuxiliaryEffectSlotiPROC alGetAuxiliaryEffectSloti;
static alGetAuxiliaryEffectSlotivPROC alGetAuxiliaryEffectSlotiv;
static alGetAuxiliaryEffectSlotfPROC alGetAuxiliaryEffectSlotf;
static alGetAuxiliaryEffectSlotfvPROC alGetAuxiliaryEffectSlotfv;
static alGenEffectsPROC alGenEffects;
static alDeleteEffectsPROC alDeleteEffects;
static alIsEffectPROC alIsEffect;
static alEffectiPROC alEffecti;
static alEffectivPROC alEffectiv;
static alEffectfPROC alEffectf;
static alEffectfvPROC alEffectfv;
static alGetEffectiPROC alGetEffecti;
static alGetEffectivPROC alGetEffectiv;
static alGetEffectfPROC alGetEffectf;
static alGetEffectfvPROC alGetEffectfv;
static alGenFiltersPROC alGenFilters;
static alDeleteFiltersPROC alDeleteFilters;
static alIsFilterPROC alIsFilter;
static alFilteriPROC alFilteri;
static alFilterivPROC alFilteriv;
static alFilterfPROC alFilterf;
static alFilterfvPROC alFilterfv;
static alGetFilteriPROC alGetFilteri;
static alGetFilterivPROC alGetFilteriv;
static alGetFilterfPROC alGetFilterf;
static alGetFilterfvPROC alGetFilterfv;

static void JNICALL Java_org_lwjgl_openal_EFX10_nalGenAuxiliaryEffectSlots(JNIEnv *env, jclass clazz, jint n, jlong auxiliaryeffectslots) {
	ALuint *auxiliaryeffectslots_address = (ALuint *)(intptr_t)auxiliaryeffectslots;
	alGenAuxiliaryEffectSlots(n, auxiliaryeffectslots_address);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_openal_EFX10_nalGenAuxiliaryEffectSlots2(JNIEnv *env, jclass clazz, jint n) {
	ALuint __result;
	alGenAuxiliaryEffectSlots(n, &__result);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalDeleteAuxiliaryEffectSlots(JNIEnv *env, jclass clazz, jint n, jlong auxiliaryeffectslots) {
	ALuint *auxiliaryeffectslots_address = (ALuint *)(intptr_t)auxiliaryeffectslots;
	alDeleteAuxiliaryEffectSlots(n, auxiliaryeffectslots_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_openal_EFX10_nalDeleteAuxiliaryEffectSlots2(JNIEnv *env, jclass clazz, jint n, jint auxiliaryeffectslot) {
	alDeleteAuxiliaryEffectSlots(n, (ALuint*)&auxiliaryeffectslot);
}

static jboolean JNICALL Java_org_lwjgl_openal_EFX10_nalIsAuxiliaryEffectSlot(JNIEnv *env, jclass clazz, jint auxiliaryeffectslot) {
	ALboolean __result = alIsAuxiliaryEffectSlot(auxiliaryeffectslot);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalAuxiliaryEffectSloti(JNIEnv *env, jclass clazz, jint auxiliaryeffectslot, jint param, jint value) {
	alAuxiliaryEffectSloti(auxiliaryeffectslot, param, value);
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalAuxiliaryEffectSlotiv(JNIEnv *env, jclass clazz, jint auxiliaryeffectslot, jint param, jlong values) {
	const ALint *values_address = (const ALint *)(intptr_t)values;
	alAuxiliaryEffectSlotiv(auxiliaryeffectslot, param, values_address);
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalAuxiliaryEffectSlotf(JNIEnv *env, jclass clazz, jint auxiliaryeffectslot, jint param, jfloat value) {
	alAuxiliaryEffectSlotf(auxiliaryeffectslot, param, value);
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalAuxiliaryEffectSlotfv(JNIEnv *env, jclass clazz, jint auxiliaryeffectslot, jint param, jlong values) {
	const ALfloat *values_address = (const ALfloat *)(intptr_t)values;
	alAuxiliaryEffectSlotfv(auxiliaryeffectslot, param, values_address);
}

static jint JNICALL Java_org_lwjgl_openal_EFX10_nalGetAuxiliaryEffectSloti(JNIEnv *env, jclass clazz, jint auxiliaryeffectslot, jint param) {
	ALint __result;
	alGetAuxiliaryEffectSloti(auxiliaryeffectslot, param, &__result);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalGetAuxiliaryEffectSlotiv(JNIEnv *env, jclass clazz, jint auxiliaryeffectslot, jint param, jlong intdata) {
	ALint *intdata_address = (ALint *)(intptr_t)intdata;
	alGetAuxiliaryEffectSlotiv(auxiliaryeffectslot, param, intdata_address);
}

static jfloat JNICALL Java_org_lwjgl_openal_EFX10_nalGetAuxiliaryEffectSlotf(JNIEnv *env, jclass clazz, jint auxiliaryeffectslot, jint param) {
	ALfloat __result;
	alGetAuxiliaryEffectSlotf(auxiliaryeffectslot, param, &__result);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalGetAuxiliaryEffectSlotfv(JNIEnv *env, jclass clazz, jint auxiliaryeffectslot, jint param, jlong floatdata) {
	ALfloat *floatdata_address = (ALfloat *)(intptr_t)floatdata;
	alGetAuxiliaryEffectSlotfv(auxiliaryeffectslot, param, floatdata_address);
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalGenEffects(JNIEnv *env, jclass clazz, jint n, jlong effects) {
	ALuint *effects_address = (ALuint *)(intptr_t)effects;
	alGenEffects(n, effects_address);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_openal_EFX10_nalGenEffects2(JNIEnv *env, jclass clazz, jint n) {
	ALuint __result;
	alGenEffects(n, &__result);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalDeleteEffects(JNIEnv *env, jclass clazz, jint n, jlong effects) {
	ALuint *effects_address = (ALuint *)(intptr_t)effects;
	alDeleteEffects(n, effects_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_openal_EFX10_nalDeleteEffects2(JNIEnv *env, jclass clazz, jint n, jint effect) {
	alDeleteEffects(n, (ALuint*)&effect);
}

static jboolean JNICALL Java_org_lwjgl_openal_EFX10_nalIsEffect(JNIEnv *env, jclass clazz, jint effect) {
	ALboolean __result = alIsEffect(effect);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalEffecti(JNIEnv *env, jclass clazz, jint effect, jint param, jint value) {
	alEffecti(effect, param, value);
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalEffectiv(JNIEnv *env, jclass clazz, jint effect, jint param, jlong values) {
	const ALint *values_address = (const ALint *)(intptr_t)values;
	alEffectiv(effect, param, values_address);
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalEffectf(JNIEnv *env, jclass clazz, jint effect, jint param, jfloat value) {
	alEffectf(effect, param, value);
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalEffectfv(JNIEnv *env, jclass clazz, jint effect, jint param, jlong values) {
	const ALfloat *values_address = (const ALfloat *)(intptr_t)values;
	alEffectfv(effect, param, values_address);
}

static jint JNICALL Java_org_lwjgl_openal_EFX10_nalGetEffecti(JNIEnv *env, jclass clazz, jint effect, jint param) {
	ALint __result;
	alGetEffecti(effect, param, &__result);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalGetEffectiv(JNIEnv *env, jclass clazz, jint effect, jint param, jlong intdata) {
	ALint *intdata_address = (ALint *)(intptr_t)intdata;
	alGetEffectiv(effect, param, intdata_address);
}

static jfloat JNICALL Java_org_lwjgl_openal_EFX10_nalGetEffectf(JNIEnv *env, jclass clazz, jint effect, jint param) {
	ALfloat __result;
	alGetEffectf(effect, param, &__result);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalGetEffectfv(JNIEnv *env, jclass clazz, jint effect, jint param, jlong floatdata) {
	ALfloat *floatdata_address = (ALfloat *)(intptr_t)floatdata;
	alGetEffectfv(effect, param, floatdata_address);
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalGenFilters(JNIEnv *env, jclass clazz, jint n, jlong filters) {
	ALuint *filters_address = (ALuint *)(intptr_t)filters;
	alGenFilters(n, filters_address);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_openal_EFX10_nalGenFilters2(JNIEnv *env, jclass clazz, jint n) {
	ALuint __result;
	alGenFilters(n, &__result);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalDeleteFilters(JNIEnv *env, jclass clazz, jint n, jlong filters) {
	ALuint *filters_address = (ALuint *)(intptr_t)filters;
	alDeleteFilters(n, filters_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_openal_EFX10_nalDeleteFilters2(JNIEnv *env, jclass clazz, jint n, jint filter) {
	alDeleteFilters(n, (ALuint*)&filter);
}

static jboolean JNICALL Java_org_lwjgl_openal_EFX10_nalIsFilter(JNIEnv *env, jclass clazz, jint filter) {
	ALboolean __result = alIsFilter(filter);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalFilteri(JNIEnv *env, jclass clazz, jint filter, jint param, jint value) {
	alFilteri(filter, param, value);
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalFilteriv(JNIEnv *env, jclass clazz, jint filter, jint param, jlong values) {
	const ALint *values_address = (const ALint *)(intptr_t)values;
	alFilteriv(filter, param, values_address);
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalFilterf(JNIEnv *env, jclass clazz, jint filter, jint param, jfloat value) {
	alFilterf(filter, param, value);
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalFilterfv(JNIEnv *env, jclass clazz, jint filter, jint param, jlong values) {
	const ALfloat *values_address = (const ALfloat *)(intptr_t)values;
	alFilterfv(filter, param, values_address);
}

static jint JNICALL Java_org_lwjgl_openal_EFX10_nalGetFilteri(JNIEnv *env, jclass clazz, jint filter, jint param) {
	ALint __result;
	alGetFilteri(filter, param, &__result);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalGetFilteriv(JNIEnv *env, jclass clazz, jint filter, jint param, jlong intdata) {
	ALint *intdata_address = (ALint *)(intptr_t)intdata;
	alGetFilteriv(filter, param, intdata_address);
}

static jfloat JNICALL Java_org_lwjgl_openal_EFX10_nalGetFilterf(JNIEnv *env, jclass clazz, jint filter, jint param) {
	ALfloat __result;
	alGetFilterf(filter, param, &__result);
	return __result;
}

static void JNICALL Java_org_lwjgl_openal_EFX10_nalGetFilterfv(JNIEnv *env, jclass clazz, jint filter, jint param, jlong floatdata) {
	ALfloat *floatdata_address = (ALfloat *)(intptr_t)floatdata;
	alGetFilterfv(filter, param, floatdata_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_openal_EFX10_initNativeStubs(JNIEnv *env, jclass clazz) {
	JavaMethodAndExtFunction functions[] = {
		{"nalGenAuxiliaryEffectSlots", "(IJ)V", (void *)&Java_org_lwjgl_openal_EFX10_nalGenAuxiliaryEffectSlots, "alGenAuxiliaryEffectSlots", (void *)&alGenAuxiliaryEffectSlots, false},
		{"nalGenAuxiliaryEffectSlots2", "(I)I", (void *)&Java_org_lwjgl_openal_EFX10_nalGenAuxiliaryEffectSlots2, "alGenAuxiliaryEffectSlots", (void *)&alGenAuxiliaryEffectSlots, false},
		{"nalDeleteAuxiliaryEffectSlots", "(IJ)V", (void *)&Java_org_lwjgl_openal_EFX10_nalDeleteAuxiliaryEffectSlots, "alDeleteAuxiliaryEffectSlots", (void *)&alDeleteAuxiliaryEffectSlots, false},
		{"nalDeleteAuxiliaryEffectSlots2", "(II)V", (void *)&Java_org_lwjgl_openal_EFX10_nalDeleteAuxiliaryEffectSlots2, "alDeleteAuxiliaryEffectSlots", (void *)&alDeleteAuxiliaryEffectSlots, false},
		{"nalIsAuxiliaryEffectSlot", "(I)Z", (void *)&Java_org_lwjgl_openal_EFX10_nalIsAuxiliaryEffectSlot, "alIsAuxiliaryEffectSlot", (void *)&alIsAuxiliaryEffectSlot, false},
		{"nalAuxiliaryEffectSloti", "(III)V", (void *)&Java_org_lwjgl_openal_EFX10_nalAuxiliaryEffectSloti, "alAuxiliaryEffectSloti", (void *)&alAuxiliaryEffectSloti, false},
		{"nalAuxiliaryEffectSlotiv", "(IIJ)V", (void *)&Java_org_lwjgl_openal_EFX10_nalAuxiliaryEffectSlotiv, "alAuxiliaryEffectSlotiv", (void *)&alAuxiliaryEffectSlotiv, false},
		{"nalAuxiliaryEffectSlotf", "(IIF)V", (void *)&Java_org_lwjgl_openal_EFX10_nalAuxiliaryEffectSlotf, "alAuxiliaryEffectSlotf", (void *)&alAuxiliaryEffectSlotf, false},
		{"nalAuxiliaryEffectSlotfv", "(IIJ)V", (void *)&Java_org_lwjgl_openal_EFX10_nalAuxiliaryEffectSlotfv, "alAuxiliaryEffectSlotfv", (void *)&alAuxiliaryEffectSlotfv, false},
		{"nalGetAuxiliaryEffectSloti", "(II)I", (void *)&Java_org_lwjgl_openal_EFX10_nalGetAuxiliaryEffectSloti, "alGetAuxiliaryEffectSloti", (void *)&alGetAuxiliaryEffectSloti, false},
		{"nalGetAuxiliaryEffectSlotiv", "(IIJ)V", (void *)&Java_org_lwjgl_openal_EFX10_nalGetAuxiliaryEffectSlotiv, "alGetAuxiliaryEffectSlotiv", (void *)&alGetAuxiliaryEffectSlotiv, false},
		{"nalGetAuxiliaryEffectSlotf", "(II)F", (void *)&Java_org_lwjgl_openal_EFX10_nalGetAuxiliaryEffectSlotf, "alGetAuxiliaryEffectSlotf", (void *)&alGetAuxiliaryEffectSlotf, false},
		{"nalGetAuxiliaryEffectSlotfv", "(IIJ)V", (void *)&Java_org_lwjgl_openal_EFX10_nalGetAuxiliaryEffectSlotfv, "alGetAuxiliaryEffectSlotfv", (void *)&alGetAuxiliaryEffectSlotfv, false},
		{"nalGenEffects", "(IJ)V", (void *)&Java_org_lwjgl_openal_EFX10_nalGenEffects, "alGenEffects", (void *)&alGenEffects, false},
		{"nalGenEffects2", "(I)I", (void *)&Java_org_lwjgl_openal_EFX10_nalGenEffects2, "alGenEffects", (void *)&alGenEffects, false},
		{"nalDeleteEffects", "(IJ)V", (void *)&Java_org_lwjgl_openal_EFX10_nalDeleteEffects, "alDeleteEffects", (void *)&alDeleteEffects, false},
		{"nalDeleteEffects2", "(II)V", (void *)&Java_org_lwjgl_openal_EFX10_nalDeleteEffects2, "alDeleteEffects", (void *)&alDeleteEffects, false},
		{"nalIsEffect", "(I)Z", (void *)&Java_org_lwjgl_openal_EFX10_nalIsEffect, "alIsEffect", (void *)&alIsEffect, false},
		{"nalEffecti", "(III)V", (void *)&Java_org_lwjgl_openal_EFX10_nalEffecti, "alEffecti", (void *)&alEffecti, false},
		{"nalEffectiv", "(IIJ)V", (void *)&Java_org_lwjgl_openal_EFX10_nalEffectiv, "alEffectiv", (void *)&alEffectiv, false},
		{"nalEffectf", "(IIF)V", (void *)&Java_org_lwjgl_openal_EFX10_nalEffectf, "alEffectf", (void *)&alEffectf, false},
		{"nalEffectfv", "(IIJ)V", (void *)&Java_org_lwjgl_openal_EFX10_nalEffectfv, "alEffectfv", (void *)&alEffectfv, false},
		{"nalGetEffecti", "(II)I", (void *)&Java_org_lwjgl_openal_EFX10_nalGetEffecti, "alGetEffecti", (void *)&alGetEffecti, false},
		{"nalGetEffectiv", "(IIJ)V", (void *)&Java_org_lwjgl_openal_EFX10_nalGetEffectiv, "alGetEffectiv", (void *)&alGetEffectiv, false},
		{"nalGetEffectf", "(II)F", (void *)&Java_org_lwjgl_openal_EFX10_nalGetEffectf, "alGetEffectf", (void *)&alGetEffectf, false},
		{"nalGetEffectfv", "(IIJ)V", (void *)&Java_org_lwjgl_openal_EFX10_nalGetEffectfv, "alGetEffectfv", (void *)&alGetEffectfv, false},
		{"nalGenFilters", "(IJ)V", (void *)&Java_org_lwjgl_openal_EFX10_nalGenFilters, "alGenFilters", (void *)&alGenFilters, false},
		{"nalGenFilters2", "(I)I", (void *)&Java_org_lwjgl_openal_EFX10_nalGenFilters2, "alGenFilters", (void *)&alGenFilters, false},
		{"nalDeleteFilters", "(IJ)V", (void *)&Java_org_lwjgl_openal_EFX10_nalDeleteFilters, "alDeleteFilters", (void *)&alDeleteFilters, false},
		{"nalDeleteFilters2", "(II)V", (void *)&Java_org_lwjgl_openal_EFX10_nalDeleteFilters2, "alDeleteFilters", (void *)&alDeleteFilters, false},
		{"nalIsFilter", "(I)Z", (void *)&Java_org_lwjgl_openal_EFX10_nalIsFilter, "alIsFilter", (void *)&alIsFilter, false},
		{"nalFilteri", "(III)V", (void *)&Java_org_lwjgl_openal_EFX10_nalFilteri, "alFilteri", (void *)&alFilteri, false},
		{"nalFilteriv", "(IIJ)V", (void *)&Java_org_lwjgl_openal_EFX10_nalFilteriv, "alFilteriv", (void *)&alFilteriv, false},
		{"nalFilterf", "(IIF)V", (void *)&Java_org_lwjgl_openal_EFX10_nalFilterf, "alFilterf", (void *)&alFilterf, false},
		{"nalFilterfv", "(IIJ)V", (void *)&Java_org_lwjgl_openal_EFX10_nalFilterfv, "alFilterfv", (void *)&alFilterfv, false},
		{"nalGetFilteri", "(II)I", (void *)&Java_org_lwjgl_openal_EFX10_nalGetFilteri, "alGetFilteri", (void *)&alGetFilteri, false},
		{"nalGetFilteriv", "(IIJ)V", (void *)&Java_org_lwjgl_openal_EFX10_nalGetFilteriv, "alGetFilteriv", (void *)&alGetFilteriv, false},
		{"nalGetFilterf", "(II)F", (void *)&Java_org_lwjgl_openal_EFX10_nalGetFilterf, "alGetFilterf", (void *)&alGetFilterf, false},
		{"nalGetFilterfv", "(IIJ)V", (void *)&Java_org_lwjgl_openal_EFX10_nalGetFilterfv, "alGetFilterfv", (void *)&alGetFilterfv, false}
	};
	int num_functions = NUMFUNCTIONS(functions);
	extal_InitializeClass(env, clazz, num_functions, functions);
}

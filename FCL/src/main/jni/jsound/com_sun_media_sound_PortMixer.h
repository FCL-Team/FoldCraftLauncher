/* libjsound 构建所需的 JNI 声明，按 vendor 的 .c 定义生成（替代 javah 产物） */
#ifndef _Included_com_sun_media_sound_PortMixer
#define _Included_com_sun_media_sound_PortMixer
#ifdef __cplusplus
extern "C" {
#endif
#include <jni.h>

JNIEXPORT jlong JNICALL Java_com_sun_media_sound_PortMixer_nOpen(JNIEnv *env, jclass cls, jint mixerIndex);
JNIEXPORT void JNICALL Java_com_sun_media_sound_PortMixer_nClose(JNIEnv *env, jclass cls, jlong id);
JNIEXPORT jint JNICALL Java_com_sun_media_sound_PortMixer_nGetPortCount(JNIEnv *env, jclass cls, jlong id);
JNIEXPORT jint JNICALL Java_com_sun_media_sound_PortMixer_nGetPortType(JNIEnv *env, jclass cls, jlong id, jint portIndex);
JNIEXPORT jstring JNICALL Java_com_sun_media_sound_PortMixer_nGetPortName(JNIEnv *env, jclass cls, jlong id, jint portIndex);
JNIEXPORT void JNICALL Java_com_sun_media_sound_PortMixer_nControlSetIntValue(JNIEnv *env, jclass cls, jlong controlID, jint value);
JNIEXPORT jint JNICALL Java_com_sun_media_sound_PortMixer_nControlGetIntValue(JNIEnv *env, jclass cls, jlong controlID);
JNIEXPORT void JNICALL Java_com_sun_media_sound_PortMixer_nControlSetFloatValue(JNIEnv *env, jclass cls, jlong controlID, jfloat value);
JNIEXPORT jfloat JNICALL Java_com_sun_media_sound_PortMixer_nControlGetFloatValue(JNIEnv *env, jclass cls, jlong controlID);
JNIEXPORT void JNICALL Java_com_sun_media_sound_PortMixer_nGetControls(JNIEnv *env, jclass cls, jlong id, jint portIndex, jobject vector);

#ifdef __cplusplus
}
#endif
#endif

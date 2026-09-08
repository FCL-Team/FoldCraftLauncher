/* libjsound 构建所需的 JNI 声明，按 vendor 的 .c 定义生成（替代 javah 产物） */
#ifndef _Included_com_sun_media_sound_MidiOutDeviceProvider
#define _Included_com_sun_media_sound_MidiOutDeviceProvider
#ifdef __cplusplus
extern "C" {
#endif
#include <jni.h>

JNIEXPORT jint JNICALL Java_com_sun_media_sound_MidiOutDeviceProvider_nGetNumDevices(JNIEnv* e, jobject thisObj);
JNIEXPORT jstring JNICALL Java_com_sun_media_sound_MidiOutDeviceProvider_nGetName(JNIEnv* e, jobject thisObj, jint index);
JNIEXPORT jstring JNICALL Java_com_sun_media_sound_MidiOutDeviceProvider_nGetVendor(JNIEnv* e, jobject thisObj, jint index);
JNIEXPORT jstring JNICALL Java_com_sun_media_sound_MidiOutDeviceProvider_nGetDescription(JNIEnv* e, jobject thisObj, jint index);
JNIEXPORT jstring JNICALL Java_com_sun_media_sound_MidiOutDeviceProvider_nGetVersion(JNIEnv* e, jobject thisObj, jint index);

#ifdef __cplusplus
}
#endif
#endif

/* libjsound 构建所需的 JNI 声明，按 vendor 的 .c 定义生成（替代 javah 产物） */
#ifndef _Included_com_sun_media_sound_MidiInDevice
#define _Included_com_sun_media_sound_MidiInDevice
#ifdef __cplusplus
extern "C" {
#endif
#include <jni.h>

JNIEXPORT jlong JNICALL Java_com_sun_media_sound_MidiInDevice_nOpen(JNIEnv* e, jobject thisObj, jint index);
JNIEXPORT void JNICALL Java_com_sun_media_sound_MidiInDevice_nClose(JNIEnv* e, jobject thisObj, jlong deviceHandle);
JNIEXPORT void JNICALL Java_com_sun_media_sound_MidiInDevice_nStart(JNIEnv* e, jobject thisObj, jlong deviceHandle);
JNIEXPORT void JNICALL Java_com_sun_media_sound_MidiInDevice_nStop(JNIEnv* e, jobject thisObj, jlong deviceHandle);
JNIEXPORT jlong JNICALL Java_com_sun_media_sound_MidiInDevice_nGetTimeStamp(JNIEnv* e, jobject thisObj, jlong deviceHandle);
JNIEXPORT void JNICALL Java_com_sun_media_sound_MidiInDevice_nGetMessages(JNIEnv* e, jobject thisObj, jlong deviceHandle);

#ifdef __cplusplus
}
#endif
#endif

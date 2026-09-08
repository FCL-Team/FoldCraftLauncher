/* libjsound 构建所需的 JNI 声明，按 vendor 的 .c 定义生成（替代 javah 产物） */
#ifndef _Included_com_sun_media_sound_MidiOutDevice
#define _Included_com_sun_media_sound_MidiOutDevice
#ifdef __cplusplus
extern "C" {
#endif
#include <jni.h>

JNIEXPORT jlong JNICALL Java_com_sun_media_sound_MidiOutDevice_nOpen(JNIEnv* e, jobject thisObj, jint index);
JNIEXPORT void JNICALL Java_com_sun_media_sound_MidiOutDevice_nClose(JNIEnv* e, jobject thisObj, jlong deviceHandle);
JNIEXPORT jlong JNICALL Java_com_sun_media_sound_MidiOutDevice_nGetTimeStamp(JNIEnv* e, jobject thisObj, jlong deviceHandle);
JNIEXPORT void JNICALL Java_com_sun_media_sound_MidiOutDevice_nSendShortMessage(JNIEnv* e, jobject thisObj, jlong deviceHandle, jint packedMsg, jlong timeStamp);
JNIEXPORT void JNICALL Java_com_sun_media_sound_MidiOutDevice_nSendLongMessage(JNIEnv* e, jobject thisObj, jlong deviceHandle, jbyteArray jData, jint size, jlong timeStamp);

#ifdef __cplusplus
}
#endif
#endif

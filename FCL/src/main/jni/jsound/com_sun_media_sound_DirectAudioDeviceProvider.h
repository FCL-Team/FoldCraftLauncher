/* libjsound 构建所需的 JNI 声明，按 vendor 的 .c 定义生成（替代 javah 产物） */
#ifndef _Included_com_sun_media_sound_DirectAudioDeviceProvider
#define _Included_com_sun_media_sound_DirectAudioDeviceProvider
#ifdef __cplusplus
extern "C" {
#endif
#include <jni.h>

JNIEXPORT jint JNICALL Java_com_sun_media_sound_DirectAudioDeviceProvider_nGetNumDevices(JNIEnv *env, jclass cls);
JNIEXPORT jobject JNICALL Java_com_sun_media_sound_DirectAudioDeviceProvider_nNewDirectAudioDeviceInfo(JNIEnv *env, jclass cls, jint mixerIndex);

#ifdef __cplusplus
}
#endif
#endif

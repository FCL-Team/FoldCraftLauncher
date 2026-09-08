/* libjsound 构建所需的 JNI 声明，按 vendor 的 .c 定义生成（替代 javah 产物） */
#ifndef _Included_com_sun_media_sound_DirectAudioDevice
#define _Included_com_sun_media_sound_DirectAudioDevice
#ifdef __cplusplus
extern "C" {
#endif
#include <jni.h>

JNIEXPORT void JNICALL Java_com_sun_media_sound_DirectAudioDevice_nGetFormats(JNIEnv *env, jclass clazz, jint mixerIndex, jint deviceID, jboolean isSource, jobject formats);
JNIEXPORT jlong JNICALL Java_com_sun_media_sound_DirectAudioDevice_nOpen(JNIEnv* env, jclass clazz, jint mixerIndex, jint deviceID, jboolean isSource, jint encoding, jfloat sampleRate, jint sampleSizeInBits, jint frameSize, jint channels, jboolean isSigned, jboolean isBigendian, jint bufferSizeInBytes);
JNIEXPORT void JNICALL Java_com_sun_media_sound_DirectAudioDevice_nStart(JNIEnv* env, jclass clazz, jlong id, jboolean isSource);
JNIEXPORT void JNICALL Java_com_sun_media_sound_DirectAudioDevice_nStop(JNIEnv* env, jclass clazz, jlong id, jboolean isSource);
JNIEXPORT void JNICALL Java_com_sun_media_sound_DirectAudioDevice_nClose(JNIEnv* env, jclass clazz, jlong id, jboolean isSource);
JNIEXPORT jint JNICALL Java_com_sun_media_sound_DirectAudioDevice_nWrite(JNIEnv *env, jclass clazz, jlong id, jbyteArray jData, jint offset, jint len, jint conversionSize, jfloat leftGain, jfloat rightGain);
JNIEXPORT jint JNICALL Java_com_sun_media_sound_DirectAudioDevice_nRead(JNIEnv* env, jclass clazz, jlong id, jbyteArray jData, jint offset, jint len, jint conversionSize);
JNIEXPORT jint JNICALL Java_com_sun_media_sound_DirectAudioDevice_nGetBufferSize(JNIEnv* env, jclass clazz, jlong id, jboolean isSource);
JNIEXPORT jboolean JNICALL Java_com_sun_media_sound_DirectAudioDevice_nIsStillDraining(JNIEnv* env, jclass clazz, jlong id, jboolean isSource);
JNIEXPORT void JNICALL Java_com_sun_media_sound_DirectAudioDevice_nFlush(JNIEnv* env, jclass clazz, jlong id, jboolean isSource);
JNIEXPORT jint JNICALL Java_com_sun_media_sound_DirectAudioDevice_nAvailable(JNIEnv* env, jclass clazz, jlong id, jboolean isSource);
JNIEXPORT jlong JNICALL Java_com_sun_media_sound_DirectAudioDevice_nGetBytePosition(JNIEnv* env, jclass clazz, jlong id, jboolean isSource, jlong javaBytePos);
JNIEXPORT void JNICALL Java_com_sun_media_sound_DirectAudioDevice_nSetBytePosition(JNIEnv* env, jclass clazz, jlong id, jboolean isSource, jlong pos);
JNIEXPORT jboolean JNICALL Java_com_sun_media_sound_DirectAudioDevice_nRequiresServicing(JNIEnv* env, jclass clazz, jlong id, jboolean isSource);
JNIEXPORT void JNICALL Java_com_sun_media_sound_DirectAudioDevice_nService(JNIEnv* env, jclass clazz, jlong id, jboolean isSource);

#ifdef __cplusplus
}
#endif
#endif

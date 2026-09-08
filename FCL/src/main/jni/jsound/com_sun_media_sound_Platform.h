/* libjsound 构建所需的 JNI 声明，按 vendor 的 .c 定义生成（替代 javah 产物） */
#ifndef _Included_com_sun_media_sound_Platform
#define _Included_com_sun_media_sound_Platform
#ifdef __cplusplus
extern "C" {
#endif
#include <jni.h>

JNIEXPORT jboolean JNICALL Java_com_sun_media_sound_Platform_nIsBigEndian(JNIEnv *env, jclass clss);

#ifdef __cplusplus
}
#endif
#endif

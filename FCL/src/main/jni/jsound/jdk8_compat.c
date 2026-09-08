/*
 * JDK8 兼容层：com.sun.media.sound.Platform 在 JDK8 上比 9+ 多三个 native，
 * 由 Platform 的静态初始化（readProperties/loadLibraries）直接调用，缺失即
 * UnsatisfiedLinkError 并导致整个 Java Sound 不可用。语义对齐 jdk8u 实现
 * （https://github.com/openjdk/jdk8u/blob/master/jdk/src/share/native/com/sun/media/sound/Platform.c）。
 */
#include <jni.h>

/* 仅 SPARC 平台返回 TRUE，其余恒 FALSE */
JNIEXPORT jboolean JNICALL Java_com_sun_media_sound_Platform_nIsSigned8(JNIEnv *env, jclass clss) {
    return JNI_FALSE;
}

/* 主库之外需要附加加载的音频库列表，本实现没有 */
JNIEXPORT jstring JNICALL Java_com_sun_media_sound_Platform_nGetExtraLibraries(JNIEnv *env, jclass clss) {
    return (*env)->NewStringUTF(env, "");
}

/* 特性所在库编号：三个特性全部落在主库（LIB_MAIN = 1），令 JDK8 侧的
 * Provider 特性门控放行；PORT/MIDI 子系统由 USE_*=FALSE 的桩返回 0 设备。 */
JNIEXPORT jint JNICALL Java_com_sun_media_sound_Platform_nGetLibraryForFeature(JNIEnv *env, jclass clss, jint feature) {
    return 1;
}

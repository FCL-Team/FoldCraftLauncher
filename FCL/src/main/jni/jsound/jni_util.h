/*
 * jni_util.h 的最小桩：libjsound 核心（vendor 自 OpenJDK）仅用到以下宏。
 * 原文件位于 OpenJDK java.base（https://github.com/openjdk/jdk17u/blob/master/src/java.base/share/native/libjava/jni_util.h），
 * 动态链接构建下只需这几个空实现宏。
 */
#ifndef FCL_JSOUND_JNI_UTIL_H
#define FCL_JSOUND_JNI_UTIL_H

#define CHECK_NULL(x) do { if ((x) == NULL) return; } while (0)

#define CHECK_NULL_RETURN(x, y) do { if ((x) == NULL) return (y); } while (0)

/* 静态链接构建专用的 JNI_OnLoad 声明宏，动态链接下为空 */
#define DEF_STATIC_JNI_OnLoad

#endif

//
// Created by maks on 23.01.2025.
//

#ifndef POJAVLAUNCHER_JVM_HOOKS_H
#define POJAVLAUNCHER_JVM_HOOKS_H

#include <jni.h>

void installEMUIIteratorMititgation(JNIEnv *env);
void installLwjglDlopenHook(JNIEnv *env);
void hookExec(JNIEnv *env);

#endif //POJAVLAUNCHER_JVM_HOOKS_H

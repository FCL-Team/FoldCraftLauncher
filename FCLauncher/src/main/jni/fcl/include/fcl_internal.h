//
// Created by Tungsten on 2022/10/11.
//

#ifndef FOLD_CRAFT_LAUNCHER_FCL_INTERNAL_H
#define FOLD_CRAFT_LAUNCHER_FCL_INTERNAL_H

#include <stdlib.h>
#include <pthread.h>
#include <jni.h>
#include <string.h>
#include <unistd.h>
#include <sys/epoll.h>
#include <errno.h>
#include <android/native_window.h>

#include "fcl_bridge.h"
#include "fcl_keycodes.h"
////==============only for boat=============
typedef struct _QueueElement {
    struct _QueueElement* next;
    FCLEvent event;
} QueueElement;

typedef struct {
    int count;
    int capacity;
    QueueElement* head;
    QueueElement* tail;
} EventQueue;
//==============only for boat=============

struct FCLInternal {
    JavaVM* android_jvm;
    jclass class_FCLBridge;
    jobject object_FCLBridge;
    ANativeWindow* window;
    char* clipboard_string;
    FILE* logFile;
    //only for boat
    EventQueue event_queue;
    pthread_mutex_t event_queue_mutex;
    int has_event_pipe;
    int event_pipe_fd[2];
    int epoll_fd;
    int fps;
};

extern struct FCLInternal *fcl;

_Noreturn void nominal_exit(int code);

#define FCL_INTERNAL_LOG(x...) do { \
    fprintf(fcl->logFile, "[FCL Internal] %s:%d\n", __FILE__, __LINE__); \
    fprintf(fcl->logFile, x); \
    fprintf(fcl->logFile, "\n"); \
    fflush(fcl->logFile); \
    } while (0)

#define FCL_LOG(x...) do { \
    fprintf(fcl->logFile, x); \
    fprintf(fcl->logFile, "\n"); \
    fflush(fcl->logFile); \
    } while (0)

#define PrepareFCLBridgeJNI() \
    JavaVM* vm = fcl->android_jvm; \
    JNIEnv* env = NULL; \
    jint attached = (*vm)->GetEnv(vm, (void**)&env, JNI_VERSION_1_2); \
    if (attached == JNI_EDETACHED) { \
        attached = (*vm)->AttachCurrentThread(vm, &env, NULL); \
        if (attached != JNI_OK || env == NULL) { \
            FCL_INTERNAL_LOG("Failed to attach thread to Android JavaVM."); \
        } \
    } \
    do {} while(0)

#define CallFCLBridgeJNIFunc(return_exp, func_type, func_name, signature, args...) \
    jmethodID FCLBridge_##func_name = (*env)->GetMethodID(env, fcl->class_FCLBridge, #func_name, signature); \
    if (FCLBridge_##func_name == NULL) { \
        FCL_INTERNAL_LOG("Failed to find method FCLBridge_"#func_name ); \
    } \
    return_exp (*env)->Call##func_type##Method(env, fcl->object_FCLBridge, FCLBridge_##func_name, ##args); \
    do {} while(0)

#endif //FOLD_CRAFT_LAUNCHER_FCL_INTERNAL_H

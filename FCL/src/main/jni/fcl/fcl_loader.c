//
// Created by Tungsten on 2022/10/12.
//

#include <fcntl.h>
#include <unistd.h>
#include <jni.h>
#include <signal.h>
#include <stdbool.h>
#include <stdlib.h>
#include <assert.h>
#include <android/log.h>
#include <bytehook.h>
#include <string.h>
#include "log.h"
#include <sys/mman.h>
#include <pthread.h>

// exitTrap 的全局引用：每阶段 setupExitTrap 重设，旧引用经原子交换后删除；
// 该变量只在 native 线程与退出线程间共享，读写统一走 __atomic_* 内建
static jobject exitTrap_bridge;
static volatile jmethodID exitTrap_method;
static JavaVM *exitTrap_jvm;
static volatile jmethodID log_method;
static JavaVM *log_pipe_jvm;
// 当前 stdio 重定向管道的原始写端：保留（不 close），下一轮重定向关闭它时旧管道
// 再无任何写端，阻塞中的旧 logger 经 EOF 自然退出（同进程多轮 JVM 的安装器复用场景）
static volatile int fclLogWriteFd = -1;

// logger 线程的每轮参数：读端与回调引用均由线程退出时自行释放，不进全局
typedef struct {
    int readFd;
    jobject bridge;
} log_thread_arg;

void correctUtfBytes(char *bytes) {
    char three = 0;
    while (*bytes != '\0') {
        unsigned char utf8 = *(bytes++);
        three = 0;
        // Switch on the high four bits.
        switch (utf8 >> 4) {
            case 0x00:
            case 0x01:
            case 0x02:
            case 0x03:
            case 0x04:
            case 0x05:
            case 0x06:
            case 0x07:
                // Bit pattern 0xxx. No need for any extra bytes.
                break;
            case 0x08:
            case 0x09:
            case 0x0a:
            case 0x0b:
            case 0x0f:
                /*
                 * Bit pattern 10xx or 1111, which are illegal start bytes.
                 * Note: 1111 is valid for normal UTF-8, but not the
                 * modified UTF-8 used here.
                 */
                *(bytes - 1) = '?';
                break;
            case 0x0e:
                // Bit pattern 1110, so there are two additional bytes.
                utf8 = *(bytes++);
                if ((utf8 & 0xc0) != 0x80) {
                    --bytes;
                    *(bytes - 1) = '?';
                    break;
                }
                three = 1;
                // Fall through to take care of the final byte.
            case 0x0c:
            case 0x0d:
                // Bit pattern 110x, so there is one additional byte.
                utf8 = *(bytes++);
                if ((utf8 & 0xc0) != 0x80) {
                    --bytes;
                    if (three)--bytes;
                    *(bytes - 1) = '?';
                }
                break;
        }
    }
}

static void *logger_thread(void *arg) {
    log_thread_arg *data = arg;
    JavaVM *vm = log_pipe_jvm;
    if (vm == NULL) {
        close(data->readFd);
        free(data);
        return NULL;
    }
    JNIEnv *env;
    if ((*vm)->AttachCurrentThread(vm, &env, NULL) != JNI_OK || env == NULL) {
        // 未能 attach 就无法删除 bridge 全局引用，只能释放读端与参数
        __android_log_print(ANDROID_LOG_ERROR, "FCL", "Failed to attach log thread!");
        close(data->readFd);
        free(data);
        return NULL;
    }
    char buffer[2048];
    ssize_t _s;
    jstring str;
    while (1) {
        memset(buffer, '\0', sizeof(buffer));
        _s = read(data->readFd, buffer, sizeof(buffer) - 1);
        if (_s <= 0) {
            // 管道 EOF（写端全部关闭）或读错误：退出本线程。
            // 旧实现在 EOF 时 continue 会空转烧 CPU，复用场景下必须退出
            if (_s < 0) {
                __android_log_print(ANDROID_LOG_ERROR, "FCL", "Failed to read log!");
            }
            close(data->readFd);
            // 全部 JNI 调用必须先于 DetachCurrentThread：脱离后旧 env 失效，
            // 再删全局引用会触发 CheckJNI 的 CheckAttachedThread abort 整个进程
            (*env)->DeleteGlobalRef(env, data->bridge);
            free(data);
            (*vm)->DetachCurrentThread(vm);
            return NULL;
        }
        buffer[_s] = '\0';
        if (buffer[0] == '\0')
            continue;
        else {
            //fix "input is not valid Modified UTF-8" caused by NewStringUTF
            correctUtfBytes(buffer);
            str = (*env)->NewStringUTF(env, buffer);
            (*env)->CallVoidMethod(env, data->bridge, log_method, str);
            (*env)->DeleteLocalRef(env, str);
        }
    }
}

JNIEXPORT jint JNICALL
Java_com_tungsten_fclauncher_bridge_FCLBridge_redirectStdio(JNIEnv *env, jobject jobject,
                                                            jstring path) {
    setvbuf(stdout, 0, _IOLBF, 0);
    setvbuf(stderr, 0, _IONBF, 0);
    int fds[2];
    if (pipe(fds) < 0) {
        __android_log_print(ANDROID_LOG_ERROR, "FCL", "Failed to create log pipe!");
        return 1;
    }
    log_thread_arg *arg = malloc(sizeof(log_thread_arg));
    if (arg == NULL) {
        close(fds[0]);
        close(fds[1]);
        return 1;
    }
    arg->readFd = fds[0];
    arg->bridge = (*env)->NewGlobalRef(env, jobject);
    if (arg->bridge == NULL) {
        free(arg);
        close(fds[0]);
        close(fds[1]);
        return 1;
    }
    // 先解析回调再重定向：失败时 stdout/stderr 尚未改投，无需清理已生效的管道
    jclass bridgeClass = (*env)->FindClass(env, "com/tungsten/fclauncher/bridge/FCLBridge");
    jmethodID receiveLog = bridgeClass == NULL ? NULL
                                               : (*env)->GetMethodID(env, bridgeClass,
                                                                     "receiveLog",
                                                                     "(Ljava/lang/String;)V");
    if (receiveLog == NULL) {
        __android_log_print(ANDROID_LOG_ERROR, "FCL", "Failed to find receive method!");
        (*env)->ExceptionClear(env);
        (*env)->DeleteGlobalRef(env, arg->bridge);
        free(arg);
        close(fds[0]);
        close(fds[1]);
        return 4;
    }
    log_method = receiveLog;
    (*env)->DeleteLocalRef(env, bridgeClass);
    if (dup2(fds[1], STDOUT_FILENO) != STDOUT_FILENO ||
        dup2(fds[1], STDERR_FILENO) != STDERR_FILENO) {
        __android_log_print(ANDROID_LOG_ERROR, "FCL", "failed to redirect stdio!");
        (*env)->DeleteGlobalRef(env, arg->bridge);
        free(arg);
        close(fds[0]);
        close(fds[1]);
        return 2;
    }
    JavaVM *vm = NULL;
    if ((*env)->GetJavaVM(env, &vm) != JNI_OK || vm == NULL) {
        __android_log_print(ANDROID_LOG_ERROR, "FCL", "Failed to get JavaVM for log pipe!");
        (*env)->DeleteGlobalRef(env, arg->bridge);
        free(arg);
        close(fds[0]);
        close(fds[1]);
        return 6;
    }
    log_pipe_jvm = vm;
    int oldWrite = fclLogWriteFd;
    fclLogWriteFd = fds[1];
    FCL_LOG("Log pipe ready.");
    // 回收上一轮 logger：stdout/stderr 已改投新管道，关闭旧管道最后一个写端，
    // 阻塞中的旧 logger 经 EOF 自然退出并自行释放读端与回调引用——避免两线程同读
    // 一条管道交错分发。不写唤醒字节（EOF 足以唤醒阻塞读），也不从本线程关旧读端：
    // close 不唤醒阻塞中的 read，fd 号被后续轮次复用会重新引入双读者竞态
    if (oldWrite >= 0) {
        close(oldWrite);
    }
    pthread_t logger;
    int result = pthread_create(&logger, 0, logger_thread, arg);
    if (result != 0) {
        // 重定向已生效无法回滚：归还读端并恢复写端记录，写端关闭后 stdout 退化为 EPIPE
        fclLogWriteFd = oldWrite;
        close(fds[0]);
        close(fds[1]);
        (*env)->DeleteGlobalRef(env, arg->bridge);
        free(arg);
        return 5;
    }
    pthread_detach(logger);
    return 0;
}

JNIEXPORT void JNICALL
Java_com_tungsten_fclauncher_bridge_FCLBridge_setenv(JNIEnv *env, jobject jobject, jstring str1,
                                                     jstring str2) {
    char const *name = (*env)->GetStringUTFChars(env, str1, 0);
    char const *value = (*env)->GetStringUTFChars(env, str2, 0);

    setenv(name, value, 1);

    (*env)->ReleaseStringUTFChars(env, str1, name);
    (*env)->ReleaseStringUTFChars(env, str2, value);
}

_Noreturn void nominal_exit(int code) {
    // System.exit(0) 会经 libc exit 再次触发 atexit → nominal_exit：第一次执行时
    // 已删除过全局引用，重复进入会对已释放引用做 JNI 调用并撞 CheckJNI。用原子交换
    // 保证退出流程只执行一次，重入直接终止进程
    static bool nominal_exit_entered = false;
    if (__atomic_exchange_n(&nominal_exit_entered, true, __ATOMIC_SEQ_CST)) {
        _exit(0);
    }
    JNIEnv *env;
    jint errorCode = (*exitTrap_jvm)->GetEnv(exitTrap_jvm, (void **) &env, JNI_VERSION_1_6);
    if (errorCode == JNI_EDETACHED) {
        errorCode = (*exitTrap_jvm)->AttachCurrentThread(exitTrap_jvm, &env, NULL);
    }
    if (errorCode != JNI_OK) {
        // Step on a landmine and die, since we can't invoke the Dalvik exit without attaching to
        // Dalvik.
        // I mean, if Zygote can do that, why can't I?
        killpg(getpgrp(), SIGTERM);
        _exit(1);
    }
    jobject trapBridge = __atomic_load_n(&exitTrap_bridge, __ATOMIC_SEQ_CST);
    if (trapBridge != NULL) {
        // 退出码 0 也必须回调：安装器阶段 main 内调用 System.exit(0) 时，
        // 主进程只有收到退出码文件才会开始下一阶段
        (*env)->CallVoidMethod(env, trapBridge, exitTrap_method, code);
        // Delete the reference, not gonna need 'em later anyway
        (*env)->DeleteGlobalRef(env, trapBridge);
        __atomic_store_n(&exitTrap_bridge, NULL, __ATOMIC_SEQ_CST);
    }

    // A hat trick, if you will
    // Call the Android System.exit() to perform Android's shutdown hooks and do a
    // fully clean exit.
    // After doing this, either of these will happen:
    // 1. Runtime calls exit() for real and it will be handled by ByteHook's recurse handler
    // and redirected back to the OS
    // 2. Zygote sends SIGTERM (no handling necessary, the process perishes)
    // 3. A different thread calls exit() and the hook will go through the exit_tripped path
    jclass systemClass = (*env)->FindClass(env, "java/lang/System");
    jmethodID exitMethod = (*env)->GetStaticMethodID(env, systemClass, "exit", "(I)V");
    (*env)->CallStaticVoidMethod(env, systemClass, exitMethod, 0);
    // System.exit() should not ever return, but the compiler doesn't know about that
    // so put a while loop here
    while (1) {}
}

JNIEXPORT void JNICALL
Java_com_tungsten_fclauncher_bridge_FCLBridge_setupExitTrap(JNIEnv *env, jobject jobject1,
                                                            jobject bridge) {
    jclass exitTrapClass = (*env)->FindClass(env, "com/tungsten/fclauncher/bridge/FCLBridge");
    if (exitTrapClass == NULL) {
        (*env)->ExceptionClear(env);
        __android_log_print(ANDROID_LOG_ERROR, "FCL", "Failed to find FCLBridge for exit trap!");
        return;
    }
    jmethodID onExitMethod = (*env)->GetMethodID(env, exitTrapClass, "onExit", "(I)V");
    if (onExitMethod == NULL) {
        (*env)->ExceptionClear(env);
        __android_log_print(ANDROID_LOG_ERROR, "FCL", "Failed to find onExit method!");
        (*env)->DeleteLocalRef(env, exitTrapClass);
        return;
    }
    jobject newRef = (*env)->NewGlobalRef(env, bridge);
    if (newRef == NULL) {
        __android_log_print(ANDROID_LOG_ERROR, "FCL", "Failed to create exit trap reference!");
        (*env)->DeleteLocalRef(env, exitTrapClass);
        return;
    }
    // 每阶段都会重设：旧全局引用经原子交换取出后删除，避免多阶段累积泄漏
    jobject oldRef = __atomic_exchange_n(&exitTrap_bridge, newRef, __ATOMIC_SEQ_CST);
    if (oldRef != NULL && oldRef != newRef) {
        (*env)->DeleteGlobalRef(env, oldRef);
    }
    (*env)->GetJavaVM(env, &exitTrap_jvm);
    exitTrap_method = onExitMethod;
    (*env)->DeleteLocalRef(env, exitTrapClass);
}
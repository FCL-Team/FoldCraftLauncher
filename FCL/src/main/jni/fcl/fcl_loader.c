//
// Created by Tungsten on 2022/10/12.
//

#include <fcntl.h>
#include <unistd.h>
#include <jni.h>
#include <stdlib.h>
#include <assert.h>
#include <android/log.h>
#include <bytehook.h>
#include <string.h>
#include "log.h"
#include <sys/mman.h>
#include <pthread.h>

static volatile jobject exitTrap_bridge;
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
    JNIEnv *env;
    JavaVM *vm = log_pipe_jvm;
    (*vm)->AttachCurrentThread(vm, &env, NULL);
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
    if (dup2(fds[1], STDOUT_FILENO) != STDOUT_FILENO ||
        dup2(fds[1], STDERR_FILENO) != STDERR_FILENO) {
        __android_log_print(ANDROID_LOG_ERROR, "FCL", "failed to redirect stdio!");
        (*env)->DeleteGlobalRef(env, arg->bridge);
        free(arg);
        close(fds[0]);
        close(fds[1]);
        return 2;
    }
    int oldWrite = fclLogWriteFd;
    fclLogWriteFd = fds[1];
    jclass bridge = (*env)->FindClass(env, "com/tungsten/fclauncher/bridge/FCLBridge");
    log_method = (*env)->GetMethodID(env, bridge, "receiveLog", "(Ljava/lang/String;)V");
    if (!log_method) {
        __android_log_print(ANDROID_LOG_ERROR, "FCL", "Failed to find receive method!");
        return 4;
    }
    FCL_LOG("Log pipe ready.");
    (*env)->GetJavaVM(env, &log_pipe_jvm);
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
    }
    if (code != 0) {
        // Exit code 0 is pretty established as "eh it's fine"
        // so only open the GUI if the code is != 0
        (*env)->CallVoidMethod(env, exitTrap_bridge, exitTrap_method, code);
    }
    // Delete the reference, not gonna need 'em later anyway
    (*env)->DeleteGlobalRef(env, exitTrap_bridge);

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
    exitTrap_bridge = (*env)->NewGlobalRef(env, bridge);
    (*env)->GetJavaVM(env, &exitTrap_jvm);
    jclass exitTrap_exitClass = (*env)->NewGlobalRef(env, (*env)->FindClass(env,
                                                                            "com/tungsten/fclauncher/bridge/FCLBridge"));
    exitTrap_method = (*env)->GetMethodID(env, exitTrap_exitClass, "onExit", "(I)V");
    (*env)->DeleteGlobalRef(env, exitTrap_exitClass);
}
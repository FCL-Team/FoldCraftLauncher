//
// JVM 直启入口：不再经 JLI_Launch 让 Oracle launcher 代管生命周期，直接 dlsym
// JNI_CreateJavaVM 引导游戏 JVM，并 CallStaticVoidMethod 调用主类。
// 退出语义通过 JVM 调用 API 的 "exit"/"abort" option hook 接管：hook 记录退出码后
// 原地停车（System.exit 永不返回到游戏代码，进程终止权收归启动器），由 futex
// waiter 线程经 nominal_exit 走 ART 侧 FCLBridge.onExit 通知启动器——与旧
// bytehook exit 拦截/SIGABRT pipe waiter 链路同终点，但不再依赖 PLT hook。
// 被替代的原启动方式：参考 PojavLauncher（https://github.com/PojavLauncherTeam/PojavLauncher/blob/v3_openjdk/app_pojavlauncher/src/main/jni/jre_launcher.c）
//

#include <dlfcn.h>
#include <errno.h>
#include <jni.h>
#include <linux/futex.h>
#include <pthread.h>
#include <signal.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <sys/syscall.h>
#include <unistd.h>

#include "fcl/fcl_internal.h"
#include "utils.h"

typedef jint (*JNI_CreateJavaVM_t)(JavaVM **, JNIEnv **, void *);

// futex 初值 1=待命；hook 置 0 并 WAKE，waiter 从 WAIT 返回后取走退出码，
// 处理完复位待命——同一进程内连续多轮 JVM（如 installer 多阶段）各轮共用该 waiter
static volatile uint32_t futex_word = 1;
// hook（游戏 JVM 线程）写 / waiter 线程读，原子访问消除跨线程数据竞争
static _Atomic int exit_info_code = 0;
// 常驻 HotSpot（首个 launchJVM 创建后保留，多阶段经 runMainInExistingVM 复用；
// DestroyJavaVM+二次 CreateJavaVM 的重建路径存在并发调度残留问题，不再使用）
static JavaVM *game_vm = NULL;

static void *exit_waiter_thread(void *unused) {
    (void) unused;
    while (true) {
        while (syscall(SYS_futex, &futex_word, FUTEX_WAIT, 1, NULL) == -1 && errno == EINTR) {}
        int code = exit_info_code;
        futex_word = 1;
        nominal_exit(code);
    }
    return NULL;
}

static void spawn_exit_waiter(void) {
    pthread_t waiter;
    int result = pthread_create(&waiter, NULL, exit_waiter_thread, NULL);
    if (result != 0) {
        FCL_INTERNAL_LOG("Failed to start exit waiter thread: %s", strerror(result));
    }
}

// 常驻 waiter：整个进程生命周期只起一次，多轮 JVM 共用
static void setup_exit_waiter(void) {
    static pthread_once_t once = PTHREAD_ONCE_INIT;
    pthread_once(&once, spawn_exit_waiter);
}

// JVM "exit" hook：记录退出码、唤醒 waiter，随后永久停车
_Noreturn static void vm_exit_hook(jint code) {
    exit_info_code = code;
    futex_word = 0;
    syscall(SYS_futex, &futex_word, FUTEX_WAKE, 1);
    while (true) {}
}

// JVM "abort" hook：JVM 致命错误（VM_error/os::abort）路径；外部 SIGABRT 也会被
// JVM 信号处理汇聚到这里，与旧 abort_waiter 一样以原始信号值上报
_Noreturn static void vm_abort_hook(void) {
    vm_exit_hook(SIGABRT);
}

static jint launchJVM(JNIEnv *env, const char *jvmLibPath, char **vmArgs, int vmArgCount, const char *mainClass,
                      char **appArgs, int appArgCount) {
    // Unset all signal handlers to create a good slate for JVM signal detection.
    // (原 JLI 路径行为保留)
    struct sigaction clean_sa;
    memset(&clean_sa, 0, sizeof(struct sigaction));
    for (int sigid = SIGHUP; sigid < NSIG; sigid++) {
        // For some reason Android specifically checks if you set SIGSEGV to SIG_DFL.
        // There's probably a good reason for that but the signal handler here is
        // temporary and will be replaced by the Java VM's signal/crash handler.
        // Work around the warning by using SIG_IGN for SIGSEGV
        if (sigid == SIGSEGV) clean_sa.sa_handler = SIG_IGN;
        else clean_sa.sa_handler = SIG_DFL;
        sigaction(sigid, &clean_sa, NULL);
    }
    setup_exit_waiter();

    // 显式按路径 dlopen 目标 HotSpot：app 进程里 ART 已创建 VM 且导出同名
    // JNI_CreateJavaVM，若经 RTLD_DEFAULT 查符号会命中 ART 侧并返回 JNI_EEXIST(-5)
    void *libjvm = dlopen(jvmLibPath, RTLD_NOW | RTLD_GLOBAL);
    if (libjvm == NULL) {
        FCL_INTERNAL_LOG("dlopen(%s) failed: %s", jvmLibPath, dlerror());
        return -1;
    }
    JNI_CreateJavaVM_t createJavaVM = (JNI_CreateJavaVM_t) dlsym(libjvm, "JNI_CreateJavaVM");
    if (createJavaVM == NULL) {
        FCL_INTERNAL_LOG("JNI_CreateJavaVM = NULL: %s", dlerror());
        return -1;
    }

    int optionCount = vmArgCount + 2; // exit/abort hook 两项
    JavaVMOption *options = malloc(sizeof(JavaVMOption) * optionCount);
    if (options == NULL) {
        FCL_INTERNAL_LOG("Failed to allocate JavaVMOption array");
        return -1;
    }
    for (int i = 0; i < vmArgCount; i++) {
        options[i].optionString = vmArgs[i];
        options[i].extraInfo = NULL;
        FCL_LOG("VM arg: %s", vmArgs[i]);
    }
    options[vmArgCount].optionString = "exit";
    options[vmArgCount].extraInfo = (void *) vm_exit_hook;
    options[vmArgCount + 1].optionString = "abort";
    options[vmArgCount + 1].extraInfo = (void *) vm_abort_hook;

    JavaVMInitArgs initArgs;
    initArgs.nOptions = optionCount;
    initArgs.options = options;
    // JLI 时代未知选项由 java launcher 报错退出，直启改为静默忽略：跨 JRE 容错优先，
    // 旧 JVM 收到新版本的选项时不再硬性失败（代价是错误选项不再可见）
    initArgs.ignoreUnrecognized = JNI_TRUE;
    initArgs.version = JNI_VERSION_1_6;

    JavaVM *gameVM = NULL;
    JNIEnv *gameEnv = NULL;
    jint result = createJavaVM(&gameVM, &gameEnv, &initArgs);
    free(options);
    if (result != JNI_OK) {
        FCL_INTERNAL_LOG("JNI_CreateJavaVM failed: %d", result);
        return result;
    }
    game_vm = gameVM;
    FCL_LOG("Game JVM created");

    // mainClass 点分形式转 JNI 斜杠形式（动态分配，不设长度上限）
    char *mainClassPath = strdup(mainClass);
    if (mainClassPath == NULL) {
        FCL_INTERNAL_LOG("Failed to allocate main class buffer");
        return -1;
    }
    for (char *p = mainClassPath; *p != '\0'; p++) {
        if (*p == '.') *p = '/';
    }

    jclass mainClazz = (*gameEnv)->FindClass(gameEnv, mainClassPath);
    if (mainClazz == NULL) {
        if ((*gameEnv)->ExceptionCheck(gameEnv)) (*gameEnv)->ExceptionDescribe(gameEnv);
        FCL_INTERNAL_LOG("FindClass(%s) failed", mainClassPath);
        free(mainClassPath);
        return -1;
    }
    jmethodID mainMethod = (*gameEnv)->GetStaticMethodID(gameEnv, mainClazz, "main",
                                                         "([Ljava/lang/String;)V");
    if (mainMethod == NULL) {
        if ((*gameEnv)->ExceptionCheck(gameEnv)) (*gameEnv)->ExceptionDescribe(gameEnv);
        FCL_INTERNAL_LOG("GetStaticMethodID(main) on %s failed", mainClassPath);
        free(mainClassPath);
        return -1;
    }
    free(mainClassPath);

    jobjectArray appArgsArray = convert_from_char_array(gameEnv, appArgs, appArgCount);
    if (appArgsArray == NULL) {
        FCL_INTERNAL_LOG("Failed to convert app args");
        return -1;
    }

    (*gameEnv)->CallStaticVoidMethod(gameEnv, mainClazz, mainMethod, appArgsArray);
    // System.exit 已被 vm_exit_hook 接管并停车；能走到这里说明 main 正常返回或抛了异常。
    // JVM 不销毁：留给同进程后续阶段经 runMainInExistingVM 复用
    jint exitCode = 0;
    if ((*gameEnv)->ExceptionCheck(gameEnv)) {
        (*gameEnv)->ExceptionDescribe(gameEnv);
        (*gameEnv)->ExceptionClear(gameEnv);
        exitCode = 1;
    }
    return exitCode;
}

/*
 * Class:     com_oracle_dalvik_VMLauncher
 * Method:    launchJVM
 * Signature: (Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)I
 */
jint JNICALL
Java_com_oracle_dalvik_VMLauncher_launchJVM(JNIEnv *env, jclass clazz, jstring jvmLibPath,
                                            jobjectArray vmArgsArray,
                                            jstring mainClass, jobjectArray appArgsArray) {
    if (jvmLibPath == NULL || vmArgsArray == NULL || mainClass == NULL || appArgsArray == NULL) {
        FCL_LOG("Args array null, returning");
        return 0;
    }

    const char *mainClassUtf = (*env)->GetStringUTFChars(env, mainClass, NULL);
    if (mainClassUtf == NULL) return -1;
    const char *jvmLibPathUtf = (*env)->GetStringUTFChars(env, jvmLibPath, NULL);
    if (jvmLibPathUtf == NULL) {
        (*env)->ReleaseStringUTFChars(env, mainClass, mainClassUtf);
        return -1;
    }

    int vmArgCount = (*env)->GetArrayLength(env, vmArgsArray);
    int appArgCount = (*env)->GetArrayLength(env, appArgsArray);
    char **vmArgs = convert_to_char_array(env, vmArgsArray);
    char **appArgs = convert_to_char_array(env, appArgsArray);

    FCL_LOG("Done processing args");

    jint res = launchJVM(env, jvmLibPathUtf, vmArgs, vmArgCount, mainClassUtf, appArgs, appArgCount);

    FCL_LOG("Going to free args");

    (*env)->ReleaseStringUTFChars(env, mainClass, mainClassUtf);
    (*env)->ReleaseStringUTFChars(env, jvmLibPath, jvmLibPathUtf);
    free_char_array(env, vmArgsArray, (const char **) vmArgs);
    free_char_array(env, appArgsArray, (const char **) appArgs);

    FCL_LOG("Free done");

    return res;
}

/*
 * Class:     com_oracle_dalvik_VMLauncher
 * Method:    runMainInExistingVM
 * Signature: (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)I
 *
 * 常驻 JVM 的多阶段复用入口：为每阶段建独立 URLClassLoader 隔离 classpath，
 * attach 后 invoke main。返回 0=main 正常返回，1=main 抛异常，-1=启动失败。
 * 阶段内若发生 System.exit 仍走 exit hook 停车流程（进程终止，不返回）。
 */
jint JNICALL
Java_com_oracle_dalvik_VMLauncher_runMainInExistingVM(JNIEnv *env, jclass clazz, jstring classpath,
                                                      jstring mainClass, jobjectArray appArgsArray) {
    if (classpath == NULL || mainClass == NULL || appArgsArray == NULL) {
        FCL_LOG("Args array null, returning");
        return -1;
    }
    if (game_vm == NULL) {
        FCL_INTERNAL_LOG("No resident JVM for reuse");
        return -1;
    }

    JNIEnv *gameEnv;
    jint attachResult = (*game_vm)->GetEnv(game_vm, (void **) &gameEnv, JNI_VERSION_1_6);
    if (attachResult == JNI_EDETACHED)
        attachResult = (*game_vm)->AttachCurrentThread(game_vm, &gameEnv, NULL);
    if (attachResult != JNI_OK) {
        FCL_INTERNAL_LOG("Failed to attach to resident JVM: %d", attachResult);
        return -1;
    }

    // URL[] urls = classpath.split(":") 各项加 file:// 前缀
    const char *classpathUtf = (*env)->GetStringUTFChars(env, classpath, NULL);
    if (classpathUtf == NULL) return -1;
    int urlCount = 1;
    for (const char *p = classpathUtf; *p != '\0'; p++)
        if (*p == ':') urlCount++;
    char *cpCopy = strdup(classpathUtf);
    (*env)->ReleaseStringUTFChars(env, classpath, classpathUtf);
    if (cpCopy == NULL) return -1;

    jclass urlClass = (*gameEnv)->FindClass(gameEnv, "java/net/URL");
    jmethodID urlCtor = urlClass ? (*gameEnv)->GetMethodID(gameEnv, urlClass, "<init>",
                                                           "(Ljava/lang/String;)V") : NULL;
    jobjectArray urls = urlCtor ? (*gameEnv)->NewObjectArray(gameEnv, urlCount, urlClass, NULL) : NULL;
    if (urls == NULL) {
        if ((*gameEnv)->ExceptionCheck(gameEnv)) (*gameEnv)->ExceptionClear(gameEnv);
        FCL_INTERNAL_LOG("Failed to prepare URL array");
        free(cpCopy);
        return -1;
    }
    int urlIndex = 0;
    char *saveptr = NULL;
    for (char *token = strtok_r(cpCopy, ":", &saveptr); token != NULL;
         token = strtok_r(NULL, ":", &saveptr)) {
        char spec[1024];
        snprintf(spec, sizeof(spec), "file://%s", token);
        jstring specStr = (*gameEnv)->NewStringUTF(gameEnv, spec);
        if (specStr == NULL) break;
        jobject url = (*gameEnv)->NewObject(gameEnv, urlClass, urlCtor, specStr);
        (*gameEnv)->DeleteLocalRef(gameEnv, specStr);
        if (url == NULL) break;
        (*gameEnv)->SetObjectArrayElement(gameEnv, urls, urlIndex++, url);
        (*gameEnv)->DeleteLocalRef(gameEnv, url);
    }
    free(cpCopy);

    jclass loaderClass = (*gameEnv)->FindClass(gameEnv, "java/net/URLClassLoader");
    jmethodID loaderCtor = loaderClass ? (*gameEnv)->GetMethodID(gameEnv, loaderClass, "<init>",
                                                                 "([Ljava/net/URL;)V") : NULL;
    jobject loader = loaderCtor ? (*gameEnv)->NewObject(gameEnv, loaderClass, loaderCtor, urls) : NULL;
    if (loader == NULL) {
        if ((*gameEnv)->ExceptionCheck(gameEnv)) (*gameEnv)->ExceptionClear(gameEnv);
        FCL_INTERNAL_LOG("Failed to create URLClassLoader");
        return -1;
    }
    jmethodID loadClass = (*gameEnv)->GetMethodID(gameEnv, loaderClass, "loadClass",
                                                  "(Ljava/lang/String;)Ljava/lang/Class;");

    const char *mainClassUtf = (*env)->GetStringUTFChars(env, mainClass, NULL);
    if (mainClassUtf == NULL) return -1;
    jstring mainClassStr = (*gameEnv)->NewStringUTF(gameEnv, mainClassUtf);
    (*env)->ReleaseStringUTFChars(env, mainClass, mainClassUtf);
    if (mainClassStr == NULL) {
        if ((*gameEnv)->ExceptionCheck(gameEnv)) (*gameEnv)->ExceptionClear(gameEnv);
        return -1;
    }
    jclass mainClazz = (*gameEnv)->CallObjectMethod(gameEnv, loader, loadClass, mainClassStr);
    (*gameEnv)->DeleteLocalRef(gameEnv, mainClassStr);
    if (mainClazz == NULL) {
        if ((*gameEnv)->ExceptionCheck(gameEnv)) (*gameEnv)->ExceptionDescribe(gameEnv);
        (*gameEnv)->ExceptionClear(gameEnv);
        FCL_INTERNAL_LOG("loadClass(%s) failed on stage classloader", mainClass);
        return -1;
    }

    jmethodID mainMethod = (*gameEnv)->GetStaticMethodID(gameEnv, mainClazz, "main",
                                                         "([Ljava/lang/String;)V");
    if (mainMethod == NULL) {
        if ((*gameEnv)->ExceptionCheck(gameEnv)) (*gameEnv)->ExceptionDescribe(gameEnv);
        (*gameEnv)->ExceptionClear(gameEnv);
        FCL_INTERNAL_LOG("GetStaticMethodID(main) on %s failed", mainClass);
        return -1;
    }

    int appArgCount = (*env)->GetArrayLength(env, appArgsArray);
    char **appArgs = convert_to_char_array(env, appArgsArray);
    if (appArgs == NULL) return -1;
    jobjectArray appArgsArray2 = convert_from_char_array(gameEnv, appArgs, appArgCount);
    free_char_array(env, appArgsArray, (const char **) appArgs);
    if (appArgsArray2 == NULL) {
        FCL_INTERNAL_LOG("Failed to convert app args");
        return -1;
    }

    FCL_LOG("Running stage main in resident JVM");
    (*gameEnv)->CallStaticVoidMethod(gameEnv, mainClazz, mainMethod, appArgsArray2);
    // System.exit 已被 vm_exit_hook 接管并停车；能走到这里说明 main 正常返回或抛了异常
    jint exitCode = 0;
    if ((*gameEnv)->ExceptionCheck(gameEnv)) {
        (*gameEnv)->ExceptionDescribe(gameEnv);
        (*gameEnv)->ExceptionClear(gameEnv);
        exitCode = 1;
    }
    (*game_vm)->DetachCurrentThread(game_vm);
    return exitCode;
}

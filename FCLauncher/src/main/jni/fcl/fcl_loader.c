//
// Created by Tungsten on 2022/10/12.
//

#include <fcntl.h>
#include <unistd.h>
#include <jni.h>
#include <dlfcn.h>
#include <stdlib.h>
#include <android/log.h>
#include <xhook.h>
#include <string.h>
#include <fcl_internal.h>
#include <sys/mman.h>

#define FULL_VERSION "1.8.0-internal"
#define DOT_VERSION "1.8"
#define PROGNAME "java"
#define LAUNCHER_NAME "openjdk"

static char* const_progname = PROGNAME;
static const char* const_launcher = LAUNCHER_NAME;
static const char** const_jargs = NULL;
static const char** const_appclasspath = NULL;
static const jboolean const_cpwildcard = JNI_TRUE;
static const jboolean const_javaw = JNI_FALSE;
static const jint const_ergo_class = 0;    //DEFAULT_POLICY

typedef void (*android_update_LD_LIBRARY_PATH_t)(const char*);
static volatile jobject exitTrap_bridge;
static volatile jmethodID exitTrap_method;
static JavaVM *exitTrap_jvm;
static bool logPipeReady = false;
static volatile jobject log_bridge;
static volatile jmethodID log_method;
static JavaVM *log_pipe_jvm;
jstring CStr2Jstring(JNIEnv *env, const char *buffer);

jstring CStr2Jstring(JNIEnv *env, const char *buffer) {
    jsize len = strlen(buffer);
    jclass strClass = (*env)->FindClass(env, "java/lang/String");
    jstring encoding = (*env)->NewStringUTF(env, "UTF-8");
    jmethodID ctorID = (*env)->GetMethodID(env, strClass, "<init>", "([BLjava/lang/String;)V");
    jbyteArray bytes = (*env)->NewByteArray(env, len);
    (*env)->SetByteArrayRegion(env, bytes, 0, len, (jbyte *) buffer);
    return (jstring) (*env)->NewObject(env, strClass, ctorID, bytes, encoding);
}

void fclLog(const char *buffer) {
    if (logPipeReady) {
        JNIEnv *env;
        (*log_pipe_jvm)->AttachCurrentThread(log_pipe_jvm, &env, NULL);
        (*env)->CallVoidMethod(env, log_bridge, log_method, CStr2Jstring(env, buffer));
        (*log_pipe_jvm)->DetachCurrentThread(log_pipe_jvm);
    }
}

JNIEXPORT jint JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_redirectStdio(JNIEnv* env, jobject jobject, jstring path) {
    int fclFd[2];
    if  (pipe(fclFd) < 0) {
        __android_log_print(ANDROID_LOG_ERROR, "FCL", "Failed to create log pipe!");
        return 1;
    }

    if (dup2(fclFd[1], STDOUT_FILENO) != STDOUT_FILENO && dup2(fclFd[1], STDERR_FILENO) != STDERR_FILENO) {
        __android_log_print(ANDROID_LOG_ERROR, "FCL", "failed to redirect stdio!");
        return 2;
    }
    char buffer[1024];
    jclass bridge = (*env) -> FindClass(env, "com/tungsten/fclauncher/bridge/FCLBridge");
    jmethodID method_setLogPipeReady = (*env) -> GetMethodID(env, bridge, "setLogPipeReady", "()V");
    if (!method_setLogPipeReady) {
        __android_log_print(ANDROID_LOG_ERROR, "FCL", "Failed to find setLogPipeReady method!");
        return 3;
    }
    fcl->logFile = fdopen(fclFd[1], "a");
    FCL_INTERNAL_LOG("Log pipe ready.");
    (*env) -> CallVoidMethod(env, jobject, method_setLogPipeReady);
    log_method = (*env) -> GetMethodID(env, bridge, "receiveLog", "(Ljava/lang/String;)V");
    if (!log_method) {
        __android_log_print(ANDROID_LOG_ERROR, "FCL", "Failed to find receive method!");
        return 4;
    }
    log_bridge = (*env)->NewGlobalRef(env, jobject);
    (*env)->GetJavaVM(env, &log_pipe_jvm);
    logPipeReady = true;
    while (1) {
        memset(buffer, '\0', sizeof(buffer));
        ssize_t _s = read(fclFd[0], buffer, sizeof(buffer) - 1);
        if (_s < 0) {
            __android_log_print(ANDROID_LOG_ERROR, "FCL", "Failed to read log!");
            close(fclFd[0]);
            close(fclFd[1]);
            return 5;
        } else {
            buffer[_s] = '\0';
        }
        if (buffer[0] == '\0')
            continue;
        else {
            (*env)->CallVoidMethod(env, jobject, log_method, CStr2Jstring(env, buffer));
        }
    }
}

JNIEXPORT jint JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_chdir(JNIEnv* env, jobject jobject, jstring path) {
    char const* dir = (*env)->GetStringUTFChars(env, path, 0);

    int b = chdir(dir);

    (*env)->ReleaseStringUTFChars(env, path, dir);
    return b;
}

JNIEXPORT void JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_setenv(JNIEnv* env, jobject jobject, jstring str1, jstring str2) {
    char const* name = (*env)->GetStringUTFChars(env, str1, 0);
    char const* value = (*env)->GetStringUTFChars(env, str2, 0);

    setenv(name, value, 1);

    (*env)->ReleaseStringUTFChars(env, str1, name);
    (*env)->ReleaseStringUTFChars(env, str2, value);
}

JNIEXPORT jint JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_dlopen(JNIEnv* env, jobject jobject, jstring str) {
    dlerror();

    int ret = 0;
    char const* lib_name = (*env)->GetStringUTFChars(env, str, 0);

    void* handle;
    dlerror();
    handle = dlopen(lib_name, RTLD_GLOBAL | RTLD_LAZY);

    char * error = dlerror();
    __android_log_print(error == NULL ? ANDROID_LOG_INFO : ANDROID_LOG_ERROR, "FCL", "loading %s (error = %s)", lib_name, error);

    if (handle == NULL) {
        ret = -1;
    }

    (*env)->ReleaseStringUTFChars(env, str, lib_name);
    return ret;
}

JNIEXPORT void JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_setLdLibraryPath(JNIEnv *env, jobject jobject, jstring ldLibraryPath) {
    android_update_LD_LIBRARY_PATH_t android_update_LD_LIBRARY_PATH;
    void *libdl_handle = dlopen("libdl.so", RTLD_LAZY);
    void *updateLdLibPath = dlsym(libdl_handle, "android_update_LD_LIBRARY_PATH");
    if (updateLdLibPath == NULL) {
        updateLdLibPath = dlsym(libdl_handle, "__loader_android_update_LD_LIBRARY_PATH");
        char * error = dlerror();
        __android_log_print(error == NULL ? ANDROID_LOG_INFO : ANDROID_LOG_ERROR, "FCL", "loading %s (error = %s)", "libdl.so", error);
    }
    android_update_LD_LIBRARY_PATH = (android_update_LD_LIBRARY_PATH_t) updateLdLibPath;
    const char* ldLibPathUtf = (*env)->GetStringUTFChars(env, ldLibraryPath, 0);
    android_update_LD_LIBRARY_PATH(ldLibPathUtf);
    (*env)->ReleaseStringUTFChars(env, ldLibraryPath, ldLibPathUtf);
}

void (*old_exit)(int code);
void custom_exit(int code) {
    __android_log_print(code == 0 ? ANDROID_LOG_INFO : ANDROID_LOG_ERROR, "FCL", "JVM exit with code %d.", code);
    JNIEnv *env;
    (*exitTrap_jvm)->AttachCurrentThread(exitTrap_jvm, &env, NULL);
    (*env)->CallVoidMethod(env, exitTrap_bridge, exitTrap_method, code);
    (*env)->DeleteGlobalRef(env, exitTrap_bridge);
    (*exitTrap_jvm)->DetachCurrentThread(exitTrap_jvm);
    old_exit(code);
}

JNIEXPORT jint JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_setupExitTrap(JNIEnv *env, jobject jobject1, jobject bridge) {
    exitTrap_bridge = (*env)->NewGlobalRef(env, bridge);
    (*env)->GetJavaVM(env, &exitTrap_jvm);
    jclass exitTrap_exitClass = (*env)->NewGlobalRef(env,(*env)->FindClass(env, "com/tungsten/fclauncher/bridge/FCLBridge"));
    exitTrap_method = (*env)->GetMethodID(env, exitTrap_exitClass, "onExit", "(I)V");
    (*env)->DeleteGlobalRef(env, exitTrap_exitClass);
    // Enable xhook debug mode here
    // xhook_enable_debug(1);
    xhook_register(".*\\.so$", "exit", custom_exit, (void **) &old_exit);
    return xhook_refresh(1);
}

int
(*JLI_Launch)(int argc, char ** argv,              /* main argc, argc */
              int jargc, const char** jargv,          /* java args */
              int appclassc, const char** appclassv,  /* app classpath */
              const char* fullversion,                /* full version defined */
              const char* dotversion,                 /* dot version defined */
              const char* pname,                      /* program name */
              const char* lname,                      /* launcher name */
              jboolean javaargs,                      /* JAVA_ARGS */
              jboolean cpwildcard,                    /* classpath wildcard */
              jboolean javaw,                         /* windows-only javaw */
              jint     ergo_class                     /* ergnomics policy */
);

JNIEXPORT void JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_setupJLI(JNIEnv* env, jobject jobject){

    void* handle;
    handle = dlopen("libjli.so", RTLD_LAZY | RTLD_GLOBAL);
    JLI_Launch = (int (*)(int, char **, int, const char**, int, const char**, const char*, const char*, const char*, const char*, jboolean, jboolean, jboolean, jint))dlsym(handle, "JLI_Launch");

}

JNIEXPORT jint JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_jliLaunch(JNIEnv *env, jobject jobject, jobjectArray argsArray){
    int argc = (*env)->GetArrayLength(env, argsArray);
    char* argv[argc];
    for (int i = 0; i < argc; i++) {
        jstring str = (*env)->GetObjectArrayElement(env, argsArray, i);
        int len = (*env)->GetStringUTFLength(env, str);
        char* buf = malloc(len + 1);
        int characterLen = (*env)->GetStringLength(env, str);
        (*env)->GetStringUTFRegion(env, str, 0, characterLen, buf);
        buf[len] = 0;
        argv[i] = buf;
    }

    return JLI_Launch(argc, argv,
                      sizeof(const_jargs) / sizeof(char *), const_jargs,
                      sizeof(const_appclasspath) / sizeof(char *), const_appclasspath,
                      FULL_VERSION,
                      DOT_VERSION,
                      (const_progname != NULL) ? const_progname : *argv,
                      (const_launcher != NULL) ? const_launcher : *argv,
                      (const_jargs != NULL) ? JNI_TRUE : JNI_FALSE,
                      const_cpwildcard, const_javaw, const_ergo_class);

}
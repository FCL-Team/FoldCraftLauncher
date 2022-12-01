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

static volatile jobject exitTrap_bridge;
static volatile jmethodID exitTrap_method;
static JavaVM *exitTrap_jvm;

void (*old_exit)(int code);
void custom_exit(int code) {
    JNIEnv *env;
    (*exitTrap_jvm)->AttachCurrentThread(exitTrap_jvm, &env, NULL);
    (*env)->CallVoidMethod(env, exitTrap_bridge, exitTrap_method, code);
    (*env)->DeleteGlobalRef(env, exitTrap_bridge);
    (*exitTrap_jvm)->DetachCurrentThread(exitTrap_jvm);
    old_exit(code);
}

JNIEXPORT void JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_redirectStdio(JNIEnv* env, jclass clazz, jstring path) {
    char const* file = (*env)->GetStringUTFChars(env, path, 0);

    int fd = open(file, O_WRONLY | O_CREAT | O_TRUNC, 0666);
    dup2(fd, 1);
    dup2(fd, 2);

    (*env)->ReleaseStringUTFChars(env, path, file);
}

JNIEXPORT jint JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_chdir(JNIEnv* env, jclass clazz, jstring path) {
    char const* dir = (*env)->GetStringUTFChars(env, path, 0);

    int b = chdir(dir);

    (*env)->ReleaseStringUTFChars(env, path, dir);
    return b;
}

JNIEXPORT void JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_setenv(JNIEnv* env, jclass clazz, jstring str1, jstring str2) {
    char const* name = (*env)->GetStringUTFChars(env, str1, 0);
    char const* value = (*env)->GetStringUTFChars(env, str2, 0);

    setenv(name, value, 1);

    (*env)->ReleaseStringUTFChars(env, str1, name);
    (*env)->ReleaseStringUTFChars(env, str2, value);
}

JNIEXPORT jint JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_dlopen(JNIEnv* env, jclass clazz, jstring str) {
    dlerror();

    int ret = 0;
    char const* lib_name = (*env)->GetStringUTFChars(env, str, 0);

    void* handle;
    dlerror();
    handle = dlopen(lib_name, RTLD_GLOBAL);
    __android_log_print(dlerror() == NULL ? ANDROID_LOG_INFO : ANDROID_LOG_ERROR, "FCL", "loading %s (error = %s)", lib_name, dlerror());

    if (handle == NULL) {
        ret = -1;
    }

    (*env)->ReleaseStringUTFChars(env, str, lib_name);
    return ret;
}

JNIEXPORT void JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_setupExitTrap(JNIEnv *env, jclass clazz, jobject bridge) {
    exitTrap_bridge = (*env)->NewGlobalRef(env, bridge);
    (*env)->GetJavaVM(env, &exitTrap_jvm);
    jclass exitTrap_exitClass = (*env)->NewGlobalRef(env,(*env)->FindClass(env, "com/tungsten/fclauncher/bridge/FCLBridge"));
    exitTrap_method = (*env)->GetMethodID(env, exitTrap_exitClass, "exit", "(I)V");
    (*env)->DeleteGlobalRef(env, exitTrap_exitClass);
    // xhook_enable_debug(1);
    xhook_register(".*\\.so$", "exit", custom_exit, (void **) &old_exit);
    xhook_refresh(1);
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

JNIEXPORT void JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_setupJLI(JNIEnv* env, jclass clazz){

    void* handle;
    handle = dlopen("libjli.so", RTLD_LAZY);
    JLI_Launch = (int (*)(int, char **, int, const char**, int, const char**, const char*, const char*, const char*, const char*, jboolean, jboolean, jboolean, jint))dlsym(handle, "JLI_Launch");

}

JNIEXPORT jint JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_jliLaunch(JNIEnv *env, jclass clazz, jobjectArray argsArray){
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
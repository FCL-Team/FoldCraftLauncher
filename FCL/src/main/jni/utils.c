//
// utils.c — 自 Amethyst-Android 迁移，JNI 类名已调整为 FCL 的 FCLBridge
// （android.view.Surface 与 android.os.OpenJDKNativeRegister 为运行时类挂钩，保留原类名）
//
#include <jni.h>
#include <dlfcn.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <unistd.h>
#include <android/log.h>

#include "utils.h"

typedef int (*Main_Function_t)(int, char**);
typedef void (*android_update_LD_LIBRARY_PATH_t)(const char*);

long shared_awt_surface;

char** convert_to_char_array(JNIEnv *env, jobjectArray jstringArray) {
    int num_rows = (*env)->GetArrayLength(env, jstringArray);
    char **cArray = (char **) malloc(num_rows * sizeof(char*));
    jstring row;

    for (int i = 0; i < num_rows; i++) {
        row = (jstring) (*env)->GetObjectArrayElement(env, jstringArray, i);
        cArray[i] = (char*)(*env)->GetStringUTFChars(env, row, 0);
    }

    return cArray;
}

jobjectArray convert_from_char_array(JNIEnv *env, char **charArray, int num_rows) {
    jobjectArray resultArr = (*env)->NewObjectArray(env, num_rows, (*env)->FindClass(env, "java/lang/String"), NULL);
    jstring row;

    for (int i = 0; i < num_rows; i++) {
        row = (jstring) (*env)->NewStringUTF(env, charArray[i]);
        (*env)->SetObjectArrayElement(env, resultArr, i, row);
    }

    return resultArr;
}

void free_char_array(JNIEnv *env, jobjectArray jstringArray, const char **charArray) {
    int num_rows = (*env)->GetArrayLength(env, jstringArray);
    jstring row;

    for (int i = 0; i < num_rows; i++) {
        row = (jstring) (*env)->GetObjectArrayElement(env, jstringArray, i);
        (*env)->ReleaseStringUTFChars(env, row, charArray[i]);
    }
}

jstring convertStringJVM(JNIEnv* srcEnv, JNIEnv* dstEnv, jstring srcStr) {
    if (srcStr == NULL) {
        return NULL;
    }

    const char* srcStrC = (*srcEnv)->GetStringUTFChars(srcEnv, srcStr, 0);
    jstring dstStr = (*dstEnv)->NewStringUTF(dstEnv, srcStrC);
    (*srcEnv)->ReleaseStringUTFChars(srcEnv, srcStr, srcStrC);
    return dstStr;
}

jintArray convertIntArrayJVM(JNIEnv* srcEnv, JNIEnv* dstEnv, jintArray srcIntArray) {
    if (srcIntArray == NULL) {
        return NULL;
    }

    jsize len = (*srcEnv)->GetArrayLength(srcEnv, srcIntArray);
    jint* srcPtr = (*srcEnv)->GetIntArrayElements(srcEnv, srcIntArray, NULL);

    jintArray dstIntArray = (*dstEnv)->NewIntArray(dstEnv, len);
    (*dstEnv)->SetIntArrayRegion(dstEnv, dstIntArray, 0, len, srcPtr);

    (*srcEnv)->ReleaseIntArrayElements(srcEnv, srcIntArray, srcPtr, JNI_ABORT);

    return dstIntArray;
}

JNIEnv* get_attached_env(JavaVM* jvm) {
    JNIEnv *jvm_env = NULL;
    jint env_result = (*jvm)->GetEnv(jvm, (void**)&jvm_env, JNI_VERSION_1_4);
    if(env_result == JNI_EDETACHED) {
        env_result = (*jvm)->AttachCurrentThread(jvm, &jvm_env, NULL);
    }
    if(env_result != JNI_OK) {
        printf("get_attached_env failed: %i\n", env_result);
        return NULL;
    }
    return jvm_env;
}

JNIEXPORT void JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_setupBridgeSurfaceAWT(JNIEnv *env, jclass clazz, jlong surface) {
    shared_awt_surface = surface;
}

JNIEXPORT jlong JNICALL Java_android_view_Surface_nativeGetBridgeSurfaceAWT(JNIEnv *env, jclass clazz) {
    return (jlong) shared_awt_surface;
}

JNIEXPORT jint JNICALL Java_android_os_OpenJDKNativeRegister_nativeRegisterNatives(JNIEnv *env, jclass clazz, jstring registerSymbol) {
    const char *register_symbol_c = (*env)->GetStringUTFChars(env, registerSymbol, 0);
    void *symbol = dlsym(RTLD_DEFAULT, register_symbol_c);
    if (symbol == NULL) {
        printf("dlsym %s failed: %s\n", register_symbol_c, dlerror());
        return -1;
    }

    int (*registerNativesForClass)(JNIEnv*) = symbol;
    int result = registerNativesForClass(env);
    (*env)->ReleaseStringUTFChars(env, registerSymbol, register_symbol_c);

    return (jint) result;
}

JNIEXPORT void JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_setLdLibraryPath(JNIEnv *env, jobject jobject, jstring ldLibraryPath) {
    android_update_LD_LIBRARY_PATH_t android_update_LD_LIBRARY_PATH;

    void *libdl_handle = dlopen("libdl.so", RTLD_LAZY);
    void *updateLdLibPath = dlsym(libdl_handle, "android_update_LD_LIBRARY_PATH");
    if (updateLdLibPath == NULL) {
        updateLdLibPath = dlsym(libdl_handle, "__loader_android_update_LD_LIBRARY_PATH");
        char *error = dlerror();
        __android_log_print(error == NULL ? ANDROID_LOG_INFO : ANDROID_LOG_ERROR, "FCL", "loading %s (error = %s)", "libdl.so", error);
    }

    android_update_LD_LIBRARY_PATH = (android_update_LD_LIBRARY_PATH_t) updateLdLibPath;
    const char* ldLibPathUtf = (*env)->GetStringUTFChars(env, ldLibraryPath, 0);
    android_update_LD_LIBRARY_PATH(ldLibPathUtf);
    (*env)->ReleaseStringUTFChars(env, ldLibraryPath, ldLibPathUtf);
}

JNIEXPORT jlong JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_dlopen(JNIEnv* env, jobject jobject, jstring name) {
    dlerror();

    char const* lib_name = (*env)->GetStringUTFChars(env, name, 0);

    void* handle;
    dlerror();
    handle = dlopen(lib_name, RTLD_GLOBAL | RTLD_LAZY);

    char * error = dlerror();
    if(error != NULL && handle == NULL) {
        __android_log_print(ANDROID_LOG_ERROR, "FCL", "DLOPEN: loading %s (error = %s)", lib_name, error);
    } else {
        __android_log_print(ANDROID_LOG_INFO, "FCL", "DLOPEN: loading %s", lib_name);
    }

    (*env)->ReleaseStringUTFChars(env, name, lib_name);
    return (jlong) handle;
}

JNIEXPORT jint JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_chdir(JNIEnv* env, jobject jobject, jstring path) {
    char const* dir = (*env)->GetStringUTFChars(env, path, 0);

    int b = chdir(dir);

    (*env)->ReleaseStringUTFChars(env, path, dir);
    return b;
}

JNIEXPORT jint JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_executeBinary(JNIEnv *env, jclass clazz, jobjectArray cmdArgs) {
    jclass exception_cls = (*env)->FindClass(env, "java/lang/UnsatisfiedLinkError");
    jstring execFile = (*env)->GetObjectArrayElement(env, cmdArgs, 0);

    char *exec_file_c = (char*) (*env)->GetStringUTFChars(env, execFile, 0);
    void *exec_binary_handle = dlopen(exec_file_c, RTLD_LAZY);

    (*env)->ReleaseStringUTFChars(env, execFile, exec_file_c);

    char *exec_error_c = dlerror();
    if (exec_error_c != NULL) {
        __android_log_print(ANDROID_LOG_ERROR, "FCL", "Error: %s", exec_error_c);
        (*env)->ThrowNew(env, exception_cls, exec_error_c);
        return -1;
    }

    Main_Function_t Main_Function;
    Main_Function = (Main_Function_t) dlsym(exec_binary_handle, "main");

    exec_error_c = dlerror();
    if (exec_error_c != NULL) {
        __android_log_print(ANDROID_LOG_ERROR, "FCL", "Error: %s", exec_error_c);
        (*env)->ThrowNew(env, exception_cls, exec_error_c);
        return -1;
    }

    int cmd_argv = (*env)->GetArrayLength(env, cmdArgs);
    char **cmd_args_c = convert_to_char_array(env, cmdArgs);
    int result = Main_Function(cmd_argv, cmd_args_c);
    free_char_array(env, cmdArgs, cmd_args_c);
    return result;
}

// WARNING: This does not release the global ref, this can be a memory leak
JNIEXPORT jstring JNICALL
Java_com_tungsten_fclauncher_bridge_FCLBridge_jObjectToString(JNIEnv *env, jclass clazz, jobject object) {
    if (object == NULL) {
        return NULL;
    }

    jobject globalRef = (*env)->NewGlobalRef(env, object);
    if (globalRef == NULL) {
        return NULL;
    }

    char buf[32];
    snprintf(buf, sizeof(buf), "%p", globalRef);

    return (*env)->NewStringUTF(env, buf);
}

JNIEXPORT jlong JNICALL
Java_com_tungsten_fclauncher_bridge_FCLBridge_getJavaVMPointer(JNIEnv *env, jclass clazz) {
    JavaVM *vm;
    if ((*env)->GetJavaVM(env, &vm) != JNI_OK) {
        return -1;
    }

    return (jlong)(uintptr_t)vm;
}
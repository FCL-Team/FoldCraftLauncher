//
// awt_bridge.c — 自 Amethyst-Android 迁移，JNI 类名已调整为 FCL 的 FCLBridge
//
#include <jni.h>
#include <assert.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <dlfcn.h>

#include "log.h"

static JavaVM *dalvikJavaVMPtr;
static JavaVM* runtimeJavaVMPtr;
static JNIEnv* runtimeJNIEnvPtr_GRAPHICS;
static JNIEnv* runtimeJNIEnvPtr_INPUT;

jclass class_FCLBridge;
jclass class_CTCScreen;
jclass class_CTCAndroidInput;
jclass class_Frame;
jclass class_Rectangle;
jmethodID method_OpenLink;
jmethodID method_GetRGB;
jmethodID method_ReceiveInput;
jmethodID constructor_Rectangle;
jmethodID method_GetFrames;
jmethodID method_GetBounds;
jmethodID method_SetBounds;

jmethodID method_QuerySystemClipboard;
jmethodID method_PutClipboardData;
jclass class_CTCClipboard = NULL;
jmethodID method_SystemClipboardDataReceived = NULL;

// 游戏内 AWT 窗口 overlay：Swing 测试对话框（经 bootclasspath 上的 AwtHelper 在 EDT 弹出）
static jclass class_AwtHelper = NULL;
static jmethodID method_HelperShowMessageDialog = NULL;
static jmethodID method_HelperDumpAllStacks = NULL;
static jmethodID method_HelperPollAwtScreen = NULL;

// AwtHelper 的懒加载（方法缺失时返回 0 并清理异常）
static jboolean ensureAwtHelper(JNIEnv *rt) {
    if (method_HelperPollAwtScreen != NULL) {
        return JNI_TRUE;
    }
    jclass helperClass = (*rt)->FindClass(rt, "com/tungsten/fcl/awt/AwtHelper");
    if (helperClass == NULL) {
        FCL_LOG("[AWT] AwtHelper not found (bootclasspath jar missing?)");
        (*rt)->ExceptionDescribe(rt);
        (*rt)->ExceptionClear(rt);
        return JNI_FALSE;
    }
    class_AwtHelper = (*rt)->NewGlobalRef(rt, helperClass);
    method_HelperShowMessageDialog = (*rt)->GetStaticMethodID(rt, class_AwtHelper, "showMessageDialog",
                                                              "(Ljava/lang/String;Ljava/lang/String;)V");
    method_HelperDumpAllStacks = (*rt)->GetStaticMethodID(rt, class_AwtHelper, "dumpAllStacks", "()V");
    method_HelperPollAwtScreen = (*rt)->GetStaticMethodID(rt, class_AwtHelper, "pollAwtScreen", "()[I");
    if (method_HelperShowMessageDialog == NULL || method_HelperDumpAllStacks == NULL || method_HelperPollAwtScreen == NULL) {
        FCL_LOG("[AWT] AwtHelper methods not found");
        (*rt)->ExceptionDescribe(rt);
        (*rt)->ExceptionClear(rt);
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

// 运行时 JVM 指针获取：JNI_OnLoad 要等 cacio 初始化（首个 AWT 窗口）才触发，
// 游戏启动后即可经 libjvm 的 JNI_GetCreatedJavaVMs 主动取到
static jint (*getCreatedJavaVMsFunc)(JavaVM **, jsize, jsize *) = NULL;

// 从已加载库列表中定位实际路径后按全路径打开，绕开按名解析的命名空间限制
static void *openLoadedLib(const char *name) {
    FILE *maps = fopen("/proc/self/maps", "r");
    if (maps == NULL) {
        return NULL;
    }
    char line[1024];
    void *handle = NULL;
    while (fgets(line, sizeof(line), maps) != NULL) {
        char *path = strstr(line, name);
        if (path == NULL || path == line || path[-1] != '/') {
            continue;
        }
        char *end = path + strlen(path);
        while (end > path && (end[-1] == '\n' || end[-1] == '\r')) {
            end--;
        }
        *end = '\0';
        handle = dlopen(path, RTLD_LAZY | RTLD_GLOBAL);
        break;
    }
    fclose(maps);
    return handle;
}

static jboolean vmReadyLogged = JNI_FALSE;

static void ensureRuntimeJavaVM(void) {
    if (runtimeJavaVMPtr != NULL) {
        if (vmReadyLogged == JNI_FALSE) {
            vmReadyLogged = JNI_TRUE;
            FCL_LOG("[AWT] runtime JVM ready");
        }
        return;
    }
    if (dalvikJavaVMPtr == NULL) {
        return;
    }
    if (getCreatedJavaVMsFunc == NULL) {
        const char *libs[] = {"libjvm.so", "libjli.so"};
        for (size_t i = 0; i < sizeof(libs) / sizeof(libs[0]) && getCreatedJavaVMsFunc == NULL; i++) {
            void *handle = openLoadedLib(libs[i]);
            if (handle == NULL) {
                handle = dlopen(libs[i], RTLD_LAZY | RTLD_GLOBAL);
            }
            if (handle != NULL) {
                getCreatedJavaVMsFunc = (jint (*)(JavaVM **, jsize, jsize *)) dlsym(handle, "JNI_GetCreatedJavaVMs");
            }
        }
        if (getCreatedJavaVMsFunc == NULL) {
            FCL_LOG("[AWT] JNI_GetCreatedJavaVMs not found in loaded libs");
            return;
        }
    }
    JavaVM *vms[8];
    jsize count = 0;
    if ((*getCreatedJavaVMsFunc)(vms, 8, &count) == JNI_OK) {
        for (jsize i = 0; i < count; i++) {
            if (vms[i] != NULL && vms[i] != dalvikJavaVMPtr) {
                runtimeJavaVMPtr = vms[i];
                vmReadyLogged = JNI_TRUE;
                FCL_LOG("[AWT] runtime JVM located (%d vms)", count);
                break;
            }
        }
    }
}

jfieldID field_x;
jfieldID field_y;

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    if (dalvikJavaVMPtr == NULL) {
        dalvikJavaVMPtr = vm;
        JNIEnv *env = NULL;
        (*vm)->GetEnv(vm, (void **) &env, JNI_VERSION_1_4);
        class_FCLBridge = (*env)->NewGlobalRef(env,(*env)->FindClass(env, "com/tungsten/fclauncher/bridge/FCLBridge"));
        method_OpenLink = (*env)->GetStaticMethodID(env, class_FCLBridge, "openLink",
                                                    "(Ljava/lang/String;)V");
        method_QuerySystemClipboard = (*env)->GetStaticMethodID(env, class_FCLBridge,
                                                                "querySystemClipboard", "()V");
        method_PutClipboardData = (*env)->GetStaticMethodID(env, class_FCLBridge,
                                                            "putClipboardData",
                                                            "(Ljava/lang/String;Ljava/lang/String;)V");
    } else if (dalvikJavaVMPtr != vm) {
        runtimeJavaVMPtr = vm;
    }
    return JNI_VERSION_1_4;
}

JNIEXPORT void JNICALL
Java_net_java_openjdk_cacio_ctc_CTCDesktopPeer_openFile(JNIEnv *env, jclass clazz,
                                                        jstring filePath) {
    JNIEnv *dalvikEnv;
    char detachable = 0;
    if ((*dalvikJavaVMPtr)->GetEnv(dalvikJavaVMPtr, (void **) &dalvikEnv, JNI_VERSION_1_6) ==
        JNI_EDETACHED) {
        (*dalvikJavaVMPtr)->AttachCurrentThread(dalvikJavaVMPtr, &dalvikEnv, NULL);
        detachable = 1;
    }
    const char *stringChars = (*env)->GetStringUTFChars(env, filePath, NULL);
    (*dalvikEnv)->CallStaticVoidMethod(dalvikEnv, class_FCLBridge, method_OpenLink,
                                       (*dalvikEnv)->NewStringUTF(dalvikEnv, stringChars));
    (*env)->ReleaseStringUTFChars(env, filePath, stringChars);
    if (detachable) (*dalvikJavaVMPtr)->DetachCurrentThread(dalvikJavaVMPtr);
}

JNIEXPORT void JNICALL
Java_net_java_openjdk_cacio_ctc_CTCDesktopPeer_openUri(JNIEnv *env, jclass clazz, jstring uri) {
    JNIEnv *dalvikEnv;
    char detachable = 0;
    if ((*dalvikJavaVMPtr)->GetEnv(dalvikJavaVMPtr, (void **) &dalvikEnv, JNI_VERSION_1_6) ==
        JNI_EDETACHED) {
        (*dalvikJavaVMPtr)->AttachCurrentThread(dalvikJavaVMPtr, &dalvikEnv, NULL);
        detachable = 1;
    }
    const char *stringChars = (*env)->GetStringUTFChars(env, uri, NULL);
    (*dalvikEnv)->CallStaticVoidMethod(dalvikEnv, class_FCLBridge, method_OpenLink,
                                       (*dalvikEnv)->NewStringUTF(dalvikEnv, stringChars));
    (*env)->ReleaseStringUTFChars(env, uri, stringChars);
    if (detachable) (*dalvikJavaVMPtr)->DetachCurrentThread(dalvikJavaVMPtr);
}

JNIEXPORT void JNICALL Java_net_java_openjdk_cacio_ctc_CTCClipboard_nQuerySystemClipboard(JNIEnv *env, jclass clazz) {
    JNIEnv *dalvikEnv;char detachable = 0;
    if((*dalvikJavaVMPtr)->GetEnv(dalvikJavaVMPtr, (void **) &dalvikEnv, JNI_VERSION_1_6) == JNI_EDETACHED) {
        (*dalvikJavaVMPtr)->AttachCurrentThread(dalvikJavaVMPtr, &dalvikEnv, NULL);
        detachable = 1;
    }
    if(method_SystemClipboardDataReceived == NULL) {
        class_CTCClipboard = (*env)->NewGlobalRef(env, clazz);
        method_SystemClipboardDataReceived = (*env)->GetStaticMethodID(env, clazz, "systemClipboardDataReceived", "(Ljava/lang/String;Ljava/lang/String;)V");
    }
    (*dalvikEnv)->CallStaticVoidMethod(dalvikEnv, class_FCLBridge, method_QuerySystemClipboard);
    if(detachable) (*dalvikJavaVMPtr)->DetachCurrentThread(dalvikJavaVMPtr);
}

JNIEXPORT void JNICALL Java_net_java_openjdk_cacio_ctc_CTCClipboard_nPutClipboardData(JNIEnv* env, jclass clazz, jstring clipboardData, jstring clipboardDataMime) {
    JNIEnv *dalvikEnv;char detachable = 0;
    if((*dalvikJavaVMPtr)->GetEnv(dalvikJavaVMPtr, (void **) &dalvikEnv, JNI_VERSION_1_6) == JNI_EDETACHED) {
        (*dalvikJavaVMPtr)->AttachCurrentThread(dalvikJavaVMPtr, &dalvikEnv, NULL);
        detachable = 1;
    }

    const char* dataChars = (*env)->GetStringUTFChars(env, clipboardData, NULL);
    const char* mimeChars = (*env)->GetStringUTFChars(env, clipboardDataMime, NULL);
    (*dalvikEnv)->CallStaticVoidMethod(dalvikEnv, class_FCLBridge, method_PutClipboardData,
                                       (*dalvikEnv)->NewStringUTF(dalvikEnv, dataChars),
                                       (*dalvikEnv)->NewStringUTF(dalvikEnv, mimeChars));
    (*env)->ReleaseStringUTFChars(env, clipboardData, dataChars);
    (*env)->ReleaseStringUTFChars(env, clipboardDataMime, mimeChars);
    if(detachable) (*dalvikJavaVMPtr)->DetachCurrentThread(dalvikJavaVMPtr);
}

JNIEXPORT void JNICALL Java_com_github_caciocavallosilano_cacio_ctc_CTCClipboard_nQuerySystemClipboard(JNIEnv *env, jclass clazz) {
    Java_net_java_openjdk_cacio_ctc_CTCClipboard_nQuerySystemClipboard(env, clazz);
}

JNIEXPORT void JNICALL Java_com_github_caciocavallosilano_cacio_ctc_CTCClipboard_nPutClipboardData(JNIEnv* env, jclass clazz, jstring clipboardData, jstring clipboardDataMime) {
    Java_net_java_openjdk_cacio_ctc_CTCClipboard_nPutClipboardData(env, clazz, clipboardData, clipboardDataMime);
}

JNIEXPORT jintArray JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_renderAWTScreenFrame(JNIEnv* env, jclass clazz) {
    if (runtimeJNIEnvPtr_GRAPHICS == NULL) {
        if (runtimeJavaVMPtr == NULL) {
            return NULL;
        } else {
            (*runtimeJavaVMPtr)->AttachCurrentThread(runtimeJavaVMPtr, &runtimeJNIEnvPtr_GRAPHICS, NULL);
        }
    }

    int *rgbArray;
    jintArray jreRgbArray, androidRgbArray;

    if (method_GetRGB == NULL) {
        class_CTCScreen = (*runtimeJNIEnvPtr_GRAPHICS)->FindClass(runtimeJNIEnvPtr_GRAPHICS, "net/java/openjdk/cacio/ctc/CTCScreen");
        if ((*runtimeJNIEnvPtr_GRAPHICS)->ExceptionCheck(runtimeJNIEnvPtr_GRAPHICS) == JNI_TRUE) {
            (*runtimeJNIEnvPtr_GRAPHICS)->ExceptionClear(runtimeJNIEnvPtr_GRAPHICS);
            class_CTCScreen = (*runtimeJNIEnvPtr_GRAPHICS)->FindClass(runtimeJNIEnvPtr_GRAPHICS, "com/github/caciocavallosilano/cacio/ctc/CTCScreen");
        }
        assert(class_CTCScreen != NULL);
        method_GetRGB = (*runtimeJNIEnvPtr_GRAPHICS)->GetStaticMethodID(runtimeJNIEnvPtr_GRAPHICS, class_CTCScreen, "getCurrentScreenRGB", "()[I");
        assert(method_GetRGB != NULL);
    }
    jreRgbArray = (jintArray) (*runtimeJNIEnvPtr_GRAPHICS)->CallStaticObjectMethod(
            runtimeJNIEnvPtr_GRAPHICS,
            class_CTCScreen,
            method_GetRGB
    );
    if (jreRgbArray == NULL) {
        return NULL;
    }

    // Copy JRE RGB array memory to Android.
    int arrayLength = (*runtimeJNIEnvPtr_GRAPHICS)->GetArrayLength(runtimeJNIEnvPtr_GRAPHICS, jreRgbArray);
    rgbArray = (*runtimeJNIEnvPtr_GRAPHICS)->GetIntArrayElements(runtimeJNIEnvPtr_GRAPHICS, jreRgbArray, 0);
    androidRgbArray = (*env)->NewIntArray(env, arrayLength);
    (*env)->SetIntArrayRegion(env, androidRgbArray, 0, arrayLength, rgbArray);

    (*runtimeJNIEnvPtr_GRAPHICS)->ReleaseIntArrayElements(runtimeJNIEnvPtr_GRAPHICS, jreRgbArray, rgbArray, 0);
    // (*env)->DeleteLocalRef(env, androidRgbArray);
    // free(rgbArray);

    return androidRgbArray;
}

JNIEXPORT void JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_nativeSendData(JNIEnv* env, jclass clazz, jint type, jint i1, jint i2, jint i3, jint i4) {
    if (runtimeJNIEnvPtr_INPUT == NULL) {
        if (runtimeJavaVMPtr == NULL) {
            return;
        } else {
            (*runtimeJavaVMPtr)->AttachCurrentThread(runtimeJavaVMPtr, &runtimeJNIEnvPtr_INPUT, NULL);
        }
    }

    if (method_ReceiveInput == NULL) {
        class_CTCAndroidInput = (*runtimeJNIEnvPtr_INPUT)->FindClass(runtimeJNIEnvPtr_INPUT, "net/java/openjdk/cacio/ctc/CTCAndroidInput");
        if ((*runtimeJNIEnvPtr_INPUT)->ExceptionCheck(runtimeJNIEnvPtr_INPUT) == JNI_TRUE) {
            (*runtimeJNIEnvPtr_INPUT)->ExceptionClear(runtimeJNIEnvPtr_INPUT);
            class_CTCAndroidInput = (*runtimeJNIEnvPtr_INPUT)->FindClass(runtimeJNIEnvPtr_INPUT, "com/github/caciocavallosilano/cacio/ctc/CTCAndroidInput");
        }
        assert(class_CTCAndroidInput != NULL);
        method_ReceiveInput = (*runtimeJNIEnvPtr_INPUT)->GetStaticMethodID(runtimeJNIEnvPtr_INPUT, class_CTCAndroidInput, "receiveData", "(IIIII)V");
        assert(method_ReceiveInput != NULL);
    }
    (*runtimeJNIEnvPtr_INPUT)->CallStaticVoidMethod(
            runtimeJNIEnvPtr_INPUT,
            class_CTCAndroidInput,
            method_ReceiveInput,
            type, i1, i2, i3, i4
    );
}

JNIEXPORT void JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_nativeClipboardReceived(JNIEnv *env, jclass clazz, jstring clipboardData, jstring clipboardDataMime) {
    if(method_SystemClipboardDataReceived == NULL || class_CTCClipboard == NULL) return;
    if (runtimeJNIEnvPtr_INPUT == NULL) {
        if (runtimeJavaVMPtr == NULL) {
            return;
        } else {
            (*runtimeJavaVMPtr)->AttachCurrentThread(runtimeJavaVMPtr, &runtimeJNIEnvPtr_INPUT, NULL);
        }
    }
    const char* dataChars = clipboardData != NULL ? (*env)->GetStringUTFChars(env, clipboardData, NULL) : NULL;
    const char* mimeChars = clipboardDataMime != NULL ? (*env)->GetStringUTFChars(env, clipboardDataMime, NULL) : NULL;
    (*runtimeJNIEnvPtr_INPUT)->CallStaticVoidMethod(runtimeJNIEnvPtr_INPUT, class_CTCClipboard, method_SystemClipboardDataReceived,
                                                    clipboardData != NULL ? (*runtimeJNIEnvPtr_INPUT)->NewStringUTF(runtimeJNIEnvPtr_INPUT, dataChars) : NULL,
                                                    clipboardDataMime != NULL ? (*runtimeJNIEnvPtr_INPUT)->NewStringUTF(runtimeJNIEnvPtr_INPUT, mimeChars) : NULL);
    if(dataChars != NULL) (*env)->ReleaseStringUTFChars(env, clipboardData, dataChars);
    if(mimeChars != NULL) (*env)->ReleaseStringUTFChars(env, clipboardDataMime, mimeChars);
}

JNIEXPORT void JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_nativeMoveWindow(JNIEnv *env, jclass clazz, jint x, jint y) {
    if (runtimeJNIEnvPtr_INPUT == NULL) {
        if (runtimeJavaVMPtr == NULL) {
            return;
        } else {
            (*runtimeJavaVMPtr)->AttachCurrentThread(runtimeJavaVMPtr, &runtimeJNIEnvPtr_INPUT, NULL);
        }
    }
    if (field_y == NULL) {
        class_Frame = (*runtimeJNIEnvPtr_INPUT)->FindClass(runtimeJNIEnvPtr_INPUT, "java/awt/Frame");
        method_GetFrames = (*runtimeJNIEnvPtr_INPUT)->GetStaticMethodID(runtimeJNIEnvPtr_INPUT, class_Frame, "getFrames", "()[Ljava/awt/Frame;");
        method_GetBounds = (*runtimeJNIEnvPtr_INPUT)->GetMethodID(runtimeJNIEnvPtr_INPUT, class_Frame, "getBounds", "(Ljava/awt/Rectangle;)Ljava/awt/Rectangle;");
        method_SetBounds = (*runtimeJNIEnvPtr_INPUT)->GetMethodID(runtimeJNIEnvPtr_INPUT, class_Frame, "setBounds", "(Ljava/awt/Rectangle;)V");
        class_Rectangle = (*runtimeJNIEnvPtr_INPUT)->FindClass(runtimeJNIEnvPtr_INPUT, "java/awt/Rectangle");
        constructor_Rectangle = (*runtimeJNIEnvPtr_INPUT)->GetMethodID(runtimeJNIEnvPtr_INPUT, class_Rectangle, "<init>", "()V");
        field_x = (*runtimeJNIEnvPtr_INPUT)->GetFieldID(runtimeJNIEnvPtr_INPUT, class_Rectangle, "x", "I");
        field_y = (*runtimeJNIEnvPtr_INPUT)->GetFieldID(runtimeJNIEnvPtr_INPUT, class_Rectangle, "y", "I");
    }
    jobject rectangle = (*runtimeJNIEnvPtr_INPUT)->NewObject(runtimeJNIEnvPtr_INPUT, class_Rectangle, constructor_Rectangle);
    jobjectArray frames = (*runtimeJNIEnvPtr_INPUT)->CallStaticObjectMethod(runtimeJNIEnvPtr_INPUT, class_Frame, method_GetFrames);
    for (jsize i = 0; i < (*runtimeJNIEnvPtr_INPUT)->GetArrayLength(runtimeJNIEnvPtr_INPUT, frames); i++) {
        jobject frame = (*runtimeJNIEnvPtr_INPUT)->GetObjectArrayElement(runtimeJNIEnvPtr_INPUT, frames, i);
        (*runtimeJNIEnvPtr_INPUT)->CallObjectMethod(runtimeJNIEnvPtr_INPUT, frame, method_GetBounds, rectangle);
        (*runtimeJNIEnvPtr_INPUT)->SetIntField(runtimeJNIEnvPtr_INPUT, rectangle,  field_x, (*runtimeJNIEnvPtr_INPUT)->GetIntField(runtimeJNIEnvPtr_INPUT, rectangle, field_x) + x);
        (*runtimeJNIEnvPtr_INPUT)->SetIntField(runtimeJNIEnvPtr_INPUT, rectangle,  field_y, (*runtimeJNIEnvPtr_INPUT)->GetIntField(runtimeJNIEnvPtr_INPUT, rectangle, field_y) + y);
        (*runtimeJNIEnvPtr_INPUT)->CallVoidMethod(runtimeJNIEnvPtr_INPUT, frame, method_SetBounds, rectangle);
        (*runtimeJNIEnvPtr_INPUT)->DeleteLocalRef(runtimeJNIEnvPtr_INPUT, frame);
    }
    (*runtimeJNIEnvPtr_INPUT)->DeleteLocalRef(runtimeJNIEnvPtr_INPUT, rectangle);
    (*runtimeJNIEnvPtr_INPUT)->DeleteLocalRef(runtimeJNIEnvPtr_INPUT, frames);
}

/**
 * 取一份游戏 JVM 内 AWT 虚拟屏快照
 *
 * @return [screenWidth, screenHeight, count, x1, y1, w1, h1, ..., rgb...]，无可见窗口时返回 null
 */
JNIEXPORT jintArray JNICALL
Java_com_tungsten_fclauncher_bridge_FCLBridge_nativePollAwtFrame(JNIEnv *env, jclass clazz) {
    ensureRuntimeJavaVM();
    if (runtimeJavaVMPtr == NULL) {
        return NULL;
    }
    JNIEnv *rt = NULL;
    (*runtimeJavaVMPtr)->AttachCurrentThread(runtimeJavaVMPtr, &rt, NULL);
    if (ensureAwtHelper(rt) != JNI_TRUE) {
        return NULL;
    }
    jobject result = (*rt)->CallStaticObjectMethod(rt, class_AwtHelper, method_HelperPollAwtScreen);
    if ((*rt)->ExceptionCheck(rt) == JNI_TRUE) {
        FCL_LOG("[AWT] pollAwtScreen failed");
        (*rt)->ExceptionDescribe(rt);
        (*rt)->ExceptionClear(rt);
        return NULL;
    }
    if (result == NULL) {
        return NULL;
    }
    // 运行时 JVM 的引用不能直接返回给 Dalvik，须拷贝到 Dalvik 侧新数组
    jsize length = (*rt)->GetArrayLength(rt, result);
    jint *elements = (*rt)->GetIntArrayElements(rt, result, 0);
    jintArray out = (*env)->NewIntArray(env, length);
    (*env)->SetIntArrayRegion(env, out, 0, length, elements);
    (*rt)->ReleaseIntArrayElements(rt, result, elements, 0);
    (*rt)->DeleteLocalRef(rt, result);
    return out;
}

/**
 * 在游戏 JVM 内弹出 Swing 消息对话框，阻塞至用户关闭（模拟模组的 AWT/Swing 弹窗）
 */
JNIEXPORT void JNICALL
Java_com_tungsten_fclauncher_bridge_FCLBridge_nativeShowTestDialog(JNIEnv *env, jclass clazz, jstring title, jstring message) {
    FCL_LOG("[AWT] showTestDialog invoked");
    ensureRuntimeJavaVM();
    if (runtimeJavaVMPtr == NULL) {
        FCL_LOG("[AWT] showTestDialog: runtime JVM not available");
        return;
    }
    JNIEnv *rt = NULL;
    (*runtimeJavaVMPtr)->AttachCurrentThread(runtimeJavaVMPtr, &rt, NULL);

    if (ensureAwtHelper(rt) != JNI_TRUE) {
        (*runtimeJavaVMPtr)->DetachCurrentThread(runtimeJavaVMPtr);
        return;
    }

    const char *titleChars = (*env)->GetStringUTFChars(env, title, NULL);
    const char *messageChars = (*env)->GetStringUTFChars(env, message, NULL);
    jstring rtTitle = (*rt)->NewStringUTF(rt, titleChars);
    jstring rtMessage = (*rt)->NewStringUTF(rt, messageChars);
    // 诊断：转储游戏 JVM 全部线程栈
    (*rt)->CallStaticVoidMethod(rt, class_AwtHelper, method_HelperDumpAllStacks);
    if ((*rt)->ExceptionCheck(rt) == JNI_TRUE) {
        (*rt)->ExceptionDescribe(rt);
        (*rt)->ExceptionClear(rt);
    }
    // 对话框由 helper 转 EDT 弹出，本调用立即返回
    (*rt)->CallStaticVoidMethod(rt, class_AwtHelper, method_HelperShowMessageDialog, rtTitle, rtMessage);
    FCL_LOG("[AWT] helper showMessageDialog posted");
    if ((*rt)->ExceptionCheck(rt) == JNI_TRUE) {
        (*rt)->ExceptionDescribe(rt);
        (*rt)->ExceptionClear(rt);
    }
    (*env)->ReleaseStringUTFChars(env, title, titleChars);
    (*env)->ReleaseStringUTFChars(env, message, messageChars);
    (*runtimeJavaVMPtr)->DetachCurrentThread(runtimeJavaVMPtr);
}
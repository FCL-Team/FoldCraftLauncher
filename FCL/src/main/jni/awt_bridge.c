//
// awt_bridge.c — 自 Amethyst-Android 迁移，JNI 类名已调整为 FCL 的 FCLBridge
//
#include <jni.h>
#include <assert.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <dlfcn.h>

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

// 游戏内 AWT 窗口 overlay：窗口枚举与虚拟屏尺寸
static jclass class_Window = NULL;
static jclass class_Rect = NULL;
static jclass class_Toolkit = NULL;
static jclass class_Dimension = NULL;
static jmethodID method_GetWindows = NULL;
static jmethodID method_IsVisible = NULL;
static jmethodID method_WindowGetBounds = NULL;
static jmethodID constructor_Rect = NULL;
static jfieldID field_rectX = NULL;
static jfieldID field_rectY = NULL;
static jfieldID field_rectWidth = NULL;
static jfieldID field_rectHeight = NULL;
static jmethodID method_GetDefaultToolkit = NULL;
static jmethodID method_GetScreenSize = NULL;
static jfieldID field_dimWidth = NULL;
static jfieldID field_dimHeight = NULL;

// 游戏内 AWT 窗口 overlay：Swing 测试对话框
static jclass class_JOptionPane = NULL;
static jmethodID method_ShowMessageDialog = NULL;

// 运行时 JVM 指针获取：JNI_OnLoad 要等 cacio 初始化（首个 AWT 窗口）才触发，
// 游戏启动后即可经 libjvm 的 JNI_GetCreatedJavaVMs 主动取到
static jint (*getCreatedJavaVMsFunc)(JavaVM **, jsize, jsize *) = NULL;

static void ensureRuntimeJavaVM(void) {
    if (runtimeJavaVMPtr != NULL || dalvikJavaVMPtr == NULL) {
        return;
    }
    if (getCreatedJavaVMsFunc == NULL) {
        const char *libs[] = {"libjvm.so", "libjli.so"};
        for (size_t i = 0; i < sizeof(libs) / sizeof(libs[0]) && getCreatedJavaVMsFunc == NULL; i++) {
            void *handle = dlopen(libs[i], RTLD_LAZY | RTLD_GLOBAL);
            if (handle == NULL) {
                continue;
            }
            getCreatedJavaVMsFunc = (jint (*)(JavaVM **, jsize, jsize *)) dlsym(handle, "JNI_GetCreatedJavaVMs");
        }
        if (getCreatedJavaVMsFunc == NULL) {
            return;
        }
    }
    JavaVM *vms[8];
    jsize count = 0;
    if ((*getCreatedJavaVMsFunc)(vms, 8, &count) == JNI_OK) {
        for (jsize i = 0; i < count; i++) {
            if (vms[i] != NULL && vms[i] != dalvikJavaVMPtr) {
                runtimeJavaVMPtr = vms[i];
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
 * 枚举游戏 JVM 内可见的 AWT 顶层窗口
 *
 * @return [screenWidth, screenHeight, x1, y1, w1, h1, x2, ...]，无可见窗口或运行时 JVM 未就绪时返回 NULL
 */
JNIEXPORT jintArray JNICALL
Java_com_tungsten_fclauncher_bridge_FCLBridge_nativeGetAWTWindowRects(JNIEnv *env, jclass clazz) {
    ensureRuntimeJavaVM();
    if (runtimeJavaVMPtr == NULL) {
        return NULL;
    }
    JNIEnv *rt = NULL;
    (*runtimeJavaVMPtr)->AttachCurrentThread(runtimeJavaVMPtr, &rt, NULL);

    if (method_GetWindows == NULL) {
        jclass windowClass = (*rt)->FindClass(rt, "java/awt/Window");
        jclass rectClass = (*rt)->FindClass(rt, "java/awt/Rectangle");
        if (windowClass == NULL || rectClass == NULL) {
            (*rt)->ExceptionClear(rt);
            return NULL;
        }
        class_Window = (*rt)->NewGlobalRef(rt, windowClass);
        class_Rect = (*rt)->NewGlobalRef(rt, rectClass);
        method_GetWindows = (*rt)->GetStaticMethodID(rt, class_Window, "getWindows", "()[Ljava/awt/Window;");
        method_IsVisible = (*rt)->GetMethodID(rt, class_Window, "isVisible", "()Z");
        method_WindowGetBounds = (*rt)->GetMethodID(rt, class_Window, "getBounds", "(Ljava/awt/Rectangle;)Ljava/awt/Rectangle;");
        constructor_Rect = (*rt)->GetMethodID(rt, class_Rect, "<init>", "()V");
        field_rectX = (*rt)->GetFieldID(rt, class_Rect, "x", "I");
        field_rectY = (*rt)->GetFieldID(rt, class_Rect, "y", "I");
        field_rectWidth = (*rt)->GetFieldID(rt, class_Rect, "width", "I");
        field_rectHeight = (*rt)->GetFieldID(rt, class_Rect, "height", "I");
        if (method_GetWindows == NULL || method_IsVisible == NULL || method_WindowGetBounds == NULL
            || constructor_Rect == NULL || field_rectX == NULL || field_rectY == NULL
            || field_rectWidth == NULL || field_rectHeight == NULL) {
            (*rt)->ExceptionClear(rt);
            return NULL;
        }
    }

    jobjectArray windows = (jobjectArray) (*rt)->CallStaticObjectMethod(rt, class_Window, method_GetWindows);
    if ((*rt)->ExceptionCheck(rt) == JNI_TRUE) {
        (*rt)->ExceptionClear(rt);
        return NULL;
    }
    jsize count = (*rt)->GetArrayLength(rt, windows);
    jint *rects = NULL;
    jint visibleCount = 0;
    if (count > 0) {
        rects = malloc(sizeof(jint) * 4 * count);
        jobject rectangle = (*rt)->NewObject(rt, class_Rect, constructor_Rect);
        for (jsize i = 0; i < count; i++) {
            jobject window = (*rt)->GetObjectArrayElement(rt, windows, i);
            if (window != NULL && (*rt)->CallBooleanMethod(rt, window, method_IsVisible) == JNI_TRUE) {
                (*rt)->CallObjectMethod(rt, window, method_WindowGetBounds, rectangle);
                jint x = (*rt)->GetIntField(rt, rectangle, field_rectX);
                jint y = (*rt)->GetIntField(rt, rectangle, field_rectY);
                jint w = (*rt)->GetIntField(rt, rectangle, field_rectWidth);
                jint h = (*rt)->GetIntField(rt, rectangle, field_rectHeight);
                if (w > 0 && h > 0) {
                    rects[visibleCount * 4] = x;
                    rects[visibleCount * 4 + 1] = y;
                    rects[visibleCount * 4 + 2] = w;
                    rects[visibleCount * 4 + 3] = h;
                    visibleCount++;
                }
            }
            if (window != NULL) {
                (*rt)->DeleteLocalRef(rt, window);
            }
            if ((*rt)->ExceptionCheck(rt) == JNI_TRUE) {
                (*rt)->ExceptionClear(rt);
            }
        }
        (*rt)->DeleteLocalRef(rt, rectangle);
    }
    (*rt)->DeleteLocalRef(rt, windows);
    if (visibleCount == 0) {
        free(rects);
        return NULL;
    }

    // 虚拟屏尺寸直接向运行时查询；仅在确认存在窗口后触达 Toolkit，避免提前初始化 AWT
    if (method_GetDefaultToolkit == NULL) {
        jclass toolkitClass = (*rt)->FindClass(rt, "java/awt/Toolkit");
        jclass dimensionClass = (*rt)->FindClass(rt, "java/awt/Dimension");
        if (toolkitClass == NULL || dimensionClass == NULL) {
            (*rt)->ExceptionClear(rt);
            free(rects);
            return NULL;
        }
        class_Toolkit = (*rt)->NewGlobalRef(rt, toolkitClass);
        class_Dimension = (*rt)->NewGlobalRef(rt, dimensionClass);
        method_GetDefaultToolkit = (*rt)->GetStaticMethodID(rt, class_Toolkit, "getDefaultToolkit", "()Ljava/awt/Toolkit;");
        method_GetScreenSize = (*rt)->GetMethodID(rt, class_Toolkit, "getScreenSize", "()Ljava/awt/Dimension;");
        field_dimWidth = (*rt)->GetFieldID(rt, class_Dimension, "width", "D");
        field_dimHeight = (*rt)->GetFieldID(rt, class_Dimension, "height", "D");
        if (method_GetDefaultToolkit == NULL || method_GetScreenSize == NULL
            || field_dimWidth == NULL || field_dimHeight == NULL) {
            (*rt)->ExceptionClear(rt);
            free(rects);
            return NULL;
        }
    }
    jintArray result = NULL;
    jobject toolkit = (*rt)->CallStaticObjectMethod(rt, class_Toolkit, method_GetDefaultToolkit);
    if (toolkit != NULL && (*rt)->ExceptionCheck(rt) != JNI_TRUE) {
        jobject dimension = (*rt)->CallObjectMethod(rt, toolkit, method_GetScreenSize);
        if (dimension != NULL && (*rt)->ExceptionCheck(rt) != JNI_TRUE) {
            jint header[2];
            header[0] = (jint) (*rt)->GetDoubleField(rt, dimension, field_dimWidth);
            header[1] = (jint) (*rt)->GetDoubleField(rt, dimension, field_dimHeight);
            if (header[0] > 0 && header[1] > 0) {
                result = (*env)->NewIntArray(env, 2 + visibleCount * 4);
                (*env)->SetIntArrayRegion(env, result, 0, 2, header);
                (*env)->SetIntArrayRegion(env, result, 2, visibleCount * 4, rects);
            }
            (*rt)->DeleteLocalRef(rt, dimension);
        }
        (*rt)->DeleteLocalRef(rt, toolkit);
    }
    if ((*rt)->ExceptionCheck(rt) == JNI_TRUE) {
        (*rt)->ExceptionClear(rt);
    }
    free(rects);
    return result;
}

/**
 * 在游戏 JVM 内弹出 Swing 消息对话框，阻塞至用户关闭（模拟模组的 AWT/Swing 弹窗）
 */
JNIEXPORT void JNICALL
Java_com_tungsten_fclauncher_bridge_FCLBridge_nativeShowTestDialog(JNIEnv *env, jclass clazz, jstring title, jstring message) {
    ensureRuntimeJavaVM();
    if (runtimeJavaVMPtr == NULL) {
        return;
    }
    JNIEnv *rt = NULL;
    (*runtimeJavaVMPtr)->AttachCurrentThread(runtimeJavaVMPtr, &rt, NULL);

    if (method_ShowMessageDialog == NULL) {
        jclass optionPaneClass = (*rt)->FindClass(rt, "javax/swing/JOptionPane");
        if (optionPaneClass == NULL) {
            (*rt)->ExceptionClear(rt);
            (*runtimeJavaVMPtr)->DetachCurrentThread(runtimeJavaVMPtr);
            return;
        }
        class_JOptionPane = (*rt)->NewGlobalRef(rt, optionPaneClass);
        method_ShowMessageDialog = (*rt)->GetStaticMethodID(rt, class_JOptionPane, "showMessageDialog",
                                                            "(Ljava/awt/Component;Ljava/lang/Object;Ljava/lang/String;I)V");
        if (method_ShowMessageDialog == NULL) {
            (*rt)->ExceptionClear(rt);
            (*runtimeJavaVMPtr)->DetachCurrentThread(runtimeJavaVMPtr);
            return;
        }
    }
    const char *titleChars = (*env)->GetStringUTFChars(env, title, NULL);
    const char *messageChars = (*env)->GetStringUTFChars(env, message, NULL);
    jstring rtTitle = (*rt)->NewStringUTF(rt, titleChars);
    jstring rtMessage = (*rt)->NewStringUTF(rt, messageChars);
    // 最后一个参数为 JOptionPane.INFORMATION_MESSAGE
    (*rt)->CallStaticVoidMethod(rt, class_JOptionPane, method_ShowMessageDialog, NULL, rtMessage, rtTitle, 1);
    if ((*rt)->ExceptionCheck(rt) == JNI_TRUE) {
        (*rt)->ExceptionClear(rt);
    }
    (*env)->ReleaseStringUTFChars(env, title, titleChars);
    (*env)->ReleaseStringUTFChars(env, message, messageChars);
    (*runtimeJavaVMPtr)->DetachCurrentThread(runtimeJavaVMPtr);
}
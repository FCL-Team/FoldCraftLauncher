/*
 * V3 input bridge implementation.
 *
 * Status:
 * - Active development
 * - Works with some bugs:
 *  + Modded versions gives broken stuff..
 *
 * 
 * - Implements glfwSetCursorPos() to handle grab camera pos correctly.
 */

#include <assert.h>
#include <dlfcn.h>
#include <jni.h>
#include <stdlib.h>
#include <string.h>
#include <stdatomic.h>
#include <math.h>
#include <android/log.h>

#include "environ/environ.h"
#include "fcl/include/fcl_internal.h"
#include "jvm_hooks/jvm_hooks.h"

#define EVENT_TYPE_CHAR 1000
#define EVENT_TYPE_CHAR_MODS 1001
#define EVENT_TYPE_CURSOR_ENTER 1002
#define EVENT_TYPE_KEY 1005
#define EVENT_TYPE_MOUSE_BUTTON 1006
#define EVENT_TYPE_SCROLL 1007

static void registerFunctions(JNIEnv *env);

JNIEXPORT jstring JNICALL
Java_org_lwjgl_glfw_CallbackBridge_nativeClipboard(JNIEnv *env, jclass clazz, jint action,
                                                   jbyteArray copySrc);

jint JNI_OnLoad(JavaVM *vm, __attribute__((unused)) void *reserved) {
    if (pojav_environ->dalvikJavaVMPtr == NULL) {
        __android_log_print(ANDROID_LOG_INFO, "Native", "Saving DVM environ...");
        //Save dalvik global JavaVM pointer
        pojav_environ->dalvikJavaVMPtr = vm;
        (*vm)->GetEnv(vm, (void **) &pojav_environ->dalvikJNIEnvPtr_ANDROID, JNI_VERSION_1_4);
        pojav_environ->bridgeClazz = (*pojav_environ->dalvikJNIEnvPtr_ANDROID)->NewGlobalRef(
                pojav_environ->dalvikJNIEnvPtr_ANDROID,
                (*pojav_environ->dalvikJNIEnvPtr_ANDROID)->FindClass(
                        pojav_environ->dalvikJNIEnvPtr_ANDROID, "org/lwjgl/glfw/CallbackBridge"));
        pojav_environ->method_accessAndroidClipboard = (*pojav_environ->dalvikJNIEnvPtr_ANDROID)->GetStaticMethodID(
                pojav_environ->dalvikJNIEnvPtr_ANDROID, pojav_environ->bridgeClazz,
                "accessAndroidClipboard", "(ILjava/lang/String;)Ljava/lang/String;");
        pojav_environ->method_onGrabStateChanged = (*pojav_environ->dalvikJNIEnvPtr_ANDROID)->GetStaticMethodID(
                pojav_environ->dalvikJNIEnvPtr_ANDROID, pojav_environ->bridgeClazz,
                "onGrabStateChanged", "(Z)V");
        pojav_environ->isUseStackQueueCall = JNI_FALSE;
    } else if (pojav_environ->dalvikJavaVMPtr != vm) {
        __android_log_print(ANDROID_LOG_INFO, "Native", "Saving JVM environ...");
        pojav_environ->runtimeJavaVMPtr = vm;
        (*vm)->GetEnv(vm, (void **) &pojav_environ->runtimeJNIEnvPtr_JRE, JNI_VERSION_1_4);
        JNIEnv *vmEnv = pojav_environ->runtimeJNIEnvPtr_JRE;
        hookExec(vmEnv);
        installLwjglDlopenHook(vmEnv);
        installEMUIIteratorMititgation(vmEnv);
    }

    if (pojav_environ->dalvikJavaVMPtr == vm) {
        //perform in all DVM instances, not only during first ever set up
        JNIEnv *env;
        (*vm)->GetEnv(vm, (void **) &env, JNI_VERSION_1_4);
        registerFunctions(env);
    }
    pojav_environ->isGrabbing = JNI_FALSE;

    return JNI_VERSION_1_4;
}

/**
 * 初始化 GLFW/JVM 侧通信所需的方法 ID 与按键缓冲。
 * 由 GLFW.java 静态块在类加载后调用——GLFW 的加载时机远晚于 pojavexec 被 dlopen，
 * 此时才能保证 FindClass 成功。
 */
JNIEXPORT void JNICALL
Java_org_lwjgl_glfw_GLFW_nativeInitializeGLFWNativeBridge(__attribute__((unused)) JNIEnv *env,
                                                          __attribute__((unused)) jclass clazz) {
    JNIEnv *vmEnv;
    (*pojav_environ->runtimeJavaVMPtr)->GetEnv(pojav_environ->runtimeJavaVMPtr, (void **) &vmEnv,
                                               JNI_VERSION_1_4);
    pojav_environ->vmGlfwClass = (*vmEnv)->NewGlobalRef(vmEnv,
                                                        (*vmEnv)->FindClass(vmEnv,
                                                                            "org/lwjgl/glfw/GLFW"));
    pojav_environ->method_glftSetWindowAttrib = (*vmEnv)->GetStaticMethodID(
            vmEnv, pojav_environ->vmGlfwClass, "glfwSetWindowAttrib", "(JII)V");
    pojav_environ->method_internalWindowSizeChanged = (*vmEnv)->GetStaticMethodID(
            vmEnv, pojav_environ->vmGlfwClass, "internalWindowSizeChanged", "(J)V");
    pojav_environ->method_internalChangeMonitorSize = (*vmEnv)->GetStaticMethodID(
            vmEnv, pojav_environ->vmGlfwClass, "internalChangeMonitorSize", "(II)V");
    jfieldID field_keyDownBuffer = (*vmEnv)->GetStaticFieldID(
            vmEnv, pojav_environ->vmGlfwClass, "keyDownBuffer", "Ljava/nio/ByteBuffer;");
    jobject keyDownBufferJ = (*vmEnv)->GetStaticObjectField(
            vmEnv, pojav_environ->vmGlfwClass, field_keyDownBuffer);
    pojav_environ->keyDownBuffer = (*vmEnv)->GetDirectBufferAddress(vmEnv, keyDownBufferJ);
    jfieldID field_mouseDownBuffer = (*vmEnv)->GetStaticFieldID(
            vmEnv, pojav_environ->vmGlfwClass, "mouseDownBuffer", "Ljava/nio/ByteBuffer;");
    jobject mouseDownBufferJ = (*vmEnv)->GetStaticObjectField(
            vmEnv, pojav_environ->vmGlfwClass, field_mouseDownBuffer);
    pojav_environ->mouseDownBuffer = (*vmEnv)->GetDirectBufferAddress(vmEnv, mouseDownBufferJ);
}

#define ADD_CALLBACK_WWIN(NAME) \
JNIEXPORT jlong JNICALL Java_org_lwjgl_glfw_GLFW_nglfwSet##NAME##Callback(JNIEnv * env, jclass cls, jlong window, jlong callbackptr) { \
    void** oldCallback = (void**) &pojav_environ->GLFW_invoke_##NAME; \
    pojav_environ->GLFW_invoke_##NAME = (GLFW_invoke_##NAME##_func*) (uintptr_t) callbackptr; \
    return (jlong) (uintptr_t) *oldCallback; \
}

ADD_CALLBACK_WWIN(Char)

ADD_CALLBACK_WWIN(CharMods)

ADD_CALLBACK_WWIN(CursorEnter)

ADD_CALLBACK_WWIN(CursorPos)

ADD_CALLBACK_WWIN(FramebufferSize)

ADD_CALLBACK_WWIN(Key)

ADD_CALLBACK_WWIN(MouseButton)

ADD_CALLBACK_WWIN(Scroll)

ADD_CALLBACK_WWIN(WindowSize)

#undef ADD_CALLBACK_WWIN

void updateMonitorSize(int width, int height) {
    (*pojav_environ->glfwThreadVmEnv)->CallStaticVoidMethod(
            pojav_environ->glfwThreadVmEnv, pojav_environ->vmGlfwClass,
            pojav_environ->method_internalChangeMonitorSize, width, height);
}

void updateWindowSize(void *window) {
    (*pojav_environ->glfwThreadVmEnv)->CallStaticVoidMethod(
            pojav_environ->glfwThreadVmEnv, pojav_environ->vmGlfwClass,
            pojav_environ->method_internalWindowSizeChanged, (jlong) window);
}

void pojavPumpEvents(void *window) {
    if (pojav_environ->shouldUpdateMouse) {
        pojav_environ->GLFW_invoke_CursorPos(window, floor(pojav_environ->cursorX),
                                             floor(pojav_environ->cursorY));
    }
    if (pojav_environ->shouldUpdateMonitorSize) {
        updateWindowSize(window);
    }

    size_t index = pojav_environ->outEventIndex;
    size_t targetIndex = pojav_environ->outTargetIndex;

    while (targetIndex != index) {
        GLFWInputEvent event = pojav_environ->events[index];
        switch (event.type) {
            case EVENT_TYPE_CHAR:
                if (pojav_environ->GLFW_invoke_Char)
                    pojav_environ->GLFW_invoke_Char(window, event.i1);
                break;
            case EVENT_TYPE_CHAR_MODS:
                if (pojav_environ->GLFW_invoke_CharMods)
                    pojav_environ->GLFW_invoke_CharMods(window, event.i1, event.i2);
                break;
            case EVENT_TYPE_KEY:
                if (pojav_environ->GLFW_invoke_Key)
                    pojav_environ->GLFW_invoke_Key(window, event.i1, event.i2, event.i3, event.i4);
                break;
            case EVENT_TYPE_MOUSE_BUTTON:
                if (pojav_environ->GLFW_invoke_MouseButton)
                    pojav_environ->GLFW_invoke_MouseButton(window, event.i1, event.i2, event.i3);
                break;
            case EVENT_TYPE_CURSOR_ENTER:
                if (pojav_environ->GLFW_invoke_CursorEnter)
                    pojav_environ->GLFW_invoke_CursorEnter(window, event.i1);
                break;
            case EVENT_TYPE_SCROLL:
                if (pojav_environ->GLFW_invoke_Scroll)
                    pojav_environ->GLFW_invoke_Scroll(window, event.i1, event.i2);
                break;
        }

        index++;
        if (index >= EVENT_WINDOW_SIZE)
            index -= EVENT_WINDOW_SIZE;
    }
}

/** Prepare the library for sending out callbacks to all windows */
void pojavStartPumping() {
    size_t counter = atomic_load_explicit(&pojav_environ->eventCounter, memory_order_acquire);
    size_t index = pojav_environ->outEventIndex;

    unsigned targetIndex = index + counter;
    if (targetIndex >= EVENT_WINDOW_SIZE)
        targetIndex -= EVENT_WINDOW_SIZE;

    // Only accessed by one unique thread, no need for atomic store
    pojav_environ->inEventCount = counter;
    pojav_environ->outTargetIndex = targetIndex;

    //PumpEvents is called for every window, so this logic should be there in order to correctly distribute events to all windows.
    if ((pojav_environ->cLastX != pojav_environ->cursorX ||
         pojav_environ->cLastY != pojav_environ->cursorY) && pojav_environ->GLFW_invoke_CursorPos) {
        pojav_environ->cLastX = pojav_environ->cursorX;
        pojav_environ->cLastY = pojav_environ->cursorY;
        pojav_environ->shouldUpdateMouse = true;
    }
    if (pojav_environ->shouldUpdateMonitorSize) {
        // Perform a monitor size update here to avoid doing it on every single window
        updateMonitorSize(pojav_environ->savedWidth, pojav_environ->savedHeight);
        // Mark the monitor size as consumed (since GLFW was made aware of it)
        pojav_environ->monitorSizeConsumed = true;
    }
}

/** Prepare the library for the next round of new events */
void pojavStopPumping() {
    pojav_environ->outEventIndex = pojav_environ->outTargetIndex;

    // New events may have arrived while pumping, so remove only the difference before the start and end of execution
    atomic_fetch_sub_explicit(&pojav_environ->eventCounter, pojav_environ->inEventCount,
                              memory_order_acquire);
    // Make sure the next frame won't send mouse or monitor updates if it's unnecessary
    pojav_environ->shouldUpdateMouse = false;
    // Only reset the update flag if the monitor size was consumed by pojavStartPumping. This
    // will delay the update to next frame if it had occured between pojavStartPumping and pojavStopPumping,
    // but it's better than not having it apply at all
    if (pojav_environ->shouldUpdateMonitorSize && pojav_environ->monitorSizeConsumed) {
        pojav_environ->shouldUpdateMonitorSize = false;
        pojav_environ->monitorSizeConsumed = false;
    }
}

JNIEXPORT void JNICALL
Java_org_lwjgl_glfw_GLFW_nglfwGetCursorPos(JNIEnv *env, __attribute__((unused)) jclass clazz,
                                           __attribute__((unused)) jlong window, jobject xpos,
                                           jobject ypos) {
    *(double *) (*env)->GetDirectBufferAddress(env, xpos) = pojav_environ->cursorX;
    *(double *) (*env)->GetDirectBufferAddress(env, ypos) = pojav_environ->cursorY;
}

JNIEXPORT void JNICALL
JavaCritical_org_lwjgl_glfw_GLFW_nglfwGetCursorPosA(__attribute__((unused)) jlong window,
                                                    jint lengthx, jdouble *xpos, jint lengthy,
                                                    jdouble *ypos) {
    *xpos = pojav_environ->cursorX;
    *ypos = pojav_environ->cursorY;
}

JNIEXPORT void JNICALL
Java_org_lwjgl_glfw_GLFW_nglfwGetCursorPosA(JNIEnv *env, __attribute__((unused)) jclass clazz,
                                            __attribute__((unused)) jlong window,
                                            jdoubleArray xpos, jdoubleArray ypos) {
    (*env)->SetDoubleArrayRegion(env, xpos, 0, 1, &pojav_environ->cursorX);
    (*env)->SetDoubleArrayRegion(env, ypos, 0, 1, &pojav_environ->cursorY);
}

JNIEXPORT void JNICALL
JavaCritical_org_lwjgl_glfw_GLFW_glfwSetCursorPos(__attribute__((unused)) jlong window,
                                                  jdouble xpos,
                                                  jdouble ypos) {
    pojav_environ->cLastX = pojav_environ->cursorX = xpos;
    pojav_environ->cLastY = pojav_environ->cursorY = ypos;
}

JNIEXPORT void JNICALL
Java_org_lwjgl_glfw_GLFW_glfwSetCursorPos(__attribute__((unused)) JNIEnv *env,
                                          __attribute__((unused)) jclass clazz,
                                          __attribute__((unused)) jlong window, jdouble xpos,
                                          jdouble ypos) {
    JavaCritical_org_lwjgl_glfw_GLFW_glfwSetCursorPos(window, xpos, ypos);
}


void sendData(int type, int i1, int i2, int i3, int i4) {
    GLFWInputEvent *event = &pojav_environ->events[pojav_environ->inEventIndex];
    event->type = type;
    event->i1 = i1;
    event->i2 = i2;
    event->i3 = i3;
    event->i4 = i4;

    if (++pojav_environ->inEventIndex >= EVENT_WINDOW_SIZE)
        pojav_environ->inEventIndex -= EVENT_WINDOW_SIZE;

    atomic_fetch_add_explicit(&pojav_environ->eventCounter, 1, memory_order_acquire);
}

void critical_set_stackqueue(jboolean use_input_stack_queue) {
    pojav_environ->isUseStackQueueCall = (int) use_input_stack_queue;
}

void noncritical_set_stackqueue(__attribute__((unused)) JNIEnv *env,
                                __attribute__((unused)) jclass clazz,
                                jboolean use_input_stack_queue) {
    critical_set_stackqueue(use_input_stack_queue);
}

jstring convertStringJVM(JNIEnv *srcEnv, JNIEnv *dstEnv, jstring srcStr) {
    if (srcStr == NULL) {
        return NULL;
    }

    const char *srcStrC = (*srcEnv)->GetStringUTFChars(srcEnv, srcStr, 0);
    jstring dstStr = (*dstEnv)->NewStringUTF(dstEnv, srcStrC);
    (*srcEnv)->ReleaseStringUTFChars(srcEnv, srcStr, srcStrC);
    return dstStr;
}

JNIEXPORT jstring JNICALL Java_org_lwjgl_glfw_CallbackBridge_nativeClipboard(JNIEnv *env,
                                                                             __attribute__((unused)) jclass clazz,
                                                                             jint action,
                                                                             jbyteArray copySrc) {
#ifdef DEBUG
    FCL_INTERNAL_LOG("Debug: Clipboard access is going on\n", pojav_environ->isUseStackQueueCall);
#endif

    JNIEnv *dalvikEnv;
    (*pojav_environ->dalvikJavaVMPtr)->AttachCurrentThread(pojav_environ->dalvikJavaVMPtr,
                                                           &dalvikEnv, NULL);
    assert(dalvikEnv != NULL);
    assert(pojav_environ->bridgeClazz != NULL);

    char *copySrcC;
    jstring copyDst = NULL;
    if (copySrc) {
        copySrcC = (char *) ((*env)->GetByteArrayElements(env, copySrc, NULL));
        copyDst = (*dalvikEnv)->NewStringUTF(dalvikEnv, copySrcC);
    }

    jstring pasteDst = convertStringJVM(dalvikEnv, env,
                                        (jstring) (*dalvikEnv)->CallStaticObjectMethod(dalvikEnv,
                                                                                       pojav_environ->bridgeClazz,
                                                                                       pojav_environ->method_accessAndroidClipboard,
                                                                                       action,
                                                                                       copyDst));

    if (copySrc) {
        (*dalvikEnv)->DeleteLocalRef(dalvikEnv, copyDst);
        (*env)->ReleaseByteArrayElements(env, copySrc, (jbyte *) copySrcC, 0);
    }
    (*pojav_environ->dalvikJavaVMPtr)->DetachCurrentThread(pojav_environ->dalvikJavaVMPtr);
    return pasteDst;
}

JNIEXPORT jboolean JNICALL
JavaCritical_org_lwjgl_glfw_CallbackBridge_nativeSetInputReady(jboolean inputReady) {
#ifdef DEBUG
    FCL_INTERNAL_LOG("Debug: Changing input state, isReady=%d, pojav_environ->isUseStackQueueCall=%d\n", inputReady, pojav_environ->isUseStackQueueCall);
#endif
    __android_log_print(ANDROID_LOG_INFO, "NativeInput", "Input ready: %i", inputReady);
    pojav_environ->isInputReady = inputReady;
    return pojav_environ->isUseStackQueueCall;
}

JNIEXPORT jboolean JNICALL
Java_org_lwjgl_glfw_CallbackBridge_nativeSetInputReady(__attribute__((unused)) JNIEnv *env,
                                                       __attribute__((unused)) jclass clazz,
                                                       jboolean inputReady) {
    return JavaCritical_org_lwjgl_glfw_CallbackBridge_nativeSetInputReady(inputReady);
}

JNIEXPORT void JNICALL
Java_org_lwjgl_glfw_CallbackBridge_nativeSetGrabbing(__attribute__((unused)) JNIEnv *env,
                                                     __attribute__((unused)) jclass clazz,
                                                     jboolean grabbing) {
    JNIEnv *dalvikEnv;
    (*pojav_environ->dalvikJavaVMPtr)->AttachCurrentThread(pojav_environ->dalvikJavaVMPtr,
                                                           &dalvikEnv, NULL);
    (*dalvikEnv)->CallStaticVoidMethod(dalvikEnv, pojav_environ->bridgeClazz,
                                       pojav_environ->method_onGrabStateChanged, grabbing);
    (*pojav_environ->dalvikJavaVMPtr)->DetachCurrentThread(pojav_environ->dalvikJavaVMPtr);
    pojav_environ->isGrabbing = grabbing;
}

jboolean critical_send_char(jchar codepoint) {
    if (pojav_environ->GLFW_invoke_Char && pojav_environ->isInputReady) {
        if (pojav_environ->isUseStackQueueCall) {
            sendData(EVENT_TYPE_CHAR, codepoint, 0, 0, 0);
        } else {
            pojav_environ->GLFW_invoke_Char((void *) pojav_environ->showingWindow,
                                            (unsigned int) codepoint);
        }
        return JNI_TRUE;
    }
    return JNI_FALSE;
}

jboolean
noncritical_send_char(__attribute__((unused)) JNIEnv *env, __attribute__((unused)) jclass clazz,
                      jchar codepoint) {
    return critical_send_char(codepoint);
}

jboolean critical_send_char_mods(jchar codepoint, jint mods) {
    if (pojav_environ->GLFW_invoke_CharMods && pojav_environ->isInputReady) {
        if (pojav_environ->isUseStackQueueCall) {
            sendData(EVENT_TYPE_CHAR_MODS, (int) codepoint, mods, 0, 0);
        } else {
            pojav_environ->GLFW_invoke_CharMods((void *) pojav_environ->showingWindow, codepoint,
                                                mods);
        }
        return JNI_TRUE;
    }
    return JNI_FALSE;
}

jboolean noncritical_send_char_mods(__attribute__((unused)) JNIEnv *env,
                                    __attribute__((unused)) jclass clazz, jchar codepoint,
                                    jint mods) {
    return critical_send_char_mods(codepoint, mods);
}

/*
JNIEXPORT void JNICALL Java_org_lwjgl_glfw_CallbackBridge_nativeSendCursorEnter(JNIEnv* env, jclass clazz, jint entered) {
    if (pojav_environ->GLFW_invoke_CursorEnter && pojav_environ->isInputReady) {
        pojav_environ->GLFW_invoke_CursorEnter(pojav_environ->showingWindow, entered);
    }
}
*/

void critical_send_cursor_pos(jfloat x, jfloat y) {
#ifdef DEBUG
    FCL_INTERNAL_LOG("Sending cursor position \n");
#endif
    if (pojav_environ->GLFW_invoke_CursorPos && pojav_environ->isInputReady) {
#ifdef DEBUG
        FCL_INTERNAL_LOG("pojav_environ->GLFW_invoke_CursorPos && pojav_environ->isInputReady \n");
#endif
        if (!pojav_environ->isCursorEntered) {
            if (pojav_environ->GLFW_invoke_CursorEnter) {
                pojav_environ->isCursorEntered = true;
                if (pojav_environ->isUseStackQueueCall) {
                    sendData(EVENT_TYPE_CURSOR_ENTER, 1, 0, 0, 0);
                } else {
                    pojav_environ->GLFW_invoke_CursorEnter((void *) pojav_environ->showingWindow,
                                                           1);
                }
            }
        }

        if (!pojav_environ->isUseStackQueueCall) {
            pojav_environ->GLFW_invoke_CursorPos((void *) pojav_environ->showingWindow,
                                                 (double) (x), (double) (y));
        } else {
            pojav_environ->cursorX = x;
            pojav_environ->cursorY = y;
        }
    }
}

void noncritical_send_cursor_pos(__attribute__((unused)) JNIEnv *env,
                                 __attribute__((unused)) jclass clazz, jfloat x, jfloat y) {
    critical_send_cursor_pos(x, y);
}

#define max(a, b) \
   ({ __typeof__ (a) _a = (a); \
       __typeof__ (b) _b = (b); \
     _a > _b ? _a : _b; })

void critical_send_key(jint key, jint scancode, jint action, jint mods) {
    if (pojav_environ->GLFW_invoke_Key && pojav_environ->isInputReady) {
        pojav_environ->keyDownBuffer[max(0, key - 31)] = (jbyte) action;
        if (pojav_environ->isUseStackQueueCall) {
            sendData(EVENT_TYPE_KEY, key, scancode, action, mods);
        } else {
            pojav_environ->GLFW_invoke_Key((void *) pojav_environ->showingWindow, key, scancode,
                                           action, mods);
        }
    }
}

void noncritical_send_key(__attribute__((unused)) JNIEnv *env, __attribute__((unused)) jclass clazz,
                          jint key, jint scancode, jint action, jint mods) {
    critical_send_key(key, scancode, action, mods);
}

void critical_send_mouse_button(jint button, jint action, jint mods) {
    if (pojav_environ->GLFW_invoke_MouseButton && pojav_environ->isInputReady) {
        pojav_environ->mouseDownBuffer[max(0, button)] = (jbyte) action;
        if (pojav_environ->isUseStackQueueCall) {
            sendData(EVENT_TYPE_MOUSE_BUTTON, button, action, mods, 0);
        } else {
            pojav_environ->GLFW_invoke_MouseButton((void *) pojav_environ->showingWindow, button,
                                                   action, mods);
        }
    }
}

void noncritical_send_mouse_button(__attribute__((unused)) JNIEnv *env,
                                   __attribute__((unused)) jclass clazz, jint button, jint action,
                                   jint mods) {
    critical_send_mouse_button(button, action, mods);
}

void critical_send_screen_size(jint width, jint height) {
    pojav_environ->savedWidth = width;
    pojav_environ->savedHeight = height;
    // Even if there was call to pojavStartPumping that consumed the size, this call
    // might happen right after it (or right before pojavStopPumping)
    // So unmark the size as "consumed"
    pojav_environ->monitorSizeConsumed = false;
    pojav_environ->shouldUpdateMonitorSize = true;
    // Don't use the direct updates for screen dimensions.
    // This is done to ensure that we have predictable conditions to correctly call
    // updateMonitorSize() and updateWindowSize() while on the render thread with an attached
    // JNIEnv.
}

void noncritical_send_screen_size(__attribute__((unused)) JNIEnv *env,
                                  __attribute__((unused)) jclass clazz, jint width, jint height) {
    critical_send_screen_size(width, height);
}

void critical_send_scroll(jdouble xoffset, jdouble yoffset) {
    if (pojav_environ->GLFW_invoke_Scroll && pojav_environ->isInputReady) {
        if (pojav_environ->isUseStackQueueCall) {
            sendData(EVENT_TYPE_SCROLL, (int) xoffset, (int) yoffset, 0, 0);
        } else {
            pojav_environ->GLFW_invoke_Scroll((void *) pojav_environ->showingWindow,
                                              (double) xoffset, (double) yoffset);
        }
    }
}

void
noncritical_send_scroll(__attribute__((unused)) JNIEnv *env, __attribute__((unused)) jclass clazz,
                        jdouble xoffset, jdouble yoffset) {
    critical_send_scroll(xoffset, yoffset);
}


JNIEXPORT void JNICALL
Java_org_lwjgl_glfw_GLFW_nglfwSetShowingWindow(__attribute__((unused)) JNIEnv *env,
                                               __attribute__((unused)) jclass clazz, jlong window) {
    pojav_environ->showingWindow = (long) window;
}

JNIEXPORT void JNICALL
Java_org_lwjgl_glfw_CallbackBridge_nativeSetWindowAttrib(__attribute__((unused)) JNIEnv *env,
                                                         __attribute__((unused)) jclass clazz,
                                                         jint attrib, jint value) {
    // Check for stack queue no longer necessary here as the JVM crash's origin is resolved
    if (!pojav_environ->showingWindow) {
        // If the window is not shown, there is nothing to do yet.
        return;
    }

    // We cannot use pojav_environ->runtimeJNIEnvPtr_JRE here because that environment is attached
    // on the thread that loaded pojavexec (which is the thread that first references the GLFW class)
    // But this method is only called from the Android UI thread

    // Technically the better solution would be to have a permanently attached env pointer stored
    // in environ for the Android UI thread but this is the only place that uses it
    // (very rarely, only in lifecycle callbacks) so i dont care

    JavaVM *jvm = pojav_environ->runtimeJavaVMPtr;
    JNIEnv *jvm_env = NULL;
    jboolean attached = JNI_FALSE;
    jint env_result = (*jvm)->GetEnv(jvm, (void **) &jvm_env, JNI_VERSION_1_4);
    if (env_result == JNI_EDETACHED) {
        env_result = (*jvm)->AttachCurrentThread(jvm, &jvm_env, NULL);
        attached = JNI_TRUE;
    }
    if (env_result != JNI_OK) {
        printf("input_bridge nativeSetWindowAttrib() JNI call failed: %i\n", env_result);
        return;
    }
    (*jvm_env)->CallStaticVoidMethod(
            jvm_env, pojav_environ->vmGlfwClass,
            pojav_environ->method_glftSetWindowAttrib,
            (jlong) pojav_environ->showingWindow, attrib, value
    );
    if (attached) {
        (*jvm)->DetachCurrentThread(jvm);
    }
    // Attaching every time is annoying, so stick the attachment to the Android GUI thread around
}

const static JNINativeMethod critical_fcns[] = {
        {"nativeSetUseInputStackQueue", "(Z)V",    critical_set_stackqueue},
        {"nativeSendChar",              "(C)Z",    critical_send_char},
        {"nativeSendCharMods",          "(CI)Z",   critical_send_char_mods},
        {"nativeSendKey",               "(IIII)V", critical_send_key},
        {"nativeSendCursorPos",         "(FF)V",   critical_send_cursor_pos},
        {"nativeSendMouseButton",       "(III)V",  critical_send_mouse_button},
        {"nativeSendScroll",            "(DD)V",   critical_send_scroll},
        {"nativeSendScreenSize",        "(II)V",   critical_send_screen_size}
};

const static JNINativeMethod noncritical_fcns[] = {
        {"nativeSetUseInputStackQueue", "(Z)V",    noncritical_set_stackqueue},
        {"nativeSendChar",              "(C)Z",    noncritical_send_char},
        {"nativeSendCharMods",          "(CI)Z",   noncritical_send_char_mods},
        {"nativeSendKey",               "(IIII)V", noncritical_send_key},
        {"nativeSendCursorPos",         "(FF)V",   noncritical_send_cursor_pos},
        {"nativeSendMouseButton",       "(III)V",  noncritical_send_mouse_button},
        {"nativeSendScroll",            "(DD)V",   noncritical_send_scroll},
        {"nativeSendScreenSize",        "(II)V",   noncritical_send_screen_size}
};


static bool criticalNativeAvailable;

void dvm_testCriticalNative(void *arg0, void *arg1, void *arg2, void *arg3) {
    if (arg0 != 0 && arg2 == 0 && arg3 == 0) {
        criticalNativeAvailable = false;
    } else if (arg0 == 0 && arg1 == 0) {
        criticalNativeAvailable = true;
    } else {
        criticalNativeAvailable = false; // just to be safe
    }
}

static bool tryCriticalNative(JNIEnv *env) {
    static const JNINativeMethod testJNIMethod[] = {
            {"testCriticalNative", "(II)V", dvm_testCriticalNative}
    };
    jclass criticalNativeTest = (*env)->FindClass(env,
                                                  "com/tungsten/fclauncher/CriticalNativeTest");
    if (criticalNativeTest == NULL) {
        (*env)->ExceptionClear(env);
        return false;
    }
    jmethodID criticalNativeTestMethod = (*env)->GetStaticMethodID(env, criticalNativeTest,
                                                                   "invokeTest", "()V");
    (*env)->RegisterNatives(env, criticalNativeTest, testJNIMethod, 1);
    (*env)->CallStaticVoidMethod(env, criticalNativeTest, criticalNativeTestMethod);
    (*env)->UnregisterNatives(env, criticalNativeTest);
    return criticalNativeAvailable;
}

static void registerFunctions(JNIEnv *env) {
    bool use_critical_cc = tryCriticalNative(env);
    jclass bridge_class = (*env)->FindClass(env, "org/lwjgl/glfw/CallbackBridge");
    if (use_critical_cc) {
        __android_log_print(ANDROID_LOG_INFO, "pojavexec",
                            "CriticalNative is available. Enjoy the 4.6x times faster input!");
    } else {
        __android_log_print(ANDROID_LOG_INFO, "pojavexec",
                            "CriticalNative is not available. Upgrade, maybe?");
    }
    (*env)->RegisterNatives(env,
                            bridge_class,
                            use_critical_cc ? critical_fcns : noncritical_fcns,
                            sizeof(critical_fcns) / sizeof(critical_fcns[0]));
}

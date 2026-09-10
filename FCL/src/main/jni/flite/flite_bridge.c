// libflite 桥接核心：游戏 text2speech 库经 JNA 按名加载 flite 语音引擎，
// 本库把 flite 接口转接到安卓系统 TTS。启动器在游戏 JVM 启动前注入环境变量
// DALVIK_JAVAVM（Dalvik/ART JavaVM 指针，十进制）与 DALVIK_APPLICATION
// （Application 全局引用地址，十六进制），由此 attach 回 Dalvik，
// 经 Application 的 ClassLoader 反射调用 APK 内的 com.mio.flite.FliteTts。

#include <android/log.h>
#include <jni.h>
#include <pthread.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>

#define LOG_TAG "FCL-flite"
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)

static const char *BRIDGE_CLASS = "com.mio.flite.FliteTts";

static pthread_mutex_t bridge_lock = PTHREAD_MUTEX_INITIALIZER;
static JavaVM *dalvik_vm;
static jobject application;
static jclass bridge_class;
static jmethodID bridge_init;   // ()Z
static jmethodID bridge_speak;  // ([BF)F
static jmethodID bridge_stop;   // ()V
static int bridge_ready;

// text2speech 1.18.11（MC 26.x）的合成流程在 Java 侧逐句持有
// utterance/wave 指针（synth → rescale → play → delete），
// 安卓 TTS 没有 wave 概念，用 token 携带文本与音量系数串起这四个调用
struct utterance_token {
    char *text;
    int factor;   // cst_wave_rescale 的系数，65536 为原始音量
};

static JNIEnv *attach_dalvik(void) {
    JNIEnv *env;
    if ((*dalvik_vm)->GetEnv(dalvik_vm, (void **) &env, JNI_VERSION_1_4) == JNI_OK) {
        return env;
    }
    if ((*dalvik_vm)->AttachCurrentThread(dalvik_vm, &env, NULL) != JNI_OK) {
        return NULL;
    }
    // 游戏线程生命周期与进程一致，attach 后不 detach
    return env;
}

// 同 fcl_loader.c：把非法的 modified UTF-8 序列替换为 '?'，避免 NewStringUTF 异常
static int setup_bridge(JNIEnv *env) {
    jclass app_class = (*env)->GetObjectClass(env, application);
    jmethodID get_loader = app_class == NULL
            ? NULL
            : (*env)->GetMethodID(env, app_class, "getClassLoader", "()Ljava/lang/ClassLoader;");
    if (app_class != NULL) (*env)->DeleteLocalRef(env, app_class);
    if (get_loader == NULL || (*env)->ExceptionCheck(env)) goto fail;

    jobject loader = (*env)->CallObjectMethod(env, application, get_loader);
    jclass loader_class = loader == NULL ? NULL : (*env)->GetObjectClass(env, loader);
    jmethodID load_class = loader_class == NULL
            ? NULL
            : (*env)->GetMethodID(env, loader_class, "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;");
    if (loader_class != NULL) (*env)->DeleteLocalRef(env, loader_class);
    if (load_class == NULL || (*env)->ExceptionCheck(env)) goto fail;

    jstring name = (*env)->NewStringUTF(env, BRIDGE_CLASS);
    jclass cls = name == NULL
            ? NULL
            : (jclass) (*env)->CallObjectMethod(env, loader, load_class, name);
    if (name != NULL) (*env)->DeleteLocalRef(env, name);
    if (cls == NULL || (*env)->ExceptionCheck(env)) goto fail;

    bridge_class = (*env)->NewGlobalRef(env, cls);
    (*env)->DeleteLocalRef(env, cls);
    bridge_init = (*env)->GetStaticMethodID(env, bridge_class, "init", "()Z");
    bridge_speak = (*env)->GetStaticMethodID(env, bridge_class, "speak", "([BF)F");
    bridge_stop = (*env)->GetStaticMethodID(env, bridge_class, "stop", "()V");
    if (bridge_init == NULL || bridge_speak == NULL || bridge_stop == NULL) return 0;
    return 1;

fail:
    if ((*env)->ExceptionCheck(env)) (*env)->ExceptionClear(env);
    return 0;
}

static int ensure_bridge(void) {
    int ready;
    pthread_mutex_lock(&bridge_lock);
    if (!bridge_ready) {
        const char *vm_env = getenv("DALVIK_JAVAVM");
        const char *app_env = getenv("DALVIK_APPLICATION");
        if (vm_env == NULL || app_env == NULL) {
            LOGW("DALVIK_JAVAVM or DALVIK_APPLICATION not set");
        } else {
            dalvik_vm = (JavaVM *) (uintptr_t) strtoull(vm_env, NULL, 10);
            application = (jobject) (uintptr_t) strtoull(app_env, NULL, 16);
            JNIEnv *env = dalvik_vm == NULL || application == NULL ? NULL : attach_dalvik();
            if (env == NULL) {
                LOGW("Failed to attach Dalvik VM");
            } else if (setup_bridge(env)) {
                bridge_ready = 1;
            } else {
                LOGW("Failed to set up bridge class %s", BRIDGE_CLASS);
            }
        }
    }
    ready = bridge_ready;
    pthread_mutex_unlock(&bridge_lock);
    return ready;
}

static jfloat fcl_flite_say_with_gain(JNIEnv *env, const char *text, jfloat gain) {
    // 以 UTF-8 字节数组传递文本，规避 NewStringUTF 的 modified UTF-8
    // 对增补平面字符（emoji 等）的破坏
    jsize len = (jsize) strlen(text);
    jbyteArray bytes = (*env)->NewByteArray(env, len);
    if (bytes == NULL) return -1.0f;
    (*env)->SetByteArrayRegion(env, bytes, 0, len, (const jbyte *) text);
    jfloat duration = (*env)->CallStaticFloatMethod(env, bridge_class, bridge_speak, bytes, gain);
    (*env)->DeleteLocalRef(env, bytes);
    if ((*env)->ExceptionCheck(env)) {
        (*env)->ExceptionClear(env);
        return -1.0f;
    }
    return duration;
}

// 供 fliteWrapper 转发的内部桥接接口
JNIEXPORT int JNICALL fcl_flite_init(void) {
    if (!ensure_bridge()) return 0;
    JNIEnv *env = attach_dalvik();
    if (env == NULL) return 0;
    jboolean ok = (*env)->CallStaticBooleanMethod(env, bridge_class, bridge_init);
    if ((*env)->ExceptionCheck(env)) {
        (*env)->ExceptionClear(env);
        LOGW("FliteTts.init threw");
        return 0;
    }
    if (!ok) LOGW("FliteTts.init returned false");
    return ok ? 1 : 0;
}

JNIEXPORT jfloat JNICALL fcl_flite_say(const char *text) {
    if (!ensure_bridge()) return -1.0f;
    JNIEnv *env = attach_dalvik();
    if (env == NULL) return -1.0f;
    return fcl_flite_say_with_gain(env, text, 1.0f);
}

// 打断当前朗读：游戏 clear() 经 MioLibPatcher 注入的 JNA 调用进入
// （TTSCancelTransformer 按 "flite_cancel" 符号懒解析，勿改名）
JNIEXPORT void JNICALL flite_cancel(void) {
    if (!ensure_bridge()) return;
    JNIEnv *env = attach_dalvik();
    if (env == NULL) return;
    (*env)->CallStaticVoidMethod(env, bridge_class, bridge_stop);
    if ((*env)->ExceptionCheck(env)) (*env)->ExceptionClear(env);
}

// flite 引擎接口（text2speech 1.16.7~1.17.9，MC 1.20.2~1.21.x）
JNIEXPORT jint JNICALL flite_init(void) {
    return fcl_flite_init() ? 0 : 1;
}

JNIEXPORT jfloat JNICALL flite_text_to_speech(const char *text, void *voice, const char *out) {
    // voice 为 flite_cmu_us_kal16 的占位指针，out 恒为 "play"（直接播放），
    // 两者均无需处理
    (void) voice;
    (void) out;
    return fcl_flite_say(text);
}

// flite 引擎接口（text2speech 1.18.11+，MC 26.x）：
// Java 侧按 synth → rescale → play → delete 的顺序逐句调用
JNIEXPORT void *JNICALL flite_synth_text(const char *text, void *voice) {
    (void) voice;
    struct utterance_token *token = malloc(sizeof(struct utterance_token));
    if (token == NULL) return NULL;
    token->text = strdup(text);
    if (token->text == NULL) {
        free(token);
        return NULL;
    }
    token->factor = 65536;
    return token;
}

JNIEXPORT void *JNICALL utt_wave(void *utterance) {
    // 安卓 TTS 不经过 wave，直接把 utterance token 当 wave 透传
    return utterance;
}

JNIEXPORT void JNICALL cst_wave_rescale(void *wave, int factor) {
    struct utterance_token *token = wave;
    if (token != NULL) token->factor = factor;
}

JNIEXPORT void JNICALL play_wave(void *wave) {
    struct utterance_token *token = wave;
    if (token == NULL || !ensure_bridge()) return;
    JNIEnv *env = attach_dalvik();
    if (env == NULL) return;
    // flite 的 rescale 语义为 factor/65536 的线性增益
    fcl_flite_say_with_gain(env, token->text, token->factor / 65536.0f);
}

JNIEXPORT void JNICALL delete_utterance(void *utterance) {
    struct utterance_token *token = utterance;
    if (token == NULL) return;
    free(token->text);
    free(token);
}

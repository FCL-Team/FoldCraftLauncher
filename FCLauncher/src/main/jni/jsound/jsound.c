#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>

#define MAX_DEVICES 8
#define DEVICE_NAME "Android PCM Output"

typedef struct {
    SLObjectItf engineObj;
    SLEngineItf engine;
    SLObjectItf outputMixObj;
    int initialized;
    pthread_mutex_t mutex;
} AudioEngine;

typedef struct {
    int in_use;
    int channels;
    int sample_rate;
    int bits_per_sample;
    int frame_size;
    int buffer_size;
    int state;
    SLObjectItf playerObj;
    SLPlayItf player;
    SLAndroidSimpleBufferQueueItf bufferQueue;
    AudioEngine *engine;
} PcmDevice;

static AudioEngine g_engine = {0};

static pthread_mutex_t g_device_mutex = PTHREAD_MUTEX_INITIALIZER;
static PcmDevice g_devices[MAX_DEVICES];

static int init_engine(AudioEngine *e) {
    pthread_mutex_lock(&e->mutex);
    if (e->initialized) { pthread_mutex_unlock(&e->mutex); return 0; }
    SLresult r;
    r = slCreateEngine(&e->engineObj, 0, NULL, 0, NULL, NULL);
    if (r != SL_RESULT_SUCCESS) { pthread_mutex_unlock(&e->mutex); return -1; }
    r = (*e->engineObj)->Realize(e->engineObj, SL_BOOLEAN_FALSE);
    if (r != SL_RESULT_SUCCESS) { (*e->engineObj)->Destroy(e->engineObj); e->engineObj = NULL; pthread_mutex_unlock(&e->mutex); return -1; }
    r = (*e->engineObj)->GetInterface(e->engineObj, SL_IID_ENGINE, &e->engine);
    if (r != SL_RESULT_SUCCESS) { (*e->engineObj)->Destroy(e->engineObj); e->engineObj = NULL; pthread_mutex_unlock(&e->mutex); return -1; }
    const SLInterfaceID ids[] = { SL_IID_VOLUME };
    const SLboolean req[] = { SL_BOOLEAN_FALSE };
    r = (*e->engine)->CreateOutputMix(e->engine, &e->outputMixObj, 1, ids, req);
    if (r != SL_RESULT_SUCCESS) { (*e->engineObj)->Destroy(e->engineObj); e->engineObj = NULL; e->engine = NULL; pthread_mutex_unlock(&e->mutex); return -1; }
    r = (*e->outputMixObj)->Realize(e->outputMixObj, SL_BOOLEAN_FALSE);
    if (r != SL_RESULT_SUCCESS) { (*e->outputMixObj)->Destroy(e->outputMixObj); e->outputMixObj = NULL; (*e->engineObj)->Destroy(e->engineObj); e->engineObj = NULL; e->engine = NULL; pthread_mutex_unlock(&e->mutex); return -1; }
    e->initialized = 1;
    fprintf(stderr, "jsound: OpenSL ES engine initialized\n");
    pthread_mutex_unlock(&e->mutex);
    return 0;
}

static void buffer_callback(SLAndroidSimpleBufferQueueItf bq, void *context) {
    (void)bq; (void)context;
}

static int alloc_device(void) {
    for (int i = 0; i < MAX_DEVICES; i++) {
        if (!g_devices[i].in_use) {
            memset(&g_devices[i], 0, sizeof(PcmDevice));
            g_devices[i].in_use = 1;
            return i;
        }
    }
    return -1;
}

static void free_device(int id) {
    if (id >= 0 && id < MAX_DEVICES) {
        g_devices[id].in_use = 0;
    }
}

#define MIXER_TYPE_SOURCE 2
#define MIXER_TYPE_TARGET 1

static jclass audioFormatClass = NULL;
static jclass encodingClass = NULL;
static jobject pcmSignedEncoding = NULL;

static int init_jni_refs(JNIEnv *env) {
    if (audioFormatClass) return 0;
    jclass cls = (*env)->FindClass(env, "javax/sound/sampled/AudioFormat");
    if (!cls) return -1;
    audioFormatClass = (jclass)(*env)->NewGlobalRef(env, cls);

    jclass encCls = (*env)->FindClass(env, "javax/sound/sampled/AudioFormat$Encoding");
    if (!encCls) return -1;
    encodingClass = (jclass)(*env)->NewGlobalRef(env, encCls);

    jfieldID pcmSignedField = (*env)->GetStaticFieldID(env, encodingClass, "PCM_SIGNED", "Ljavax/sound/sampled/AudioFormat$Encoding;");
    if (pcmSignedField) {
        jobject obj = (*env)->GetStaticObjectField(env, encodingClass, pcmSignedField);
        if (obj) pcmSignedEncoding = (*env)->NewGlobalRef(env, obj);
    }
    return 0;
}

JNIEXPORT jint JNICALL Java_com_sun_media_sound_PortMixer_nGetNumDevices(JNIEnv *env, jclass cls, jint mixerType) {
    (void)env; (void)cls;
    if (mixerType == MIXER_TYPE_SOURCE) return 1;
    return 0;
}

JNIEXPORT jobjectArray JNICALL Java_com_sun_media_sound_PortMixer_nGetDeviceDescription(JNIEnv *env, jclass cls, jint mixerType, jint deviceID) {
    (void)cls;
    if (mixerType != MIXER_TYPE_SOURCE || deviceID != 0) return NULL;
    jclass stringClass = (*env)->FindClass(env, "java/lang/String");
    if (!stringClass) return NULL;
    jobjectArray arr = (*env)->NewObjectArray(env, 4, stringClass, NULL);
    if (!arr) return NULL;
    (*env)->SetObjectArrayElement(env, arr, 0, (*env)->NewStringUTF(env, "Android PCM Output"));
    (*env)->SetObjectArrayElement(env, arr, 1, (*env)->NewStringUTF(env, "FCL-Team"));
    (*env)->SetObjectArrayElement(env, arr, 2, (*env)->NewStringUTF(env, "Android PCM audio via OpenSL ES"));
    (*env)->SetObjectArrayElement(env, arr, 3, (*env)->NewStringUTF(env, "1.0"));
    return arr;
}

JNIEXPORT jlong JNICALL Java_com_sun_media_sound_PortMixer_nOpen(JNIEnv *env, jobject obj, jint mixerType, jint deviceID, jobject format, jint bufferSize) {
    (void)obj;
    if (mixerType != MIXER_TYPE_SOURCE || deviceID != 0) return -1;
    if (!g_engine.initialized && init_engine(&g_engine) != 0) return -1;
    if (init_jni_refs(env) != 0) return -1;

    int id = alloc_device();
    if (id < 0) return -1;
    PcmDevice *dev = &g_devices[id];

    jclass fmtCls = (*env)->GetObjectClass(env, format);

    jmethodID getEncoding = (*env)->GetMethodID(env, fmtCls, "getEncoding", "()Ljavax/sound/sampled/AudioFormat$Encoding;");
    jmethodID getSampleRate = (*env)->GetMethodID(env, fmtCls, "getSampleRate", "()F");
    jmethodID getSampleSize = (*env)->GetMethodID(env, fmtCls, "getSampleSizeInBits", "()I");
    jmethodID getChannels = (*env)->GetMethodID(env, fmtCls, "getChannels", "()I");
    jmethodID getFrameSize = (*env)->GetMethodID(env, fmtCls, "getFrameSize", "()I");
    jmethodID isBigEndian = (*env)->GetMethodID(env, fmtCls, "isBigEndian", "()Z");

    if (!getEncoding || !getSampleRate || !getSampleSize || !getChannels) {
        free_device(id); return -1;
    }

    jfloat sampleRate = 44100.0f;
    if (getSampleRate) sampleRate = (*env)->CallFloatMethod(env, format, getSampleRate);
    int sampleSize = 16;
    if (getSampleSize) sampleSize = (*env)->CallIntMethod(env, format, getSampleSize);
    int channels = 2;
    if (getChannels) channels = (*env)->CallIntMethod(env, format, getChannels);
    int frameSize = (sampleSize / 8) * channels;
    if (getFrameSize) frameSize = (*env)->CallIntMethod(env, format, getFrameSize);

    dev->channels = channels;
    dev->sample_rate = (int)sampleRate;
    dev->bits_per_sample = sampleSize;
    dev->frame_size = frameSize;
    dev->buffer_size = bufferSize > 0 ? bufferSize : 4096;
    dev->state = 0;
    dev->engine = &g_engine;

    SLDataFormat_PCM fmt;
    fmt.formatType = SL_DATAFORMAT_PCM;
    fmt.numChannels = channels;
    fmt.samplesPerSec = (SLuint32)(sampleRate * 1000);
    fmt.bitsPerSample = sampleSize;
    fmt.containerSize = sampleSize;
    fmt.channelMask = (channels == 1) ? SL_SPEAKER_FRONT_CENTER : SL_SPEAKER_FRONT_LEFT | SL_SPEAKER_FRONT_RIGHT;
    fmt.endianness = SL_BYTEORDER_LITTLEENDIAN;

    SLDataLocator_AndroidSimpleBufferQueue loc_bufq = { SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, 1 };
    SLDataSource audioSrc = { &loc_bufq, &fmt };
    SLDataLocator_OutputMix loc_outmix = { SL_DATALOCATOR_OUTPUTMIX, g_engine.outputMixObj };
    SLDataSink audioSink = { &loc_outmix, NULL };
    SLInterfaceID ids[] = { SL_IID_ANDROIDSIMPLEBUFFERQUEUE };
    SLboolean req[] = { SL_BOOLEAN_TRUE };

    SLresult r = (*g_engine.engine)->CreateAudioPlayer(g_engine.engine, &dev->playerObj, &audioSrc, &audioSink, 1, ids, req);
    if (r != SL_RESULT_SUCCESS) { free_device(id); return -1; }
    r = (*dev->playerObj)->Realize(dev->playerObj, SL_BOOLEAN_FALSE);
    if (r != SL_RESULT_SUCCESS) { (*dev->playerObj)->Destroy(dev->playerObj); free_device(id); return -1; }
    r = (*dev->playerObj)->GetInterface(dev->playerObj, SL_IID_PLAY, &dev->player);
    if (r != SL_RESULT_SUCCESS) { (*dev->playerObj)->Destroy(dev->playerObj); free_device(id); return -1; }
    r = (*dev->playerObj)->GetInterface(dev->playerObj, SL_IID_ANDROIDSIMPLEBUFFERQUEUE, &dev->bufferQueue);
    if (r != SL_RESULT_SUCCESS) { (*dev->playerObj)->Destroy(dev->playerObj); free_device(id); return -1; }
    r = (*dev->bufferQueue)->RegisterCallback(dev->bufferQueue, buffer_callback, dev);
    if (r != SL_RESULT_SUCCESS) { (*dev->playerObj)->Destroy(dev->playerObj); free_device(id); return -1; }

    fprintf(stderr, "jsound: nOpen id=%d %dHz %dch %dbit buf=%d\n", id, dev->sample_rate, channels, sampleSize, bufferSize);
    return (jlong)(intptr_t)dev;
}

JNIEXPORT void JNICALL Java_com_sun_media_sound_PortMixer_nStart(JNIEnv *env, jobject obj, jlong id) {
    (void)env; (void)obj;
    PcmDevice *dev = (PcmDevice*)(intptr_t)id;
    if (!dev || !dev->in_use) return;
    if (dev->player) {
        (*dev->player)->SetPlayState(dev->player, SL_PLAYSTATE_PLAYING);
    }
    dev->state = 1;
}

JNIEXPORT void JNICALL Java_com_sun_media_sound_PortMixer_nStop(JNIEnv *env, jobject obj, jlong id) {
    (void)env; (void)obj;
    PcmDevice *dev = (PcmDevice*)(intptr_t)id;
    if (!dev || !dev->in_use) return;
    if (dev->player) {
        (*dev->player)->SetPlayState(dev->player, SL_PLAYSTATE_STOPPED);
    }
    dev->state = 0;
}

JNIEXPORT void JNICALL Java_com_sun_media_sound_PortMixer_nClose(JNIEnv *env, jobject obj, jlong id) {
    (void)env; (void)obj;
    PcmDevice *dev = (PcmDevice*)(intptr_t)id;
    if (!dev || !dev->in_use) return;
    if (dev->player) {
        (*dev->player)->SetPlayState(dev->player, SL_PLAYSTATE_STOPPED);
    }
    if (dev->playerObj) {
        (*dev->playerObj)->Destroy(dev->playerObj);
    }
    free_device((int)((intptr_t)id));
}

JNIEXPORT jint JNICALL Java_com_sun_media_sound_PortMixer_nWrite(JNIEnv *env, jobject obj, jlong id, jbyteArray b, jint off, jint len) {
    (void)obj;
    PcmDevice *dev = (PcmDevice*)(intptr_t)id;
    if (!dev || !dev->in_use || !dev->bufferQueue) return -1;

    if (dev->state == 0) {
        (*dev->player)->SetPlayState(dev->player, SL_PLAYSTATE_PLAYING);
        dev->state = 1;
    }

    jbyte *data = (*env)->GetByteArrayElements(env, b, NULL);
    if (!data) return -1;

    SLresult r = (*dev->bufferQueue)->Enqueue(dev->bufferQueue, data + off, len);
    (*env)->ReleaseByteArrayElements(env, b, data, JNI_ABORT);

    if (r != SL_RESULT_SUCCESS) return -1;
    return len;
}

JNIEXPORT jint JNICALL Java_com_sun_media_sound_PortMixer_nRead(JNIEnv *env, jobject obj, jlong id, jbyteArray b, jint off, jint len) {
    (void)env; (void)obj; (void)id; (void)b; (void)off; (void)len;
    return -1;
}

JNIEXPORT jint JNICALL Java_com_sun_media_sound_PortMixer_nAvailable(JNIEnv *env, jobject obj, jlong id) {
    (void)env; (void)obj;
    PcmDevice *dev = (PcmDevice*)(intptr_t)id;
    if (!dev || !dev->in_use) return -1;
    return dev->buffer_size * dev->frame_size;
}

JNIEXPORT void JNICALL Java_com_sun_media_sound_PortMixer_nDrain(JNIEnv *env, jobject obj, jlong id) {
    (void)env; (void)obj;
    PcmDevice *dev = (PcmDevice*)(intptr_t)id;
    if (!dev || !dev->in_use) return;
    if (dev->player) {
        (*dev->player)->SetPlayState(dev->player, SL_PLAYSTATE_STOPPED);
    }
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    (void)vm; (void)reserved;
    g_engine.mutex = (pthread_mutex_t)PTHREAD_MUTEX_INITIALIZER;
    fprintf(stderr, "jsound: library loaded\n");
    return JNI_VERSION_1_6;
}
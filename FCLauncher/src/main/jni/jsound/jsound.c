#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#include <jni.h>
#include <pthread.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>

#define MAX_DEVICES 8
#define BUFFER_COUNT 4
#define BUFFER_SIZE 4096
#define RING_SIZE (256 * 1024)

typedef struct {
    SLObjectItf engineObj;
    SLEngineItf engine;
    SLObjectItf outputMixObj;
    SLObjectItf playerObj;
    SLPlayItf play;
    SLBufferQueueItf queue;

    char ring[RING_SIZE];
    volatile int writePos;
    volatile int readPos;

    char buffers[BUFFER_COUNT][BUFFER_SIZE];
    volatile int enqueueHead;
    volatile int enqueueTail;
    volatile int buffersInQueue;

    pthread_mutex_t mutex;
    pthread_cond_t dataCond;
    pthread_cond_t spaceCond;
    pthread_cond_t drainCond;

    volatile int running;
    volatile int draining;
    volatile int drained;
    int inUse;
} AudioCtx;

static SLObjectItf gEngineObj = NULL;
static SLEngineItf gEngine = NULL;
static int gRefCount = 0;
static AudioCtx gDevices[MAX_DEVICES];

static int minInt(int a, int b) { return a < b ? a : b; }

static int ringAvailable(AudioCtx *ctx) {
    return (ctx->readPos - ctx->writePos - 1 + RING_SIZE) % RING_SIZE;
}

static int ringUsed(AudioCtx *ctx) {
    return (ctx->writePos - ctx->readPos + RING_SIZE) % RING_SIZE;
}

static void ringWrite(AudioCtx *ctx, const char *src, int len) {
    int part = minInt(len, RING_SIZE - ctx->writePos);
    memcpy(ctx->ring + ctx->writePos, src, part);
    if (part < len)
        memcpy(ctx->ring, src + part, len - part);
    ctx->writePos = (ctx->writePos + len) % RING_SIZE;
}

static void ringRead(AudioCtx *ctx, char *dst, int len) {
    int part = minInt(len, RING_SIZE - ctx->readPos);
    memcpy(dst, ctx->ring + ctx->readPos, part);
    if (part < len)
        memcpy(dst + part, ctx->ring, len - part);
    ctx->readPos = (ctx->readPos + len) % RING_SIZE;
}

static void bufferQueueCallback(SLBufferQueueItf caller, void *context) {
    AudioCtx *ctx = (AudioCtx*)context;
    int enqueueIdx = -1;
    pthread_mutex_lock(&ctx->mutex);
    if (ctx->buffersInQueue > 0) {
        ctx->buffersInQueue--;
        ctx->enqueueHead = (ctx->enqueueHead + 1) % BUFFER_COUNT;
    }
    if (ringUsed(ctx) >= BUFFER_SIZE) {
        enqueueIdx = ctx->enqueueTail;
        ringRead(ctx, ctx->buffers[enqueueIdx], BUFFER_SIZE);
        ctx->enqueueTail = (ctx->enqueueTail + 1) % BUFFER_COUNT;
        ctx->buffersInQueue++;
        pthread_cond_signal(&ctx->spaceCond);
    } else if (ctx->draining) {
        if (ringUsed(ctx) == 0 && ctx->buffersInQueue == 0) {
            ctx->drained = 1;
            pthread_cond_signal(&ctx->drainCond);
        }
        memset(ctx->buffers[ctx->enqueueTail], 0, BUFFER_SIZE);
        enqueueIdx = ctx->enqueueTail;
        ctx->enqueueTail = (ctx->enqueueTail + 1) % BUFFER_COUNT;
        ctx->buffersInQueue++;
    } else {
        memset(ctx->buffers[ctx->enqueueTail], 0, BUFFER_SIZE);
        enqueueIdx = ctx->enqueueTail;
        ctx->enqueueTail = (ctx->enqueueTail + 1) % BUFFER_COUNT;
        ctx->buffersInQueue++;
    }
    pthread_mutex_unlock(&ctx->mutex);
    if (enqueueIdx >= 0)
        (*caller)->Enqueue(caller, ctx->buffers[enqueueIdx], BUFFER_SIZE);
}

static int initOpenSL(AudioCtx *ctx) {
    SLresult result;
    pthread_mutex_lock(&ctx->mutex);
    if (gEngineObj == NULL) {
        result = slCreateEngine(&gEngineObj, 0, NULL, 0, NULL, NULL);
        if (result != SL_RESULT_SUCCESS) { pthread_mutex_unlock(&ctx->mutex); return 0; }
        result = (*gEngineObj)->Realize(gEngineObj, SL_BOOLEAN_FALSE);
        if (result != SL_RESULT_SUCCESS) { (*gEngineObj)->Destroy(gEngineObj); gEngineObj = NULL; pthread_mutex_unlock(&ctx->mutex); return 0; }
        result = (*gEngineObj)->GetInterface(gEngineObj, SL_IID_ENGINE, &gEngine);
        if (result != SL_RESULT_SUCCESS) { (*gEngineObj)->Destroy(gEngineObj); gEngineObj = NULL; pthread_mutex_unlock(&ctx->mutex); return 0; }
    }
    gRefCount++;
    ctx->engineObj = gEngineObj;
    ctx->engine = gEngine;
    pthread_mutex_unlock(&ctx->mutex);

    result = (*ctx->engine)->CreateOutputMix(ctx->engine, &ctx->outputMixObj, 0, NULL, NULL);
    if (result != SL_RESULT_SUCCESS) return 0;
    result = (*ctx->outputMixObj)->Realize(ctx->outputMixObj, SL_BOOLEAN_FALSE);
    if (result != SL_RESULT_SUCCESS) return 0;

    SLDataLocator_AndroidSimpleBufferQueue locBufQueue = { SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, BUFFER_COUNT };
    SLDataFormat_PCM formatPcm = { SL_DATAFORMAT_PCM, 2, SL_SAMPLINGRATE_44_1,
        SL_PCMSAMPLEFORMAT_FIXED_16, SL_PCMSAMPLEFORMAT_FIXED_16,
        SL_SPEAKER_FRONT_LEFT | SL_SPEAKER_FRONT_RIGHT, SL_BYTEORDER_LITTLEENDIAN };
    SLDataSource audioSrc = { &locBufQueue, &formatPcm };
    SLDataLocator_OutputMix locOutMix = { SL_DATALOCATOR_OUTPUTMIX, ctx->outputMixObj };
    SLDataSink audioSnk = { &locOutMix, NULL };
    SLInterfaceID ids[] = { SL_IID_BUFFERQUEUE };
    SLboolean req[] = { SL_BOOLEAN_TRUE };

    result = (*ctx->engine)->CreateAudioPlayer(ctx->engine, &ctx->playerObj, &audioSrc, &audioSnk, 1, ids, req);
    if (result != SL_RESULT_SUCCESS) return 0;
    result = (*ctx->playerObj)->Realize(ctx->playerObj, SL_BOOLEAN_FALSE);
    if (result != SL_RESULT_SUCCESS) return 0;
    result = (*ctx->playerObj)->GetInterface(ctx->playerObj, SL_IID_PLAY, &ctx->play);
    if (result != SL_RESULT_SUCCESS) return 0;
    result = (*ctx->playerObj)->GetInterface(ctx->playerObj, SL_IID_BUFFERQUEUE, &ctx->queue);
    if (result != SL_RESULT_SUCCESS) return 0;
    result = (*ctx->queue)->RegisterCallback(ctx->queue, bufferQueueCallback, ctx);
    if (result != SL_RESULT_SUCCESS) return 0;

    for (int i = 0; i < BUFFER_COUNT; i++) {
        memset(ctx->buffers[ctx->enqueueTail], 0, BUFFER_SIZE);
        result = (*ctx->queue)->Enqueue(ctx->queue, ctx->buffers[ctx->enqueueTail], BUFFER_SIZE);
        if (result != SL_RESULT_SUCCESS) break;
        ctx->enqueueTail = (ctx->enqueueTail + 1) % BUFFER_COUNT;
        ctx->buffersInQueue++;
    }

    result = (*ctx->play)->SetPlayState(ctx->play, SL_PLAYSTATE_PLAYING);
    return (result == SL_RESULT_SUCCESS) ? 1 : 0;
}

static void shutdownOpenSL(AudioCtx *ctx) {
    if (ctx->play) (*ctx->play)->SetPlayState(ctx->play, SL_PLAYSTATE_STOPPED);
    if (ctx->queue) (*ctx->queue)->Clear(ctx->queue);
    if (ctx->playerObj) { (*ctx->playerObj)->Destroy(ctx->playerObj); ctx->playerObj = NULL; }
    if (ctx->outputMixObj) { (*ctx->outputMixObj)->Destroy(ctx->outputMixObj); ctx->outputMixObj = NULL; }
    pthread_mutex_lock(&ctx->mutex);
    gRefCount--;
    if (gRefCount == 0 && gEngineObj != NULL) {
        (*gEngineObj)->Destroy(gEngineObj);
        gEngineObj = NULL;
        gEngine = NULL;
    }
    pthread_mutex_unlock(&ctx->mutex);
}

static int allocDeviceId(void) {
    for (int i = 0; i < MAX_DEVICES; i++) {
        if (!gDevices[i].inUse) {
            memset(&gDevices[i], 0, sizeof(AudioCtx));
            gDevices[i].inUse = 1;
            gDevices[i].writePos = 0;
            gDevices[i].readPos = 0;
            gDevices[i].buffersInQueue = 0;
            gDevices[i].enqueueHead = 0;
            gDevices[i].enqueueTail = 0;
            gDevices[i].running = 1;
            gDevices[i].draining = 0;
            gDevices[i].drained = 0;
            gDevices[i].engineObj = NULL;
            gDevices[i].engine = NULL;
            gDevices[i].outputMixObj = NULL;
            gDevices[i].playerObj = NULL;
            gDevices[i].play = NULL;
            gDevices[i].queue = NULL;
            pthread_mutex_init(&gDevices[i].mutex, NULL);
            pthread_cond_init(&gDevices[i].dataCond, NULL);
            pthread_cond_init(&gDevices[i].spaceCond, NULL);
            pthread_cond_init(&gDevices[i].drainCond, NULL);
            return i;
        }
    }
    return -1;
}

static void freeDeviceId(int id) {
    if (id < 0 || id >= MAX_DEVICES) return;
    AudioCtx *ctx = &gDevices[id];
    if (!ctx->inUse) return;
    shutdownOpenSL(ctx);
    pthread_mutex_destroy(&ctx->mutex);
    pthread_cond_destroy(&ctx->dataCond);
    pthread_cond_destroy(&ctx->spaceCond);
    pthread_cond_destroy(&ctx->drainCond);
    memset(ctx, 0, sizeof(AudioCtx));
}

// ==================== DirectAudioDeviceProvider ====================

JNIEXPORT jint JNICALL Java_com_sun_media_sound_DirectAudioDeviceProvider_nGetNumDevices(JNIEnv *env, jclass clazz) {
    return 1;
}

JNIEXPORT jobject JNICALL Java_com_sun_media_sound_DirectAudioDeviceProvider_nNewDirectAudioDeviceInfo(JNIEnv *env, jclass clazz, jint deviceIndex) {
    jclass infoClass = (*env)->FindClass(env, "com/sun/media/sound/DirectAudioDeviceProvider$DirectAudioDeviceInfo");
    if (!infoClass) { (*env)->ExceptionClear(env); return NULL; }
    jmethodID ctor = (*env)->GetMethodID(env, infoClass, "<init>", "(I)V");
    if (!ctor) {
        (*env)->ExceptionClear(env);
        ctor = (*env)->GetMethodID(env, infoClass, "<init>",
            "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IIII)V");
        if (!ctor) { (*env)->ExceptionClear(env); return NULL; }
        jstring name = (*env)->NewStringUTF(env, "Android OpenSL ES Audio");
        jstring vendor = (*env)->NewStringUTF(env, "Android");
        jstring desc = (*env)->NewStringUTF(env, "OpenSL ES Audio Device");
        jstring ver = (*env)->NewStringUTF(env, "1.0");
        jobject obj = (*env)->NewObject(env, infoClass, ctor, name, vendor, desc, ver, deviceIndex, 2, 65536, 1);
        (*env)->DeleteLocalRef(env, name);
        (*env)->DeleteLocalRef(env, vendor);
        (*env)->DeleteLocalRef(env, desc);
        (*env)->DeleteLocalRef(env, ver);
        return obj;
    }
    return (*env)->NewObject(env, infoClass, ctor, deviceIndex);
}

// ==================== DirectAudioDevice ====================

JNIEXPORT jint JNICALL Java_com_sun_media_sound_DirectAudioDevice_nAvailable(JNIEnv *env, jclass clazz, jlong id) {
    if (id < 0 || id >= MAX_DEVICES) return 0;
    AudioCtx *ctx = &gDevices[(int)id];
    if (!ctx->inUse) return 0;
    pthread_mutex_lock(&ctx->mutex);
    int avail = ringAvailable(ctx);
    pthread_mutex_unlock(&ctx->mutex);
    return avail;
}

JNIEXPORT jint JNICALL Java_com_sun_media_sound_DirectAudioDevice_nGetBufferSize(JNIEnv *env, jclass clazz, jlong id) {
    return BUFFER_SIZE * BUFFER_COUNT;
}

JNIEXPORT jlong JNICALL Java_com_sun_media_sound_DirectAudioDevice_nGetBytePosition(JNIEnv *env, jclass clazz, jlong id, jboolean isBytePos) {
    AudioCtx *ctx = (id >= 0 && id < MAX_DEVICES) ? &gDevices[(int)id] : NULL;
    if (!ctx || !ctx->inUse) return 0;
    pthread_mutex_lock(&ctx->mutex);
    jlong pos = (jlong)ctx->writePos;
    pthread_mutex_unlock(&ctx->mutex);
    return pos;
}

JNIEXPORT void JNICALL Java_com_sun_media_sound_DirectAudioDevice_nSetBytePosition(JNIEnv *env, jclass clazz, jlong id, jlong pos, jboolean isBytePos) {
}

JNIEXPORT jint JNICALL Java_com_sun_media_sound_DirectAudioDevice_nOpen(JNIEnv *env, jclass clazz, jlong id, jboolean isSource, jboolean isOutput, jfloat sampleRate, jint sampleSizeInBits, jint channels, jint bufferSize, jint encoding) {
    if (!isOutput || !isSource) return -1;
    int devId = allocDeviceId();
    if (devId < 0) return -1;
    AudioCtx *ctx = &gDevices[devId];
    if (!initOpenSL(ctx)) { freeDeviceId(devId); return -1; }
    return devId;
}

JNIEXPORT void JNICALL Java_com_sun_media_sound_DirectAudioDevice_nClose(JNIEnv *env, jclass clazz, jlong id) {
    freeDeviceId((int)id);
}

JNIEXPORT void JNICALL Java_com_sun_media_sound_DirectAudioDevice_nStart(JNIEnv *env, jclass clazz, jlong id, jboolean isSource) {
    AudioCtx *ctx = (id >= 0 && id < MAX_DEVICES) ? &gDevices[(int)id] : NULL;
    if (!ctx || !ctx->inUse || !ctx->play) return;
    ctx->running = 1;
    (*ctx->play)->SetPlayState(ctx->play, SL_PLAYSTATE_PLAYING);
}

JNIEXPORT void JNICALL Java_com_sun_media_sound_DirectAudioDevice_nStop(JNIEnv *env, jclass clazz, jlong id, jboolean isSource) {
    AudioCtx *ctx = (id >= 0 && id < MAX_DEVICES) ? &gDevices[(int)id] : NULL;
    if (!ctx || !ctx->inUse || !ctx->play) return;
    ctx->running = 0;
    (*ctx->play)->SetPlayState(ctx->play, SL_PLAYSTATE_PAUSED);
}

JNIEXPORT void JNICALL Java_com_sun_media_sound_DirectAudioDevice_nFlush(JNIEnv *env, jclass clazz, jlong id, jboolean isSource) {
    AudioCtx *ctx = (id >= 0 && id < MAX_DEVICES) ? &gDevices[(int)id] : NULL;
    if (!ctx || !ctx->inUse || !ctx->queue) return;
    pthread_mutex_lock(&ctx->mutex);
    ctx->writePos = 0;
    ctx->readPos = 0;
    ctx->buffersInQueue = 0;
    ctx->enqueueHead = 0;
    ctx->enqueueTail = 0;
    pthread_mutex_unlock(&ctx->mutex);
    (*ctx->queue)->Clear(ctx->queue);
    for (int i = 0; i < BUFFER_COUNT; i++) {
        memset(ctx->buffers[i], 0, BUFFER_SIZE);
        (*ctx->queue)->Enqueue(ctx->queue, ctx->buffers[i], BUFFER_SIZE);
    }
}

JNIEXPORT jint JNICALL Java_com_sun_media_sound_DirectAudioDevice_nWrite(JNIEnv *env, jclass clazz, jlong id, jbyteArray b, jint off, jint len) {
    AudioCtx *ctx = (id >= 0 && id < MAX_DEVICES) ? &gDevices[(int)id] : NULL;
    if (!ctx || !ctx->inUse || !b) return -1;
    jbyte *data = (*env)->GetByteArrayElements(env, b, NULL);
    if (!data) return -1;
    int written = 0;
    while (written < len) {
        pthread_mutex_lock(&ctx->mutex);
        int avail = ringAvailable(ctx);
        if (avail == 0) { pthread_mutex_unlock(&ctx->mutex); break; }
        int chunk = minInt(len - written, avail);
        ringWrite(ctx, (const char*)(data + off + written), chunk);
        written += chunk;
        while (ctx->buffersInQueue < BUFFER_COUNT && ringUsed(ctx) >= BUFFER_SIZE) {
            int idx = ctx->enqueueTail;
            ringRead(ctx, ctx->buffers[idx], BUFFER_SIZE);
            (*ctx->queue)->Enqueue(ctx->queue, ctx->buffers[idx], BUFFER_SIZE);
            ctx->enqueueTail = (ctx->enqueueTail + 1) % BUFFER_COUNT;
            ctx->buffersInQueue++;
        }
        pthread_mutex_unlock(&ctx->mutex);
    }
    (*env)->ReleaseByteArrayElements(env, b, data, JNI_ABORT);
    return written;
}

JNIEXPORT jint JNICALL Java_com_sun_media_sound_DirectAudioDevice_nRead(JNIEnv *env, jclass clazz, jlong id, jbyteArray b, jint off, jint len) {
    return -1;
}

JNIEXPORT jobjectArray JNICALL Java_com_sun_media_sound_DirectAudioDevice_nGetFormats(JNIEnv *env, jclass clazz, jlong id, jboolean isSource, jint sampleSizeInBits, jint channels, jfloat sampleRate, jint encoding) {
    return NULL;
}

JNIEXPORT jboolean JNICALL Java_com_sun_media_sound_DirectAudioDevice_nIsStillDraining(JNIEnv *env, jclass clazz, jlong id, jboolean isSource) {
    AudioCtx *ctx = (id >= 0 && id < MAX_DEVICES) ? &gDevices[(int)id] : NULL;
    if (!ctx || !ctx->inUse) return JNI_FALSE;
    pthread_mutex_lock(&ctx->mutex);
    jboolean draining = (ctx->draining || ringUsed(ctx) > 0 || ctx->buffersInQueue > 0) ? JNI_TRUE : JNI_FALSE;
    pthread_mutex_unlock(&ctx->mutex);
    return draining;
}

JNIEXPORT jboolean JNICALL Java_com_sun_media_sound_DirectAudioDevice_nRequiresServicing(JNIEnv *env, jclass clazz, jlong id, jboolean isSource) {
    return JNI_FALSE;
}

JNIEXPORT void JNICALL Java_com_sun_media_sound_DirectAudioDevice_nService(JNIEnv *env, jclass clazz, jlong id, jboolean isSource) {
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    if ((*vm)->GetEnv(vm, (void**)&env, JNI_VERSION_1_6) != JNI_OK)
        return JNI_ERR;
    return JNI_VERSION_1_6;
}
